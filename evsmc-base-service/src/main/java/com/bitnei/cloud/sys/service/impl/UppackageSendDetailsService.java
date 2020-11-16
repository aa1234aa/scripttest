package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.config.FtpConfig;
import com.bitnei.cloud.sys.dao.UppackageSendDetailsMapper;
import com.bitnei.cloud.sys.dao.UppackageSendMapper;
import com.bitnei.cloud.sys.domain.UppackageSend;
import com.bitnei.cloud.sys.domain.UppackageSendDetails;
import com.bitnei.cloud.sys.model.UppackageInfoModel;
import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleUpgradeLogSumDto;
import com.bitnei.cloud.sys.service.IUppackageInfoService;
import com.bitnei.cloud.sys.service.IUppackageSendDetailsService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.RedisUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UppackageSendDetailsService实现<br>
 * 描述： UppackageSendDetailsService实现<br>
 * 授权 : (C) Copyright (c) 2017 <br>
 * 公司 : 北京理工新源信息科技有限公司<br>
 * ----------------------------------------------------------------------------- <br>
 * 修改历史 <br>
 * <table width="432" border="1">
 * <tr>
 * <td>版本</td>
 * <td>时间</td>
 * <td>作者</td>
 * <td>改变</td>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2019-03-05 15:53:14</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UppackageSendDetailsMapper")
@RequiredArgsConstructor
public class UppackageSendDetailsService extends BaseService implements IUppackageSendDetailsService {

    private final UppackageSendDetailsMapper detailsMapper;
    private final IUppackageInfoService uppackageInfoService;
    private final IVehicleService vehicleService;
    private final RedisUtil redisUtil;
    private final FtpConfig ftpConfig;
    private final UppackageSendMapper uppackageSendMapper;

    @Value("${upgrade.biz.code:U0001}")
    private String bizCode; //默认U0001是吉利,U0002是金龙
    private final String JINLONG="U0002";
    private final String GEELY="U0001";
    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UppackageSendDetails> entries = findBySqlId("pagerModel", params);
            List<UppackageSendDetailsModel> models = new ArrayList();
            for (UppackageSendDetails entry : entries) {
                UppackageSendDetails obj = (UppackageSendDetails) entry;
                models.add(UppackageSendDetailsModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UppackageSendDetailsModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                UppackageSendDetails obj = (UppackageSendDetails) entry;
                models.add(UppackageSendDetailsModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public void exportVehicleUpgradeLog(PagerInfo pagerInfo) {

//        //获取权限sql
//        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
//        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
//
//        PagerResult pagerResult = vehicleService.vehicleListByBaseInfo(pagerInfo);
//        List<VehicleModel> vehicleModels = (List<VehicleModel>) pagerResult.getData().get(0);
//        Map<String, VehicleModel> vinVehicleMap = vehicleModels.stream().collect(
//                Collectors.toMap(VehicleModel::getVin, it -> it));
//
//        //没有记录，直接结束
//        if (CollectionUtils.isEmpty(vehicleModels)) {
//            throw new ExportErrorException("导出记录为空");
//        }
//
//        List<String> vins = vehicleModels.stream().map(VehicleModel::getVin).collect(Collectors.toList());
//        params.put("vins", vins);

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");

        ExcelExportHandler handler = new ExcelExportHandler<UppackageSendDetails>(this,
                "vehicleUpgradeDetails", params,
                "sys/res/upgradeLog/detail_export.xls", "车辆升级记录详情") {
        };

        List<UppackageSendDetails> list = findBySqlId("getAllVehUpgradeLogs", params);

        if (CollectionUtils.isEmpty(list)) {
            throw new ExportErrorException("导出记录为空");
        }

        List<Object> objects = new ArrayList();

        for (int i = 0; i < list.size(); ++i) {
            Object obj = handler.process(list.get(i));
            if (obj != null) {
                objects.add(obj);
            }
        }
        DataLoader.loadNames(objects);
        String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String srcFile = srcBase + "sys/res/upgradeLog/detail_export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("车辆升级记录详情");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(objects);
        String outName = String.format("%s-导出-%s.xls", "车辆升级记录详情", DateUtil.getShortDate());
        EasyExcel.renderResponse(srcFile, outName, ed);

        return;
    }

    @Override
    public List<VehicleUpgradeLogSumDto> importSearchVehicleUpgradeDetails(List<String> vins,
                                                                           List<String> interNos,
                                                                           List<String> licensePlates,
                                                                           List<String> iccids) {

        List<VehicleModel> vehicleModels = vehicleService.vehicleListByBaseInfoList(vins, interNos,
                licensePlates, iccids);

        return getSumDtosByVehicleModels(vehicleModels);
    }

    @Override
    public PagerResult vehicleUpgradeDetails(PagerInfo pagerInfo) {

        PagerResult pagerResult = vehicleService.vehicleListByBaseInfo(pagerInfo);
        List<VehicleModel> vehicleModels = (List<VehicleModel>) pagerResult.getData().get(0);

        List<VehicleUpgradeLogSumDto> result = getSumDtosByVehicleModels(vehicleModels);

        PagerResult pr = new PagerResult();
        pr.setData(Collections.singletonList(result));

        ServletUtil.setPageInfoThreadLocal(pagerInfo);
        pr.setTotal(pagerResult.getTotal());

        return pr;

    }

    @Override
    public PagerResult getAllVehUpgradeLogs() {
        List<UppackageSendDetailsModel> result = getAllVehUpgradeLogsList();
        PagerResult pr = new PagerResult();
        pr.setData(Collections.singletonList(result));
        return pr;
    }

    /**
     * 通过车辆model获取车辆对应的升级记录列表
     *
     * @param vehicleModels
     * @return
     */
    private List<VehicleUpgradeLogSumDto> getSumDtosByVehicleModels(List<VehicleModel> vehicleModels) {

        if (CollectionUtils.isEmpty(vehicleModels)) return new ArrayList<>();

        List<String> vins = vehicleModels.stream().map(VehicleModel::getVin).collect(Collectors.toList());

        //map<vin, upgradeDetails> 车辆vin对应的升级明细map
        List<UppackageSendDetailsModel> details = vehicleUpgradeDetails(vins);

        DataLoader.loadNames(details);

        Map<String, List<UppackageSendDetailsModel>> vinDetailsMap = new HashMap<>();
        details.forEach(it -> {
            List<UppackageSendDetailsModel> perVinDetails;
            if (vinDetailsMap.containsKey(it.getVin())) {
                perVinDetails = vinDetailsMap.get(it.getVin());
            } else {
                perVinDetails = new ArrayList<>();
                vinDetailsMap.put(it.getVin(), perVinDetails);
            }
            perVinDetails.add(it);
        });

        return vehicleModels.stream().map(it -> {
            VehicleUpgradeLogSumDto sumDto = new VehicleUpgradeLogSumDto();
            sumDto.setVin(it.getVin());
            sumDto.setLicensePlate(it.getLicensePlate());
            sumDto.setInterNo(it.getInterNo());
            sumDto.setIccid(it.getIccid());
            sumDto.setDetails(vinDetailsMap.get(it.getVin()));
            return sumDto;
        }).collect(Collectors.toList());
    }

    List<UppackageSendDetailsModel> vehicleUpgradeDetails(List<String> vins) {
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.put("vins", vins);

        List<UppackageSendDetails> details = detailsMapper.vehicleUpgradeDetails(params);
        if (CollectionUtils.isEmpty(details)) return new ArrayList<>();
        return details.stream().map(UppackageSendDetailsModel::fromEntry).collect(Collectors.toList());
    }

    List<UppackageSendDetailsModel> getAllVehUpgradeLogsList() {
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");

        List<UppackageSendDetails> details = detailsMapper.getAllVehUpgradeLogs(params);
        if (CollectionUtils.isEmpty(details)) return new ArrayList<>();
        return details.stream().map(UppackageSendDetailsModel::fromEntry).collect(Collectors.toList());
    }

    @Override
    public UppackageSendDetailsModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.put("id", id);
        UppackageSendDetails entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UppackageSendDetailsModel.fromEntry(entry);
    }

    @Override
    public UppackageSendDetailsModel getOrNull(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.put("id", id);
        UppackageSendDetails entry = unique("findById", params);
        if (entry == null) {
            return null;
        }
        return UppackageSendDetailsModel.fromEntry(entry);
    }

    @Override
    public void insert(UppackageSendDetailsModel model) {

        UppackageSendDetails obj = new UppackageSendDetails();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setUpdateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(UppackageSendDetailsModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");

        UppackageSendDetails obj = new UppackageSendDetails();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        String taskId = model.getUppackageSendId();
        //任务状态——未开始和执行中数量
        Map<String, Object> countParams = new HashMap<>();
        countParams.put("taskId", taskId);
        Integer uppackageCount = unique("findUppackageSendNoStartIngWaitCountByTaskId", countParams);

        //获取当权限的map
        Map<String, Object> uppackageSendParams = new HashMap<>();
        uppackageSendParams.put("id", taskId);
        if (uppackageCount == 0) {
            UppackageSend uppackageSend = uppackageSendMapper.findById(uppackageSendParams);
            uppackageSend.setUppackageSendStatus(2);
            uppackageSendParams.putAll(MapperUtil.Object2Map(uppackageSend));
            uppackageSendMapper.update(uppackageSendParams);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<UppackageSendDetails>(this, "pagerModel", params, "sys/res/uppackageSendDetails/export.xls", "远程升级任务详情") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "UPPACKAGESENDDETAILS" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UppackageSendDetailsModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageSendDetailsModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageSendDetailsModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "UPPACKAGESENDDETAILS" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UppackageSendDetailsModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageSendDetailsModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageSendDetailsModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public void resetState(String taskId, String remark, String vin) {

        Map<String, Object> params = new HashMap<>();
        params.put("taskId", taskId);
        params.put("remark", remark);
        if (StringUtils.isNotEmpty(vin)) {
            params.put("vin", vin);
        }
        detailsMapper.resetState(params);
    }

    @Override
    public List<UppackageSendDetailsModel> getAllByTaskId(String taskId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_send_details", "susd");
        params.put("uppackageSendId", taskId);

        List<UppackageSendDetails> entries = findBySqlId("getAllByTaskId", params);
        List<UppackageSendDetailsModel> models = new ArrayList();
        for (UppackageSendDetails entry : entries) {
            UppackageSendDetails obj = (UppackageSendDetails) entry;
            models.add(UppackageSendDetailsModel.fromEntry(obj));
        }
        return models;
    }

    @Override
    public String initUppackageArgs(UppackageSendDetailsModel uppackageSendDetailsModel,
                                    String uppackageId, String protocolType) {
        Integer protocolTypeInt = Integer.parseInt(protocolType);
        UppackageInfoModel uppackageInfoModel = uppackageInfoService.get(uppackageId);
        String flowNumber ;
        if (!bizCode.equals(JINLONG))
            flowNumber= redisUtil.getFlowNumber();
        else{
            flowNumber= redisUtil.getFlowNumberJinLong();
        }
        String[] test = null;
        if (protocolTypeInt == 1 || protocolTypeInt == 99) {
            test = new String[]{"", "", "", "", "", "", "", "", "", ""};
        } else {
            //如果是吉利自定义
            if (protocolTypeInt == 132) {
                if(bizCode.equals(GEELY)){
                    test = new String[]{"", "", "", "", "", "", "", "", "", "", ""};
                }
                else if (bizCode.equals(JINLONG)){
                    //金龙
                    test = new String[]{"", "", "", "", "", "", "", "", "", "", "",""};
                }
                else {
                    test = new String[]{"", "", "", "", "", "", "", "", "", "", ""};
                }

            } else {
                test = new String[]{"", "", "", "", "", "", "", "", "", "", "", ""};
            }
        }
//        test[0] = "ftp://" + SysDefine.FTP_USERNAME + ":" + SysDefine.FTP_PWD + "@" + SysDefine.FTP_PUBLIC_HOST + ":" + SysDefine.FTP_PORT + "/"+sysUppackageInfoEntity.getPath();//地址
        test[2] = ftpConfig.getUsername();
        test[3] = ftpConfig.getPassword();
        test[4] = ftpConfig.getPublicHost();
        if (protocolTypeInt == 2) {
            test[0] = "ftp://" + ftpConfig.getUsername() + ":" + ftpConfig.getPassword() + "@" + ftpConfig.getHost() + ":"
                    + ftpConfig.getPort() + "/" + uppackageInfoModel.getPath();//地址
        } else if (protocolTypeInt == 99) {
//            test[0] = uppackageSendDetailsModel.getFileName();
            // test[0] = "ftp://" + SysDefine.FTP_USERNAME + ":" + SysDefine.FTP_PWD + "@" + SysDefine.FTP_PUBLIC_HOST + ":" + SysDefine.FTP_PORT ;//地址
            test[0] = "ftp://" + ftpConfig.getUsername() + ":" + ftpConfig.getPassword() + "@" + ftpConfig.getPublicHost()
                    + ":" + ftpConfig.getPort() + "/" + uppackageInfoModel.getPath();//地址
        } else {
            test[0] = "ftp://" + ftpConfig.getUsername() + ":" + ftpConfig.getPassword() + "@" + ftpConfig.getPublicHost()
                    + ":" + ftpConfig.getPort() + "/" + uppackageInfoModel.getPath();//地址
        }
        test[5] = ftpConfig.getPort() + "";//端口
        if (protocolTypeInt != 99) {
            test[7] = uppackageInfoModel.getHardwareVersion();//硬件版本
            test[8] = uppackageInfoModel.getFirmwareVersion();//固件版本ftp://qiye:v5EQryQqVjVM@114.251.179.119:21/admin/
        }
        if (bizCode.equals(JINLONG) && protocolTypeInt != 1 && protocolTypeInt != 99){

            if (StringUtils.isNotBlank(uppackageInfoModel.getExtVersion())){
                test[9]=uppackageInfoModel.getExtVersion();
            }

            test[10] = "2";//超时时限min
        }
        else
        {
            test[9] = "2";//超时时限min
        }
        if (protocolTypeInt == 128 || protocolTypeInt == 132) {
            //如果不是吉利系统 则拼装文件类型
            if (protocolTypeInt != 132) {
                test[10] = uppackageInfoModel.getType();//文件类型
                test[11] = flowNumber; //会话标识
            } else {
                if (bizCode.equals(JINLONG)){

                    test[11] = flowNumber;
                }
                else {
                    test[10] = flowNumber; //会话标识
                }
            }
            uppackageSendDetailsModel.setSessionId(flowNumber);//更改会话标识 为流水号
            update(uppackageSendDetailsModel);
        }
        String testStr = Arrays.toString(test);
        String arg = testStr.substring(1, testStr.length() - 1).replaceAll(",", ";").replaceAll(" ", "");
        log.info("原始命令值：" + arg);
        return arg;
    }

    @Override
    public Integer getImplementUpgradeCountByVin(String vin) {
        return detailsMapper.getImplementUpgradeCountByVin(vin);
    }
}
