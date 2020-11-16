package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultObjMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.dc.dao.RuleMapper;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.domain.Rule;
import com.bitnei.cloud.dc.domain.RuleItemLk;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.dc.model.RuleItemLkModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.dc.service.IRuleItemLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RuleItemLkService实现<br>
* 描述： RuleItemLkService实现<br>
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
* <td>2019-01-31 17:34:33</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.RuleItemLkMapper" )
public class RuleItemLkService extends BaseService implements IRuleItemLkService {

    @Autowired
    private IDataItemService dataItemService;

    @Resource
    private RuleMapper ruleMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<RuleItemLk> entries = findBySqlId("pagerModel", params);
            List<RuleItemLkModel> models = new ArrayList();
            for(RuleItemLk entry: entries){
                RuleItemLk obj = (RuleItemLk)entry;
                models.add(RuleItemLkModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RuleItemLkModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                RuleItemLk obj = (RuleItemLk)entry;
                models.add(RuleItemLkModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public RuleItemLkModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");
        params.put("id",id);
        RuleItemLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RuleItemLkModel.fromEntry(entry);
    }




    @Override
    public void insert(RuleItemLkModel model) {

        RuleItemLk obj = new RuleItemLk();
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
    public void update(RuleItemLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");

        RuleItemLk obj = new RuleItemLk();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }

    /**
     * 新增
     * @param demo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRuleItems(RuleItemLkModel demo) {
        //保存
        addRuleItems(demo);

        //修改同步状态
        updateSyncRuleItems(demo);
    }

    /**
     * 删除
     * @param ruleId
     * @param delItemIds
     * @return
     */
    @Override
    public int deleteRuleItemLks(String ruleId, String delItemIds) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");
        int count = 0;
        if(!StringUtils.isEmpty(delItemIds)) {
            String[] arr = delItemIds.split(",");
            for (String itemId : arr) {
                params.put("ruleId", ruleId);
                params.put("itemId", itemId);
                int r = super.delete("deleteByRuleId", params);
                count += r;
            }
        }
        return count;
    }


    /**
     * 根据协议类型ID查询树形列表
     * @param pagerInfo
     * @return
     */
    @Override
    public Object tree(PagerInfo pagerInfo) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //获取当前通讯协议的数据项
        List<RuleItemLk> ruleItemLks = findBySqlId("pagerModel", params);
        Map<String, RuleItemLk> riMap = new HashMap<>();
        for(RuleItemLk ruleItemLk : ruleItemLks){
            riMap.put(ruleItemLk.getItemId(), ruleItemLk);
        }

        List<DataItem> entries = dataItemService.findBySqlId("findTreeListByRuleType", params);
        List<DataItemModel> models = new ArrayList();
        for (DataItem entry : entries) {
            DataItem obj = (DataItem) entry;
            DataItemModel model = DataItemModel.fromEntry(obj);
            if(riMap.get(model.getId()) != null){
                model.setDisabled("true");
            }else{
                model.setDisabled("false");
            }
            models.add(model);
        }

        //如果为空，转化树会报错再此处判断
        if(CollectionUtils.isEmpty(models)){
            ResultObjMsg msg = new ResultObjMsg();
            msg.setData(null);
            return msg;
        }

        TreeNode root = new TreeHandler<DataItemModel>(models) {

            @Override
            protected TreeNode beanToTreeNode(DataItemModel bean) {
                TreeNode tn = new TreeNode();
                tn.setId(bean.getId());
                tn.setParentId(bean.getParentId());
                tn.setLabel(bean.getName());
                Map<String, Object> attr = new HashMap<>();
                attr.put("name", bean.getName());
                attr.put("path", bean.getPath());
                attr.put("isItem", bean.getIsItem());
                attr.put("disabled", bean.getDisabled());
                tn.setAttributes(attr);
                return tn;
            }

            @Override
            protected boolean isRoot(TreeNode treeNode) {
                return "0".equals(treeNode.getId())?true:false;
            }
        }.toTree();
        return root;
    }

    private void addRuleItems(RuleItemLkModel demo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule_item_lk", "lk");
        params.put("ruleId", demo.getRuleId());
        List<RuleItemLk> entries = findBySqlId("pagerModel", params);
        Map<String, RuleItemLk> map = new HashMap<>();
        for(RuleItemLk entry: entries) {
            map.put(entry.getItemId(), entry);
        }
        String addItemIds = demo.getAddItemIds();
        if(!StringUtils.isEmpty(addItemIds)){
            String[] arr = addItemIds.split(",");
            RuleItemLk obj = new RuleItemLk();
            for (String itemId : arr){
                if(map.get(itemId) == null) {
                    //单元测试指定id，如果是单元测试，那么就不使用uuid
                    String id = UtilHelper.getUUID();
                    obj.setId(id);
                    obj.setItemId(itemId);
                    obj.setRuleId(demo.getRuleId());
                    int res = super.insert(obj);
                    if (res == 0) {
                        throw new BusinessException("新增失败");
                    }
                }
            }
        }
    }

    /**
     * 修改同步数据项状态
     * @param demo
     * @return
     */
    private void updateSyncRuleItems(RuleItemLkModel demo) {
        //1、修改同步状态
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule", "ru");
        params.put("id", demo.getRuleId());
        params.put("syncItem", demo.getSyncDataItem());
        params.put("updateTime", DateUtil.getNow());
        params.put("updateBy", ServletUtil.getCurrentUser());
        ruleMapper.updateSyncDataItem(params);
    }

    @Override
    public void syncRuleTypeDataItemToRule(){
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_rule", "ru");
        params.put("syncDataItem", 1);
        List<Rule> rules = ruleMapper.pagerModel(params);
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        StringBuilder sb = null;
        for (Rule rule: rules) {
            List<DataItem> dataItems = dataItemService.findRuleTypeDataItems(rule.getRuleTypeId());
            sb = new StringBuilder();

            for (DataItem item : dataItems) {
                sb.append(item.getId());
                sb.append(",");
            }
            if (sb.toString().endsWith(",")) {
                RuleItemLkModel demo = new RuleItemLkModel();
                demo.setRuleId(rule.getId());
                demo.setAddItemIds(sb.substring(0, sb.length() - 1));
                addRuleItems(demo);
            }
        }
    }
}
