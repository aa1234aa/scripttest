package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultObjMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.domain.DataCheckRuleItem;
import com.bitnei.cloud.veh.model.DataCheckRuleItemModel;
import com.bitnei.cloud.veh.service.IDataCheckRuleItemService;
import com.bitnei.cloud.common.util.ServletUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRuleItemService实现<br>
* 描述： DataCheckRuleItemService实现<br>
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
* <td>2019-09-16 15:40:45</td>
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
@Mybatis(namespace = "com.bitnei.cloud.veh.dao.DataCheckRuleItemMapper" )
public class DataCheckRuleItemService extends BaseService implements IDataCheckRuleItemService {

    @Autowired
    private IDataItemService dataItemService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataCheckRuleItem> entries = findBySqlId("pagerModel", params);
            List<DataCheckRuleItemModel> models = new ArrayList();
            for(DataCheckRuleItem entry: entries){
                DataCheckRuleItem obj = (DataCheckRuleItem)entry;
                models.add(DataCheckRuleItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataCheckRuleItemModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DataCheckRuleItem obj = (DataCheckRuleItem)entry;
                models.add(DataCheckRuleItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public DataCheckRuleItemModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");
        params.put("id",id);
        DataCheckRuleItem entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataCheckRuleItemModel.fromEntry(entry);
    }




    @Override
    public void insert(DataCheckRuleItemModel model) {

        DataCheckRuleItem obj = new DataCheckRuleItem();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(DataCheckRuleItemModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");

        DataCheckRuleItem obj = new DataCheckRuleItem();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("veh_data_check_rule_item", "dcri");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }

    @Override
    public Object tree(PagerInfo pagerInfo) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        Map<String, DataCheckRuleItem> crMap = new HashMap<>();
        if (params.get("checkRuleId") != null) {
            //获取当前检测规则的数据项
            List<DataCheckRuleItem> checkRuleItems = findBySqlId("pagerModel", params);
            for (DataCheckRuleItem checkRuleItem : checkRuleItems) {
                crMap.put(checkRuleItem.getDataItemId(), checkRuleItem);
            }
        }

        List<DataItem> entries = dataItemService.findBySqlId("findTreeListByRule", params);
        List<DataItemModel> models = new ArrayList();
        for (DataItem entry : entries) {
            DataItem obj = (DataItem) entry;
            DataItemModel model = DataItemModel.fromEntry(obj);
            if(crMap.get(model.getId()) != null){
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


}
