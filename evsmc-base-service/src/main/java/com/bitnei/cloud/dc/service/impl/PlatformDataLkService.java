package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultObjMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.dao.ForwardPlatformMapper;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.domain.ForwardPlatform;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.dc.model.RuleItemLkModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.PlatformDataLk;
import com.bitnei.cloud.dc.model.PlatformDataLkModel;
import com.bitnei.cloud.dc.service.IPlatformDataLkService;
import com.bitnei.cloud.common.util.ServletUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformDataLkService实现<br>
* 描述： PlatformDataLkService实现<br>
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
* <td>2019-02-12 17:08:40</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.mapper.PlatformDataLkMapper" )
public class PlatformDataLkService extends BaseService implements IPlatformDataLkService {

    @Autowired
    private IDataItemService dataItemService;

    @Resource
    private ForwardPlatformMapper forwardPlatformMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "palk");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<PlatformDataLk> entries = findBySqlId("pagerModel", params);
            List<PlatformDataLkModel> models = new ArrayList();
            for(PlatformDataLk entry: entries){
                PlatformDataLk obj = (PlatformDataLk)entry;
                models.add(PlatformDataLkModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PlatformDataLkModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                PlatformDataLk obj = (PlatformDataLk)entry;
                models.add(PlatformDataLkModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public PlatformDataLkModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "palk");
        params.put("id",id);
        PlatformDataLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PlatformDataLkModel.fromEntry(entry);
    }




    @Override
    public void insert(PlatformDataLkModel model) {

        PlatformDataLk obj = new PlatformDataLk();
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
    public void update(PlatformDataLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "palk");

        PlatformDataLk obj = new PlatformDataLk();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "palk");

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
    @Transactional(rollbackFor = Exception.class)
    public void insertPlatformItems(PlatformDataLkModel demo) {
        //保存属性
        addPlatformItems(demo, false);
        //修改同步状态
        updateSyncPlatformItems(demo);
    }

    @Override
    public int deletePlatformItemLks(PlatformDataLkModel demo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "lk");
        String delItemIds = demo.getDelItemIds();
        int count = 0;
        if(!StringUtils.isEmpty(delItemIds)) {
            String[] arr = delItemIds.split(",");
            for (String itemId : arr) {
                params.put("platformId", demo.getPlatformId());
                params.put("dataItemId", itemId);
                int r = super.delete("deleteByPlatformId", params);
                count += r;
            }
        }
        return count;
    }

    @Override
    public Object tree(PagerInfo pagerInfo) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //获取当前转发平台的数据项
        List<PlatformDataLk> platformDataLks = findBySqlId("pagerModel", params);
        Map<String, PlatformDataLk> pdMap = new HashMap<>();
        for(PlatformDataLk platformDataLk : platformDataLks){
            pdMap.put(platformDataLk.getDataItemId(), platformDataLk);
        }

        List<DataItem> entries = dataItemService.findBySqlId("findTreeListByRule", params);
        List<DataItemModel> models = new ArrayList();
        for (DataItem entry : entries) {
            DataItem obj = (DataItem) entry;
            DataItemModel model = DataItemModel.fromEntry(obj);
            if(pdMap.get(model.getId()) != null){
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

    @Override
    public void deletePlatformDataItem(String platformId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "lk");
        params.put("platformId",platformId);
        super.delete("deleteByPlatformId", params);
    }
    private void addPlatformItems(PlatformDataLkModel demo, boolean snyc){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_data_lk", "palk");
        params.put("platformId", demo.getPlatformId());
        List<PlatformDataLk> entries = findBySqlId("pagerModel", params);
        Map<String, PlatformDataLk> map = new HashMap<>();
        for(PlatformDataLk entry: entries) {
            map.put(entry.getDataItemId(), entry);
        }
        String addItemIds = demo.getAddItemIds();
        if(!StringUtils.isEmpty(addItemIds)){
            String[] arr = addItemIds.split(",");
            PlatformDataLk obj = new PlatformDataLk();
            for (String itemId : arr){
                if(map.get(itemId) == null) {
                    //单元测试指定id，如果是单元测试，那么就不使用uuid
                    String id = UtilHelper.getUUID();
                    obj.setId(id);
                    obj.setDataItemId(itemId);
                    obj.setPlatformId(demo.getPlatformId());
                    int res = super.insert(obj);
                    if (res == 0) {
                        throw new BusinessException("新增失败");
                    }
                }
            }
        }
        //删除取消的属性
        if (!snyc) {
            removeDataItmes(entries, addItemIds);
        }
    }

    private void removeDataItmes(List<PlatformDataLk> entries, String addItemIds) {
        if (CollectionUtils.isNotEmpty(entries) && StringUtils.isNotBlank(addItemIds)) {
            for(PlatformDataLk entry: entries) {
                if (!addItemIds.contains(entry.getDataItemId())) {
                    //
                    deleteMulti(entry.getId());
                }
            }
        }
    }

    private void updateSyncPlatformItems(PlatformDataLkModel demo) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        params.put("id", demo.getPlatformId());
        params.put("syncItem", demo.getSyncDataItem());
        params.put("updateTime", DateUtil.getNow());
        params.put("updateBy", ServletUtil.getCurrentUser());
        forwardPlatformMapper.updatePlatformSyncDataItem(params);
    }

    @Override
    public void syncPlatformDataItem() {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        List<ForwardPlatform> entries = forwardPlatformMapper.pagerModel(params);

        StringBuilder dataItemIdStr = null;
        for (ForwardPlatform platform : entries) {
            //自动同步
            if (platform.getSyncDataItem().intValue() == 1) {
                dataItemIdStr = new StringBuilder();
                params = new HashMap<>();
                params.put("ruleId", platform.getRuleId());
                List<DataItem> ruleDataItems = dataItemService.findBySqlId("findListByRuleId", params);
                for(DataItem item : ruleDataItems) {
                    dataItemIdStr.append(item.getId());
                    dataItemIdStr.append(",");
                }
                if (dataItemIdStr.toString().endsWith(",")) {
                    PlatformDataLkModel demo = new PlatformDataLkModel();
                    demo.setPlatformId(platform.getId());
                    demo.setAddItemIds(dataItemIdStr.substring(0, dataItemIdStr.length() - 1));
                    addPlatformItems(demo, true);
                }
            }
        }
    }
}
