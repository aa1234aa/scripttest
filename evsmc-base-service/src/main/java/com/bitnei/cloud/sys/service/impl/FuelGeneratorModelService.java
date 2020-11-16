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
import com.bitnei.cloud.sys.dao.PowerDeviceMapper;
import com.bitnei.cloud.sys.domain.FuelGeneratorModel;
import com.bitnei.cloud.sys.model.FuelGeneratorModelModel;
import com.bitnei.cloud.sys.service.IFuelGeneratorModelService;
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
* 功能： FuelGeneratorModelService实现<br>
* 描述： FuelGeneratorModelService实现<br>
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
* <td>2018-12-10 17:02:52</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.FuelGeneratorModelMapper" )
public class FuelGeneratorModelService extends BaseService implements IFuelGeneratorModelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUnitService unitService;
    @Resource
    private PowerDeviceMapper powerDeviceMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<FuelGeneratorModel> entries = findBySqlId("pagerModel", params);
            List<FuelGeneratorModelModel> models = new ArrayList();
            for(FuelGeneratorModel entry: entries){
                FuelGeneratorModel obj = (FuelGeneratorModel)entry;
                models.add(FuelGeneratorModelModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<FuelGeneratorModelModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                FuelGeneratorModel obj = (FuelGeneratorModel)entry;
                models.add(FuelGeneratorModelModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public FuelGeneratorModelModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");
        params.put("id",id);
        FuelGeneratorModel entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return FuelGeneratorModelModel.fromEntry(entry);
    }


    @Override
    public FuelGeneratorModelModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");
        params.put("name",name);
        FuelGeneratorModel entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return FuelGeneratorModelModel.fromEntry(entry);
    }


    @Override
    public void insert(FuelGeneratorModelModel model) {

        FuelGeneratorModel obj = new FuelGeneratorModel();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
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
    public void update(FuelGeneratorModelModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");

        FuelGeneratorModel obj = new FuelGeneratorModel();
        BeanUtils.copyProperties(model, obj);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Integer gmCount = powerDeviceMapper.findByGeneratorModelIdCount(id);
            if(gmCount > 0){
                throw new BusinessException("燃油发电机型号已关联发电装置信息，不可删除", 300);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_fuel_generator_model", "fgm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<FuelGeneratorModel>(this, "pagerModel", params, "sys/res/fuelGeneratorModel/export.xls", "燃油发电机型号") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "FUELGENERATORMODEL"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<FuelGeneratorModelModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String prodUnitId = "";
            String controllerProdUnitId = "";
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(FuelGeneratorModelModel model) {
                List<String> list = new ArrayList<String>();
                if(!StringUtils.isEmpty(model.getAirInletDisplay())){
                    if(Constants.IntakeWay.getValue(model.getAirInletDisplay().trim()) == null) {
                        list.add("进气方式不存在，请确认");
                    }
                }
                if(StringUtils.isEmpty(model.getFuelFormDisplay())){
                    list.add("燃料形式必填");
                }else{
                    if(Constants.GeneratorFuelForm.getValue(model.getFuelFormDisplay().trim()) == null) {
                        list.add("燃料形式不存在，请确认");
                    }
                }
                // 获取当权限的map
                Map<String, Object> params = DataAccessKit.getAuthMap("sys_unit", "un");
                if(StringUtils.isEmpty(model.getProdUnitDisplay())){
                    list.add("燃油发电机生产企业必填");
                }else{
                    try {
                        prodUnitId = unitService.validateNameCode(model.getProdUnitDisplay().trim(), "1007");
                    }catch (BusinessException e){
                        if(e.getCode() == 1001){
                            list.add("燃油发电机生产企业不存在，请确认");
                        }
                        if(e.getCode() == 1002){
                            list.add("燃油发电机生产企业的单位类型须为供应商，请确认");
                        }
                    }
                }
                if(!StringUtils.isEmpty(model.getControllerProdUnitDisplay())){
                    try {
                        controllerProdUnitId = unitService.validateNameCode(model.getControllerProdUnitDisplay().trim(), "1007");
                    }catch (BusinessException e){
                        if(e.getCode() == 1001){
                            list.add("发燃油发电机控制器生产企业不存在，请确认");
                        }
                        if(e.getCode() == 1002){
                            list.add("发燃油发电机控制器生产企业的单位类型须为供应商，请确认");
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
            public void saveObject(FuelGeneratorModelModel model) {
                if(!StringUtils.isEmpty(model.getAirInletDisplay())){
                    model.setAirInlet(Constants.IntakeWay.getValue(model.getAirInletDisplay().trim()));
                }
                if(!StringUtils.isEmpty(model.getFuelFormDisplay())){
                    model.setFuelForm(Constants.GeneratorFuelForm.getValue(model.getFuelFormDisplay().trim()));
                }
                model.setProdUnitId(prodUnitId);
                model.setControllerProdUnitId(controllerProdUnitId);
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "FUELGENERATORMODEL"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<FuelGeneratorModelModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(FuelGeneratorModelModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(FuelGeneratorModelModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("燃油发电机型号导入模板.xls", FuelGeneratorModelModel.class);
    }


}
