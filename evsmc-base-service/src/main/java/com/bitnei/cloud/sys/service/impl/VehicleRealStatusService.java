package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.DataRequest;
import com.bitnei.cloud.common.client.model.RuleTypeEnum;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.screen.protocol.StringUtil;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.VehicleRealStatusMapper;
import com.bitnei.cloud.sys.domain.DataChangeLog;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.domain.VehicleRealStatus;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleRealStatusService实现<br>
 * 描述： VehicleRealStatusService实现<br>
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
 * <td>2019-03-16 14:55:45</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehicleRealStatusMapper")
public class VehicleRealStatusService extends CommonBaseService implements IVehicleRealStatusService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    private final IAttentionVehService attentionVehService;

    private final IUserService userService;

    private final RealDataClient realDataClient;

    private final IDataItemDetailService dataItemDetailService;

    private final IVehicleService vehicleService;


    @Value("${vehicleRealStatus.pageSize:50}")
    private int pageSize;
    @Value("${homepage.default.lng:123.2666015625}")
    private double defaultLng;
    @Value("${homepage.default.lat:51.8629239136}")
    private double defaultLat;

    @Override
    public Object list(PagerInfo pagerInfo) {


        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");

        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        if (null != params.get("isAttention") && params.get("isAttention").equals("true")) {
            params.put("userId", userService.findByUsername(ServletUtil.getCurrentUser()).getId());
        }

        if (StringUtils.isNotBlank(getQueryCondition(pagerInfo,"isFault"))){
            //北京现代app显示车辆实时列表中的是否故障字段
            params.put("isFault",1);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehicleRealStatus> entries = findBySqlId("pagerModel", params);
            List<VehicleRealStatusModel> models = new ArrayList<>();
            for (VehicleRealStatus entry : entries) {
                VehicleRealStatus obj = (VehicleRealStatus) entry;
                if(obj.getPowerMode()==null){
                    obj.setPowerMode(1);
                }
                models.add(VehicleRealStatusModel.fromEntry(obj));
            }
            dealAttentionState(models);
            int page = models.size() / pageSize;
            if (models.size() % pageSize > 0) {
                page += 1;
            }
            for (int i = 0; i < page; i++) {

                fillMulRTData(models.stream().skip((long)(i * pageSize)).limit(pageSize).collect(Collectors.toList()),
                        dataItems());
            }

            return models;

        }
        //分页查询
        else {


            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehicleRealStatusModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                VehicleRealStatus obj = (VehicleRealStatus) entry;
                if (null==obj.getPowerMode()){
                    obj.setPowerMode(1);
                }
                models.add(VehicleRealStatusModel.fromEntry(obj));
            }
            String loadExtra= getQueryCondition(pagerInfo,"loadExtra");
            if (StringUtils.isBlank(loadExtra)||"1".equals(loadExtra)){
                fillMulRTData(models,
                       dataItems());
            }

            dealAttentionState(models);
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private String splitAreaCode(String regionCode) {
        if (StringUtils.isBlank(regionCode)) {
            regionCode = "null";
        }
        String[] regionArr = regionCode.split("\\|");
        if (regionArr.length > 0 && StringUtils.isNumeric(regionArr[0])) {
            regionCode = regionArr[0];
            if (regionArr.length > 1 && StringUtils.isNumeric(regionArr[1])){
                //优先保存市级别区域码
                regionCode = regionArr[1];
            }

        }
        return regionCode;
    }
    private void dealGpsArea(List<VehicleRealStatusModel> models,Map<String, Map<String, String>> redis){
        if (CollectionUtils.isEmpty(models)){
            return;
        }
        if (redis==null||redis.size()==0){
            return;
        }
        models.forEach(f->{
            f.setGpsCityName("");
        });
        Map<String,String> maps= Maps.newHashMap();
        List<Map<String,String>> areaCodes=findBySqlId("selectGpsArea",null);
        if (CollectionUtils.isNotEmpty(areaCodes)){
            areaCodes.forEach(a->{
                if (StringUtils.isNotBlank(a.get("id"))&&StringUtils.isNotBlank(a.get("name"))){
                    maps.put(a.get("id"),a.get("name"));
                }
            });
        }
        models.forEach(m->{
            Map<String,String> data= redis.get(m.getUuid());
            if (data!=null&&StringUtils.isNotBlank(data.get(DataItemKey.GPS_REGION))){
                String code=splitAreaCode(data.get(DataItemKey.GPS_REGION));
                if (maps.containsKey(code)){
                    m.setGpsCityName(maps.get(code));
                }
            }
        });
    }

    /**
     * 填充实时数据和解析经纬度
     *
     * @param models
     * @param dataNos
     */
    private void fillMulRTData(List<VehicleRealStatusModel> models, String... dataNos) {
        try {

            Map<String, Map<String, String>> datas = new HashMap<>();



            DataRequest dataRequest = new DataRequest();
            dataRequest.setColumns(Arrays.asList(dataNos));
            dataRequest.setReadMode(DataReadMode.GPS_ADDRESS | DataReadMode.TRANSLATE);

            //处理国标
            List<VehicleRealStatusModel> g6Models =
                    models.stream().filter(VehicleRealStatusModel::isG6).collect(Collectors.toList());
            List<VehicleRealStatusModel> gbModels = models.stream()
                    .filter(it -> !g6Models.contains(it)).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(g6Models)) {
                String[] uuids = g6Models.stream().map(VehicleRealStatusModel::getUuid).toArray(String[]::new);

                dataRequest.setVids(uuids);
                dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                datas.putAll(realDataClient.findByUuids(dataRequest));
            }

            if (CollectionUtils.isNotEmpty(gbModels)) {
                String[] uuids = gbModels.stream().map(VehicleRealStatusModel::getUuid).toArray(String[]::new);

                dataRequest.setVids(uuids);
                dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
                datas.putAll(realDataClient.findByUuids(dataRequest));
            }

            if (datas != null) {
                models.forEach(model -> {
                    try {
                        String timeVal=model.getUpdateTime();
                        if (StringUtils.isNotBlank(timeVal)&& !timeVal.contains("-")){
                            //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                            timeVal= DateUtil.formatTime(new Date(Long.valueOf(timeVal)),DateUtil.FULL_ST_FORMAT);
                            model.setUpdateTime(timeVal);
                        }
                    }
                    catch (Exception ex){

                    }
                    Map<String, String> valueOfVeh = datas.get(model.getUuid());
                    if (valueOfVeh != null&&valueOfVeh.size()>0) {

                        //车辆状态
                        String vehicleState = valueOfVeh.get(DataItemKey.desc(DataItemKey.item3201));
                        if (StringUtils.isNotBlank(vehicleState)) {

                            model.setVehicleState(vehicleState);
                        }

                        else {
                            model.setVehicleState("异常");
                        }
                        if (model.isG6()){
                            model.setVehicleState("");
                        }
                        String speed = valueOfVeh.get(DataItemKey.VehicleSpeed);
                        if (model.isG6()){
                           speed= valueOfVeh.get(DataItemKey.G6_ITEM_60025);
                        }
                        String gpsDesc = valueOfVeh.get(DataItemKey.desc(DataItemKey.LocateState));
                        if (model.isG6()){
                           gpsDesc= valueOfVeh.get(DataItemKey.desc(DataItemKey.G6_ITEM_60040)); //定位状态
                        }
                        //是否行驶
                        if (StringUtils.isNotBlank(speed)) {
                           double sp= Double.parseDouble(speed);
                            //车辆状态启动速度大于0才认为是行驶
                            model.setIsRun( sp> 0&&StringUtils.isNotBlank(vehicleState)&&valueOfVeh.get(DataItemKey.item3201).equals("1") ? "是" : "否");
                            model.setIsRunValue(sp>0?1:0);

                            if (model.isG6()){
                                model.setIsRun(sp>0?"是":"否");
                                model.setIsRunValue(sp>0?1:0);
                            }

                        } else {
                            model.setIsRun("否");
                            model.setIsRunValue(0);
                        }


                        //GPS 是否定位
//                        if (StringUtils.isNotBlank(gpsDesc)) {
//                            model.setIsGps(gpsDesc);
//                            model.setIsGpsValue("有效定位".equals(gpsDesc)?1:0);
//                        } else {
//                            model.setIsGps("否");
//                            model.setIsGpsValue(0);
//                        }

                        String lng = valueOfVeh.get(DataItemKey.Lng);
                        String lat = valueOfVeh.get(DataItemKey.Lat);
                        String soc = valueOfVeh.get(DataItemKey.SOC);
                        String mils = model.getMils() == null ? "" : String.valueOf(model.getMils());
                        String vol = valueOfVeh.get(DataItemKey.BatTotalV);
                        String current = valueOfVeh.get(DataItemKey.BatTotalI);

                        if (model.isG6()){
                            lng=valueOfVeh.get(DataItemKey.G6_ITEM_60041);
                            lat=valueOfVeh.get(DataItemKey.G6_ITEM_60042);
                            soc=valueOfVeh.get(DataItemKey.G6_ITEM_60039);
                            mils= valueOfVeh.get(DataItemKey.G6_ITEM_60043);
                            vol="1";
                            current="1";

                        }

                        //是否有can
                        if (StringUtils.isAllBlank(speed, lng, lat, soc, mils, vol, current)) {
                            model.setIsCan("否");
                            model.setIsCanValue(0);
                        }

                        else {
                            model.setIsCan("是");
                            model.setIsCanValue(1);
                        }
                        if (StringUtils.isNotBlank(soc)){
                            model.setSoc(Float.valueOf(soc));
                        }

                        if (model.getOnlined() == null || model.getOnlined() == 0){
                            //从未上过线
                            model.setOnlineStatus(0);
                        }
                        if (model.getOnlined() == null || model.getOnlined() == 0
                                ||model.getOnlineStatus()==null
                                ||model.getOnlineStatus()==2
                        ) {
                            //从未上过线或者离线 则相关数据都认为无,比如速度, can

                            model.setVehicleState("");
                            model.setIsRun("否");
                            model.setIsRunValue(0);
//                            model.setIsGps("否");
//                            model.setIsGpsValue(0);
                            model.setIsCan("否");
                            model.setIsCanValue(0);

                        }
                        model.setAddress(valueOfVeh.get("address"));

                    }
                });

            }

            dealGpsArea(models,datas);
        } catch (Exception e) {
            log.error("error:", e);
        }
    }

    /***
     * 简单查询uuid和车辆id
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> listSimple(Map<String, Object> params) {

        return findBySqlId("pagerSimple", params);

    }

    /**
     * 处理关注情况
     *
     * @param models
     */
    private void dealAttentionState(List<VehicleRealStatusModel> models) {

        //获取当前用户所有的关注车辆，转换成Set<id>
        PagerInfo pagerInfo = new PagerInfo();
        Condition condition = new Condition();
        condition.setName("userId");
        condition.setValue(userService.findByUsername(ServletUtil.getCurrentUser()).getId());
        pagerInfo.setConditions(Collections.singletonList(condition));

        List<AttentionVehModel> attentionVehModels = (List<AttentionVehModel>) attentionVehService.list(pagerInfo);
        Set<String> vehIdSet = attentionVehModels.stream().map(AttentionVehModel::getVehId)
            .collect(Collectors.toSet());

        models.forEach(it -> {
            if (!vehIdSet.add(it.getVehicleId())) {
                it.setIsAttention(true);
            }
        });

    }

    @Override
    public VehicleRealStatusModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_real_status", "vs");
        params.put("id", id);
        VehicleRealStatus entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehicleRealStatusModel.fromEntry(entry);
    }

    private void fillRTData(VehicleRealStatusModel model, String... dataNos) {
        try {


            List<String> ds=Arrays.asList(dataNos);

            Map<String, String> datas = new HashMap<>();

            DataRequest dataRequest = new DataRequest();
            dataRequest.setVid(model.getUuid());
            dataRequest.setColumns(ds);
            dataRequest.setReadMode(DataReadMode.TRANSLATE | DataReadMode.GPS_OFFSET_GD | DataReadMode.GPS_ADDRESS);

            if (model.isG6()) {
                dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
            }

            if (model.isGb()) {
                dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            }
            datas = realDataClient.findByUuid(dataRequest);

            if (datas != null&&datas.size()>0&&StringUtils.isNotBlank(datas.get(DataItemKey.RtDateTime))) {


              Map<String,String> mapper= null; //Stream.of(dataNos).collect(Collectors.toMap(k->k,v->v));
                Map<String, String> map ;

                if(!model.isG6()){

                  map=  dataItemDetailService.dataItemsSlimming(Collections.singletonList(datas), mapper, false,true,false).get(0);

                }
                else {
                   map= dataItemDetailService.G6DataItemsSlimming(Collections.singletonList(datas), mapper, false,true,false).get(0);
                    if (null!=map){
                        map.put(DataItemKey.G6_ITEM_60060,model.getFilingTime());
                        map.put(DataItemKey.G6_ITEM_60061,model.getIdentificationId());
                        map.put(DataItemKey.G6_ITEM_60062,model.getTermPubKey());
                        map.put(DataItemKey.G6_ITEM_60063,model.getVin());
                        String result="未备案";
                        if (model.getFilingStatus()!=null){
                            if (model.getFilingStatus()==0){
                                result="备案失败";
                            }
                            else if (model.getFilingStatus()==1){
                                result="备案成功";
                            }
                        }
                        map.put(DataItemKey.G6_ITEM_60064,result);
                    }
                }

                model.setDataItems(map);
                if (StringUtils.isNotBlank(datas.get(DataItemKey.LastUpdateTime))) {
                    String timeVal=datas.get(DataItemKey.LastUpdateTime);
                    if (!timeVal.contains("-")){
                        //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                        timeVal= DateUtil.formatTime(new Date(Long.valueOf(timeVal)),DateUtil.FULL_ST_FORMAT);
                    }
                    model.setUpdateTime(timeVal);
                }
                model.setAddress(datas.getOrDefault("address",""));
            }
            if (model.getOnlined() == null || model.getOnlined() == 0) {
                model.setOnlineStatus(0);
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }


    @Override
    public void insert(VehicleRealStatusModel model) {

        VehicleRealStatus obj = new VehicleRealStatus();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
//        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
//            id = model.getId();
//        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehicleRealStatusModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_real_status", "vs");

        VehicleRealStatus obj = new VehicleRealStatus();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle_real_status", "vs");

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
    public <T> List<T> convertListResult(PagerInfo pagerInfo, boolean translateWarnDataItem, String limitDay) {
        List<T> datas;

        pagerInfo.setLimit(-1);
        Object result = list(pagerInfo);
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() <= 0) {
            datas = (List<T>) result;
        } else {
            //分页
            PagerResult pagerResult = (PagerResult) result;
            if (pagerResult.getData() == null || pagerResult.getData().size() == 0) {
                throw new BusinessException("无数据");
            }
            datas = (List<T>) pagerResult.getData().get(0);
        }
        return datas;
    }

    @Override
    public void export(PagerInfo pagerInfo) {


        List<VehicleRealStatusModel> datas = convertListResult(pagerInfo, false, "");

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<VehicleRealStatus>(this, "pagerModel", params, "sys/res/vehicleRealStatus/export.xls", "车辆动态信息") {

            @Override
            public void work() {

                DataLoader.loadNames(datas);
                String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
                String srcFile = srcBase + this.excelTemplateFile;
                ExcelData ed = new ExcelData();
                ed.setTitle(this.moduleName);
                ed.setExportTime(DateUtil.getNow());
                ed.setData((List) datas);
                String outName = String.format("%s-导出-%s.xls", this.moduleName, DateUtil.getShortDate());
                EasyExcel.renderResponse(srcFile, outName, ed);
            }
        }.work();
        return;


    }

    public static void main(String[] args) {

    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHICLEREALSTATUS" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<VehicleRealStatusModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(VehicleRealStatusModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(VehicleRealStatusModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "VEHICLEREALSTATUS" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<VehicleRealStatusModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(VehicleRealStatusModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(VehicleRealStatusModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 获取数据项速度，如果为空，则速度默认为0
     *
     * @param statuses
     * @return
     */
    private String getSpeedNotNull(Map<String, String> statuses,boolean isG6) {
        String speed ;
        if (!isG6){
            speed= statuses.get(DataItemKey.VehicleSpeed);
        }
        else {
            speed= statuses.get(DataItemKey.G6_ITEM_60025);
        }


        if (StringUtils.isEmpty(speed)) {
            speed = "0";
        }

        return speed;
    }

    @Override
    public void batchUpdate(String sqlId, List<Object> params) {

        this.update("updateByVehicleIdBatch",params);
    }

    @Override
    public PowerOneMakerModel getMakerForPowerOne(String vehId) {
        PowerOneMakerModel powerOneMakerModel = new PowerOneMakerModel();
        //获取实时表取部分数据
        Map<String, String> statuses = supplyBaseMakerInfo(powerOneMakerModel, vehId);

        powerOneMakerModel.setSoc(getVal(statuses,DataItemKey.SOC));

        return powerOneMakerModel;
    }

    @Override
    public PowerTwoMakerModel getMakerForPowerTwo(String vehId) {
        PowerTwoMakerModel powerTwoMakerModel = new PowerTwoMakerModel();
        //获取实时表取部分数据
        Map<String, String> statuses = supplyBaseMakerInfo(powerTwoMakerModel, vehId);

        powerTwoMakerModel.setSoc(getVal(statuses,DataItemKey.SOC));

        //TODO 暂时不知道油量取哪个值
//        powerFourMakerModel.setOil();

        return powerTwoMakerModel;
    }

    @Override
    public PowerThreeMakerModel getMakerForPowerThree(String vehId) {
        PowerThreeMakerModel powerThreeMakerModel = new PowerThreeMakerModel();
        //获取实时表取部分数据
        Map<String, String> statuses = supplyBaseMakerInfo(powerThreeMakerModel, vehId);

        //TODO 时不知道剩余氢量取哪个值
//        powerThreeMakerModel.setHydrogen();

        return powerThreeMakerModel;
    }

    @Override
    public PowerFourMakerModel getMakerForPowerFour(String vehId) {
        PowerFourMakerModel powerFourMakerModel = new PowerFourMakerModel();

        Map<String, String> statuses = supplyBaseMakerInfo(powerFourMakerModel, vehId);

        //TODO 暂时不知道油量取哪个值
//        powerFourMakerModel.setOil();

        //TODO 暂时不知道气量取哪个值
//        powerFourMakerModel.setAir();

        return powerFourMakerModel;
    }

    @Override
    public PowerFiveMakerModel getMakerForPowerFive(String vehId) {
        PowerFiveMakerModel powerFiveMakerModel = new PowerFiveMakerModel();

        Map<String, String> statuses = supplyBaseMakerInfo(powerFiveMakerModel, vehId);

        powerFiveMakerModel.setSoc(getVal(statuses,DataItemKey.SOC));

        //TODO 暂时不知道油量取哪个值
//        powerFiveMakerModel.setOil();

        return powerFiveMakerModel;
    }

    @Override
    public PowerSixMakerModel getMakerForPowerSix(String vehId) {
        PowerSixMakerModel powerSixMakerModel = new PowerSixMakerModel();

        Map<String, String> statuses = supplyBaseMakerInfo(powerSixMakerModel, vehId);

        //我猜剩余油量是这个油箱液位
        powerSixMakerModel.setOil(getVal(statuses,DataItemKey.G6_ITEM_60039));

        return powerSixMakerModel;
    }

    private Map<String, String> supplyBaseMakerInfo(PowerBaseMakerModel powerBaseMakerModel, String vehId) {
        //获取实时表取部分数据
        VehicleRealStatusModel realStatusModel = getByVehicleId(vehId);
        BeanUtils.copyProperties(realStatusModel, powerBaseMakerModel);
        VehicleModel vehicleModel = vehicleService.getByVin(realStatusModel.getVin());
        DataRequest dataRequest = new DataRequest();
        dataRequest.setReadMode(DataReadMode.GPS_ADDRESS |
                DataReadMode.TRANSLATE|DataReadMode.GPS_OFFSET_GD);
        dataRequest.setVid(vehicleModel.getUuid());
        //获取实时数据
        String[] dataItems ;
        Map<String,Object> params= Maps.newHashMap();
        params.put("id",vehId);
        boolean isG6=dataItemDetailService.isG6(params);
        if (!isG6){
            //国标
            dataItems= DataItemKey.getPowerBaseMakerItemArray();
            dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
        }
        else {
            dataItems= DataItemKey.getG6PowerBaseMakerItemArray();
            dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
        }
        dataRequest.setColumns(Arrays.asList(dataItems));

        Map<String, String> statuses = realDataClient.findByUuid(dataRequest);


        if (!isG6){
            if (StringUtils.isNotEmpty(statuses.get(DataItemKey.VehicleDistance))) {
                try {
                    powerBaseMakerModel.setMils(Double.valueOf(statuses.get(DataItemKey.VehicleDistance)));
                } catch (Exception e) {
                    //避免转换失败
                }
            }
            powerBaseMakerModel.setLng(statuses.get(DataItemKey.Lng+".gd"));
            powerBaseMakerModel.setLat(statuses.get(DataItemKey.Lat+".gd"));
            String timeVal=statuses.get(DataItemKey.ServerTime);
            if (StringUtils.isNotBlank(timeVal)&& !timeVal.contains("-")){
                //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                timeVal= DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
            }
            powerBaseMakerModel.setFinalConnectTime(timeVal);
        }
        else
        {
            if (StringUtils.isNotEmpty(statuses.get(DataItemKey.G6_ITEM_60043))) {
                try {
                    powerBaseMakerModel.setMils(Double.valueOf(statuses.get(DataItemKey.G6_ITEM_60043)));
                } catch (Exception e) {
                    //避免转换失败
                }
            }
            powerBaseMakerModel.setLng(statuses.get(DataItemKey.G6_ITEM_60041+".gd"));
            powerBaseMakerModel.setLat(statuses.get(DataItemKey.G6_ITEM_60042+".gd"));
            String timeVal=statuses.get(DataItemKey.G6_ITEM_ServerTime);
            if (StringUtils.isNotBlank(timeVal)&& !timeVal.contains("-")){
                //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                timeVal= DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);

            }
            powerBaseMakerModel.setFinalConnectTime(timeVal);
        }


        String speed = getSpeedNotNull(statuses,isG6);

        powerBaseMakerModel.setSpeed(speed);

        String onlineStatus = statuses.get(DataItemKey.OnlineState);
        if (StringUtils.isEmpty(onlineStatus) || !"1".equals(onlineStatus)) {
            //车辆离线，速度为空
            speed = "";
        }
        powerBaseMakerModel.setIsRun(StringUtils.isEmpty(speed) ? "离线" : "在线");

        //解析地址相关

        powerBaseMakerModel.setAddress(statuses.getOrDefault("address",""));

        //TODO 暂时不知道油量取哪个值
//        powerTwoMakerModel.setOil();
        if (null == powerBaseMakerModel.getPowerMode()) {
            powerBaseMakerModel.setPowerModeDisplay("-");
        }

        return statuses;
    }

    /**
     * 获取数据项速度，如果为空，则速度默认为0
     *
     * @param statuses
     * @return
     */
    private String getSpeedNotNull(Map<String, String> statuses) {
        String speed = statuses.get(DataItemKey.VehicleSpeed);
        if (StringUtils.isEmpty(speed)) {
            speed = "0";
        }

        return speed;
    }
    private String getVal(Map<String, String> statuses,String key) {
        String speed = statuses.get(key);
        if (StringUtils.isEmpty(speed)) {
            speed = "0";
        }

        return speed;
    }

    @Override
   public void updateSingle(String sqlId,Object params){
        this.update(sqlId, params);
    }




    @Override
    public VehicleRealStatusModel getByVehicleId(String vehicleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vehicleId", vehicleId);
        VehicleRealStatus entry = unique("findByVehicleId", params);
        if (entry == null) {
            throw new BusinessException("车辆不存在");
        }
        return VehicleRealStatusModel.fromEntry(entry);
    }

    @Override
    public List<Map<String, Object>> vehOnMap(PagerInfo pagerInfo) {

        return vehOnMap(pagerInfo, false);
    }

    @Override
    public List<Map<String, Object>> vehOnMap(final PagerInfo pagerInfo, final boolean ignorePermission) {
        //只要经纬度大于0的车辆
        addQueryCondition(pagerInfo,"checkLngLat","1");
        Map<String, Object> params = null;
        if (ignorePermission) {
            params = new HashMap<>(10);
        } else {
            params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        }

        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<Map<String, Object>> list = findBySqlId("vehOnMap", params);
        if (list != null && list.size() > 0) {
//            list = list.stream().filter(q ->
//                q.get("lng") != null
//                    && q.get("lat") != null
//                    && (Double) q.get("lng") > 0
//                    && (Double) q.get("lat") > 0
//            ).collect(Collectors.toList());

            list.forEach(q -> {
                q.putIfAbsent("powerMode", 1);

            });
        }
        return list;
    }

    @Override
    public VehicleRealStatusModel getByVehicleId(String vehicleId, String dataItemIds) {
        //获取当权限的map


        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vehicleId", vehicleId);
        VehicleRealStatusModel model = new VehicleRealStatusModel();
        VehicleRealStatus entry = unique("findByVehicleId", params);

        if (entry != null) {
            model = VehicleRealStatusModel.fromEntry(entry);

            //判断要使用哪种协议数据项，返回给前端
            if (model.isG6()) {
                //1、国标， 2、国六
                model.setDataItemType(2);
            } else {
                model.setDataItemType(1);
            }

            //填充实时数据
            VehicleModel v = vehicleService.get(model.getVehicleId());

            fillRTData(
                model,
                dataItemDetailService
                    // 取出数据项
                    .parseDataNos(
                        String.format(
                            "@ruleId@%s",
                            v.getSupportProtocol() == null
                                ? ""
                                : v.getSupportProtocol()
                        )
                    )
                    .stream()
                    // 获取序号
                    .map(dataItem -> dataItem.getSeqNo())
                    // 转成 String 数组
                    .toArray(value -> new String[value])
            );
            Map<String, Object> param = new HashMap<>();
            param.put("vin", model.getVin());
            Calendar calendar = Calendar.getInstance();
            param.put("startTime", String.format("%d-%d-01", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
            param.put("endTime", String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
            List<Map<String,Object>> dayReport = findBySqlId("dayReport", param);
            if (CollectionUtils.isNotEmpty(dayReport)){
               String days= (String)dayReport.get(0).get("days");
               if (StringUtils.isNotBlank(days)){
                  String[] dayArray= days.split(",");
                  if (ArrayUtils.isNotEmpty(dayArray)){
                      for (String today:dayArray){
                          String[] s= today.split("\\|");
                          if( ArrayUtils.isNotEmpty(s)&&s.length==2&&Integer.valueOf(s[0])==calendar.get(Calendar.DAY_OF_MONTH)){
                              String[] milsArray= s[1].split("_");
                              if (ArrayUtils.isNotEmpty(milsArray)&&milsArray.length>1){
                                  dayReport.get(0).put("dayMils",milsArray[1]);
                              }
                          }
                      }

                  }
               }
            }
            model.setDayReport(dayReport);
            Object customFaults = dataItemDetailService.customFaults(v);
            model.setCustomFaults(customFaults);

        } else {
            throw new BusinessException("车辆不存在");
        }
        return model;
    }

    private final IOfflineExportService offlineExportService;

    @Override
    public void exportOffline(PagerInfo pagerInfo) {
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "车辆实时状态导出";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.


        final String exportMethodParams = JSON.toJSONString(pagerInfo);
//        try {
//            exportOfflineProcessor("admin",new Date(),
//                    exportFilePrefixName,
//                    exportMethodParams
//            );
//        } catch (Exception e) {
//           log.error("error", e);
//        }
        // 创建离线导出任务
        offlineExportService.createTask(
            exportFilePrefixName,
            exportServiceName,
            exportMethodName,
            exportMethodParams
        );
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void exportOfflineProcessor(
        @NotNull final String taskId,
        @NotNull final String createBy,
        @NotNull final Date createTime,
        @NotNull final String exportFileName,
        @NotNull final String exportMethodParams) throws Exception {

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);

        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);

        final UserModel userModel = userService.findByUsername(createBy);
        final String userId = userModel.getId();
        final String tableName = "sys_vehicle";
        final String tableAlias = "v";
        final String authSql = DataAccessKit.getUserAuthSql(userId, tableName, tableAlias);
        params.put(Constants.AUTH_SQL, authSql);

        final String excelTemplateFile = "sys/res/vehicleRealStatus/export.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.vehicleRealStatusMapper::pagerModel,
            params,
            this::fromEntityToModel,
            this.vehicleRealStatusMapper::pagerModel,
            redis,
            ws
        );
    }

    @NotNull
    private List<VehicleRealStatusModel> fromEntityToModel(final @NotNull List<VehicleRealStatus> entities) {

        final ArrayList<VehicleRealStatusModel> models = Lists.newArrayList();

        for (final VehicleRealStatus entity : entities) {
            if (null==entity.getPowerMode()){
                entity.setPowerMode(1);
            }
            VehicleRealStatusModel model = VehicleRealStatusModel.fromEntry(entity);

            fillMulRTData(Collections.singletonList(model),
                dataItems());

            models.add(model);
        }

        DataLoader.loadNames(models);
        models.forEach(model->{
            model.setOnlineStatusDisplay("离线");

            if (model.getOnlineStatus() != null && model.getOnlineStatus() == 1) {
                model.setOnlineStatusDisplay("在线");
            }
        });


        return models;
    }

    @Override
    public Map<String, String> findAreaByCode(String code) {
        return unique("findAreaByCode", code);
    }
    @Override
    public List<Map<String,String>> findAreaByCodes(List<String> list){
        return  findBySqlId("findAreaByCodes",list);
    }
    private String[] dataItems(){
      return   new String[]{
              DataItemKey.item3201,DataItemKey.GPS_REGION,
              DataItemKey.LocateState,DataItemKey.G6_ITEM_60040,
              DataItemKey.Lng,DataItemKey.G6_ITEM_60041,
              DataItemKey.Lat, DataItemKey.G6_ITEM_60042,
              DataItemKey.SOC, DataItemKey.G6_ITEM_60039,
              DataItemKey.VehicleSpeed,DataItemKey.G6_ITEM_60025,
              DataItemKey.BatTotalV, DataItemKey.BatTotalI
      };
    }

    final private VehicleRealStatusMapper vehicleRealStatusMapper;
}
