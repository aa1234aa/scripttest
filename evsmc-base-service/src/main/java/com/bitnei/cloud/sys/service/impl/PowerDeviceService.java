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
import com.bitnei.cloud.sys.domain.FuelGeneratorModel;
import com.bitnei.cloud.sys.domain.FuelSystemModel;
import com.bitnei.cloud.sys.domain.PowerDevice;
import com.bitnei.cloud.sys.model.PowerDeviceModel;
import com.bitnei.cloud.sys.model.PowerDeviceTempModel;
import com.bitnei.cloud.sys.service.IFuelGeneratorModelService;
import com.bitnei.cloud.sys.service.IFuelSystemModelService;
import com.bitnei.cloud.sys.service.IPowerDeviceService;
import com.bitnei.cloud.sys.service.IVehiclePowerDeviceLkService;
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
* 功能： PowerDeviceService实现<br>
* 描述： PowerDeviceService实现<br>
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
* <td>2018-12-10 17:03:09</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.PowerDeviceMapper" )
public class PowerDeviceService extends BaseService implements IPowerDeviceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFuelGeneratorModelService fuelGeneratorModelService;
    @Autowired
    private IFuelSystemModelService fuelSystemModelService;
    @Autowired
    private IVehiclePowerDeviceLkService powerDeviceLkService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<PowerDevice> entries = findBySqlId("pagerModel", params);
            List<PowerDeviceModel> models = new ArrayList();
            for(PowerDevice entry: entries){
                PowerDevice obj = (PowerDevice)entry;
                models.add(PowerDeviceModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PowerDeviceModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                PowerDevice obj = (PowerDevice)entry;
                models.add(PowerDeviceModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public PowerDeviceModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.put("id",id);
        PowerDevice entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PowerDeviceModel.fromEntry(entry);
    }


    @Override
    public PowerDeviceModel getByCode(String code){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.put("code",code);
        PowerDevice entry = unique("findByCode", params);
        if (entry == null){
            throw new BusinessException("发电装置编码不存在");
        }
        return PowerDeviceModel.fromEntry(entry);
    }


    @Override
    public void insert(PowerDeviceModel model) {

        PowerDevice obj = new PowerDevice();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        if(model.getModelType()!= null && model.getModelType() == 1){ //燃油发电机型号
            obj.setFuelGeneratorModelId(model.getModelId());
            obj.setFuelBatteryModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2){ //燃料电池系统型号
            obj.setFuelBatteryModelId(model.getModelId());
            obj.setFuelGeneratorModelId(null);
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
    public void update(PowerDeviceModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");

        PowerDevice obj = new PowerDevice();
        BeanUtils.copyProperties(model, obj);
        if(model.getModelType()!= null && model.getModelType() == 1){ //燃油发电机型号
            obj.setFuelGeneratorModelId(model.getModelId());
            obj.setFuelBatteryModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2){ //燃料电池系统型号
            obj.setFuelBatteryModelId(model.getModelId());
            obj.setFuelGeneratorModelId(null);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> map = new HashMap<>();
            map.put("deviceId", id);
            Integer pdCount = powerDeviceLkService.unique("findByDeviceIdCount", map);
            if (pdCount > 0) {
                throw new BusinessException("发电装置已关联车辆信息，不可删除", 300);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<PowerDevice>(this, "pagerModel", params, "sys/res/powerDevice/export.xls", "发电装置信息") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "POWERDEVICE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<PowerDeviceModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String modelId = null;
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PowerDeviceModel model) {
                List<String> list = new ArrayList<String>();
                if(StringUtils.isEmpty(model.getModelTypeDisplay())){
                    list.add("发电装置类型必填");
                }else{
                    if(Constants.PowerDevice.getValue(model.getModelTypeDisplay().trim()) == null) {
                        list.add("发电装置类型不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getModelName())){
                    list.add("发电装置型号必填");
                }else {
                    if (model.getModelTypeDisplay() != null && model.getModelTypeDisplay().equals("燃油发电机")) {
                        //获取当权限的map
                        Map<String, Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");
                        params.put("name", model.getModelName().trim());
                        FuelGeneratorModel entry = fuelGeneratorModelService.unique("findByName", params);
                        if (entry == null) {
                            list.add("燃油发电机型号不存在，请确认");
                        } else {
                            modelId = entry.getId();
                        }
                    } else {
                        //获取当权限的map
                        Map<String, Object> params = DataAccessKit.getAuthMap("sys_fuel_system_model", "fsm");
                        params.put("name", model.getModelName().trim());
                        FuelSystemModel entry = fuelSystemModelService.unique("findByName", params);
                        if (entry == null) {
                            list.add("燃料电池系统型号不存在，请确认");
                        } else {
                            modelId = entry.getId();
                        }
                    }
                }
                if(!StringUtils.isEmpty(model.getInstallPositionDisplay())){
                    if(Constants.GeneratorInstallPosition.getValue(model.getInstallPositionDisplay().trim()) == null) {
                        list.add("安装位置不存在，请确认");
                    }
                }
                // 校验发电装置生产日期有效性
                if( StringUtils.isNotBlank(model.getProduceDate()) ){
                    if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getProduceDate())){
                        list.add("发电装置生产日期不合法");
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
            public void saveObject(PowerDeviceModel model) {
                if(!StringUtils.isEmpty(model.getModelTypeDisplay())) {
                    model.setModelType(Constants.PowerDevice.getValue(model.getModelTypeDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getInstallPositionDisplay())) {
                    model.setInstallPosition(Constants.GeneratorInstallPosition.getValue(model.getInstallPositionDisplay().trim()));
                }
                model.setModelId(modelId);
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "POWERDEVICE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<PowerDeviceModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PowerDeviceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PowerDeviceModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public Object findDeviceModel(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<PowerDevice> entries = findBySqlId("findDeviceModel", params);
            List<PowerDeviceTempModel> models = new ArrayList();
            for(PowerDevice entry: entries){
                PowerDevice obj = (PowerDevice)entry;
                models.add(PowerDeviceTempModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findDeviceModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PowerDeviceTempModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                PowerDevice obj = (PowerDevice)entry;
                models.add(PowerDeviceTempModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public boolean isExist(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_power_device", "pd");
        params.put("id",id);
        PowerDevice entry = unique("findById", params);
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
        EasyExcel.renderImportDemoFile("发电装置信息导入模板.xls", PowerDeviceModel.class);
    }

    @Override
    public void importFind() {
        EasyExcel.renderImportSearchDemoFile("发电装置信息导入查询模板.xls", "发电装置编码", new String[]{"QDZZ10006"});
    }
}
