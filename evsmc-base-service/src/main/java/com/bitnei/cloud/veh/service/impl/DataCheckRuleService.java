package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.dao.DataCheckRuleItemMapper;
import com.bitnei.cloud.veh.domain.DataCheckRule;
import com.bitnei.cloud.veh.domain.DataCheckRuleItem;
import com.bitnei.cloud.veh.model.DataCheckRuleModel;
import com.bitnei.cloud.veh.service.IDataCheckRuleService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRuleService实现<br>
* 描述： DataCheckRuleService实现<br>
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
* <td>2019-09-16 15:39:53</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DataCheckRuleMapper" )
public class DataCheckRuleService extends BaseService implements IDataCheckRuleService {

    @Resource
    private DataCheckRuleItemMapper dataCheckRuleItemMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataCheckRule> entries = findBySqlId("pagerModel", params);
            List<DataCheckRuleModel> models = new ArrayList();
            for(DataCheckRule entry: entries){
                DataCheckRule obj = (DataCheckRule)entry;
                models.add(DataCheckRuleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataCheckRuleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DataCheckRule obj = (DataCheckRule)entry;
                models.add(DataCheckRuleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public DataCheckRuleModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");
        params.put("id",id);
        DataCheckRule entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        DataCheckRuleModel model = DataCheckRuleModel.fromEntry(entry);
        String[] arr = model.getAddItemIds().split(",");
        model.setItemIdsCount(arr.length);
        return model;
    }


    @Override
    public DataCheckRuleModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");
        params.put("name",name);
        DataCheckRule entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataCheckRuleModel.fromEntry(entry);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(DataCheckRuleModel model) {

        DataCheckRule obj = new DataCheckRule();
        BeanUtils.copyProperties(model, obj);
        //限制 车辆型号 + 通讯协议 唯一
        //获取当权限的map
        Map<String,Object> validParams = new HashMap<>();
        validParams.put("vehModelId", obj.getVehModelId());
        validParams.put("protocolId", obj.getProtocolId());
        Integer count = unique("findByVehModelProtocolCount", validParams);
        if(count > 0){
            throw new BusinessException("新增失败，该车辆型号+通讯协议已存在");
        }

        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }else{
            //添加数据项
            if (StringUtils.isNotBlank(model.getAddItemIds())){
                String[] arr = model.getAddItemIds().split(",");
                DataCheckRuleItem lk = new DataCheckRuleItem();
                for (String itemId : arr){
                    String lkId = UtilHelper.getUUID();
                    lk.setId(lkId);
                    lk.setDataItemId(itemId);
                    lk.setCheckRuleId(id);
                    lk.setCreateBy(ServletUtil.getCurrentUser());
                    lk.setCreateTime(DateUtil.getNow());
                    int r = dataCheckRuleItemMapper.insert(lk);
                    if (r == 0 ){
                        throw new BusinessException("新增失败");
                    }
                }
            }else {
                throw new BusinessException("数据项不能为空");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DataCheckRuleModel model) {
        Map<String,Object> p = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");
        p.put("id",model.getId());
        DataCheckRule entry = unique("findById", p);
        String addItemIds = entry.getAddItemIds();

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");
        DataCheckRule obj = new DataCheckRule();
        BeanUtils.copyProperties(model, obj);
        //限制 车辆型号 + 通讯协议 唯一
        //获取当权限的map
        Map<String,Object> validParams = new HashMap<>();
        validParams.put("vehModelId", obj.getVehModelId());
        validParams.put("protocolId", obj.getProtocolId());
        validParams.put("id", obj.getId());
        Integer count = unique("findByVehModelProtocolCount", validParams);
        if(count > 0){
            throw new BusinessException("更新失败，该车辆型号+通讯协议已存在");
        }
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }else{
            if (StringUtils.isNotBlank(model.getAddItemIds())) {
                if (!addItemIds.equals(model.getAddItemIds())){
                    //删除规则原有数据项
                    Map<String,Object> delParams = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");
                    delParams.put("checkRuleId",obj.getId());
                    dataCheckRuleItemMapper.deleteByRuleId(delParams);

                    //添加数据项
                    String[] arr = model.getAddItemIds().split(",");
                    DataCheckRuleItem lk = new DataCheckRuleItem();
                    for (String itemId : arr) {
                        String lkId = UtilHelper.getUUID();
                        lk.setId(lkId);
                        lk.setDataItemId(itemId);
                        lk.setCheckRuleId(obj.getId());
                        lk.setCreateBy(ServletUtil.getCurrentUser());
                        lk.setCreateTime(DateUtil.getNow());
                        int r = dataCheckRuleItemMapper.insert(lk);
                        if (r == 0) {
                            throw new BusinessException("更新失败");
                        }
                    }
                }
            }else {
                throw new BusinessException("数据项不能为空");
            }

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
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule", "dcr");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            if (r > 0) {
                //删除规则的数据项
                Map<String, Object> delParams = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");
                delParams.put("checkRuleId", id);
                dataCheckRuleItemMapper.deleteByRuleId(delParams);
            }
            count+=r;
        }
        return count;
    }



}
