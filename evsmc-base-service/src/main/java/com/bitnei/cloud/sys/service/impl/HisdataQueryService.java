package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.HisdataQuery;
import com.bitnei.cloud.sys.domain.VehicleRealStatus;
import com.bitnei.cloud.sys.model.HisdataQueryModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleRealStatusModel;
import com.bitnei.cloud.sys.service.IDataItemDetailService;
import com.bitnei.cloud.sys.service.IHisdataQueryService;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： HisdataQueryService实现<br>
 * 描述： HisdataQueryService实现<br>
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
 * <td>2019-03-22 16:01:36</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.HisdataQueryMapper")
public class HisdataQueryService extends CommonBaseService implements IHisdataQueryService {


    @Value("${hisdataQuery.track.timeSpan:3}")
    private int timeSpan;
    private final IDataItemDetailService dataItemDetailService;

    private final IVehicleRealStatusService vehicleRealStatusService;

    public static void main(String[] args) {
    String  updateTime= com.bitnei.cloud.sys.util.DateUtil.converseStr("20190330150356");
    int a=0;

    }


    private VehicleRealStatusModel fillVeh(VehicleModel vehicleModel){
        VehicleRealStatusModel vehicleRealStatusModel=new VehicleRealStatusModel();
        if (null!=vehicleModel) {
            Map<String, String> para = Maps.newHashMap();

            para.put("vin", vehicleModel.getVin());

            List<VehicleRealStatus> vehicleRealStatusModels = vehicleRealStatusService.findBySqlId("pagerModel", para);
            if (CollectionUtils.isNotEmpty(vehicleRealStatusModels)) {

                vehicleRealStatusModel =VehicleRealStatusModel.fromEntry(vehicleRealStatusModels.get(0));

                DataLoader.loadNames(vehicleRealStatusModel);

                vehicleModel.setTermSerialNumber(vehicleRealStatusModel.getTermSerialNumber());
                vehicleModel.setPowerModeDisplay(vehicleRealStatusModel.getPowerModeDisplay());
                vehicleModel.setTermUnitName(vehicleRealStatusModel.getTermUnitName());
                vehicleModel.setOperSupportOwnerName(vehicleRealStatusModel.getOperSupportOwnerName());
                vehicleModel.setFirstRegTime(vehicleRealStatusModel.getFirstRegTime());
                vehicleModel.setOperUnitName(vehicleRealStatusModel.getOperUnitName());
                vehicleModel.setVehBrandName(vehicleRealStatusModel.getVehBrandName());
                vehicleModel.setOperLicenseCityName(vehicleRealStatusModel.getOperLicenseCityName());
                vehicleModel.setTermModelName(vehicleRealStatusModel.getTermModelName());
                vehicleModel.setVehSeriesName(vehicleRealStatusModel.getVehSeriesName());
                vehicleModel.setSellCityName(vehicleRealStatusModel.getSellCityName());
                vehicleModel.setRuleTypeName(vehicleRealStatusModel.getRuleTypeName());
                vehicleModel.setOwnerName(vehicleRealStatusModel.getOwnerName());
                vehicleModel.setVehUnitName(vehicleRealStatusModel.getVehUnitName());



            }
        }
        return vehicleRealStatusModel;
    }
    @Override
    public Object list(PagerInfo pagerInfo) {

        //hisdataType=0 历史数据， hisdataType=1  整段轨迹，hisdataType=2 分段轨迹
        String hisdataType =getQueryCondition(pagerInfo,"hisdataType");

        if (StringUtils.isBlank(hisdataType)) {
            throw new BusinessException("请输入查询类型");
        }

        Condition condition= pagerInfo.getConditions().stream().filter(f-> "dataNos".equals(f.getName())).findFirst().orElse(null);
        if (condition==null){
            condition=addQueryCondition(pagerInfo,"dataNos","");

        }
        VehicleModel vehicleModel=  dataItemDetailService.findVehicle(pagerInfo);
        Map<String,Object> param=Maps.newHashMap();
        param.put("supportProtocol",vehicleModel.getSupportProtocol());
        boolean isG6= dataItemDetailService.isG6(param);
        vehicleModel.setIsG6(isG6?1:0);
        String key=DataItemKey.RtDateTime,lng=DataItemKey.Lng,lat=DataItemKey.Lat,speed=DataItemKey.VehicleSpeed;
        String timeSeq=","+DataItemKey.RtDateTime;
        if ("1".equals(hisdataType)|| "2".equals(hisdataType)){
            //轨迹数据只查询部分数据项
            addQueryCondition(pagerInfo,"dataReadMode",String.valueOf(DataReadMode.TRANSLATE|
                    DataReadMode.GPS_OFFSET_GD|
                    DataReadMode.GPS_ADDRESS));

            List<String> ls;
            List<Map<String,String>> wholeVehDataItem;
         if (!isG6){
             //国标

             addQueryCondition(pagerInfo,"groupName","整车");
             wholeVehDataItem = dataItemDetailService.findDataItemListByRuleId(pagerInfo);
             ls=  Arrays.asList(
                     DataItemKey.MaxVPackIndex, DataItemKey.MaxVPos,DataItemKey.BatMaxV,
                     DataItemKey.MinVPackIndex, DataItemKey.MinVPos,DataItemKey.BatMinV,
                     DataItemKey.MaxTPackIndex, DataItemKey.MaxTPos,DataItemKey.BatMaxT,
                     DataItemKey.MinTPackIndex, DataItemKey.MinTPos, DataItemKey.BatMinT,
                     DataItemKey.Lng,DataItemKey.Lat,DataItemKey.BatVols,DataItemKey.BatTemps
             );
         }
         else {
             //国6
             wholeVehDataItem= dataItemDetailService.findDataItemListByRuleId(pagerInfo);
//             ls=Arrays.asList(DataItemKey.G6_ITEM_60041,DataItemKey.G6_ITEM_60042,
//                        DataItemKey.G6_ITEM_60025,DataItemKey.G6_ITEM_60043,
//                     DataItemKey.G6_ITEM_60011
//                     );
             ls=Lists.newArrayList();
         }



          ls.forEach(f->

                  {
                      Map<String,String> m=Maps.newHashMap();
                      m.put("seq_no",f);
                      wholeVehDataItem.add(m);
                  }
          );

           condition.setValue(wholeVehDataItem.stream().map(f-> f.get("seq_no")
           ).collect(Collectors.joining(",","@seq@",timeSeq)));

        }
        else {
            //历史数据

            List<Map<String,String>> dataitemList;
            //历史数据,查询车辆关联的协议数据项
//            if (!isG6){
//                //国标
//                addQueryCondition(pagerInfo,"dataReadMode",String.valueOf(DataReadMode.TRANSLATE|
//                        DataReadMode.GPS_ADDRESS|DataReadMode.GPS_OFFSET_GD));
//
//                dataitemList=dataItemDetailService.findDataItemListByRuleId(pagerInfo);
//            }
//            else {
//
//                //国6
//                addQueryCondition(pagerInfo,"dataReadMode",String.valueOf(DataReadMode.TRANSLATE|
//                        DataReadMode.GPS_ADDRESS|DataReadMode.GPS_OFFSET_GD));
//
//                dataitemList=dataItemDetailService.findDataItemListByRuleId(pagerInfo);
//            }
            addQueryCondition(pagerInfo,"dataReadMode",String.valueOf(DataReadMode.TRANSLATE|
                    DataReadMode.GPS_ADDRESS|DataReadMode.GPS_OFFSET_GD));

            dataitemList=dataItemDetailService.findDataItemListByRuleId(pagerInfo);
           if (dataitemList==null||dataitemList.size()==0){
               throw new BusinessException("车辆未关联有效数据项");
           }
            condition.setValue(dataitemList.stream().map(f->f.get("seq_no")).collect(Collectors.joining(",","@seq@",timeSeq)));

        }

        //不使用分页
        //从第一页导出
        pagerInfo.setStart(0);
        pagerInfo.setLimit(null);
        List<Map<String, String>> datas=dataItemDetailService.convertListResult(pagerInfo,false,"1") ;


        if (datas != null && datas.size() > 0) {

           final String time=key,fLng=lng,fLat=lat,fSpeed=speed;

            //过滤掉经纬度不合法的记录
            datas = datas.stream().filter(
                    d -> {

                        if (d==null) {
                            return false;
                        }
                        if (StringUtils.isBlank(d.get(time))){
                            return false;
                        }
                        if (!d.containsKey(fLng)||!d.containsKey(fLat)) {
                            return true;
                        }

                        return StringUtils.isNotBlank(d.get(fLng))
                                && StringUtils.isNotBlank(d.get(fLat))
                                && Double.valueOf(d.get(fLng)) > 0
                                && Double.valueOf(d.get(fLat)) > 0;

                    }
            )
                    .collect(Collectors.toList());
            if ("0".equals(hisdataType)){
                //历史数据添加行驶状态
                datas.forEach(f->{
                    String speedVal = f.get(fSpeed);
                    if (StringUtils.isNotBlank(speedVal)) {
                        //是否行驶
                        f.put("is_run",Double.parseDouble(speedVal) > 0 ? "是" : "否");
                    } else {
                        f.put("is_run","否");
                    }
                });
            }


            List<HisdataQueryModel> segment = new ArrayList<>();

            VehicleRealStatusModel vehicleRealStatusModel=fillVeh(vehicleModel);
            switch (hisdataType) {
                case "0":
                    //历史数据

                    HisdataQueryModel hisdataQueryModel = new HisdataQueryModel();
                    hisdataQueryModel.setVehicleRealStatusModel(vehicleRealStatusModel);
                    hisdataQueryModel.setVeh(vehicleModel);

                    hisdataQueryModel.setSubdata(datas);
                    if(CollectionUtils.isNotEmpty(datas)&&isG6){
                      VehicleRealStatusModel model=  vehicleRealStatusService.getByVehicleId(vehicleModel.getId());
                        datas.forEach(map->{
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
                        });
                    }
                    segment.add(hisdataQueryModel);
                    break;
                case "1":

//整段
                    HisdataQueryModel model=HisdataQueryModel.create(datas,vehicleModel);

                    model.setVehicleRealStatusModel(vehicleRealStatusModel);
                    segment.add(model);

                    break;
                case "2":
//分段
                    if (datas.size() == 1) {

                        HisdataQueryModel model1= HisdataQueryModel.create(datas,vehicleModel);
                        model1.setVehicleRealStatusModel(vehicleRealStatusModel);
                        segment.add(model1);
                    } else {
                        int index = 0;
                        for (int i = 0; i < datas.size() - 1; i++) {

                            int diff = DateUtil.getTimesDiff(3,
                                    datas.get(i).get(key),
                                    datas.get(i + 1).get(key)
                            );
                            if (Math.abs(diff) >= timeSpan) {
                                //3分钟分段
                                List<Map<String, String>> trackData = datas.stream().skip(index).limit((long)i - index + 1).collect(Collectors.toList());
                                HisdataQueryModel md=HisdataQueryModel.create(trackData,vehicleModel);
                                md.setVehicleRealStatusModel(vehicleRealStatusModel);
                                segment.add(md);
                                index += trackData.size();
                            }

                        }
                        if (index < datas.size()) {

                            List<Map<String, String>> o = datas.stream().skip(index).limit((long)(datas.size() - index)).collect(Collectors.toList());
                           HisdataQueryModel m= HisdataQueryModel.create(o,vehicleModel);
                           m.setVehicleRealStatusModel(vehicleRealStatusModel);
                            segment.add(m);
                        }

                    }


                    break;
                default:
                    throw new BusinessException("不支持的查询类型");
            }
            return segment;
        }
        throw new BusinessException("无数据");

    }


    @Override
    public HisdataQueryModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_hisdata_query", "hisdata_q");
        params.put("id", id);
        HisdataQuery entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return HisdataQueryModel.fromEntry(entry);
    }


    @Override
    public void insert(HisdataQueryModel model) {

        HisdataQuery obj = new HisdataQuery();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();

        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(HisdataQueryModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_hisdata_query", "hisdata_q");

        HisdataQuery obj = new HisdataQuery();
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_hisdata_query", "hisdata_q");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_hisdata_query", "hisdata_q");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<HisdataQuery>(this, "pagerModel", params, "sys/res/hisdataQuery/export.xls", "历史数据查询") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "HISDATAQUERY" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<HisdataQueryModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(HisdataQueryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(HisdataQueryModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "HISDATAQUERY" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<HisdataQueryModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(HisdataQueryModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(HisdataQueryModel model) {
                update(model);
            }
        }.work();

    }


}
