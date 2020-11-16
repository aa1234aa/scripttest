package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.compent.kafka.service.KafkaSender;
import com.bitnei.cloud.compent.kafka.service.LocalThreadFactory;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.dao.InstructTaskMapper;
import com.bitnei.cloud.sys.domain.InstructTask;
import com.bitnei.cloud.sys.enums.UpgradeLogAction;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.cloud.sys.util.DateUtil;
import com.bitnei.cloud.sys.util.RedisUtil;
import com.bitnei.cloud.sys.util.TermUtils;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author DFY
 * @description 远升远控定时任务
 * @date 2019/1/4
 */
@Service
@RequiredArgsConstructor
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.InstructTaskMapper")
@Slf4j
public class InstructTaskService extends BaseService implements IInstructTaskService {

    private final InstructTaskMapper instructTaskMapper;
    private final IInstructSendRuleService instructSendRuleService;
    private final KafkaSender kafkaSender;
    private final IUppackageSendDetailsService uppackageSendDetailsService;
    private final IVehicleService vehicleService;
    private final IUppackageSendService uppackageSendService;
    private final RealDataClient realDataClient;
    private final IUppackageInfoService uppackageInfoService;
    private final ITermParamRecordService termParamRecordService;
    private final IUserService userService;
    private final IDictService dictService;
    private final RedisUtil redisUtil;
    private final IInstructManagementService instructManagementService;
    private final IUpgradeLogService upgradeLogService;
    private final Environment env;
    private final ScheduledExecutorService upgradeResponseScheduleExecutor;
    private final ScheduledExecutorService sendRuleRespScheduleExecutor;
    private final ScheduledExecutorService upgradeBMSAndSearchScheduleExecutor;
    private final ScheduledExecutorService sendInstructRespScheduleExecutor;


    /**
     * 任务执行间隔为1分钟，有时间改为配置化
     */
    private final Integer TASK_EXCUTE_INTERVAL = 1;

    private final ThreadPoolExecutor instructTaskExecutor;

    private Lock lock = new ReentrantLock();

    /**
     * 开始远程升级、控制定时任务
     */
    @SneakyThrows
    public void beginInstructTask() {

        boolean locked = lock.tryLock();

        if (locked) {
            try {
                // 查询待执行的定时任务
                List<InstructTaskModel> list = queryUpgradeEffectiveTaskList(null, null);
                log.info("升级、控制待执行定时任务数量：" + list.size());
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }

                //前置查询车辆在线状态，过滤不在线的车辆，避免后续大量操作影响系统性能（暂时只过滤升级的不在线部分）
                List<String> vins = list.stream().filter(it -> StringUtils.isNotEmpty(it.getVin()))
                        .map(InstructTaskModel::getVin).collect(Collectors.toList());

                Map<String, Integer> onlineStatusMap = new HashMap<>();

                List<List<String>> vinsList = Lists.partition(vins, 50);

                vinsList.forEach(it -> onlineStatusMap.putAll(vehicleService.getVehicleOnlineStatusMap(it)));

                //升级任务离线车辆是否超时判断
                list.stream().filter(it -> StringUtils.isNotEmpty(it.getVin()) &&
                        onlineStatusMap.containsKey(it.getVin()) &&
                        onlineStatusMap.get(it.getVin()).equals(VehicleService.OFFLINE)
                ).forEach(it -> {
                    try {
                        dealUpgradeTaskCacheTime(it, 1);
                    } catch (Exception e) {
                        log.error("升级任务处理失败，任务类型：{}，任务id：{}",
                                it.getInstructType(), it.getInstructId());
                        log.error("error:", e);
                    }
                });

                //剩余的是控制命令任务，和在线的车辆的升级任务
                // （没有vin的就是控制任务，所以取没有vin的和有vin且在线的）
                List<InstructTaskModel> onlineOrControlRecords = list.stream().filter(it ->
                        StringUtils.isEmpty(it.getVin()) ||
                                (onlineStatusMap.containsKey(it.getVin()) &&
                                        onlineStatusMap.get(it.getVin()).equals(VehicleService.ONLINE)))
                        .collect(Collectors.toList());

                String userName = "instruct_task";
                for (InstructTaskModel entity : onlineOrControlRecords) {
                    // 指令类型：1远程控制，2远程升级
                    try {
                        if (SysDefine.INSTRUCT_TYPE_UPGRADE == entity.getInstructType()) {
                            startWaitTask(entity.getInstructId(), userName);
                        } else if (SysDefine.INSTRUCT_TYPE_CONTROL == entity.getInstructType()) {
                            beginInstructTask(entity.getInstructId());
                        }
                    } catch (Exception e) {
                        //如果有处理失败的数据，不要影响其他任务
                        log.error("升级/控制任务处理失败，任务类型：{}，任务id：{}",
                                entity.getInstructType(), entity.getInstructId());
                    }
                }
            } catch (Exception e) {
                log.error("升级任务失败：{}", e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void beginInstructTask(String instructId) {
        //命令id不存在直接结束
        if (StringUtils.isEmpty(instructId)) {
            return;
        }
        InstructSendRuleModel entity = instructSendRuleService.get(instructId);
        if (null != entity) {
            int ruleCount = instructSendRuleService.getImplementRuleCountByVin(entity.getVin());
            //国标控制指令缓存时，指令下发状态为空，所以数量判断>1，排除本条命令
            if (ruleCount > 1) {
                entity.setSendResult(1);
                entity.setInstructRemark("车辆存在正在执行的国标控制指令");
                update(entity);
                log.error("车辆vin:" + entity.getVin() + "的车辆存在正在执行的国标控制指令");
                // 更新指令缓存任务
                editControlTask(false, instructId, entity.getInstructCacheTime(),
                        entity.getInstructRemark(), 1);
                return;
            }
            int vehUppackageTaskCount = uppackageSendDetailsService.getImplementUpgradeCountByVin(entity.getVin());
            if (vehUppackageTaskCount > 0) {
                entity.setSendResult(1);
                entity.setInstructRemark("车辆存在正在执行的升级任务");
                instructSendRuleService.update(entity);
                log.error("车辆vin:" + entity.getVin() + "的车辆存在正在执行的升级任务");
                // 更新指令缓存任务
                editControlTask(false, instructId, entity.getInstructCacheTime(),
                        entity.getInstructRemark(), 1);
                return;
            }
            VehicleWithOnlineStatus vehicle = vehicleService.getVehicleWithStatusByVin(entity.getVin());
            if (1 == vehicle.getOnlineStatus()) {
                entity.setHistoryOnlineState(vehicle.getOnlineStatus());
                entity.setSendResult(null);
                entity.setExecuteResult(null);
                entity.setInstructRemark(null);
                instructSendRuleService.update(entity);

                //车辆在线，下发控制指令
                instructTaskExecutor.submit(() -> sendRule(entity, vehicle));
            } else {
                //车辆不在线，缓存控制指令
                entity.setInstructRemark("车辆当前离线，缓存控制指令");
                instructSendRuleService.update(entity);
                editControlTask(true, instructId, entity.getInstructCacheTime(), entity.getInstructRemark(), 1);
            }
        }
    }

    /**
     * 下发控制命令到kafka
     */
    public void sendRule(InstructSendRuleModel instructSendRuleModel,
                         VehicleWithOnlineStatus vehicleWithOnlineStatus) {

        //前置超时的逻辑，即使后续发生异常，任务也能结束
        //30s后进行查询控制响应
        sendRuleRespScheduleExecutor.schedule(() -> {
            InstructSendRuleModel resultEntity = instructSendRuleService.get(instructSendRuleModel.getId());
            //超时
            if (null == resultEntity.getSendResult()) {
                log.warn("车辆" + vehicleWithOnlineStatus.getVin() + "的国标控制命令" +
                        instructSendRuleModel.getStandardCode() + "响应超时！");
                resultEntity.setSendResult(2);
                resultEntity.setInstructRemark("控制命令下发超时");
                instructSendRuleService.update(resultEntity);
            }
        }, 1000 * 30L, TimeUnit.MILLISECONDS);

        try {
            //硬编码，凯马锁车解锁特殊处理
            if (instructSendRuleModel.getStandardCode().equals(TermUtils.KAI_MA_LOCK) ||
                    instructSendRuleModel.getStandardCode().equals(TermUtils.KAI_MA_UNLOCK)) {

                String sessionId = UUID.randomUUID().toString();
                instructSendRuleModel.setSessionId(sessionId);
                instructSendRuleService.update(instructSendRuleModel);

                //发送卡夫卡
                String cmd = TermUtils.getCanControlArgs(vehicleWithOnlineStatus,
                        instructSendRuleModel, instructSendRuleModel.getStandardCode());
                kafkaSender.send(SysDefine.kafkaDataCtrlTopic, "TERM_CTRL", cmd);
                log.info("车辆：" + vehicleWithOnlineStatus.getVin() + ";CAN命令下发！命令:" + cmd);
            } else {
                // 如果不是凯马的特殊处理，则是正常的国标控制
                // 增加报警等级
                String alarmInfo = instructSendRuleModel.getRemarks();
                byte[] alarmInfoData = new byte[1];
                if (StringUtils.isNotEmpty(alarmInfo)) {
                    alarmInfoData[0] = Integer.valueOf(alarmInfo).byteValue();
                } else {
                    alarmInfoData = new byte[0];
                }

                byte[] am = new byte[1];
                if (null != instructSendRuleModel.getAlarmLevel()) {
                    am[0] = instructSendRuleModel.getAlarmLevel().byteValue();
                }

                byte[] alarmLevelAndInfoBytes = addBytes(am, alarmInfoData);

                String cmd = TermUtils.getCtrlString(vehicleWithOnlineStatus,
                        instructSendRuleModel.getStandardCode(),
                        null != instructSendRuleModel.getAlarmLevel() ? instructSendRuleModel.getAlarmLevel().toString() : null,
                        alarmLevelAndInfoBytes);
                log.warn("车辆VIN：" + vehicleWithOnlineStatus.getVin() + ",开始下发国标控制命令，命令为：" + cmd);
                kafkaSender.send(SysDefine.kafkaDataCtrlTopic, "TERM_CTRL", cmd);
            }

            // 结束远程控制定时任务
            editControlTask(false, instructSendRuleModel.getId(),
                    instructSendRuleModel.getInstructCacheTime(), "远程控制命令下发成功，任务结束", 0);

        } catch (Exception e) {
            log.error("国标指令下发错误，车辆vin: {}，指令: {}",
                    vehicleWithOnlineStatus.getVin(), instructSendRuleModel.getStandardCode());
            log.error("error: {}", e);
        }

    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {

        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 更新远升远控缓存任务状态
     *
     * @param isVehOffline  是否是车辆离线执行
     * @param instructId    指令id
     * @param effectiveTime 指令缓存时长
     * @param remark        远升远控任务备注
     * @param status        有效状态
     * @return
     */
    @SneakyThrows
    public void editUpgradeTask(boolean isVehOffline, String instructId, Integer effectiveTime,
                                String remark, int status) {

        Integer finalStatus = status;
        // 校验参数
        if (StringUtils.isEmpty(instructId)) {
            return;
        }
        // 查询任务基本信息
        List<InstructTaskModel> tlist = queryUpgradeEffectiveTaskList(
                SysDefine.INSTRUCT_TYPE_UPGRADE, instructId);
        if (null == tlist || tlist.size() == 0) {
            // 任务基本信息为空且车辆不在线，则插入新的任务纪录
            if (isVehOffline) {
                insertInstructTask(instructId, effectiveTime, SysDefine.INSTRUCT_TYPE_UPGRADE,
                        "车辆不在线，创建远程升级定时任务");
            }
            return;
        }
        InstructTaskModel base = tlist.get(0);
        String date = DateUtil.getNow();
        // 查询库中相同车辆、升级指令类型、升级模式、升级文件的定时任务
        List<InstructTaskModel> list = querySimilarUpgradeTask(SysDefine.INSTRUCT_TYPE_UPGRADE,
                base.getProtocolType(), base.getSchemaType(), base.getVin(), base.getUppackageId());
        //如果定时任务不存在，直接结束
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        InstructTaskModel entity = null;
        for (InstructTaskModel task : list) {
            // 判断取出当前定时任务信息
            if (instructId.equals(task.getInstructId())) {
                entity = task;
                continue;
            }
            //覆盖其他的同类型的任务信息
            task.setStatus(0);
            task.setUpdateTime(date);
            task.setTaskResult("任务被覆盖");
            update(task);
        }
        if (null != entity) {

            if (null != effectiveTime) {

                //处理超时逻辑，判断缓存是否超时以及后续操作
                finalStatus = dealUpgradeTaskCacheTime(entity, finalStatus);
            }

            entity.setExecutedNum(entity.getExecutedNum() + 1);
            entity.setExecuteTime(date);
            entity.setStatus(finalStatus);
            entity.setTaskResult(remark);
            entity.setUpdateTime(date);
            update(entity);

            return;
        }
    }

    /**
     * 处理超时逻辑，判断缓存是否超时以及后续操作
     *
     * @param entity
     * @param status
     */
    @SneakyThrows
    private Integer dealUpgradeTaskCacheTime(InstructTaskModel entity, Integer status) {
        String date = DateUtil.getNow();
        Date now = DateUtil.strToDate(date, DateUtil.FULL_ST_FORMAT);

        //如果没有执行过，默认设执行时间为现在
        Date createTime = DateUtil.strToDate(entity.getCreateTime(), DateUtil.FULL_ST_FORMAT);

        //如果上次执行时间再加缓存时间再加1分钟，超过了现在的时间，寿命指令缓存时间到期
        Date effectiveDate = DateUtil.addMinute(createTime, entity.getEffectiveTime());

        if (now.compareTo(effectiveDate) > 0 && 1 == status) {
            // 最后一次执行时更新升级任务备注
            UppackageSendDetailsModel detailsModel = uppackageSendDetailsService.get(entity.getInstructId());
            detailsModel.setRemark("任务失败，指令缓存时间内车辆未上线");
            //升级状态：0未开始、1升级中、2升级成功、3升级失败
            detailsModel.setUpgradeStatus(3);
            //任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务
            detailsModel.setUppackageSendStatus(4);
            uppackageSendDetailsService.update(detailsModel);
            //如果超时了，改成无效
            entity.setExecutedNum(entity.getExecutedNum() + 1);
            entity.setExecuteTime(date);
            entity.setStatus(0);
            entity.setTaskResult("任务失败，指令缓存时间内车辆未上线");
            entity.setUpdateTime(date);
            update(entity);
            return 0;
        }
        //没有超时则返回原状态
        return status;
    }

    /**
     * 更新远程终端控制定时任务
     *
     * @param isVehOffline  是否是车辆离线执行
     * @param instructId    指令id
     * @param effectiveTime 指令缓存有效时间
     * @param remark        远升远控任务备注
     * @param status        有效状态
     * @return
     */
    @SneakyThrows
    public void editControlTask(boolean isVehOffline, String instructId, Integer effectiveTime,
                                String remark, int status) {
        // 校验参数
        if (StringUtils.isEmpty(instructId)) {
            return;
        }
        // 查询任务基本信息
        List<InstructTaskModel> tlist = queryControlEffectiveTaskList(SysDefine.INSTRUCT_TYPE_CONTROL,
                instructId, null, null, null);
        if (null == tlist || tlist.size() == 0) {
            // 任务基本信息为空且车辆不在线，则插入新的任务纪录
            if (isVehOffline) {
                insertInstructTask(instructId, effectiveTime, SysDefine.INSTRUCT_TYPE_CONTROL,
                        "车辆不在线，创建终端控制定时任务");
            }
            return;
        }
        InstructTaskModel base = tlist.get(0);
        String date = DateUtil.getNow();
        // 查询库中相同车辆、控制命令、报警等级的控制定时任务
        List<InstructTaskModel> list = queryControlEffectiveTaskList(SysDefine.INSTRUCT_TYPE_CONTROL,
                null, base.getStandardCode(), base.getAlarmLevel(), base.getVin());
        if (null != list && list.size() > 0) {
            InstructTaskModel entity = null;
            for (InstructTaskModel task : list) {
                // 判断取出当前定时任务信息
                if (instructId.equals(task.getInstructId())) {
                    entity = task;
                    continue;
                }
                task.setStatus(0);
                task.setUpdateTime(date);
                task.setTaskResult("任务被覆盖");
                update(task);
            }
            if (null != entity) {
                Date now = DateUtil.strToDate(date, DateUtil.FULL_ST_FORMAT);

                //如果没有执行过，默认设执行时间为现在
                Date createTime = DateUtil.strToDate(entity.getCreateTime(), DateUtil.FULL_ST_FORMAT);

                //如果上次执行时间再加缓存时间再加1分钟，超过了现在的时间，寿命指令缓存时间到期
                Date effectiveDate = DateUtil.addMinute(createTime, effectiveTime);

                if (now.compareTo(effectiveDate) > 0 && 1 == status) {
                    status = 0;
                    remark = "任务失败，指令缓存时间内车辆未上线";
                    // 最后一次执行时更新控制任务备注
                    InstructSendRuleModel instructSendRuleModel = instructSendRuleService.get(instructId);
                    instructSendRuleModel.setInstructRemark(remark);
                    instructSendRuleModel.setSendResult(1);
                    instructSendRuleService.update(instructSendRuleModel);
                }

                entity.setExecutedNum(entity.getExecutedNum() + 1);
                entity.setExecuteTime(date);
                entity.setStatus(status);
                entity.setTaskResult(remark);
                entity.setUpdateTime(date);
                update(entity);
            }
        }
        // 判断如果是车辆不在线时执行，则插入新任务纪录
//        if (isVehOffline) {
//            insertInstructTask(instructId, effectiveTime, SysDefine.INSTRUCT_TYPE_CONTROL,
//                    "车辆不在线，创建终端控制定时任务");
//        }
    }


    /**
     * 插入定时任务
     *
     * @param instructId    远升远控任务id
     * @param effectiveTime 指令缓存有效时间
     * @param instructType  指令类型
     * @param remark        备注说明
     * @return
     */
    private void insertInstructTask(String instructId, Integer effectiveTime, int instructType, String remark) {
        //缓存时间为空或者为0，都不进行缓存
        if (null == effectiveTime || effectiveTime.equals(0)) {
            return;
        }
        int executeMaxNum = 0;
        if (1 == SysDefine.INSTRUCT_EFFECTIVE_TIME_TYPE) {
            executeMaxNum = (effectiveTime * 24 * 60) / SysDefine.INSTRUCT_EFFECTIVE_TIME_INTERVAL;
        } else if (2 == SysDefine.INSTRUCT_EFFECTIVE_TIME_TYPE) {
            executeMaxNum = (effectiveTime * 60) / SysDefine.INSTRUCT_EFFECTIVE_TIME_INTERVAL;
        } else if (3 == SysDefine.INSTRUCT_EFFECTIVE_TIME_TYPE) {
            executeMaxNum = effectiveTime / SysDefine.INSTRUCT_EFFECTIVE_TIME_INTERVAL;
        }
        InstructTaskModel instruct = new InstructTaskModel();
        instruct.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        instruct.setInstructId(instructId);
        instruct.setInstructType(instructType);
        instruct.setEffectiveTime(effectiveTime);
        instruct.setEffectiveTimeType(SysDefine.INSTRUCT_EFFECTIVE_TIME_TYPE);
        instruct.setExecutedNum(0);
        instruct.setExecuteMaxNum(executeMaxNum);
        instruct.setTaskResult(remark);
        instruct.setStatus(1);
        instruct.setCreateTime(DateUtil.getNow());
        instruct.setUpdateTime(instruct.getUpdateTime());
        insert(instruct);
    }

    /**
     * 校验非空
     *
     * @param obj
     * @return
     */
    private String transObjToString(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return "";
    }

    public void startWaitTask(String vehTaskId, String userName) {
        UppackageSendDetailsModel sysUppackageSendDetailsEntity = uppackageSendDetailsService.getOrNull(vehTaskId);
        if (null == sysUppackageSendDetailsEntity) {
            return;
        }

        VehicleModel vehicleModel = vehicleService.getByVinOrNull(sysUppackageSendDetailsEntity.getVin());
        if (null == vehicleModel) {
            return;
        }
        VehicleTaskThread vehicleTaskThread = new VehicleTaskThread(vehicleModel,
                sysUppackageSendDetailsEntity.getUppackageSendId(), sysUppackageSendDetailsEntity,
                StringUtils.isNotEmpty(userName) ? userName : ServletUtil.getCurrentUser(),
                true);
        instructTaskExecutor.submit(vehicleTaskThread);
    }

    class VehicleTaskThread implements Runnable {

        private VehicleModel vehicleModel;
        private String taskId;
        private UppackageSendDetailsModel uppackageSendDetailsModel;
        private String userName;
        private boolean isQuartzStart = false;

        public VehicleTaskThread(VehicleModel vehicleModel, String taskId,
                                 UppackageSendDetailsModel uppackageSendDetailsModel,
                                 String userName) {
            this.vehicleModel = vehicleModel;
            this.taskId = taskId;
            this.uppackageSendDetailsModel = uppackageSendDetailsModel;
            this.userName = userName;
        }

        public VehicleTaskThread(VehicleModel vehicleModel, String taskId,
                                 UppackageSendDetailsModel uppackageSendDetailsModel,
                                 String userName, boolean isQuartzStart) {
            this.vehicleModel = vehicleModel;
            this.taskId = taskId;
            this.uppackageSendDetailsModel = uppackageSendDetailsModel;
            this.userName = userName;
            this.isQuartzStart = isQuartzStart;
        }

        @Override
        public void run() {
            UppackageSendModel uppackageSendModel = uppackageSendService.get(taskId);

            //获取车辆在线状态
            Integer vehState = vehicleService.getVehicleOnlineStatus(vehicleModel.getVin());

            // 是否有升级任务或国标指令
            boolean isCanUpdate = instructSendRuleService.checkIsCanSend(vehicleModel.getVin());
            if (!isCanUpdate && !isQuartzStart) {
                //失败
                uppackageSendDetailsModel.setUppackageSendStatus(4);
                uppackageSendDetailsModel.setUpgradeSendState(2);
                uppackageSendDetailsModel.setRemark("存在正在执行的升级任务或国标控制指令，升级失败");
                uppackageSendDetailsService.update(uppackageSendDetailsModel);

                //覆盖别的同类型的升级任务，更新同类型的升级任务的缓存时间
                editUpgradeTask(false, uppackageSendDetailsModel.getId(),
                        uppackageSendModel.getInstructCacheTime(), uppackageSendDetailsModel.getRemark(), 1);

                log.warn("车辆VIN：" + vehicleModel.getVin() + "，存在正在执行的升级任务或国标控制指令，升级失败");
                return;
            }
            //2为离线。如果车辆不在线，就缓存升级指令，走定时任务
            if (vehState == 2) {

                //特殊处理，如果缓存指令时间是0，则立刻失败
                if (null != uppackageSendModel.getInstructCacheTime() &&
                        uppackageSendModel.getInstructCacheTime() <= 0) {
                    //0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务
                    uppackageSendModel.setUppackageSendStatus(4);
                    uppackageSendService.update(uppackageSendModel);

                    //升级指令下发状态 0未开始；1下发成功；2下发失败；9终端离线下发失败
                    uppackageSendDetailsModel.setUppackageSendStatus(4);
                    uppackageSendDetailsModel.setUpgradeSendState(9);
                    uppackageSendDetailsModel.setRemark("任务失败，指令缓存时间内车辆未上线");
                    uppackageSendDetailsService.update(uppackageSendDetailsModel);
                    return;
                }

                uppackageSendDetailsModel.setUppackageSendStatus(5);
                uppackageSendDetailsModel.setRemark("车辆当前离线，缓存升级指令");
                uppackageSendDetailsService.update(uppackageSendDetailsModel);
                // 缓存升级指令，插入/更新定时任务表
                editUpgradeTask(true, uppackageSendDetailsModel.getId(),
                        uppackageSendModel.getInstructCacheTime(), uppackageSendDetailsModel.getRemark(), 1);
                log.warn("车辆VIN：" + vehicleModel.getVin() + "，任务ID：" +
                        uppackageSendModel.getId() + "的车辆当前离线，升级任务进入等待状态……………………");
                return;
            }
            // 协议类型
            String protocolType = uppackageSendModel.getProtocolType().toString();
            // 升级模式
            String schemaType = uppackageSendModel.getSchemaType().toString();
            // 处理升级模式
            if (handleSchemaTypeNormal(uppackageSendModel, schemaType, vehState, userName)) {
                return;
            }

            uppackageSendDetailsModel = uppackageSendDetailsService.get(uppackageSendDetailsModel.getId());
            // 终止任务
            if (uppackageSendDetailsModel.getUppackageSendStatus() != null &&
                    uppackageSendDetailsModel.getUppackageSendStatus() == 3) {
                // 任务已终止
                editUpgradeTask(false, uppackageSendDetailsModel.getId(),
                        uppackageSendModel.getInstructCacheTime(), "任务已终止", 0);
                log.warn("车辆VIN：" + vehicleModel.getVin() + "，任务已终止，指令停止下发");
                return;
            }

            // 下发kafka
            String args = uppackageSendDetailsService.initUppackageArgs(uppackageSendDetailsModel,
                    uppackageSendDetailsModel.getUppackageInfoId(), uppackageSendModel.getProtocolType().toString());
            String cmd = TermUtils.getCtrlStringKuozhan(vehicleModel, uppackageSendModel.getProtocolType().toString(),
                    args, protocolType);

            log.warn("升级命令开始下发" + cmd);
            kafkaSender.send(SysDefine.kafkaDataCtrlTopic, "TERM_CTRL", cmd);

            uppackageSendDetailsModel.setUppackageSendStatus(1);
            //国标类型直接将文件下发状态和升级状态设置为成功，任务状态改为已结束
            if (protocolType.equals("1") || protocolType.equals("2")) {
                uppackageSendDetailsModel.setFileSendStatus(1);
                uppackageSendDetailsModel.setUpgradeStatus(2);
            }
            uppackageSendDetailsModel.setRemark("升级命令已下发");
            uppackageSendDetailsService.update(uppackageSendDetailsModel);

            // 升级命令已经下发，定时任务结束
            editUpgradeTask(false, uppackageSendDetailsModel.getId(),
                    uppackageSendModel.getInstructCacheTime(), "升级命令已经下发，定时任务结束", 0);

            //已经下发，无法终止

            //30s后查看是否有升级指令响应
            upgradeResponseScheduleExecutor.schedule(() -> {
                //查看终端是否有响应
                uppackageSendDetailsModel = uppackageSendDetailsService.get(uppackageSendDetailsModel.getId());
                if (uppackageSendDetailsModel.getUpgradeSendState() == null ||
                        (uppackageSendDetailsModel.getUpgradeSendState() != null
                                && uppackageSendDetailsModel.getUpgradeSendState() == 0)) {
                    log.warn("车辆VIN：" + vehicleModel.getVin() + "，响应升级指令超时，升级失败");
                    uppackageSendDetailsModel.setUppackageSendStatus(4);
                    uppackageSendDetailsModel.setUpgradeSendState(2);
                    uppackageSendDetailsModel.setRemark("响应升级指令超时，升级失败");
                    uppackageSendDetailsService.update(uppackageSendDetailsModel);
                }
            }, env.getProperty("cache.command.upgrade", Long.class, 2 * 60 * 1000L), TimeUnit.MILLISECONDS);

        }

        private boolean handleSchemaTypeNormal(UppackageSendModel uppackageSendModel,
                                               String schemaType, Integer vehState, String userName) {
            if (schemaType.equals("2")) {
                String uppackageId = uppackageSendModel.getUppackageId();
                UppackageInfoModel uppackageInfoModel = uppackageInfoService.get(uppackageId);
                if (uppackageInfoModel.getType().equals("0x02")) {
                    try {
                        termParamRecordService.allQuery(vehicleModel.getId(),
                                userName, vehState, "BMS升级|查询指令", "BMS升级|查询指令");

                        ScheduledFuture<Boolean> future = upgradeBMSAndSearchScheduleExecutor.schedule(() -> {
                            TermParamRecordModel termParamRecordModel =
                                    termParamRecordService.findByVin(vehicleModel.getVin());
                            if (termParamRecordModel.getReceiveState() == 0) {
                                uppackageSendDetailsModel.setUppackageSendStatus(4);
                                uppackageSendDetailsService.update(uppackageSendDetailsModel);
                                log.error("BMS升级，查询终端参数失败，升级失败");
                                return true;
                            }
                            String params = termParamRecordModel.getParamValues();
                            JSONObject paramsJson = JSONObject.fromObject(params);

                            String hardVersion = paramsJson.get("4410069") == null ? "" : paramsJson.get("4410069").toString();
                            String softVersion = paramsJson.get("4410070") == null ? "" : paramsJson.get("4410070").toString();
                            if (StringUtils.isBlank(hardVersion) && StringUtils.isBlank(softVersion)) {
                                uppackageSendDetailsModel.setUppackageSendStatus(4);
                                uppackageSendDetailsService.update(uppackageSendDetailsModel);
                                log.error("BMS升级，查询终端参数失败，升级失败");
                                return true;
                            } else {
                                if (!uppackageInfoModel.getHardwareVersion().equals(hardVersion) ||
                                        !uppackageInfoModel.getFirmwareVersion().equals(softVersion)) {
                                    uppackageSendDetailsModel.setUppackageSendStatus(4);
                                    uppackageSendDetailsService.update(uppackageSendDetailsModel);
                                    log.error("BMS升级包版本与终端BMS版本不对应，升级失败");
                                    return true;
                                }
                            }
                            return false;
                        }, (long)(30 * 1000), TimeUnit.MILLISECONDS);
                        return future.get();

                    } catch (Exception e) {
                        uppackageSendDetailsModel.setUppackageSendStatus(4);
                        uppackageSendDetailsService.update(uppackageSendDetailsModel);
                        log.error("BMS升级，查询终端参数异常，升级失败");
                        return true;
                    }
                }
            }
            return false;
        }

    }

    /**
     * 下发升级命令：
     * 1、一条主线程，控制整个升级状态（终止任务操作，修改任务状态为终止）
     * 2、每个车辆升级一个子线程，控制每个车辆的任务状态
     *
     * @param uppackageSendModel
     * @return
     */
    public String startTask(UppackageSendModel uppackageSendModel) {
        uppackageSendModel = uppackageSendService.get(uppackageSendModel.getId());
        if (uppackageSendModel.getUppackageSendStatus() == 0) {
            //修改状态改为同步执行
            // 修改任务状态为进行中
            uppackageSendModel.setUppackageSendStatus(1);
            uppackageSendService.update(uppackageSendModel);

            //处理任务明细的每个车辆的任务
            List<UppackageSendDetailsModel> uppackageSendDetailsModels =
                    uppackageSendDetailsService.getAllByTaskId(uppackageSendModel.getId());

            StringBuilder licensePlates = new StringBuilder();

            for (UppackageSendDetailsModel uppackageSendDetailsModel : uppackageSendDetailsModels) {
                VehicleModel vehicleModel = vehicleService.getByVin(uppackageSendDetailsModel.getVin());
                // 启动升级线程
                VehicleTaskThread vehicleTaskThread = new VehicleTaskThread(vehicleModel, uppackageSendModel.getId(),
                        uppackageSendDetailsModel, ServletUtil.getCurrentUser());
                instructTaskExecutor.submit(vehicleTaskThread);

                if (StringUtils.isNotEmpty(uppackageSendDetailsModel.getLicensePlate())) {
                    licensePlates.append(uppackageSendDetailsModel.getLicensePlate()).append(",");
                } else {
                    licensePlates.append(uppackageSendDetailsModel.getInterNo()).append(",");
                }
            }

            if (licensePlates.length() > 0) {
                licensePlates.deleteCharAt(licensePlates.length() - 1);
            }

            //操作日志
            upgradeLogService.save(UpgradeLogAction.START_TASK.getValue(),
                    uppackageSendModel.getTaskName(), licensePlates.toString(), "开始升级任务", "");

            return "任务启动成功";
        } else if (uppackageSendModel.getUppackageSendStatus() == 1) {
            return "任务正在进行中，不能重复启动";
        } else if (uppackageSendModel.getUppackageSendStatus() == 2) {
            return "任务已经结束，不能再次启动";
        } else if (uppackageSendModel.getUppackageSendStatus() == 3) {
            //如果是已终止状态，把任务状态恢复到最初状态，重新开始
            uppackageSendModel.setUppackageSendStatus(0);
            uppackageSendService.update(uppackageSendModel);
            uppackageSendDetailsService.resetState(uppackageSendModel.getId(), "重新启动任务", null);
            return startTask(uppackageSendModel);
        }
        return "";
    }

    public String shutdownTask(String taskId) {

        UppackageSendModel model = uppackageSendService.get(taskId);
        if (model.getUppackageSendStatus() == 0) {
            return "任务还未开始，无法终止";
        } else if (model.getUppackageSendStatus() == 2) {
            return "任务已经结束，无法终止";
        } else if (model.getUppackageSendStatus() == 3) {
            return "任务已经结束，请不要重复操作";
        } else {

            // 终止车辆升级指令缓存任务
            List<UppackageSendDetailsModel> list = uppackageSendDetailsService.getAllByTaskId(taskId);

            StringBuilder licensePlates = new StringBuilder();

            if (null != list && list.size() > 0) {
                for (UppackageSendDetailsModel detail : list) {

                    //每辆车的明细任务状态修改为终止，缓存任务终止
                    shutdownDetailAndTask(detail, "任务已终止");

                    if (StringUtils.isNotEmpty(detail.getLicensePlate())) {
                        licensePlates.append(detail.getLicensePlate()).append(",");
                    } else {
                        licensePlates.append(detail.getInterNo()).append(",");
                    }
                }
            }

            // 终止完每辆车的，再修改总的任务状态为已终止
            // 避免因为明细任务全部终止导致任务状态变成已结束的问题
            model.setUppackageSendStatus(3);
            uppackageSendService.update(model);

            if (licensePlates.length() > 0) {
                licensePlates.deleteCharAt(licensePlates.length() - 1);
            }

            //操作日志
            upgradeLogService.save(UpgradeLogAction.STOP_TASK.getValue(),
                    model.getTaskName(), licensePlates.toString(), "终止升级任务", "");

            return "任务停止成功";
        }
    }

    /**
     * 终止某个车辆升级任务detail，以及终止对应的缓存升级任务
     *
     * @param detail
     */
    private void shutdownDetailAndTask(UppackageSendDetailsModel detail, String remark) {
        //更新任务明细状态
        detail.setUppackageSendStatus(3);
        detail.setRemark(remark);
        uppackageSendDetailsService.update(detail);

        editUpgradeTask(true, detail.getId(), null, remark, 0);
    }

    public String shutdownVehicleTask(String taskId, String vin) {

        UppackageSendModel model = uppackageSendService.get(taskId);
        if (model.getUppackageSendStatus() == 0) {
            return "任务还未开始，无法终止车辆任务";
        } else if (model.getUppackageSendStatus() == 2) {
            return "任务已经结束，无法终止车辆任务";
        } else if (model.getUppackageSendStatus() == 3) {
            return "任务已经结束，请不要重复操作";
        } else {
            //任务终止

            // 终止升级指令缓存任务
            List<UppackageSendDetailsModel> list = uppackageSendDetailsService.getAllByTaskId(taskId);

            StringBuilder licensePlates = new StringBuilder();

            if (null != list && list.size() > 0) {
                for (UppackageSendDetailsModel detail : list) {
                    //只停止单辆车的任务
                    if (detail.getVin().equals(vin)) {

                        shutdownDetailAndTask(detail, "任务已终止");

                        if (StringUtils.isNotEmpty(detail.getLicensePlate())) {
                            licensePlates.append(detail.getLicensePlate()).append(",");
                        } else {
                            licensePlates.append(detail.getInterNo()).append(",");
                        }
                    }
                }
            }
            if (licensePlates.length() > 0) {
                licensePlates.deleteCharAt(licensePlates.length() - 1);
            }

            //操作日志
            upgradeLogService.save(UpgradeLogAction.STOP_VEH_TASK.getValue(),
                    model.getTaskName(), licensePlates.toString(), "终止车辆升级", "");

            return "任务停止成功";
        }
    }

    public String deleteTask(String taskId) {
        UppackageSendModel model = uppackageSendService.get(taskId);
//        if (model.getUppackageSendStatus() == 1) {
////            return "任务正在进行中，不能删除";
//            //如果任务正在进行中，强制删除任务车辆所有任务，已经不在线车辆的定时任务
//
//        }
        //即便是进行中的升级任务，也可以删除

        // 修改任务记录状态为已删除，任务状态为已终止
        model.setUppackageSendStatus(3);
        model.setRecordStatus(1);
        uppackageSendService.update(model);

        // 终止升级指令缓存任务
        List<UppackageSendDetailsModel> list = uppackageSendDetailsService.getAllByTaskId(taskId);

        StringBuilder licensePlates = new StringBuilder();

        if (null != list && list.size() > 0) {
            for (UppackageSendDetailsModel detail : list) {

                shutdownDetailAndTask(detail, "任务已删除");

                if (StringUtils.isNotEmpty(detail.getLicensePlate())) {
                    licensePlates.append(detail.getLicensePlate()).append(",");
                } else {
                    licensePlates.append(detail.getInterNo()).append(",");
                }
            }
        }

        if (licensePlates.length() > 0) {
            licensePlates.deleteCharAt(licensePlates.length() - 1);
        }

        //操作日志
        upgradeLogService.save(UpgradeLogAction.DEL_TASK.getValue(),
                model.getTaskName(), licensePlates.toString(), "删除升级任务", "");

        return "任务删除成功";
    }

    public List<InstructTaskModel> queryUpgradeEffectiveTaskList(Integer instructType, String instructId) {

        Map<String, Object> params = new HashMap<>();
        params.put("instructType", instructType);
        params.put("instructId", instructId);

        List<InstructTask> instructTasks = instructTaskMapper.queryUpgradeEffectiveTaskList(params);

        if (CollectionUtils.isNotEmpty(instructTasks)) {
            return instructTasks.stream().map(InstructTaskModel::fromEntry).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public List<InstructTaskModel> querySimilarUpgradeTask(Integer instructType, Integer protocolType,
                                                           Integer schemaType, String vin, String uppackageId) {

        Map<String, Object> params = new HashMap<>();
        params.put("instructType", instructType);
        params.put("protocolType", protocolType);
        params.put("schemaType", schemaType);
        params.put("vin", vin);
        params.put("uppackageId", uppackageId);

        List<InstructTask> instructTasks = instructTaskMapper.querySimilarUpgradeTask(params);

        if (CollectionUtils.isNotEmpty(instructTasks)) {
            return instructTasks.stream().map(InstructTaskModel::fromEntry).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public List<InstructTaskModel> queryControlEffectiveTaskList(Integer instructType, String instructId,
                                                                 String standardCode, Integer alarmLevel,
                                                                 String vin) {
        Map<String, Object> params = new HashMap<>();
        params.put("instructType", instructType);
        params.put("instructId", instructId);
        params.put("standardCode", standardCode);
        params.put("alarmLevel", alarmLevel);
        params.put("vin", vin);

        List<InstructTask> instructTasks = instructTaskMapper.queryControlEffectiveTaskList(params);

        if (CollectionUtils.isNotEmpty(instructTasks)) {
            return instructTasks.stream().map(InstructTaskModel::fromEntry).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }


    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_task", "itask");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<InstructTask> entries = findBySqlId("pagerModel", params);
            List<InstructTaskModel> models = new ArrayList();
            for (InstructTask entry : entries) {
                InstructTask obj = (InstructTask) entry;
                models.add(InstructTaskModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<InstructTaskModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                InstructTask obj = (InstructTask) entry;
                models.add(InstructTaskModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public InstructTaskModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_task", "itask");
        params.put("id", id);
        InstructTask entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return InstructTaskModel.fromEntry(entry);
    }


    @Override
    public void insert(InstructTaskModel model) {

        InstructTask obj = new InstructTask();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(com.bitnei.cloud.common.util.DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(InstructTaskModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_task", "itask");

        InstructTask obj = new InstructTask();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(com.bitnei.cloud.common.util.DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        try {
            int res = super.updateByMap(params);
            if (res == 0) {
                throw new BusinessException("更新失败");
            }
        } catch (Exception e) {
           log.error("error", e);
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_task", "itask");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    /**
     * 远控任务保存下发
     *
     * @param sendRuleParamsDto
     */
    @Override
    public void sendRule(SendRuleParamsDto sendRuleParamsDto) {

        if (sendRuleParamsDto.getStandardCode().equals("6")) {
            if (StringUtils.isNotEmpty(sendRuleParamsDto.getRemarks())) {
                //校验remark
                try {
                    Integer alarmLevelInfo = Integer.valueOf(sendRuleParamsDto.getRemarks().trim());
                    if (alarmLevelInfo < Byte.MIN_VALUE || alarmLevelInfo > Byte.MAX_VALUE) {
                        throw new BusinessException("终端报警/预警，警报信息(备注)数据错误，只支持范围-128到127");
                    }
                } catch (Exception e) {
                    throw new BusinessException("终端报警/预警，警报信息(备注)数据错误，只支持范围-128到127");
                }
            }
        }

        List<String> vehIds;
        if (StringUtils.isEmpty(sendRuleParamsDto.getVehIds()) && null != sendRuleParamsDto.getPagerInfo()) {
            vehIds = getVehicleModelsByPagerInfo(sendRuleParamsDto.getPagerInfo());
        } else {
            vehIds = Arrays.asList(sendRuleParamsDto.getVehIds().split(","));
        }

        String now = com.bitnei.cloud.common.util.DateUtil.getNow();
        String userName = ServletUtil.getCurrentUser();
        String userId = userService.findByUsername(userName).getId();

        Map<String, String> instructMap = dictService.findByDictType("GB_TERM_UNIT_INSTRUCT_TYPE").stream()
                .collect(Collectors.toMap(DictModel::getValue, DictModel::getName));

        //有需要再改成异步
        for (String vehicleId : vehIds) {
            handleSendRule(sendRuleParamsDto, now, userName, userId, instructMap, vehicleId);
        }
    }

    /**
     * 控制命令保存、下发
     */

    public void handleSendRule(SendRuleParamsDto sendRuleParamsDto, String now, String user, String userId,
                               Map<String, String> instructMap, String vehicleId) {
        InstructSendRuleModel sendEntity = new InstructSendRuleModel();
        try {
            BeanUtils.copyProperties(sendRuleParamsDto, sendEntity);
            sendEntity.setSendResult(null);
            sendEntity.setExecuteResult(null);
            sendEntity.setHistoryOnlineState(null);
        } catch (Exception e) {
           log.error("error", e);
        }
        VehicleModel vehicleModel = vehicleService.get(vehicleId);

        boolean isCanSend = true;
        int ruleCount = instructSendRuleService.getImplementRuleCountByVin(vehicleModel.getVin());
        if (ruleCount > 0) {
            sendEntity.setSendResult(1);
            sendEntity.setInstructRemark("车辆存在正在执行的国标控制指令");
            log.error("车辆vin:" + vehicleModel.getVin() + "的车辆存在正在执行的国标控制指令");
            isCanSend = false;
        }
        int vehUppackageTaskCount = uppackageSendDetailsService.getImplementUpgradeCountByVin(vehicleModel.getVin());
        if (vehUppackageTaskCount > 0) {
            sendEntity.setSendResult(1);
            sendEntity.setInstructRemark("车辆存在正在执行的升级任务");
            log.error("车辆vin:" + vehicleModel.getVin() + "的车辆存在正在执行的升级任务");
            isCanSend = false;
        }
        sendEntity.setVin(vehicleModel.getVin());
        sendEntity.setLicensePlate(vehicleModel.getLicensePlate());
        sendEntity.setStandardValue(instructMap.get(sendEntity.getStandardCode()));
        sendEntity.setCreateBy(user);
        sendEntity.setCreateById(userId);
        sendEntity.setCreateTime(now);
        sendEntity.setOperatingArea(vehicleModel.getOperAreaName());
        sendEntity.setOperatingUnit(vehicleModel.getOperUnitName());
        sendEntity.setOperationUserName(vehicleModel.getOwnerName());
        sendEntity.setOperationPhone(vehicleModel.getTelPhone());
        sendEntity.setOperationUserId(vehicleModel.getSellPriVehOwnerId());

        //标记为国标远程终端控制命令：0
        // see 数据库DDL
        sendEntity.setType(0);

        // 车辆是否在线
        Integer vehState = vehicleService.getVehicleOnlineStatus(vehicleModel.getVin());

        sendEntity.setHistoryOnlineState(vehState);
        String instructId = instructSendRuleService.insertReturnId(sendEntity);
        sendEntity.setId(instructId);
        if ((vehState.equals(Constant.VEHICLE_ONLINE)) && isCanSend) {

            VehicleWithOnlineStatus vehicleWithOnlineStatus = new VehicleWithOnlineStatus();
            BeanUtils.copyProperties(vehicleModel, vehicleWithOnlineStatus);
            vehicleWithOnlineStatus.setOnlineStatus(vehState);

            instructTaskExecutor.submit(() -> sendRule(sendEntity, vehicleWithOnlineStatus));
        } else {
            //如果之前的判断就失败了，说明不需要进行缓存
            if (null != sendEntity.getSendResult() && sendEntity.getSendResult().equals(1)) {
                return;
            }

            //如果缓存时间小于或等于0，直接结束
            if (sendRuleParamsDto.getInstructCacheTime() <= 0) {
                sendEntity.setSendResult(1);
                sendEntity.setInstructRemark("车辆当前离线，下发控制指令失败");
                instructSendRuleService.update(sendEntity);
                return;
            }

            sendEntity.setInstructRemark("车辆当前离线，缓存控制指令");
            instructSendRuleService.update(sendEntity);

            // 车辆不在线，缓存控制指令
            editControlTask(true, instructId, sendEntity.getInstructCacheTime(),
                    "车辆当前离线，缓存控制指令", 1);
        }
    }

    @Override
    public String sendInstruct(SendInstructDto sendInstructDto) {

        List<String> vehIds;
        if (StringUtils.isEmpty(sendInstructDto.getVehIds()) && null != sendInstructDto.getPagerInfo()) {
            vehIds = getVehicleModelsByPagerInfo(sendInstructDto.getPagerInfo());
        } else {
            vehIds = Arrays.asList(sendInstructDto.getVehIds().split(","));
        }

        String userName = ServletUtil.getCurrentUser();
        String userId = userService.findByUsername(userName).getId();
        String now = DateUtil.getNow();
        InstructManagementModel instructManagementModel = instructManagementService.get(sendInstructDto.getInstructId());

        //有需要再改成异步
        vehIds.forEach(it -> {
            VehicleModel vehicleModel = vehicleService.get(it);
            handleSendInstruct(instructManagementModel, now, userName, userId,
                    vehicleModel, sendInstructDto.getRemarks());
        });

        return "下发远程命令成功";
    }

    public List<String> getVehicleModelsByPagerInfo(PagerInfo pagerInfo) {
        Object vehicleListResult = vehicleService.vehicleList(pagerInfo);
        if (vehicleListResult instanceof List) {
            List<VehicleModel> vehicleModels = (List<VehicleModel>) vehicleListResult;
            return vehicleModels.stream().map(VehicleModel::getId).collect(Collectors.toList());
        } else {
            PagerResult pr = (PagerResult) vehicleListResult;
            List<Object> data = pr.getData();
            List<VehicleModel> vehicleModels = (List<VehicleModel>) data.get(0);
            return vehicleModels.stream().map(VehicleModel::getId).collect(Collectors.toList());
        }
    }

    /**
     * 保存下发的数据
     * (async)
     */
    public void handleSendInstruct(InstructManagementModel instructManagementModel, String now, String userName,
                                   String userId, VehicleModel vehicleModel, String remarks) {
        //保存基础数据
        InstructSendRuleModel sendEntity = new InstructSendRuleModel();
        sendEntity.setInstructName(instructManagementModel.getName());
        sendEntity.setSendResult(null);
        sendEntity.setExecuteResult(null);
        sendEntity.setHistoryOnlineState(null);
        sendEntity.setVin(vehicleModel.getVin());
        sendEntity.setLicensePlate(vehicleModel.getLicensePlate());
        sendEntity.setCreateBy(userName);
        sendEntity.setCreateById(userId);
        sendEntity.setCreateTime(now);
        sendEntity.setUpdateTime(now);
        sendEntity.setOperatingArea(vehicleModel.getOperAreaName());
        sendEntity.setOperatingUnit(vehicleModel.getOperUnitName());
        sendEntity.setOperationUserName(vehicleModel.getOwnerName());
        sendEntity.setOperationPhone(vehicleModel.getTelPhone());
        sendEntity.setOperationUserId(vehicleModel.getSellPriVehOwnerId());
        sendEntity.setRemarks(remarks);

        //标记为锁车命令 see 数据库DDL
        sendEntity.setType(2);

        //流水号
        sendEntity.setSessionId(redisUtil.getFlowingWater());
        //数据来源1页面执行2接口执行
        sendEntity.setDataSource(1);

        //校验此车辆是否允许下发
        boolean isCanSend = checkIsCanSend(vehicleModel.getVin());

        // 车辆是否在线
        Integer vehState = vehicleService.getVehicleOnlineStatus(vehicleModel.getVin());

        //未上过线，离线，直接标记为失败
        if (vehState.equals(Constant.VEHICLE_OFFLINE) || !isCanSend) {
            //为未上过线
            sendEntity.setSendResult(1);
            sendEntity.setInstructRemark("车辆离线，命令下发失败");
        }
        sendEntity.setHistoryOnlineState(vehState);

        log.info("下发锁车指令：VIN[{}] 状态：[{}]", vehicleModel.getLicensePlate(), vehState);

        String id = instructSendRuleService.insertReturnId(sendEntity);
        sendEntity.setId(id);

        //车辆在线才发送
        if (vehState.equals(Constant.VEHICLE_ONLINE) && isCanSend) {
//                在线且允许下发指令 TODO 发送锁车指令
            log.info("发送锁车指令：车牌：[{}] 状态：[{}]", vehicleModel.getLicensePlate(), vehState);
            VehicleWithOnlineStatus vehicleWithOnlineStatus = new VehicleWithOnlineStatus();
            BeanUtils.copyProperties(vehicleModel, vehicleWithOnlineStatus);
            vehicleWithOnlineStatus.setOnlineStatus(vehState);

            sendEntity.setStandardValue(instructManagementModel.getParamData());
            sendEntity.setInstructName(instructManagementModel.getName());

            String cmd = TermUtils.getLockCarString(vehicleModel, sendEntity);
            log.warn("[远程动力锁车-发送Kafka消息] VIN：[{}],命令:[{}]：", vehicleModel.getVin(), cmd);
            kafkaSender.send(SysDefine.kafkaDataCtrlTopic, "TERM_CTRL", cmd);

            sendInstructRespScheduleExecutor.schedule(() -> {
                InstructSendRuleModel resultEntity = instructSendRuleService.get(sendEntity.getId());

                //如果响应锁车结果不存在，那么进行响应锁车超时逻辑
                if (resultEntity.getExecuteResult() == null) {
                    log.warn("[超时-远程动力锁车-发送Kafka消息] VIN:[{}] 响应超时", vehicleModel.getVin());

                    if (null == resultEntity.getSendResult()) {
                        //如果终端没有控制响应，那么判定终端接收结果超时
                        resultEntity.setSendResult(2);
                    }

                    resultEntity.setInstructRemark("终端响应命令超时");
                    //更新时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    resultEntity.setUpdateTime(sdf.format(new Date()));
                    instructSendRuleService.update(resultEntity);
                }
            }, 1000 * 60L, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 预留操作，某些车辆状态下不可锁车
     *
     * @param vin
     * @return
     */
    public boolean checkIsCanSend(String vin) {
        return true;
    }

    public static void main(String[] args) {
        String test = "123";
        for (byte a : test.getBytes()) {
            System.out.println(a);
        }
    }
}
