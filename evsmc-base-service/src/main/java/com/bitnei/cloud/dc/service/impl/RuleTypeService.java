package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.service.IDataItemGroupService;
import com.bitnei.cloud.dc.service.IPlatformInformationService;
import com.bitnei.cloud.dc.service.IRuleService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.RuleType;
import com.bitnei.cloud.dc.model.RuleTypeModel;
import com.bitnei.cloud.dc.service.IRuleTypeService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RuleTypeService实现<br>
* 描述： RuleTypeService实现<br>
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
* <td>2019-01-30 10:36:15</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.RuleTypeMapper" )
public class RuleTypeService extends BaseService implements IRuleTypeService {

    @Autowired
    private IDataItemGroupService dataItemGroupService;
    @Autowired
    private IRuleService ruleService;
    @Autowired
    private IPlatformInformationService platformInformationService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<RuleType> entries = findBySqlId("pagerModel", params);
            List<RuleTypeModel> models = new ArrayList();
            for(RuleType entry: entries){
                RuleType obj = (RuleType)entry;
                models.add(RuleTypeModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RuleTypeModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                RuleType obj = (RuleType)entry;
                models.add(RuleTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public RuleTypeModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");
        params.put("id",id);
        RuleType entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RuleTypeModel.fromEntry(entry);
    }


    @Override
    public RuleTypeModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");
        params.put("name",name);
        RuleType entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RuleTypeModel.fromEntry(entry);
    }
    @Override
    public RuleTypeModel getByCode(String code){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");
        params.put("code",code);
        RuleType entry = unique("findByCode", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RuleTypeModel.fromEntry(entry);
    }


    @Override
    public void insert(RuleTypeModel model) {

        RuleType obj = new RuleType();
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
    public void update(RuleTypeModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");

        RuleType obj = new RuleType();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> map = new HashMap<>();
            map.put("ruleTypeId", id);
            Integer ruleCount = ruleService.unique("findByRuleTypeIdCount", map);
            if (ruleCount > 0) {
                throw new BusinessException("协议类型存在关联通讯协议，不允许删除", 300);
            }
            Integer digCount = dataItemGroupService.unique("findByRuleTypeIdCount", map);
            if (digCount > 0) {
                throw new BusinessException("协议类型存在关联数据项类型，不允许删除", 300);
            }
            Integer piCount = platformInformationService.unique("findByRuleTypeIdCount", map);
            if (piCount > 0) {
                throw new BusinessException("协议类型存在关联平台接入配置，不允许删除", 300);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_type", "rt");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<RuleType>(this, "pagerModel", params, "dc/res/ruleType/export.xls", "协议类型") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "RULETYPE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<RuleTypeModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RuleTypeModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RuleTypeModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "RULETYPE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<RuleTypeModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RuleTypeModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RuleTypeModel model) {
                update(model);
            }
        }.work();

    }



}
