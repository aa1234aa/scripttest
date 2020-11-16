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
import com.bitnei.cloud.sys.domain.DriveDevice;
import com.bitnei.cloud.sys.domain.DriveMotorModel;
import com.bitnei.cloud.sys.domain.EngineModel;
import com.bitnei.cloud.sys.model.DriveDeviceModel;
import com.bitnei.cloud.sys.model.DriveDeviceTempModel;
import com.bitnei.cloud.sys.service.IDriveDeviceService;
import com.bitnei.cloud.sys.service.IDriveMotorModelService;
import com.bitnei.cloud.sys.service.IEngineModelService;
import com.bitnei.cloud.sys.service.IVehicleDriveDeviceLkService;
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
* 功能： DriveDeviceService实现<br>
* 描述： DriveDeviceService实现<br>
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
* <td>2018-12-03 11:17:33</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.DriveDeviceMapper" )
public class DriveDeviceService extends BaseService implements IDriveDeviceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDriveMotorModelService driveMotorModelService;
    @Autowired
    private IEngineModelService engineModelService;
    @Autowired
    private IVehicleDriveDeviceLkService driveDeviceLkService;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DriveDevice> entries = findBySqlId("pagerModel", params);
            List<DriveDeviceModel> models = new ArrayList();
            for(DriveDevice entry: entries){
                DriveDevice obj = (DriveDevice)entry;
                models.add(DriveDeviceModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DriveDeviceModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DriveDevice obj = (DriveDevice)entry;
                models.add(DriveDeviceModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public DriveDeviceModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.put("id",id);
        DriveDevice entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DriveDeviceModel.fromEntry(entry);
    }

    @Override
    public DriveDeviceModel findByCode(String code) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.put("code",code);
        DriveDevice entry = unique("findByCode", params);
        if (entry == null){
            throw new BusinessException("驱动装置编码不存在");
        }
        return DriveDeviceModel.fromEntry(entry);
    }


    @Override
    public void insert(DriveDeviceModel model) {

        DriveDevice obj = new DriveDevice();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        if(model.getModelType()!= null && model.getModelType() == 1){ //驱动电机
            obj.setDriveModelId(model.getModelId());
            obj.setEngineModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2){ //发动机
            obj.setEngineModelId(model.getModelId());
            obj.setDriveModelId(null);
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
    public void update(DriveDeviceModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");

        DriveDevice obj = new DriveDevice();
        BeanUtils.copyProperties(model, obj);
        if(model.getModelType()!= null && model.getModelType() == 1){ //驱动电机
            obj.setDriveModelId(model.getModelId());
            obj.setEngineModelId(null);
        }else if(model.getModelType()!= null && model.getModelType() == 2){ //发动机
            obj.setEngineModelId(model.getModelId());
            obj.setDriveModelId(null);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> map = new HashMap<>();
            map.put("deviceId", id);
            Integer ddCount = driveDeviceLkService.unique("findByDeviceIdCount", map);
            if (ddCount > 0) {
                throw new BusinessException("驱动装置已关联车辆信息，不可删除", 300);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DriveDevice>(this, "pagerModel", params, "sys/res/driveDevice/export.xls", "驱动装置信息") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DRIVEDEVICE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DriveDeviceModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String modelId = null;
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DriveDeviceModel model) {
                List<String> list = new ArrayList<String>();
                if(StringUtils.isEmpty(model.getModelTypeDisplay())){
                    list.add("驱动装置类型必填");
                }else{
                    if(Constants.DriveDevice.getValue(model.getModelTypeDisplay().trim()) == null) {
                        list.add("驱动装置类型不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getModelName())){
                    list.add("驱动装置型号必填");
                }else{
                    if(model.getModelTypeDisplay() != null && model.getModelTypeDisplay().equals("驱动电机")){
                        //获取当权限的map
                        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_motor_model", "dmm");
                        params.put("name", model.getModelName().trim());
                        DriveMotorModel entry = driveMotorModelService.unique("findByName", params);
                        if(entry == null){
                            list.add("驱动电机型号不存在，请确认");
                        }else{
                            modelId = entry.getId();
                        }
                    }else{
                        //获取当权限的map
                        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");
                        params.put("name", model.getModelName().trim());
                        EngineModel entry = engineModelService.unique("findByName", params);
                        if(entry == null){
                            list.add("发动机型号不存在，请确认");
                        }else{
                            modelId = entry.getId();
                        }
                    }
                }
                if(!StringUtils.isEmpty(model.getInstallPositionDisplay())){
                    if(Constants.InstallPosition.getValue(model.getInstallPositionDisplay().trim()) == null) {
                        list.add("安装位置不存在，请确认");
                    }
                }
                // 校验生产日期有效性
                if( StringUtils.isNotBlank(model.getProduceDate()) ){
                    if(!com.bitnei.cloud.sys.util.DateUtil.validate(model.getProduceDate())){
                        list.add("生产日期不合法");
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
            public void saveObject(DriveDeviceModel model) {
                if(!StringUtils.isEmpty(model.getModelTypeDisplay())) {
                    model.setModelType(Constants.DriveDevice.getValue(model.getModelTypeDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getInstallPositionDisplay())) {
                    model.setInstallPosition(Constants.InstallPosition.getValue(model.getInstallPositionDisplay().trim()));
                }
                model.setModelId(modelId);
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DRIVEDEVICE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DriveDeviceModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DriveDeviceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DriveDeviceModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public Object findDeviceModel(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DriveDevice> entries = findBySqlId("findDeviceModel", params);
            List<DriveDeviceTempModel> models = new ArrayList();
            for(DriveDevice entry: entries){
                DriveDevice obj = (DriveDevice)entry;
                models.add(DriveDeviceTempModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findDeviceModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DriveDeviceTempModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DriveDevice obj = (DriveDevice)entry;
                models.add(DriveDeviceTempModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public boolean isExist(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_drive_device", "dd");
        params.put("id",id);
        DriveDevice entry = unique("findById", params);
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
        EasyExcel.renderImportDemoFile("驱动装置信息导入模板.xls", DriveDeviceModel.class);
    }

    @Override
    public void importFind() {
        EasyExcel.renderImportSearchDemoFile("驱动装置信息导入查询模板.xls", "驱动装置编码", new String[]{"QDZZ10006"});
    }
}
