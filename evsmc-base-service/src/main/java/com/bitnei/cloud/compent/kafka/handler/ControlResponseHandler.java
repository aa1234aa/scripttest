package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.compent.kafka.service.KafkaSender;
import com.bitnei.cloud.compent.kafka.service.LocalThreadFactory;
import com.bitnei.cloud.sys.domain.InstructSendRule;
import com.bitnei.cloud.sys.domain.UppackageSendDetails;
import com.bitnei.cloud.sys.model.InstructSendRuleModel;
import com.bitnei.cloud.sys.model.TermParamRecordModel;
import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IInstructSendRuleService;
import com.bitnei.cloud.sys.service.ITermParamRecordService;
import com.bitnei.cloud.sys.service.IUppackageSendDetailsService;
import com.bitnei.cloud.sys.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ControlResponseHandler extends AbstractKafkaHandler {

    private final IInstructSendRuleService iInstructSendRuleService;
    private final IUppackageSendDetailsService iUppackageSendDetailsService;
    private final KafkaSender kafkaSender;
    private final IVehicleService vehicleService;
    private final ITermParamRecordService iTermParamRecordService;
    private final Environment env;
    private final ScheduledExecutorService upgradeDownFileRespScheduleExecutor;

    @Value("${upgrade.biz.code:U0001}")
    private String bizCode; //默认U0001是吉利,U0002是金龙
    private final String JINLONG="U0002";
    private final String GEELY="U0001";
    @Override
    public boolean handle(String message) {
        PlatformPojo platformPojo = PlatformPojo.parse(message);

        //CONTROL为控制应答消息处理
        if (platformPojo.getCmd().equals("CONTROL")) {
            control(platformPojo);
        }

        return true;
    }

    /**
     * 控制应答消息处理
     *
     * @param platformPojo
     */
    protected void control(PlatformPojo platformPojo) {
        log.info("消息应答处理Start");

        int interval=2;
        if (bizCode.equals(JINLONG)){
            interval=15;
        }
        //更新远程终端控制指令发送状态
        List<InstructSendRule> instructSendRuleList =
                iInstructSendRuleService.findBySqlId("findByVinAndSendResult", platformPojo.getVin());
        if (!instructSendRuleList.isEmpty()) {
            log.info("终端控制指令查询条数：" + instructSendRuleList);
            for (InstructSendRule instructSendRule : instructSendRuleList) {
                //发送状态0：成功、1：失败
                instructSendRule.setSendResult(Integer.valueOf(platformPojo.getData().get("RET")));
                InstructSendRuleModel model = InstructSendRuleModel.fromEntry(instructSendRule);

                //如果控制响应失败，补充失败原因
                if (model.getSendResult().equals(1)) {
                    model.setInstructRemark("终端响应控制失败");
                }

                iInstructSendRuleService.update(model);
                log.info("更新远程终端控制指令发送状态");
            }
        }

        //更新升级任务详情任务下发状态
        List<UppackageSendDetails> uppackageSendDetailsList =
                iUppackageSendDetailsService.findBySqlId("findByVinAndSendState", platformPojo.getVin());
        if (!uppackageSendDetailsList.isEmpty()) {
            log.info("更新升级任务详情任务下发状态条数：" + uppackageSendDetailsList.size());
            for (UppackageSendDetails uppackageSendDetails : uppackageSendDetailsList) {
                //升级指令下发状态 0:unbegining, 1:success, 2:failed
                uppackageSendDetails.setUpgradeSendState((Integer.valueOf(platformPojo.getData().get("RET"))) + 1);
                //如果指令下发状态是失败，那么任务状态也要改为失败
                //"UpgradeSendState" 升级指令下发状态 0未开始；1下发成功；2下发失败；9终端离线下发失败
                if (uppackageSendDetails.getUpgradeSendState() == 2) {
                    uppackageSendDetails.setUppackageSendStatus(4);
                }

                //接收到响应之后，如果是国标指令，任务状态改为已结束
                if (uppackageSendDetails.getProtocolType().equals(1) ||
                        uppackageSendDetails.getProtocolType().equals(2)) {
                    /** 任务状态：0: 未开始、1: 进行中、2: 已结束 **/
                    uppackageSendDetails.setUppackageSendStatus(2);
                }

                UppackageSendDetailsModel model = UppackageSendDetailsModel.fromEntry(uppackageSendDetails);
                iUppackageSendDetailsService.update(model);
                log.info("更新升级任务详情任务下发状态");
                log.info("===========================升级指令下发状态：" + uppackageSendDetails.getUpgradeSendState());
                if (uppackageSendDetails.getUpgradeSendState() == 1) {
                    sendParamQueryMess(platformPojo.getVin());

                    //如果升级指令下发成功，并且是吉利自定义的，要等待升级响应，否则认为超时失败
                    //吉利自定义的protocolType是132
                    if (!uppackageSendDetails.getProtocolType().equals(1) &&
                            !uppackageSendDetails.getProtocolType().equals(2)) {

                        upgradeDownFileRespScheduleExecutor.schedule(() -> {

                            UppackageSendDetailsModel detailsModel =
                                    iUppackageSendDetailsService.get(uppackageSendDetails.getId());

                            //如果文件下发状态还是未下发，说明终端下载文件出问题
                            if (detailsModel.getFileSendStatus().equals(0)||detailsModel.getFileSendStatus().equals(2)) {
                                log.warn("车辆VIN：" + detailsModel.getVin() + "文件下发，升级状态响应超时");
                                detailsModel.setUppackageSendStatus(4);
                                detailsModel.setRemark("升级状态响应超时，升级失败");
                                iUppackageSendDetailsService.update(detailsModel);
                            }

                        }, env.getProperty("cache.command.upgrade.status",
                                Long.class, interval * 60 * 1000L), TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
        log.info("消息应答处理End");
    }


    /**
     * 下发查询指令
     *
     * @param vin
     */
    private void sendParamQueryMess(String vin) {
        //升级成功后下发终端参数查询，查询4710034 mcu软件版本号、4710035 mpu软件版本号4710036 mpu固件版本号
        //MPU固件版本-83  MPU软件版本-82 MCU软件版本-81
        //String dicts = "81|82|83";//十六进制
        String dicts = "129|130|131";//十进制
        //终端参数查询
        VehicleModel vehicle = vehicleService.getByVin(vin);
        insertTermParamRecord(vehicle);
        //new Thread(new ParamQueryThread(dicts, vehicle)).start();
        //在线就发送指令
        String cmd = String.format("SUBMIT 1 %s GETARG {VID:%s,VTYPE:%s,5001:%s,5002:%s}", vehicle.getVin(),
                vehicle.getUuid(), vehicle.getVehTypeId(),
                DateUtil.formatTime(new Date(), DateUtil.DATA_FORMAT), dicts);
        log.info("============================开始发送查询指令：" + cmd);
        kafkaSender.send("ds_ctrlreq", "GETARG", cmd);
    }


    /**
     * 插入参数查询记录
     *
     * @param vehicle
     */
    private void insertTermParamRecord(VehicleModel vehicle) {
        log.info("============================开始插入参数查询记录：" + vehicle);
        if (null != vehicle) {
            int res = 0;
            try {
                //先删除车辆查询记录
                iTermParamRecordService.deleteBySqlId("delByVin", vehicle.getVin());
                TermParamRecordModel entity = new TermParamRecordModel();
                entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                entity.setLicensePlate(vehicle.getLicensePlate());
                entity.setVin(vehicle.getVin());
                entity.setSendTime(DateUtil.getNow());
                entity.setTermCode("版本信息");
                entity.setState(10);
                entity.setDescribes("升级指令下发查询");
                entity.setUpdateTime(entity.getSendTime());
                entity.setUpdateUser("kafkaservice");
                entity.setHistoryOnlineState(vehicle.getOnlined());
//            entity.setVehicleTypeValue(vehicle.getVehTypeId());
//            entity.setOperatingUnit(vehicle.getUseUintId());
                entity.setReceiveState(0);
                entity.setOperation("响应自动下发查询指令");
                res = iTermParamRecordService.insert(entity);
                log.info("============================插入参数查询记录结果：" + res);
            } catch (Exception e) {
                log.info("============================插入参数查询记录报错：" + e);
               log.error("error", e);
            }
        }
        log.info("============================插入参数查询记录结束");
    }
}
