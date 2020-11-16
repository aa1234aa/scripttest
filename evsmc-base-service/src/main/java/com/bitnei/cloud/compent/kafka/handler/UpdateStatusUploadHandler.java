//package com.bitnei.cloud.compent.kafka.handler;
//
//import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
//import com.bitnei.cloud.sys.domain.UppackageSendDetails;
//import com.bitnei.cloud.sys.model.TermParamRecordModel;
//import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
//import com.bitnei.cloud.sys.model.VehicleModel;
//import com.bitnei.cloud.sys.service.ITermParamRecordService;
//import com.bitnei.cloud.sys.service.IUppackageSendDetailsService;
//import com.bitnei.cloud.sys.service.IVehicleService;
//import com.bitnei.cloud.sys.util.DateUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UpdateStatusUploadHandler extends AbstractKafkaHandler {
//
//    private final Environment env;
//
//    private final IUppackageSendDetailsService iUppackageSendDetailsService;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ITermParamRecordService iTermParamRecordService;
//    private final IVehicleService vehicleService;
//
//    private static final String DOWNLOAD_SUCCESS = "1";
//    private static final String DOWNLOAD_FAIL = "0";
//    private static final String UPGRADE_SUCCESS = "3";
//    private static final String UPGRADE_FAIL = "2";
//
//    @Override
//    public boolean handle(String message) {
//    //log.info(message);
//        PlatformPojo platformPojo = PlatformPojo.parse(message);
//
//        //UPDATESTATUS为升级状态上报信息
//        if (platformPojo.getCmd().equals("UPDATESTATUS")) {
//            updateStatus(platformPojo);
//        }
//
//        return true;
//    }
//
//
//    /**
//     * 升级状态上报
//     *
//     * @param platformPojo
//     */
//    private void updateStatus(PlatformPojo platformPojo) {
//
//        //升级进度
//        String UPGRADE_PROGRESS = env.getProperty("upgrade.code.progress", "PROGRESS");
//
//        //升级状态
//        String UPGRADE_STATE = env.getProperty("upgrade.code.state", "STATUS");
//
//        //流水号
//        String SEQ = env.getProperty("upgrade.code.seq", "SEQ");
//
//        if (null == platformPojo.getData()) {
//            log.error("升级信息不存在，升级数据:{}", platformPojo.toString());
//            return;
//        }
//
//        if (!platformPojo.getData().containsKey(UPGRADE_PROGRESS) &&
//                !platformPojo.getData().containsKey(UPGRADE_STATE)) {
//            //如果不包含标准内部编码的升级响应，标准版里面不处理
//            return;
//        }
//
//        log.info("升级状态上报Start");
//        //更新升级任务详情任务下发状态
//        List<UppackageSendDetails> uppackageSendDetailsList =
//                iUppackageSendDetailsService.findBySqlId("findByVinAndUpgradeState", platformPojo.getVin());
//        if (CollectionUtils.isNotEmpty(uppackageSendDetailsList)) {
//            List<UppackageSendDetailsModel> uppackageSendDetailsModels = uppackageSendDetailsList.stream()
//                    .map(UppackageSendDetailsModel::fromEntry).collect(Collectors.toList());
//            for (UppackageSendDetailsModel uppackageSendDetailsModel : uppackageSendDetailsModels) {
//                //判断 流水号 是否一致 如果不一致 忽略
//                if (!platformPojo.getData().get(SEQ).equals(uppackageSendDetailsModel.getSessionId())) {
//                    log.info("流水号不一致忽略 接受" + platformPojo.getData().get(SEQ) + " 数据：" +
//                            uppackageSendDetailsModel.getSessionId());
//                    continue;
//                }
//                //升级进度
//                uppackageSendDetailsModel.setPercentage(platformPojo.getData().get(UPGRADE_PROGRESS));
//
//                //2:下载成功、3:下载失败、4:升级成功、5:升级失败
//                switch (platformPojo.getData().get(UPGRADE_STATE)) {
//                    case DOWNLOAD_SUCCESS:
//                        //文件下发状态：0：未下发、1：已完成、2：下发失败
//                        uppackageSendDetailsModel.setFileSendStatus(1);
//
//                        //有进度的情况下，修改升级状态为升级中，任务状态为进行中
//                        if (Integer.parseInt(platformPojo.getData().get(UPGRADE_PROGRESS)) > 0) {
//                            //升级状态：0未开始、1升级中、2升级成功、3升级失败
//                            uppackageSendDetailsModel.setUpgradeStatus(1);
//                            //任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，
//                            // 5车辆离线、车辆正在等待执行升级任务
//                            uppackageSendDetailsModel.setUppackageSendStatus(1);
//                        }
//                        break;
//                    case DOWNLOAD_FAIL:
//                        //文件下发状态：0：未下发、1：已完成、2：下发失败
//                        uppackageSendDetailsModel.setFileSendStatus(2);
//                        break;
//                    case UPGRADE_SUCCESS:
//                        //升级状态：0未开始、1升级中、2升级成功、3升级失败
//                        uppackageSendDetailsModel.setUpgradeStatus(2);
//
//                        //任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务
//                        uppackageSendDetailsModel.setUppackageSendStatus(2);
//                        sendParamQueryMess(platformPojo.getVin());
//                        break;
//                    case UPGRADE_FAIL:
//                        //升级状态：0未开始、1升级中、2升级成功、3升级失败
//                        uppackageSendDetailsModel.setUpgradeStatus(3);
//
//                        //升级失败以后，任务也要结束
//                        //任务状态：0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务
//                        uppackageSendDetailsModel.setUppackageSendStatus(2);
//                        break;
//                }
//                iUppackageSendDetailsService.update(uppackageSendDetailsModel);
//                log.info("更新升级任务详情任务升级状态");
//            }
//        }
//        log.info("升级状态上报End");
//    }
//
//    /**
//     * 下发查询指令
//     *
//     * @param vin
//     */
//    private void sendParamQueryMess(String vin) {
//        //升级成功后下发终端参数查询，查询4710034 mcu软件版本号、4710035 mpu软件版本号4710036 mpu固件版本号
//        //MPU固件版本-83  MPU软件版本-82 MCU软件版本-81
//        //String dicts = "81|82|83";//十六进制
//        String dicts = "129|130|131";//十进制
//        //终端参数查询
//        VehicleModel vehicle = vehicleService.getByVin(vin);
//        insertTermParamRecord(vehicle);
//        //new Thread(new ParamQueryThread(dicts, vehicle)).start();
//        //在线就发送指令
//        String cmd = String.format("SUBMIT 1 %s GETARG {VID:%s,VTYPE:%s,5001:%s,5002:%s}", vehicle.getVin(), vehicle.getUuid(),
//                vehicle.getVehTypeId(), DateUtil.formatTime(new Date(), DateUtil.DATA_FORMAT), dicts);
//        log.info("============================开始发送查询指令：" + cmd);
//        kafkaTemplate.send("ds_ctrlreq", "GETARG", cmd);
//    }
//
//    /**
//     * 插入参数查询记录
//     *
//     * @param vehicleModel
//     */
//    private void insertTermParamRecord(VehicleModel vehicleModel) {
//        log.info("============================开始插入参数查询记录：" + vehicleModel);
//        if (null != vehicleModel && !StringUtils.isEmpty(vehicleModel.getVin())) {
//            int res = 0;
//            try {
//                //先删除车辆查询记录
//                iTermParamRecordService.deleteBySqlId("delByVin", vehicleModel.getVin());
//                //在插入新纪录
//                TermParamRecordModel entity = new TermParamRecordModel();
//                entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//                entity.setLicensePlate(vehicleModel.getLicensePlate());
//                entity.setVin(vehicleModel.getVin());
//                entity.setSendTime(DateUtil.getNow());
//                entity.setTermCode("版本信息");
//                entity.setState(10);
//                entity.setDescribes("升级指令下发查询");
//                entity.setUpdateTime(entity.getSendTime());
//                entity.setUpdateUser("kafkaservice");
//                entity.setHistoryOnlineState(vehicleModel.getOnlined());
////            entity.setVehicleTypeValue(vehicle.getVehTypeId());
////            entity.setOperatingUnit(vehicle.getUseUintId());
//                entity.setReceiveState(0);
//                entity.setOperation("响应自动下发查询指令");
//                res = iTermParamRecordService.insert(entity);
//                log.info("============================插入参数查询记录结果：" + res);
//            } catch (Exception e) {
//                log.info("============================插入参数查询记录报错：" + e);
//                log.error("error", e);
//            }
//        }
//        log.info("============================插入参数查询记录结束");
//    }
//}
