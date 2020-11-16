package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.StringUtilExs;
import com.bitnei.cloud.common.api.*;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.screen.protocol.StringUtil;
import com.bitnei.cloud.sys.dao.*;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.IGroupResourceRuleService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： GroupResourceRuleService实现<br>
 * 描述： GroupResourceRuleService实现<br>
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
 * <td>2019-01-22 16:30:51</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.GroupResourceRuleMapper")
public class GroupResourceRuleService extends BaseService implements IGroupResourceRuleService {


    @Resource
    private CoreResourceItemMapper coreResourceItemMapper;
    @Resource
    private GroupResourceRuleMapper groupResourceRuleMapper;
    @Autowired
    private CoreResourceItemService coreResourceItemService;
    @Autowired
    private VehicleService vehicleService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private UserBlockResourceMapper userBlockResourceMapper;

    @Resource(name = "webRedisKit")
    private RedisKit redisKit;



    @Override
    public ResultListMsg<Map<String, Object>> selectResources(String groupId, String resourceItemId, PagerInfo pagerInfo, boolean hasFilter) {
        //获取column配置
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //查询资源项
        params.put("id", resourceItemId);
        CoreResourceItem coreResourceItem = coreResourceItemMapper.findById(params);


        Map<String,Object> tmpParams = new HashMap<>();
        tmpParams.put("groupId", groupId);
        tmpParams.put("resourceItemId", resourceItemId);

        //从sql中获取column配置
        String querySql = coreResourceItem.getQuerySql().trim().toLowerCase();
        String alias = getAlias(querySql);
        if (hasFilter){
            GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(tmpParams);
            if (rule != null){

                //得到所选的值
                String[] vals = rule.getVal().split(",");
                String[] valTmps = new String[vals.length];
                for (int i=0; i< vals.length; i++){
                    valTmps[i] = "'" + vals[i] + "'";
                }
                querySql = String.format("%s and %s.id not in(%s)", querySql ,alias, StringUtils.join(valTmps, ","));
            }
        }

        Map<String,Object> tempMap = ServletUtil.pageInfoToMap(pagerInfo);
        if (tempMap.containsKey("keyword") && tempMap.get("keyword") != null && StringUtils.isNotEmpty(tempMap.get("keyword").toString())){
            querySql = String.format("%s and %s.%s like '%s'", querySql, alias, coreResourceItem.getIdentifyColumnName(), "%"+tempMap.get("keyword").toString()+"%");
        }

        return findResultListBySql(querySql, coreResourceItem, pagerInfo);
    }





    @Override
    public ResultListMsg<Map<String, Object>> selectSelfResources(String groupId, String resourceId, PagerInfo pagerInfo) {

        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceId);
        if (selfItem == null){
            throw new BusinessException("该资源没有自身属性，请先配置");

        }
        return selectResources(groupId, selfItem.getId(), pagerInfo, true);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRules(GroupRuleListModel listModel) {

        //先判断条数
        if (listModel.getRules() == null || listModel.getRules().isEmpty()){
            throw new BusinessException("规则设置至少一条");
        }

        //首先移除
        Map<String, Object> delMap = new HashMap<>();
        delMap.put("groupId", listModel.getGroupId());
        delMap.put("resourceTypeId", listModel.getResourceTypeId());
        groupResourceRuleMapper.deleteByGroupAndResource(delMap);

        //重新插入规则
        List<GroupResourceRule> rules = new ArrayList<>();
        int orderMum = 0;
        for (GroupResourceRuleModel m: listModel.getRules()){
            GroupResourceRule rule = new GroupResourceRule();
            rule.setId(UtilHelper.getUUID());
            rule.setPreLogicOp(m.getPreLogicOp());
            rule.setGroupId(listModel.getGroupId());
            rule.setResourceItemId(m.getResourceItemId());
            rule.setOp(m.getOp());
            rule.setVal(m.getVal());
            rule.setCreateTime(DateUtil.getNow());
            rule.setCreateBy(ServletUtil.getCurrentUser());
            rule.setOrderNum(orderMum++);
            rules.add(rule);

        }
        groupResourceRuleMapper.saveRules(rules);

    }

    @Override
    public List<GroupResourceRuleModel> getRules(String groupId, String resourceTypeId) {

        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceTypeId", resourceTypeId);
        params.put("eqNotPreCode", "A");

        List<GroupResourceRule> rules = groupResourceRuleMapper.pagerModel(params);
        List<GroupResourceRuleModel> models = new ArrayList<>();

        for (GroupResourceRule rule: rules){
            GroupResourceRuleModel model = GroupResourceRuleModel.fromEntry(rule);
            //处理名称显示
            String desc =  coreResourceItemService.findNameByResItemIdAndIds(model.getResourceItemId(), model.getVal());
            model.setValName(desc);
            models.add(model);

        }
        return models;
    }

    /**
     * 获取已选资源（列表模式）
     *
     * @param groupId
     * @param resourceTypeId
     * @param pagerInfo
     * @return
     */
    @Override
    public ResultListMsg<Map<String, Object>> selectListResource(String groupId, String resourceTypeId, PagerInfo pagerInfo) {

        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);

        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceItemId", selfItem.getId());

        //从sql中获取column配置
        String querySql = selfItem.getQuerySql().trim().toLowerCase();
        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        String alias = getAlias(querySql);
        if (rule != null){

            //得到所选的值
            String[] vals = rule.getVal().split(",");
            String[] valTmps = new String[vals.length];
            for (int i=0; i< vals.length; i++){
                valTmps[i] = "'" + vals[i] + "'";

            }
            querySql = String.format("%s and %s.id in(%s)", querySql ,alias, StringUtils.join(valTmps, ","));

        }
        else {
            querySql = String.format("%s and 1!=1", querySql );
        }

        Map<String,Object> tempMap = ServletUtil.pageInfoToMap(pagerInfo);
        if (tempMap.containsKey("keyword") && tempMap.get("keyword") != null && StringUtils.isNotEmpty(tempMap.get("keyword").toString())){
            querySql = String.format("%s and %s.%s like '%s'", querySql, alias, selfItem.getIdentifyColumnName(), "%"+tempMap.get("keyword").toString()+"%");
        }
        return findResultListBySql(querySql, selfItem, pagerInfo);

    }


    /**
     * 通过sql查找
     * @param querySql
     * @param item
     * @param pagerInfo
     * @return
     */
    private ResultListMsg<Map<String, Object>> findResultListBySql(String querySql, CoreResourceItem item, PagerInfo pagerInfo){

        ResultListMsg<Map<String, Object>> resultListMsg = new ResultListMsg<>();

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //设置表头
        resultListMsg.setColumns(getColumns(querySql, item));
        long total = 0;
        //查询数据
        if (pagerInfo.getLimit()>0){
            PageHelper.offsetPage(pagerInfo.getStart(), pagerInfo.getLimit());
            searchParams.put("querySql", querySql);
            List<Map<String, Object>> results = coreResourceItemMapper.queryResources(searchParams);
            Page<Map<String, Object>> page = (Page) results;
            total = page.getTotal();
            //设置数据
            resultListMsg.setData(page.getResult());

        }

        else {
            List<Map<String, Object>> results = coreResourceItemMapper.queryResources(searchParams);
            //设置数据
            resultListMsg.setData(results);
            total = results.size();
        }

        //设置分页信息
        resultListMsg.setPagination(getpagination(pagerInfo.getStart(), pagerInfo.getLimit(), total));
        resultListMsg.setType(RspDataType.COLLECTION.getValue());
        resultListMsg.setCode(200);
        return resultListMsg;

    }

    /**
     * 获取查询数
     * @param conditions
     * @param key
     * @return
     */
    private String getConditionVal(Condition[] conditions, final String key){

        for (Condition condition: conditions){
            if (key.equals(condition.getName())){
                return condition.getValue();
            }
        }
        return null;
    }

    /**
     * 列表模式下的新增
     *
     * @param addModel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupListAddModelResp listAdd(GroupListAddModel addModel) {

        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(addModel.getResourceTypeId());

        if (selfItem == null){
            throw new BusinessException("该资源没有自身属性，请先配置");

        }

        //判断是否直接用户分配
        boolean userAssign = jodd.util.StringUtil.isNotEmpty(addModel.getUserId());
        Set<String> blockIds = Sets.newHashSet();
        if (userAssign){
            blockIds = userBlockResourceMapper.getBlockIds(addModel.getUserId());
        }
        //查找出组id并赋值
        if (userAssign){
            Group group = groupMapper.findByUserId(addModel.getUserId());
            addModel.setGroupId(group.getId());
        }

        Map<String,Object> params = new HashMap<>();
        params.put("groupId", addModel.getGroupId());
        params.put("resourceItemId", selfItem.getId());

        //判断是否按查询条件选中
        if (addModel.isAll()){

            if ("sys_vehicle".equalsIgnoreCase(selfItem.getObjectTableName())){


                Condition[] conditions = addModel.getConditions();

                Map<String,Object> paramsEx = new HashMap<>();
                paramsEx.put("groupId", addModel.getGroupId());
                paramsEx.put("resourceItemId", selfItem.getId());
                //查询当前已选
                GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(paramsEx);

                Map<String, Object> queryParam = Maps.newHashMap();
                if (userAssign){

                    String splitStr = "and   not exists";
                    String authNotSql = DataAccessKit.getUserNotAuthSql(addModel.getUserId(), "sys_vehicle", "sv");
                    if (authNotSql.contains(splitStr)) {
                        authNotSql = authNotSql.replaceAll(splitStr, "or exists");
                    }

                    String currentUserAuthSql = DataAccessKit.getAuthSQL("sys_vehicle", "sv");
                    if (jodd.util.StringUtil.isNotEmpty(currentUserAuthSql)) {
                        authNotSql = String.format("(%s) and (%s)", authNotSql, currentUserAuthSql);
                    }
                    queryParam.put("authSQL", authNotSql);
                }
                else {
                    queryParam = DataAccessKit.getAuthMap("sys_vehicle", "sv");
                }
                List<String> uncheckedIds = new ArrayList<>();
                if (rule != null){
                    uncheckedIds.addAll(StringUtilExs.spiltString(rule.getVal(), ","));
                }
                uncheckedIds.add("-1");
                queryParam.put("uncheckedIds", uncheckedIds);
                for (Condition con: conditions){
                    queryParam.put(con.getName(), con.getValue());
                }
                final String fileMd5 = getConditionVal(addModel.getConditions(), "fileMd5");
                if (!StringUtil.isEmpty(fileMd5)){
                    final String key = "import.search." + fileMd5;
                    Set<String> items = redisKit.smembers(key);
                    if (items != null) {
                        queryParam.put("importSearchValues", items);
                    }
                }

                final Cursor<Vehicle> entityCursor = vehicleMapper.findPagerModel(queryParam);
                List<String> addVals = new ArrayList<>();
                Iterator<Vehicle> iterator = entityCursor.iterator();
                while (iterator.hasNext()){
                    addVals.add(iterator.next().getId());
                }
                addModel.setVal(StringUtils.join(addVals, ","));
            }
            else {
                //查找出要新增的val
                String querySql = selfItem.getQuerySql().trim().toLowerCase();
                String alias = getAlias(querySql);

                Map<String,Object> tempMap = new HashMap<>();
                if (addModel.getConditions() != null && addModel.getConditions().length >0){
                    for (Condition con: addModel.getConditions()){
                        tempMap.put(con.getName(), con.getValue());
                    }
                }
                if (tempMap.containsKey("keyword") && tempMap.get("keyword") != null && StringUtils.isNotEmpty(tempMap.get("keyword").toString())){
                    querySql = String.format("%s and %s.%s like '%s'", querySql, alias, selfItem.getIdentifyColumnName(), "%"+tempMap.get("keyword").toString()+"%");

                }

                Map<String, Object> searchParams = new HashMap<>();
                searchParams.put("querySql", querySql);
                //从数据库里查找准备增加的id
                List<Map<String, Object>> results = coreResourceItemMapper.queryResources(searchParams);
                List<String> addVals = new ArrayList<>();
                for (Map<String, Object> entry: results){
                    String id = entry.get("id").toString();
                    addVals.add(id);
                }
                addModel.setVal(StringUtils.join(addVals, ","));
            }


        }
        //响应model
        GroupListAddModelResp rsp = new GroupListAddModelResp();

        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        //如果不存在，即先新增
        if (rule == null){

            if (addModel.valSize() > 1000){
                throw new BusinessException("列表模式最多增加1000条数据");
            }

            GroupResourceRule groupResourceRule = new GroupResourceRule();
            //与
            groupResourceRule.setPreLogicOp(0);
            //资源项id
            groupResourceRule.setResourceItemId(selfItem.getId());
            //操作符
            groupResourceRule.setOp(0);
            //设置值
            groupResourceRule.setVal(addModel.getVal());
            //创建人
            groupResourceRule.setCreateBy(ServletUtil.getCurrentUser());
            //创建时间
            groupResourceRule.setCreateTime(DateUtil.getNow());
            //组
            groupResourceRule.setGroupId(addModel.getGroupId());
            //排序
            groupResourceRule.setOrderNum(0);
            groupResourceRule.setId(UtilHelper.getUUID());

            //保存
            groupResourceRuleMapper.insert(groupResourceRule);

            List<String> valList = StringUtilExs.spiltString(addModel.getVal(), ",");
            for (String v: valList){
                rsp.addAppendId(v);
            }

        }
        else {

            //追加到val中
            //查询有效的val
            //从sql中获取column配置
            String querySql = selfItem.getQuerySql().trim().toLowerCase();
            String alias = getAlias(querySql);
            if (rule != null){
                //得到所选的值
                String tempVal = rule.getVal() == null? "":rule.getVal();
                String[] vals = tempVal.split(",");
                String[] valTmps = new String[vals.length];
                for (int i=0; i< vals.length; i++){
                    valTmps[i] = "'" + vals[i] + "'";
                }
                querySql = String.format("%s and %s.id in(%s)", querySql ,alias, StringUtils.join(valTmps, ","));

            }
            //查询出已选中
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("querySql", querySql);
            List<Map<String, Object>> results = coreResourceItemMapper.queryResources(searchParams);
            //将已选的加入addVal中
            List<String> newVals = new ArrayList<>();
            if (results.size() > 0 ){
                for (Map<String, Object> en: results){
                    newVals.add(en.get("id").toString());
                }
            }
            String[] addVals = addModel.getVal().split(",");
            for (int i = 0; i < addVals.length ; i++) {
                //如果是手工分配，要判断一下黑名单是否存在车辆
                if(userAssign && blockIds.contains(addVals[i])){
                    rsp.addBlockId(addVals[i]);
                    continue;
                }
                if (StringUtils.isNotEmpty(addVals[i])){
                    if (!newVals.contains(addVals[i])){
                        newVals.add(addVals[i]);
                        rsp.addAppendId(addVals[i]);
                    }
                }

            }
            if (newVals.size() > 1000){
                throw new BusinessException("列表模式最多增加1000条数据");
            }

            //设置值即可以
            rule.setVal(StringUtils.join(newVals, ","));
            //编辑人
            rule.setUpdateBy(ServletUtil.getCurrentUser());
            //编辑时间
            rule.setUpdateTime(DateUtil.getNow());
            //保存
            groupResourceRuleMapper.update(rule);

        }

        return rsp;

    }



    /**
     * 列表模式下的移除指定ids
     *
     * @param groupId        权限组ID
     * @param resourceTypeId 资源类型ID
     * @param id             对象ID列表，用逗号分隔
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int listModeDelete(String groupId, String resourceTypeId, String id) {

        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);
        if (selfItem == null){
            throw new BusinessException("该资源没有自身属性，请先配置");
        }

        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceItemId", selfItem.getId());

        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        if (rule == null){
            throw new BusinessException("该资源当前并无选中资源，请重新确认接口调用是否正确");
        }
        String[] valArr = rule.getVal().split(",");
        String[] removeVals = id.split(",");
        //将值移除
        List<String> afterVals = new ArrayList<>();
        for (String v: valArr){
            boolean isFound = false;
            for (String tv: removeVals){
                if (tv.equals(v)){
                   isFound = true;
                   break;
                }
            }
            if (!isFound){
                afterVals.add(v);
            }
        }
        String newVal = StringUtils.join(afterVals, ",");
        rule.setVal(newVal);
        rule.setUpdateBy(ServletUtil.getCurrentUser());
        rule.setUpdateTime(DateUtil.getNow());
        return groupResourceRuleMapper.update(rule);

    }

    /**
     * 列表模式下的移除，根据查询条件
     *
     * @param groupId
     * @param resourceTypeId
     * @param pagerInfo
     * @return
     */
    @Override
    public int listModeDeleteByPager(String groupId, String resourceTypeId, PagerInfo pagerInfo) {

        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);
        if (selfItem == null){
            throw new BusinessException("该资源没有自身属性，请先配置");
        }
        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceItemId", selfItem.getId());

        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        if (rule == null){
            throw new BusinessException("该资源当前并无选中资源，请重新确认接口调用是否正确");
        }

        String querySql = selfItem.getQuerySql().trim().toLowerCase();
        String alias = getAlias(querySql);
        List<String> checkedIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(rule.getVal())){
            //得到所选的值
            String[] vals = rule.getVal().split(",");
            String[] valTmps = new String[vals.length];
            for (int i=0; i< vals.length; i++){
                valTmps[i] = "'" + vals[i] + "'";
                checkedIds.add(vals[i]);
            }
            //拼接sql
            querySql = String.format("%s and %s.id in(%s)", querySql ,alias, StringUtils.join(valTmps, ","));
        }

        //存放要移除id
        List<String> removeVals = new ArrayList<>();

        if ("sys_vehicle".equalsIgnoreCase(selfItem.getObjectTableName())){

            Map<String, Object> queryParam = new HashMap<>();
            //增加一个参数，避免为空
            checkedIds.add("-1");
            queryParam.put("checkedIds", checkedIds);
            queryParam.putAll(ServletUtil.pageInfoToMap(pagerInfo));

            List<VehicleForGroupModel>  vehicleForGroupModels = groupResourceRuleMapper.groupVehicleChecked(queryParam);

            for (VehicleForGroupModel vfg :vehicleForGroupModels){
                String id = vfg.getId();
                removeVals.add(id);
            }
        }
        else {
            Map<String,Object> tempMap = ServletUtil.pageInfoToMap(pagerInfo);
            if (tempMap.containsKey("keyword") && tempMap.get("keyword") != null && StringUtils.isNotEmpty(tempMap.get("keyword").toString())){
                querySql = String.format("%s and %s.%s like '%s'", querySql, alias, selfItem.getIdentifyColumnName(), "%"+tempMap.get("keyword").toString()+"%");
            }
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("querySql", querySql);
            //从数据库里查找准备移除的id
            List<Map<String, Object>> results = coreResourceItemMapper.queryResources(searchParams);

            for (Map<String, Object> entry: results){
                String id = entry.get("id").toString();
                removeVals.add(id);
            }

        }

        //将值移除
        String[] valArr = rule.getVal().split(",");
        List<String> afterVals = new ArrayList<>();
        for (String v: valArr){
            boolean isFound = false;
            for (String tv: removeVals){
                if (tv.equals(v)){
                    isFound = true;
                    break;
                }
            }
            if (!isFound){
                afterVals.add(v);
            }
        }
        String newVal = StringUtils.join(afterVals, ",");
        rule.setVal(newVal);
        rule.setUpdateBy(ServletUtil.getCurrentUser());
        rule.setUpdateTime(DateUtil.getNow());
        return groupResourceRuleMapper.update(rule);
    }

    /**
     * 资源属性的资源查询
     *
     * @param groupId
     * @param resourceTypeId
     * @param pagerInfo
     * @return
     */
    @Override
    public ResultMsg resources(String groupId, String resourceTypeId, PagerInfo pagerInfo) {

        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.putAll(ServletUtil.pageInfoToMap(pagerInfo));


        //从sql中获取column配置
        String querySql = selfItem.getQuerySql().trim().toLowerCase();
        //拼接查询sql
        Map<String,Object> ruleParams = new HashMap<>();
        ruleParams.put("groupId", groupId);
        ruleParams.put("resourceTypeId", resourceTypeId);
        String alias = getAlias(querySql);
        String authSql = DataAccessKit.getGroupAuthSql(groupId, resourceTypeId, alias);
        if (!StringUtil.isEmpty(authSql)){
            querySql = querySql + (" and " + authSql);
        }

        //设置查找条件

        if (searchParams.containsKey("keyword") && searchParams.get("keyword") != null && StringUtils.isNotEmpty(searchParams.get("keyword").toString())){
            querySql = String.format("%s and %s.%s like '%s'", querySql, alias, selfItem.getIdentifyColumnName(), "%"+searchParams.get("keyword").toString()+"%");
        }

        return findResultListBySql(querySql, selfItem, pagerInfo);
    }

    /**
     * 查找规则，通过用户id和表名
     *
     * @param userId
     * @param tableName
     * @return
     */
    @Override
    public List<GroupRuleInfo> findRuleByUserIdAndTable(String userId, String tableName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("userId", userId);
        return groupResourceRuleMapper.findRuleByUserIdAndTable(params);
    }

    /**
     * 查找规则，通过组和属性类型id
     *
     * @param groupId
     * @param resourceId
     * @return
     */
    @Override
    public List<GroupRuleInfo> findRuleByGroupIdAndResourceId(String groupId, String resourceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId",  groupId);
        params.put("resourceId", resourceId);
        return groupResourceRuleMapper.findRuleByGroupIdAndResourceId(params);
    }

    /**
     * 获取数据权限组已选车辆
     *
     * @param groupId
     * @param pagerInfo
     * @return
     */
    @Override
    public PagerResult groupCheckedVehicles(String groupId, String resourceTypeId, PagerInfo pagerInfo) {
        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);
        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceItemId", selfItem.getId());

        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        Map<String, Object> queryParam = new HashMap<>();
        List<String> checkedIds = new ArrayList<>();
        if (rule != null && StringUtils.isNotEmpty(rule.getVal())){
            String[] vals = rule.getVal().trim().split(",");

            for (String v: vals){
                checkedIds.add(v);
            }
        }
        //增加一个参数，避免为空
        checkedIds.add("-1");
        queryParam.put("checkedIds", checkedIds);

        queryParam.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        PagerResult pr = findPagerModel("groupVehicleChecked", queryParam, pagerInfo.getStart(), pagerInfo.getLimit());
        return pr;
    }

    /**
     * 获取数据权限组未选车辆
     *
     * @param groupId
     * @param pagerInfo
     * @return
     */
    @Override
    public Object groupUnCheckedVehicles(String groupId, String resourceTypeId, PagerInfo pagerInfo) {
        //获取自身的属性项
        CoreResourceItem selfItem =  coreResourceItemMapper.getResourceSelfItem(resourceTypeId);
        Map<String,Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("resourceItemId", selfItem.getId());

        GroupResourceRule rule = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);
        Map<String, Object> queryParam = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        List<String> uncheckedIds = new ArrayList<>();
        if (rule != null && StringUtils.isNotEmpty(rule.getVal())){
            String[] vals = rule.getVal().trim().split(",");

            for (String v: vals){
                uncheckedIds.add(v);
            }
        }
        uncheckedIds.add("-1");
        queryParam.put("uncheckedIds", uncheckedIds);
        queryParam.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        return vehicleService.vehicleList(pagerInfo, queryParam);
    }


    /**
     * 获取alias
     * @param sql
     * @return
     */
    private String getAlias(String sql){

        String querySql = sql.toLowerCase().trim();

        int fromIndex = querySql.indexOf("from");
        String[] tmp = querySql.substring(fromIndex+5).trim().split(" ");
        if (tmp.length >= 2){
            return tmp[1];
        }
        return null;
    }



    /**
     * 获取分页器
     * @param start
     * @param limit
     * @param total
     * @return
     */
    private Pagination getpagination(int start, int limit, long total){


        Long pageTotal = total / limit;
        if (total % limit != 0) {
            pageTotal = pageTotal + 1;
        }
        int currentPage = 0;
        if (limit>0){
            currentPage = (start / limit)+1;

        }
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setLimit(limit);
        pagination.setPage(currentPage);
        pagination.setPageSize(limit);
        pagination.setPageTotal(pageTotal);
        return pagination;
    }

    /**
     * 获取表头
     * @param querySql
     * @param coreResourceItem
     * @return
     */
    private List<Column> getColumns(String querySql, CoreResourceItem coreResourceItem){

        querySql = querySql.toLowerCase().trim();
        int endIndex = querySql.indexOf("from");
        String selectVars = querySql.substring(0, endIndex - 1).replaceAll("select", "").trim();
        String[] vars = selectVars.split(",");

        //表头定义
        boolean hasName = false;
        List<Column> columnList = new ArrayList<>();
        for (String var : vars) {
            String[] str = var.split("as");
            String key = str[0].trim();
            String alias = str[1].trim();

            String[] tmp = key.split("\\.");

            Column column = new Column();
            column.setProp(alias);
            column.setLabel(alias);
            if (tmp[1].equals("id")) {
                column.setIdColumn(true);
            }
            if (tmp[1].equals(coreResourceItem.getIdentifyColumnName())) {
                if (!hasName){
                    column.setLabelColumn(true);
                    hasName = true;
                }

            }
            columnList.add(column);
        }
        return columnList;
    }


}
