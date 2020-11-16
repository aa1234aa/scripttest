package com.bitnei.cloud.compent.kafka.handler;

import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.compent.kafka.domain.PlatformPojo;
import com.bitnei.cloud.sys.domain.TermParamRecord;
import com.bitnei.cloud.sys.model.TermParamRecordModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleVersionInfoModel;
import com.bitnei.cloud.sys.service.ITermParamRecordService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.service.IVehicleVersionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueryResponseHandler extends AbstractKafkaHandler {

    private static final String GETARG_ANSWER_TIME = "5101";        //查询应答消息应答时间

    private final ITermParamRecordService iTermParamRecordService;

    private final IVehicleVersionInfoService vehicleVersionInfoService;

    private final IVehicleService vehicleService;

    @Override
    public boolean handle(String message) {

        PlatformPojo platformPojo = PlatformPojo.parse(message);

        //UPDATESTATUS为升级状态上报信息
        if (platformPojo.getCmd().equals("GETARG")) {
            getArg(platformPojo);
        }

        return false;
    }


    /**
     * 查询应答消息处理
     * REPORT 1 12312312312312312 GETARG {VID:4c79c42c-52d6-4800-a3ce-1f4bd54d10c4,VTYPE:228,RET:0,5101:20180125151435,5102:50,5107:50}
     *
     * @param platformPojo
     */
    private void getArg(PlatformPojo platformPojo) {
        log.info("处理查询命令返回Start");
        //处理查询命令返回
        List<TermParamRecord> termParamRecordList = iTermParamRecordService.findBySqlId(
                "findByVinAndState", platformPojo.getVin());
        if (!termParamRecordList.isEmpty()) {

            List<TermParamRecordModel> termParamRecordModels = termParamRecordList.stream().map(
                    TermParamRecordModel::fromEntry).collect(Collectors.toList());

            //只操作第一条
            TermParamRecordModel termParamRecord = termParamRecordModels.get(0);

            ///** 10: 成功  20： 错误： 30 ：超时 **/
            termParamRecord.setState(Integer.parseInt(platformPojo.getData().get("RET")) == 0 ? 10 : 20);

            //如果是成功，把错误信息置为空
            if (termParamRecord.getState().equals(10)) {
                termParamRecord.setErrorMessage("");
            } else if (termParamRecord.getState().equals(20)) {
                termParamRecord.setErrorMessage("终端响应查询错误");
            }

            //应答时间
            termParamRecord.setResponseTime(DateUtil.formatTime(
                    DateUtil.strToDate_ex(platformPojo.getData().get(GETARG_ANSWER_TIME)), DateUtil.FULL_ST_FORMAT));
            //kafka反馈状态,1:已反馈
            termParamRecord.setReceiveState(1);
            //查询应答报文
            termParamRecord.setParamValues(JSONObject.fromObject(platformPojo.getData()).toString());
            iTermParamRecordService.update(termParamRecord);

            this.addAndUpdateVehicleVersionInfo(platformPojo, termParamRecordList);
        }
        log.info("处理查询命令返回End");
    }

    /**
     * 更新车辆版本信息
     *
     * @param platformPojo
     * @param termParamRecordList
     */
    private void addAndUpdateVehicleVersionInfo(PlatformPojo platformPojo, List<TermParamRecord> termParamRecordList) {
        Map<String, String> map = platformPojo.getData();
        log.info("============【更新车辆版本信息】===============开始解析查询消息：" + map);
        if (map != null) {
            //4710034 mcu软件版本号
            String mcuSoftwareVersion = map.getOrDefault("4710034", "-999").toString().trim();
            //4710035 mpu软件版本号
            String mpuSoftwareVersion = map.getOrDefault("4710035", "-999").toString().trim();
            //4710036 mpu固件版本号
            String mpuFirmwareVersion = map.getOrDefault("4710036", "-999").toString().trim();
            if (!"-999".equals(mcuSoftwareVersion) || !"-999".equals(mpuSoftwareVersion) || !"-999".equals(mpuFirmwareVersion)) {
                VehicleVersionInfoModel vehicleVersionInfoModel = new VehicleVersionInfoModel();
                String date = DateUtil.getNow();
                vehicleVersionInfoModel.setUpdateTime(date);
                if (!"-999".equals(mcuSoftwareVersion)) {
                    vehicleVersionInfoModel.setMcuSoftwareVersion(mcuSoftwareVersion);
                }
                if (!"-999".equals(mpuSoftwareVersion)) {
                    vehicleVersionInfoModel.setMpuSoftwareVersion(mpuSoftwareVersion);
                }
                if (!"-999".equals(mpuFirmwareVersion)) {
                    vehicleVersionInfoModel.setMpuFirmwareVersion(mpuFirmwareVersion);
                }
                String vin = termParamRecordList.get(0).getVin();
                Map<String, String> mapTemp = new HashMap<>();
                mapTemp.put("vin", vin);
                VehicleVersionInfoModel versionInfoModel = vehicleVersionInfoService.findByVin(mapTemp);
                //versionInfoModel 对象为空则进行插入操作，不为空执行更新操作
                if (versionInfoModel == null) {
                    VehicleModel vehicleModel = vehicleService.getByVin(vin);

                    vehicleVersionInfoModel.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    vehicleVersionInfoModel.setVin(vin);
                    vehicleVersionInfoModel.setSerialNumber(vehicleModel.getSerialNumber());
                    vehicleVersionInfoModel.setIccid(vehicleModel.getIccid());
                    vehicleVersionInfoModel.setCreateTime(date);
                    vehicleVersionInfoModel.setState(1);
                    vehicleVersionInfoModel.setUpdateBy(date);
                    vehicleVersionInfoModel.setDataSource(3);
                    vehicleVersionInfoService.insert(vehicleVersionInfoModel);
                    log.info("============【更新车辆版本信息】===============插入结束：");
                } else {
                    vehicleVersionInfoModel.setVin(versionInfoModel.getVin());
                    vehicleVersionInfoModel.setDataSource(3);
                    vehicleVersionInfoService.update("updateByVin", vehicleVersionInfoModel);
                    log.info("============【更新车辆版本信息】===============更新结束：");
                }
            }
        }
        log.info("============【更新车辆版本信息】===============执行结束");
    }
}
