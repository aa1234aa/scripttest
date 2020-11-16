package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultObjMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.TreeHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.dao.DataItemMapper;
import com.bitnei.cloud.dc.domain.DataItemGroup;
import com.bitnei.cloud.dc.domain.RuleType;
import com.bitnei.cloud.dc.model.DataItemGroupModel;
import com.bitnei.cloud.dc.model.DataItemImportModel;
import com.bitnei.cloud.dc.model.DataItemTempModel;
import com.bitnei.cloud.dc.service.IDataItemGroupService;
import com.bitnei.cloud.dc.service.IRuleTypeService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemService实现<br>
* 描述： DataItemService实现<br>
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
* <td>2019-01-30 17:28:53</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.DataItemMapper" )
public class DataItemService extends BaseService implements IDataItemService {

    @Autowired
    private IDataItemGroupService dataItemGroupService;
    @Autowired
    private IRuleTypeService ruleTypeService;
    @Resource
    private DataItemMapper dataItemMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataItem> entries = findBySqlId("pagerModel", params);
            List<DataItemModel> models = new ArrayList();
            for(DataItem entry: entries){
                DataItem obj = (DataItem)entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataItemModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DataItem obj = (DataItem)entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public DataItemModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.put("id", id);
        DataItem entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataItemModel.fromEntry(entry);
    }




    @Override
    public void insert(DataItemModel model) {

        DataItem obj = new DataItem();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);

        //获取当权限的map
        Map<String,Object> params = new HashMap<>();
        //检查所选数据项类型是否父节点
        params.put("parentId", model.getItemGroupId());
        Integer digCount = dataItemGroupService.unique("findByParentIdCount", params);
        if(digCount > 0){
            throw new BusinessException("所选数据项类型是父节点，不允许添加数据项");
        }

        //协议类型Id赋值
        params.clear();
        params.put("id", obj.getItemGroupId());
        DataItemGroup groupModel = dataItemGroupService.unique("findById", params);
        if(groupModel == null){
            throw new BusinessException("数据项分组不存在");
        }else {
            //获取当权限的map
            Map<String, Object> itemParams = new HashMap<>();
            itemParams.put("name", model.getName().trim());
            itemParams.put("ruleTypeId", groupModel.getRuleTypeId());
            DataItem di = unique("findByName", itemParams);
            if (di != null) {
                throw new BusinessException("数据项名称已存在");
            }
            itemParams.clear();
            itemParams.put("seqNo", model.getSeqNo());
            itemParams.put("ruleTypeId", groupModel.getRuleTypeId());
            Integer seqNoCount = unique("findBySeqNoCount", itemParams);
            if (seqNoCount > 0) {
                throw new BusinessException("编号已存在");
            }
            if(model.getByteStartPos() != null && model.getBitStartPos() != null){
                //同一数据项类型下，开始byte位+开始bit位联合唯一
                itemParams.clear();
                itemParams.put("itemGroupId", groupModel.getId());
                itemParams.put("byteStartPos", model.getByteStartPos());
                itemParams.put("bitStartPos", model.getBitStartPos());
                Integer posCount = unique("findByStartPosCount", itemParams);
                if (posCount > 0) {
                    throw new BusinessException(String.format("数据项类型 %s 下，相同的开始BYTE位置和开始BIT位置 已存在", groupModel.getName()));
                }
            }
            obj.setRuleTypeId(groupModel.getRuleTypeId());
            obj.setCreateTime(DateUtil.getNow());
            obj.setCreateBy(ServletUtil.getCurrentUser());
            int res = super.insert(obj);
            if (res == 0) {
                throw new BusinessException("新增失败");
            }
        }
    }

    @Override
    public void update(DataItemModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");

        DataItem obj = new DataItem();
        BeanUtils.copyProperties(model, obj);
        //协议类型Id赋值
        //获取当权限的map
        Map<String,Object> groupParams = new HashMap<>();
        groupParams.put("id", obj.getItemGroupId());
        DataItemGroup groupModel = dataItemGroupService.unique("findById", groupParams);
        if(groupModel == null){
            throw new BusinessException("数据项类型不存在");
        }else {
            //获取当权限的map
            Map<String, Object> itemParams = new HashMap<>();
            itemParams.put("id", model.getId());
            itemParams.put("name", model.getName().trim());
            itemParams.put("ruleTypeId", groupModel.getRuleTypeId());
            DataItem di = unique("findByName", itemParams);
            if (di != null) {
                throw new BusinessException("数据项名称已存在");
            }
            itemParams.clear();
            itemParams.put("id", model.getId());
            itemParams.put("seqNo", model.getSeqNo());
            itemParams.put("ruleTypeId", groupModel.getRuleTypeId());
            Integer seqNoCount = unique("findBySeqNoCount", itemParams);
            if (seqNoCount > 0) {
                throw new BusinessException("编号已存在");
            }
            if(model.getByteStartPos() != null && model.getBitStartPos() != null){
                //同一数据项类型下，开始byte位+开始bit位联合唯一
                itemParams.clear();
                itemParams.put("itemGroupId", groupModel.getId());
                itemParams.put("byteStartPos", model.getByteStartPos());
                itemParams.put("bitStartPos", model.getBitStartPos());
                itemParams.put("id", model.getId());
                Integer posCount = unique("findByStartPosCount", itemParams);
                if (posCount > 0) {
                    throw new BusinessException(String.format("数据项类型 %s 下，相同的开始BYTE位置和开始BIT位置 已存在", groupModel.getName()));
                }
            }

            obj.setRuleTypeId(groupModel.getRuleTypeId());
            obj.setUpdateTime(DateUtil.getNow());
            obj.setUpdateBy(ServletUtil.getCurrentUser());
            params.putAll(MapperUtil.Object2Map(obj));
            int res = super.updateByMap(params);
            if (res == 0) {
                throw new BusinessException("更新失败");
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            //获取当权限的map
            Map<String,Object> diParams = DataAccessKit.getAuthMap("dc_data_item", "di");
            diParams.put("id", id);
            DataItem di = unique("findById", diParams);
            if (di.getIsCustom().equals(Constants.NO)) {
                throw new BusinessException("非自定义数据项不允许删除", 300);
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DataItem>(this, "pagerModel", params, "dc/res/dataItem/export.xls", "数据项") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DATAITEM"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DataItemImportModel>(file, messageType, GroupExcelImport.class, 0, 1) {
            String ruleTypeId = "";
            String itemGroupId = "";
            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemImportModel model) {
                List<String> list = new ArrayList<String>();

                //获取当权限的map 协议类型
                Map<String,Object> rtParams = new HashMap<>();
                rtParams.put("name",model.getRuleType().trim());
                RuleType ruleType = ruleTypeService.unique("findByName", rtParams);
                if (ruleType == null){
                    list.add("协议类型不存在，请确认");
                }else{
                    ruleTypeId = ruleType.getId();
                    //获取协议类型对应的数据项类型
                    Map<String,Object> params = new HashMap<>();
                    params.put("ruleTypeId",ruleTypeId);
                    params.put("name",model.getItemGroup().trim());
                    DataItemGroup groupModel = dataItemGroupService.unique("findByName", params);
                    if(groupModel == null){
                        list.add(String.format("协议类型为 %s 的数据项类型 %s 不存在，请确认", model.getRuleType().trim(), model.getItemGroup().trim()));
                    }else {

                        //检查所选数据项类型是否父节点
                        Map<String,Object> digParams = new HashMap<>();
                        digParams.put("parentId", groupModel.getId());
                        Integer digCount = dataItemGroupService.unique("findByParentIdCount", digParams);
                        if(digCount > 0){
                            list.add("所选数据项类型是父节点，不允许添加数据项");
                        }
                        //获取当权限的map
                        Map<String, Object> itemParams = new HashMap<>();
                        itemParams.put("name", model.getName().trim());
                        itemParams.put("ruleTypeId", ruleType.getId());
                        DataItem di = unique("findByName", itemParams);
                        if (di != null) {
                            list.add("数据项名称已存在");
                        }
                        itemParams.clear();
                        itemParams.put("seqNo", model.getSeqNo());
                        itemParams.put("ruleTypeId", ruleType.getId());
                        Integer seqNoCount = unique("findBySeqNoCount", itemParams);
                        if (seqNoCount > 0) {
                            list.add("编号已存在");
                        }
                        if(model.getByteStartPos() != null && model.getBitStartPos() != null){
                            //同一数据项类型下，开始byte位+开始bit位联合唯一
                            itemParams.clear();
                            itemParams.put("itemGroupId", groupModel.getId());
                            itemParams.put("byteStartPos", model.getByteStartPos());
                            itemParams.put("bitStartPos", model.getBitStartPos());
                            Integer posCount = unique("findByStartPosCount", itemParams);
                            if (posCount > 0) {
                                list.add(String.format("数据项类型 %s 下，相同的开始BYTE位置和开始BIT位置 已存在", groupModel.getName()));
                            }
                        }
                        itemGroupId = groupModel.getId();
                    }
                }
                if(!StringUtils.isEmpty(model.getHighLowFlg())){
                    if(Constants.HighLowFlg.getValue(model.getHighLowFlg().trim()) == null) {
                        list.add("高低位不存在，请确认");
                    }
                }
                if(!StringUtils.isEmpty(model.getDataType())){
                    if(Constants.DataType.getValue(model.getDataType().trim()) == null) {
                        list.add("数据类型不存在，请确认");
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
            public void saveObject(DataItemImportModel model) {
                DataItemModel item = new DataItemModel();
                item.setName(model.getName());
                item.setRuleTypeId(ruleTypeId);
                item.setItemGroupId(itemGroupId);
                item.setSeqNo(model.getSeqNo());
                item.setByteStartPos(model.getByteStartPos());
                item.setBitStartPos(model.getBitStartPos());
                item.setLength(model.getLength());
                item.setFactor(model.getFactor()==null?"":model.getFactor().toString());
                item.setOffset(model.getOffset()==null?"":model.getOffset().toString());
                item.setDot(model.getDot());
                item.setUnit(model.getUnit());
                if(!StringUtils.isEmpty(model.getDataType())) {
                    item.setDataType(Constants.DataType.getValue(model.getDataType().trim()));
                }
                item.setUpperLimit(model.getUpperLimit() == null ? "" : model.getUpperLimit().toString());
                item.setLowerLimit(model.getLowerLimit() == null ? "" : model.getLowerLimit().toString());
                if(!StringUtils.isEmpty(model.getHighLowFlg())) {
                    item.setHighLowFlg(Constants.HighLowFlg.getValue(model.getHighLowFlg().trim()));
                }
                if(!StringUtils.isEmpty(model.getEnableParseRule())) {
                    item.setEnableParseRule(Constants.BoolType.getValue(model.getEnableParseRule().trim()));
                }
                if(!StringUtils.isEmpty(model.getIsShow())) {
                    item.setIsShow(Constants.BoolType.getValue(model.getIsShow().trim()));
                }
                if(!StringUtils.isEmpty(model.getIsValid())) {
                    item.setIsValid(Constants.BoolType.getValue(model.getIsValid().trim()));
                }
                if(!StringUtils.isEmpty(model.getIsArray())) {
                    item.setIsArray(Constants.BoolType.getValue(model.getIsArray().trim()));
                }
                item.setIsCustom(Constants.BoolType.getValue("是"));
                item.setNote(model.getNote());
            	insert(item);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DATAITEM"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DataItemModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataItemModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 根据协议ID获取数据项列表
     * @param ruleId 查询参数
     * @return
     */
    @Override
    public List<DataItemTempModel> getListByRuleId(String ruleId) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("ruleId", ruleId);
        List<DataItem> entries = findBySqlId("findListByRuleId", params);
        List<DataItemTempModel> models = new ArrayList();
        for(DataItem entry: entries){
            DataItem obj = (DataItem)entry;
            //1.获取数据项对应的数据项类型
            DataItemGroupModel groupModel = dataItemGroupService.findById(obj.getItemGroupId());
            DataItemTempModel tempModel = DataItemTempModel.fromEntry(obj);
            tempModel.setPathName(groupModel.getPathName());
            models.add(tempModel);
        }
        return models;
    }

    @Override
    public Object getPageListByRuleId(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataItem> entries = findBySqlId("pagerModelByRuleId", params);
            List<DataItemTempModel> models = new ArrayList();
            for(DataItem entry: entries){
                DataItem obj = (DataItem)entry;
                //1.获取数据项对应的数据项类型
                DataItemGroupModel groupModel = dataItemGroupService.findById(obj.getItemGroupId());
                DataItemTempModel tempModel = DataItemTempModel.fromEntry(obj);
                tempModel.setPathName(groupModel.getPathName());
                models.add(tempModel);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModelByRuleId", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataItemModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                DataItem obj = (DataItem) entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据转发平台ID获取数据项列表
     * @param platformId 查询参数
     * @return
     */
    @Override
    public List<DataItemTempModel> getListByPlatformId(String platformId) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("platformId", platformId);
        List<DataItem> entries = findBySqlId("findListByPlatformId", params);
        List<DataItemTempModel> models = new ArrayList();
        for(DataItem entry: entries){
            DataItem obj = (DataItem)entry;
            models.add(DataItemTempModel.fromEntry(obj));
        }
        return models;
    }

    @Override
    public Object getPageListByPlatformId(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataItem> entries = findBySqlId("pagerModelByPlatformId", params);
            List<DataItemModel> models = new ArrayList();
            for(DataItem entry: entries){
                DataItem obj = (DataItem)entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModelByPlatformId", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataItemModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                DataItem obj = (DataItem) entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据数据项组ID查询树形列表
     * @param pagerInfo
     * @return
     */
    @Override
    public Object tree(PagerInfo pagerInfo) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        List<DataItem> entries = findBySqlId("findTreeList", params);
        List<DataItemModel> models = new ArrayList();
        for (DataItem entry : entries) {
            DataItem obj = (DataItem) entry;
            models.add(DataItemModel.fromEntry(obj));
        }
        Map<String,Object> searchParam = DataAccessKit.getAuthMap("dc_data_item_group", "dig");
        searchParam.put("id", params.get("itemGroupId"));
        DataItemGroup groupModel = dataItemGroupService.unique("findById", searchParam);
        if(groupModel != null && !"0".equals(groupModel.getParentId())){
            searchParam.put("id", groupModel.getParentId());
            DataItemGroup group = dataItemGroupService.unique("findById", searchParam);
            if(group != null){
                DataItemModel item = new DataItemModel();
                item.setId(group.getId());
                item.setName(group.getName());
                item.setParentId(group.getParentId());
                item.setPath(group.getPath());
                models.add(item);
            }
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

    /**
     * 拷贝数据项
     * @param demo1
     */
    @Override
    public DataItemTempModel copyItems(DataItemTempModel demo1) {
        String itemIds = demo1.getItemIds();
        String groupId = demo1.getGroupId();
        String ruleTypeId = demo1.getRuleTypeId();
        Map<String,Object> params = new HashMap<String,Object>();
        //获取协议类型的数据项
        params.put("ruleTypeId",ruleTypeId);
        List<DataItem> entries = findBySqlId("pagerModel", params);
        Map<String,DataItem> itemMap = new HashMap<String,DataItem>();
        for(DataItem dataItem : entries){
            itemMap.put(dataItem.getId(), dataItem);
        }
        DataItemTempModel resultModel = new DataItemTempModel();
        String repeatItemName = "";
        if(!StringUtils.isEmpty(itemIds) && !StringUtils.isEmpty(groupId)){
            String[] arr = itemIds.split(",");
            params.clear();
            params.put("itemIds", arr);
            List<DataItem> addEntries = findBySqlId("getByIds", params);

            DataItem ruleTypeItem = null;
            for (DataItem item : addEntries){
                //判断协议类型的数据项是否已存在
                ruleTypeItem = itemMap.get(item.getId());
                if(ruleTypeItem != null){
                    if(repeatItemName.length() > 0){
                        repeatItemName += "," + ruleTypeItem.getName();
                    }else{
                        repeatItemName = ruleTypeItem.getName();
                    }
                }else {
                    String id = UtilHelper.getUUID();
                    item.setId(id);
                    item.setItemGroupId(groupId);
                    item.setRuleTypeId(ruleTypeId);
                    item.setCreateTime(DateUtil.getNow());
                    item.setCreateBy(ServletUtil.getCurrentUser());
                    int res = super.insert(item);
                    if (res == 0) {
                        throw new BusinessException("拷贝数据项失败");
                    }
                }
            }
        }
        resultModel.setRepeatItemName(repeatItemName);
        return resultModel;
    }

    //根据数据项名称获取详细信息
    @Override
    public DataItemModel getByName(String name, String ruleTypeId) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.put("name",name);
        params.put("ruleTypeId",ruleTypeId);
        DataItem entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return DataItemModel.fromEntry(entry);
    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("数据项导入模板.xls" , DataItemImportModel.class);
    }



    @Override
    public List<DataItemModel> findCustomizeByRuleTypeCode(String ruleTypeCode) {

        List<DataItem> items = dataItemMapper.findCustomizeByRuleTypeCode(ruleTypeCode);
        List<DataItemModel> models = new ArrayList();
        for (DataItem entry : items) {
            models.add(DataItemModel.fromEntry(entry));
        }
        return models;
    }

    @Override
    public List<DataItem> findRuleTypeDataItems (String ruleTypeId) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.put("ruleTypeId", ruleTypeId);
        return dataItemMapper.pagerModel(params);
    }

    @Override
    public Object listPageListByRuleId(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<DataItem> entries = findBySqlId("pagerModelByRuleId", params);
            List<DataItemModel> models = new ArrayList();
            for(DataItem entry: entries){
                DataItem obj = entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModelByRuleId", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<DataItemModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                DataItem obj = (DataItem) entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据检测规则ID获取分页数据项列表
     * @param pagerInfo
     * @return
     */
    @Override
    public Object getListByCheckRuleId(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_data_item", "di");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataItem> entries = findBySqlId("pagerModelByCheckRuleId", params);
            List<DataItemModel> models = new ArrayList();
            for(DataItem entry: entries){
                DataItem obj = (DataItem)entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModelByCheckRuleId", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataItemModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                DataItem obj = (DataItem) entry;
                models.add(DataItemModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }
}
