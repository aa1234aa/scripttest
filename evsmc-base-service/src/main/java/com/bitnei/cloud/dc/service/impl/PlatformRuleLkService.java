package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.PlatformRuleLk;
import com.bitnei.cloud.dc.model.PlatformRuleLkModel;
import com.bitnei.cloud.dc.service.IPlatformRuleLkService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformRuleLkService实现<br>
* 描述： PlatformRuleLkService实现<br>
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
* <td>2019-02-21 14:30:57</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.PlatformRuleLkMapper" )
public class PlatformRuleLkService extends BaseService implements IPlatformRuleLkService {


    @Override
    public void insert(PlatformRuleLkModel model) {

        PlatformRuleLk obj = new PlatformRuleLk();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(PlatformRuleLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_rule_lk", "lk");

        PlatformRuleLk obj = new PlatformRuleLk();
        BeanUtils.copyProperties(model, obj);
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
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_rule_lk", "lk");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }

    //关联自动转发规则
    @Override
    public int insertPlatformRules(PlatformRuleLkModel demo) {
        String addRuleIds = demo.getAddRuleIds();
        int count = 0;
        if(!StringUtils.isEmpty(addRuleIds)){
            String[] arr = addRuleIds.split(",");
            PlatformRuleLk obj = null;
            for (String ruleId : arr){
                obj = new PlatformRuleLk();
                //单元测试指定id，如果是单元测试，那么就不使用uuid
                String id = UtilHelper.getUUID();
                obj.setId(id);
                obj.setPlatformId(demo.getPlatformId());
                obj.setForwardRuleId(ruleId);
                obj.setCreateTime(DateUtil.getNow());
                obj.setCreateBy(ServletUtil.getCurrentUser());
                int res = 0;
                try {
                    res = super.insert(obj);
                } catch (DuplicateKeyException e){
                    throw new BusinessException("此规则已关联");
                }

                count += res;
                if (res == 0 ){
                    throw new BusinessException("关联失败");
                }
            }
        }
        return count;
    }

    /**
     * 移除转发规则
     * @param platformId
     * @param delRuleIds
     * @return
     */
    @Override
    public int deletePlatformRules(String platformId, String delRuleIds) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_rule_lk", "lk");
        int count = 0;
        if(!StringUtils.isEmpty(delRuleIds)) {
            String[] arr = delRuleIds.split(",");
            for (String ruleId : arr) {
                params.put("platformId", platformId);
                params.put("forwardRuleId", ruleId);
                int r = super.delete("delete", params);
                count += r;
            }
        }
        return count;
    }

    @Override
    public Object findPlatformRule(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("defaultRule", 0);
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<PlatformRuleLk> entries = findBySqlId("pagerModel", params);
            List<PlatformRuleLkModel> models = new ArrayList();
            for(PlatformRuleLk entry: entries){
                PlatformRuleLk obj = (PlatformRuleLk)entry;
                models.add(PlatformRuleLkModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PlatformRuleLkModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                PlatformRuleLk obj = (PlatformRuleLk)entry;
                models.add(PlatformRuleLkModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public List<PlatformRuleLk> listPlatformRule(String platformId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fv");
        params.put("platformId", platformId);
        List<PlatformRuleLk> entries = findBySqlId("pagerModel", params);
        return entries;
    }
}
