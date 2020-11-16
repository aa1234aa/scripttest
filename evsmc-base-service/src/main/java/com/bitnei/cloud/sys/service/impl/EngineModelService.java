package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.DriveDeviceMapper;
import com.bitnei.cloud.sys.domain.EngeryDevice;
import com.bitnei.cloud.sys.domain.EngineModel;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.bitnei.cloud.sys.model.EngineModelModel;
import com.bitnei.cloud.sys.service.ICoreExtendValueService;
import com.bitnei.cloud.sys.service.IEngineModelService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.service.IUnitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EngineModelService实现<br>
* 描述： EngineModelService实现<br>
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
* <td>2018-12-03 11:16:29</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.EngineModelMapper" )
public class EngineModelService extends BaseService implements IEngineModelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUnitService unitService;
    @Resource
    private DriveDeviceMapper driveDeviceMapper;
    @Autowired
    private ICoreExtendValueService coreExtendValueService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<EngineModel> entries = findBySqlId("pagerModel", params);
            List<EngineModelModel> models = new ArrayList();
            for(EngineModel entry: entries){
                EngineModel obj = (EngineModel)entry;
                models.add(EngineModelModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<EngineModelModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                EngineModel obj = (EngineModel)entry;
                models.add(EngineModelModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public EngineModelModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");
        params.put("id",id);
        EngineModel entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return EngineModelModel.fromEntry(entry);
    }


    @Override
    public EngineModelModel findByName(String name) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");
        params.put("name",name);
        EngineModel entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("发动机型号不存在");
        }
        return EngineModelModel.fromEntry(entry);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(EngineModelModel model) {
        //必填校验
        if(model.getFuelForm() != 2){
            if(model.getPeakPower() == null || model.getPeakRotateSpeed() == null || model.getPeakTorque() == null || model.getPeakTorqueRotateSpeed() == null){
                throw new BusinessException("峰值功率(KW)、峰值功率转速(r/min)、峰值转矩(N.m)、峰值扭矩转速(r/min) 不能为空");
            }
        }
        EngineModel obj = new EngineModel();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        if (model.getFuelForm() == 2) {
            // 燃料形式为柴油时，保存扩展字段
            coreExtendValueService.updateByModel(model, id);
        }

        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EngineModelModel model) {
        //必填校验
        if(model.getFuelForm() != 2){
            if(model.getPeakPower() == null || model.getPeakRotateSpeed() == null || model.getPeakTorque() == null || model.getPeakTorqueRotateSpeed() == null){
                throw new BusinessException("峰值功率(KW)、峰值功率转速(r/min)、峰值转矩(N.m)、峰值扭矩转速(r/min) 不能为空");
            }
        }

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");

        EngineModel obj = new EngineModel();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        if (model.getFuelForm() == 2) {
            // 燃料形式为柴油时，保存扩展字段
            coreExtendValueService.updateByModel(model, model.getId());
        }else {
            // 燃料形式不等于柴油时，删除扩展字段记录
            coreExtendValueService.deleteByIdVal(model.getId());
        }

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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Integer emCount = driveDeviceMapper.findByEngineModelIdCount(id);
            if (emCount > 0) {
                throw new BusinessException("发动机型号已关联驱动装置信息，不可删除", 300);
            }
            params.put("id",id);
            int r = super.deleteByMap(params);
            // 燃料形式不等于柴油时，删除扩展字段记录
            coreExtendValueService.deleteByIdVal(id);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_engine_model", "em");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<EngineModel>(this, "pagerModel", params, "sys/res/engineModel/export.xls", "发动机型号") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ENGINEMODEL"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<EngineModelModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String prodUnitId = "";
            String controllerProdUnitId = "";
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EngineModelModel model) {
                List<String> list = new ArrayList<String>();
                if(StringUtils.isEmpty(model.getIntakeWayDisplay())){
                    list.add("进气方式必填");
                }else{
                    if(Constants.IntakeWay.getValue(model.getIntakeWayDisplay().trim()) == null) {
                        list.add("进气方式不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getCirculationWayDisplay())){
                    list.add("循环方式必填");
                }else{
                    if(Constants.CirculationWay.getValue(model.getCirculationWayDisplay().trim()) == null) {
                        list.add("循环方式不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getFuelFormDisplay())){
                    list.add("燃料形式必填");
                }else{
                    if(Constants.FuelForm.getValue(model.getFuelFormDisplay().trim()) == null) {
                        list.add("燃料形式不存在，请确认");
                    }
                    if("柴油".equals(model.getFuelFormDisplay())){
                        model.setTankCapacity(null);
                        model.setGasholderNumber(null);
                        model.setGasholderType(null);
                        model.setGasholderTypeDisplay(null);
                        model.setGasholderCapacity(null);
                        model.setBrandName(null);
                        model.setControllerModel(null);
                        model.setPeakPower(null);
                        model.setPeakRotateSpeed(null);
                        model.setPeakTorque(null);
                        model.setPeakTorqueRotateSpeed(null);
                        model.setControllerProdUnitId(null);
                        model.setControllerProdUnitDisplay(null);
                    }else {
                        if(model.getPeakPower() == null || model.getPeakRotateSpeed() == null || model.getPeakTorque() == null || model.getPeakTorqueRotateSpeed() == null){
                            list.add("峰值功率(KW)、峰值功率转速(r/min)、峰值转矩(N.m)、峰值扭矩转速(r/min) 不能为空");
                        }
                        if(!StringUtils.isEmpty(model.getGasholderTypeDisplay())){
                            if(Constants.GasholderType.getValue(model.getGasholderTypeDisplay().trim()) == null) {
                                list.add("储气罐类型不存在，请确认");
                            }
                        }
                        if(!StringUtils.isEmpty(model.getControllerProdUnitDisplay())){
                            try {
                                controllerProdUnitId = unitService.validateNameCode(model.getControllerProdUnitDisplay().trim(), "1007");
                            }catch (BusinessException e){
                                if(e.getCode() == 1001){
                                    list.add("发动机控制器生产企业不存在，请确认");
                                }
                                if(e.getCode() == 1002){
                                    list.add("发动机控制器生产企业的单位类型须为供应商，请确认");
                                }
                            }
                        }
                    }
                }
                if(StringUtils.isEmpty(model.getProdUnitDisplay())){
                    list.add("发动机生产企业必填");
                }else{
                    try {
                        prodUnitId = unitService.validateNameCode(model.getProdUnitDisplay().trim(), "1007");
                    }catch (BusinessException e){
                        if(e.getCode() == 1001){
                            list.add("发动机生产企业不存在，请确认");
                        }
                        if(e.getCode() == 1002){
                            list.add("发动机生产企业的单位类型须为供应商，请确认");
                        }
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
            public void saveObject(EngineModelModel model) {
                if(!StringUtils.isEmpty(model.getIntakeWayDisplay())){
                    model.setIntakeWay(Constants.IntakeWay.getValue(model.getIntakeWayDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getCirculationWayDisplay())){
                    model.setCirculationWay(Constants.CirculationWay.getValue(model.getCirculationWayDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getFuelFormDisplay())){
                    model.setFuelForm(Constants.FuelForm.getValue(model.getFuelFormDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getGasholderTypeDisplay())){
                    model.setGasholderType(Constants.GasholderType.getValue(model.getGasholderTypeDisplay().trim()));
                }
                model.setProdUnitId(prodUnitId);
                model.setControllerProdUnitId(controllerProdUnitId);
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ENGINEMODEL"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<EngineModelModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EngineModelModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(EngineModelModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("发动机型号导入模板.xls" , EngineModelModel.class);
    }


}
