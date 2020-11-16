package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.BatteryDeviceModel;
import com.bitnei.cloud.sys.domain.EngeryDevice;
import com.bitnei.cloud.sys.domain.SuperCapacitorModel;
import com.bitnei.cloud.sys.model.EnergyDeviceTempModel;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.bitnei.cloud.sys.service.IBatteryDeviceModelService;
import com.bitnei.cloud.sys.service.IEngeryDeviceService;
import com.bitnei.cloud.sys.service.ISuperCapacitorModelService;
import com.bitnei.cloud.sys.service.IVehicleEngeryDeviceLkService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EngeryDeviceService实现<br>
* 描述： EngeryDeviceService实现<br>
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
* <td>2018-12-06 23:59:45</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.EngeryDeviceMapper" )
public class EngeryDeviceService extends BaseService implements IEngeryDeviceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBatteryDeviceModelService batteryDeviceModelService;
    @Autowired
    private ISuperCapacitorModelService superCapacitorModelService;
    @Autowired
    private IVehicleEngeryDeviceLkService engeryDeviceLkService;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<EngeryDevice> entries = findBySqlId("pagerModel", params);
            List<EngeryDeviceModel> models = new ArrayList();
            for(EngeryDevice entry: entries){
                EngeryDevice obj = (EngeryDevice)entry;
                models.add(EngeryDeviceModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<EngeryDeviceModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                EngeryDevice obj = (EngeryDevice)entry;
                models.add(EngeryDeviceModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public EngeryDeviceModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.put("id",id);
        EngeryDevice entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return EngeryDeviceModel.fromEntry(entry);
    }

    @Override
    public EngeryDeviceModel findByName(String name) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.put("name",name);
        EngeryDevice entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("可充电储能编码不存在");
        }
        return EngeryDeviceModel.fromEntry(entry);
    }




    @Override
    public void insert(EngeryDeviceModel model) {

        EngeryDevice obj = new EngeryDevice();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        if(model.getModelType()!= null && model.getModelType() == 1){ //动力蓄电池
            obj.setBatteryModelId(model.getModelId());
            obj.setCapacityModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2) { //超级电容
            obj.setBatteryModelId(null);
            obj.setCapacityModelId(model.getModelId());
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(EngeryDeviceModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");

        EngeryDevice obj = new EngeryDevice();
        BeanUtils.copyProperties(model, obj);
        if(model.getModelType()!= null && model.getModelType() == 1){ //动力蓄电池
            obj.setBatteryModelId(model.getModelId());
            obj.setCapacityModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2) { //超级电容
            obj.setBatteryModelId(null);
            obj.setCapacityModelId(model.getModelId());
        }
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> map = new HashMap<>();
            map.put("deviceId", id);
            Integer edCount = engeryDeviceLkService.unique("findByDeviceIdCount", map);
            if (edCount > 0) {
                throw new BusinessException("可充电储能装置已关联车辆信息，不可删除", 300);
            }
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<EngeryDevice>(this, "pagerModel", params, "sys/res/engeryDevice/export.xls", "可充电储能装置信息") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ENGERYDEVICE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<EngeryDeviceModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String modelId = null;
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EngeryDeviceModel model) {
                List<String> list = new ArrayList<String>();
                if(StringUtils.isEmpty(model.getModelTypeDisplay())){
                    list.add("储能装置类型必填");
                }else{
                    if(Constants.EnergyDeviceType.getValue(model.getModelTypeDisplay().trim()) == null) {
                        list.add("储能装置类型不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getModelName())){
                    list.add("储能装置型号必填");
                }else{
                    if(model.getModelTypeDisplay() != null && model.getModelTypeDisplay().equals("动力蓄电池")){
                        //获取当权限的map
                        Map<String,Object> params = DataAccessKit.getAuthMap("sys_battery_device_model", "sbdm");
                        params.put("name", model.getModelName().trim());
                        BatteryDeviceModel entry = batteryDeviceModelService.unique("findByName", params);
                        if(entry == null){
                            list.add("储能装置型号不存在，请确认");
                        }else{
                            modelId = entry.getId();
                        }
                    }else{
                        //获取当权限的map
                        Map<String,Object> params = DataAccessKit.getAuthMap("sys_super_capacitor_model", "sscm");
                        params.put("name", model.getModelName().trim());
                        SuperCapacitorModel entry = superCapacitorModelService.unique("findByName", params);
                        if(entry == null){
                            list.add("储能装置型号不存在，请确认");
                        }else{
                            modelId = entry.getId();
                        }
                    }
                }
                // 校验储能装置生产日期有效性
                if( StringUtils.isNotBlank(model.getProduceDate()) ){
                    if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getProduceDate())){
                        list.add("储能装置生产日期不合法");
                    }
                }
                return list;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(EngeryDeviceModel model) {
                if(!StringUtils.isEmpty(model.getModelTypeDisplay())) {
                    model.setModelType(Constants.EnergyDeviceType.getValue(model.getModelTypeDisplay().trim()));
                }
                model.setModelId(modelId);
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ENGERYDEVICE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<EngeryDeviceModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EngeryDeviceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(EngeryDeviceModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public Object findDeviceModel(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<EngeryDevice> entries = findBySqlId("findDeviceModel", params);
            List<EnergyDeviceTempModel> models = new ArrayList();
            for(EngeryDevice entry: entries){
                EngeryDevice obj = (EngeryDevice)entry;
                models.add(EnergyDeviceTempModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findDeviceModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<EnergyDeviceTempModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                EngeryDevice obj = (EngeryDevice)entry;
                models.add(EnergyDeviceTempModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public boolean isExist(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engery_device", "sed");
        params.put("id",id);
        EngeryDevice entry = unique("findById", params);
        if (entry != null){
            return true;
        }
        return false;
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("可充电储能装置信息导入模板.xls", EngeryDeviceModel.class);
    }

    @Override
    public void importFind() {
        EasyExcel.renderImportSearchDemoFile("可充电储能装置信息导入查询模板.xls", "储能装置编码", new String[]{"ERV20171092"});
    }
}
