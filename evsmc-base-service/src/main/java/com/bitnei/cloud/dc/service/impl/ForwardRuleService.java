package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.dao.ForwardRuleMapper;
import com.bitnei.cloud.dc.domain.ForwardRuleItem;
import com.bitnei.cloud.dc.model.ForwardRuleItemModel;
import com.bitnei.cloud.dc.model.ForwardRuleVehicleModel;
import com.bitnei.cloud.dc.service.IPlatformRuleLkService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.ForwardRule;
import com.bitnei.cloud.dc.model.ForwardRuleModel;
import com.bitnei.cloud.dc.service.IForwardRuleService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.CoreResourceItem;
import com.bitnei.cloud.sys.domain.GroupRuleInfo;
import com.bitnei.cloud.sys.domain.HashValue;
import com.bitnei.cloud.sys.service.ICoreResourceItemService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import jodd.util.StringUtil;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRuleService实现<br>
* 描述： ForwardRuleService实现<br>
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
* <td>2019-02-20 10:32:15</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.ForwardRuleMapper" )
public class ForwardRuleService extends BaseService implements IForwardRuleService {

    @Autowired
    private ICoreResourceItemService coreResourceItemService;
    @Resource
    private ForwardRuleMapper forwardRuleMapper;
    @Resource
    private VehicleMapper vehicleMapper;
    @Autowired
    private IPlatformRuleLkService platformRuleLkService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardRule> entries = findBySqlId("pagerModel", params);
            List<ForwardRuleModel> models = new ArrayList();
            for(ForwardRule entry: entries){
                ForwardRule obj = (ForwardRule)entry;
                models.add(ForwardRuleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardRuleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardRule obj = (ForwardRule)entry;
                models.add(ForwardRuleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public ForwardRuleModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("id", id);
        ForwardRule entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        List<ForwardRuleItemModel> itemList = findRuleItemByRuleId(entry.getId());
        ForwardRuleModel model = ForwardRuleModel.fromEntry(entry);
        model.setRuleItemList(itemList);
        return model;
    }


    @Override
    public ForwardRuleModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("name", name);
        ForwardRule entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        List<ForwardRuleItemModel> itemList = findRuleItemByRuleId(entry.getId());
        ForwardRuleModel model = ForwardRuleModel.fromEntry(entry);
        model.setRuleItemList(itemList);
        return model;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ForwardRuleModel model) {

        ForwardRule obj = new ForwardRule();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        //不是默认规则
        obj.setDefaultRule(0);
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ForwardRuleModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");

        ForwardRule obj = new ForwardRule();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> map = new HashMap<>();
            map.put("forwardRuleId", id);
            Integer frCount = platformRuleLkService.unique("findByForwardRuleIdCount", map);
            if (frCount > 0) {
                throw new BusinessException("转发规则存在关联转发平台，不允许删除", 300);
            }
            params.put("id",id);
            int r = super.deleteByMap(params);
            //删除规则明细
            params.clear();
            params.put("ruleId", id);
            super.delete("delRuleItem", params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardRule>(this, "pagerModel", params, "dc/res/forwardRule/export.xls", "转发规则") {
            @Override
            public Object process(ForwardRule entity){
                return super.process(entity);
            }
        }.work();

        return;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRuleItems(ForwardRuleModel demo) {
//        增加规则明细
        if (demo.getRuleType().equals(Constants.ForwardRuleType.getValue("列表选择"))) {
            String addVehIds = demo.getAddVehIds();
            if(StringUtils.isEmpty(addVehIds)){
                throw new BusinessException("请选择车辆");
            }else{
                insertSelectListItem(demo.getId(), addVehIds);
            }
        }else {
            List<ForwardRuleItemModel> itemList = demo.getRuleItemList();
            if(itemList != null && itemList.size() > 0) {
                if(itemList.size() > 2){
                    throw new BusinessException("规则明细最多两条");
                }else {
                    //属性公式: 删除规则明细
                    Map<String, Object> delParams = new HashMap<String, Object>();
                    delParams.put("ruleId", demo.getId());
                    super.delete("delRuleItem", delParams);
                    int count = 0;
                    for (ForwardRuleItemModel ruleItem : itemList) {
                        //获取当权限的map
                        Map<String,Object> params = new HashMap<String, Object>();
                        params.put("forwardRuleId",demo.getId());
                        params.put("resourceItemId", ruleItem.getResourceItemId());
                        params.put("op",ruleItem.getOp());
                        params.put("preLogicOp",ruleItem.getPreLogicOp());
                        params.put("val",ruleItem.getVal());
                        params.put("createTime", DateUtil.getNow());
                        params.put("createBy", ServletUtil.getCurrentUser());
                        count += super.insert("insertForwardRuleItem", params);
                    }
                    if(count > 0){
                        //更新转发规则配置描述
                        updateForwardRule(demo.getId());
                    }
                }
            }else{
                throw new BusinessException("规则明细必填");
            }
        }
    }

    /** 更新转发规则配置描述 **/
    public void updateForwardRule(String ruleId){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("id",ruleId);
        ForwardRule entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("转发规则已不存在");
        }
        String ruleDescription = getForwardRuleResourceDesc(ruleId);
        entry.setRuleDescription(ruleDescription);
        entry.setUpdateTime(DateUtil.getNow());
        entry.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(entry));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("操作失败");
        }
    }

    /** 列表选择 类型 添加明细 **/
    @Override
    public void insertSelectListItem(String ruleId, String addVehIds) {
        //获取规则明细
        Map<String,Object> searchParams = new HashMap<>();
        searchParams.put("ruleId",ruleId);
        List<ForwardRuleItem> entries = findBySqlId("findRuleItems", searchParams);
        Map<String,Object> params = new HashMap<>();

        if(addVehIds.startsWith(",")){
            addVehIds = addVehIds.substring(1,addVehIds.length());
        }
        int count = 0;
        if(entries != null && entries.size() > 0) {
            ForwardRuleItem item = entries.get(0);
            String oldVal = item.getVal();
            oldVal = StringUtils.isNotBlank(oldVal) ? oldVal : "";
            String[] valArr = addVehIds.split(",");
            for (String val : valArr) {
                if (!StringUtils.isEmpty(val) && !oldVal.contains(val)) {
                    oldVal += "," + val;
                }
            }
            if(oldVal.startsWith(",")){
                oldVal = oldVal.substring(1,oldVal.length());
            }
            if (oldVal.split(",").length > 1000){
                throw new BusinessException("最多只能添加1000辆车");
            }
            params.put("val", oldVal);
            params.put("forwardRuleId",item.getForwardRuleId());
            params.put("id", item.getId());
            params.put("op",item.getOp());
            params.put("preLogicOp",item.getPreLogicOp());
            if(StringUtils.isBlank(item.getResourceItemId())){
                CoreResourceItem coreResourceItem = coreResourceItemService.getVehicleSelfItem();
                params.put("resourceItemId", coreResourceItem == null ? "" : coreResourceItem.getId());
            }else {
                params.put("resourceItemId", item.getResourceItemId());
            }
            params.put("updateTime", DateUtil.getNow());
            params.put("updateBy", ServletUtil.getCurrentUser());
            count = super.update("updateForwardRuleItem", params);
        }else{
            if (addVehIds.split(",").length > 1000){
                throw new BusinessException("最多只能添加1000辆车");
            }
            CoreResourceItem coreResourceItem = coreResourceItemService.getVehicleSelfItem();
            params.put("resourceItemId", coreResourceItem == null ? "" : coreResourceItem.getId());
            params.put("forwardRuleId",ruleId);
            params.put("val",addVehIds);
            params.put("createTime", DateUtil.getNow());
            params.put("createBy", ServletUtil.getCurrentUser());
            count = super.insert("insertForwardRuleItem", params);
        }
        if(count > 0){
            //更新转发规则配置描述
            updateForwardRule(ruleId);
        }
    }

    /** 根据规则id查询规则明细 **/
    public List<ForwardRuleItemModel> findRuleItemByRuleId(String ruleId) {

        //获取当权限的map
        Map<String,Object> params = new HashMap<>();
        params.put("ruleId", ruleId);
        List<ForwardRuleItem> entries = findBySqlId("findRuleItemByRuleId", params);
        List<ForwardRuleItemModel> models = new ArrayList();
        for(ForwardRuleItem entry: entries){
            ForwardRuleItem obj = (ForwardRuleItem)entry;
            List<HashValue> valList = coreResourceItemService.findHashValuesByResItemIdAndIds(obj.getResourceItemId(), obj.getVal());
            String valName = "";
            String[] ids = obj.getVal().split(",");
            for (String id : ids) {
                for (HashValue hv : valList) {
                    if(id.equals(hv.getKey())) {
                        if (valName.length() > 0) {
                            valName += "," + hv.getValue();
                        } else {
                            valName = hv.getValue();
                        }
                    }
                }
            }
            ForwardRuleItemModel model = ForwardRuleItemModel.fromEntry(obj);
            model.setValName(valName);
            models.add(model);
        }
        return models;
    }

    /**
     * 获取已选车辆列表
     * @param pagerInfo
     * @return
     */
    @Override
    public Object selectedVehs(PagerInfo pagerInfo, String ruleId) {

        Map<String,Object> searchParams = new HashMap<>();
        searchParams.put("ruleId",ruleId);
        List<ForwardRuleItem> entries = findBySqlId("findRuleItems", searchParams);
        searchParams.clear();
        searchParams.put("id", ruleId);
        ForwardRule rule = unique("findById", searchParams);
        if(entries != null && entries.size() > 0) {
            //获取当权限的map
            Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
            params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
            if(rule != null && rule.getRuleType().equals(Constants.ForwardRuleType.getValue("列表选择"))){
                ForwardRuleItem item = entries.get(0);
                String oldVal = item.getVal();
                String[] valArr = oldVal.split(",");
                params.put("vehicleIds", valArr);
            }else {
                //属性公式 获取车辆Id
//                List<String> vehIds = getVehicleIdByRuleIds(ruleId.split(","));
//                String[] array = new String[vehIds.size()];
//                valArr = vehIds.toArray(array);
                String authSql = DataAccessKit.getForwardRuleAuthSql(ruleId.split(","), "sv");
                params.put(Constants.AUTH_SQL, authSql);
            }
            // 非分页查询
            if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
                List<ForwardRuleVehicleModel> vehs = findBySqlId("findRuleVehicles", params);
                return vehs;
            }
            else { //分页查询
                PagerResult pr = findPagerModel("findRuleVehicles", params, pagerInfo.getStart(), pagerInfo.getLimit());
                List<ForwardRuleVehicleModel> models = Lists.newArrayList();
                for (Object entry : pr.getData()) {
                    ForwardRuleVehicleModel obj = (ForwardRuleVehicleModel) entry;
                    models.add(obj);
                }
                pr.setData(Collections.singletonList(models));
                return pr;
            }
        }
        PagerResult pr = new PagerResult();
        pr.setData(Collections.singletonList(new ArrayList<>()));
        pr.setTotal((long) 0);
        pr.setPagerInfo(pagerInfo);
        return pr;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteVehs(String ruleId, String delVehIds) {
        Map<String,Object> searchParams = new HashMap<>();
        searchParams.put("ruleId",ruleId);
        List<ForwardRuleItem> entries = findBySqlId("findRuleItems", searchParams);
        if(entries != null && entries.size() > 0) {
            ForwardRuleItem item = entries.get(0);
            String[] valArr = item.getVal().split(",");
            String[] removeVals = delVehIds.split(",");
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
            Map<String,Object> params = new HashMap<>();
            params.put("val", newVal);
            params.put("forwardRuleId",item.getForwardRuleId());
            params.put("id", item.getId());
            params.put("op",item.getOp());
            params.put("preLogicOp",item.getPreLogicOp());
            params.put("updateTime", DateUtil.getNow());
            params.put("updateBy", ServletUtil.getCurrentUser());
            if(StringUtils.isBlank(item.getResourceItemId())){
                CoreResourceItem coreResourceItem = coreResourceItemService.getVehicleSelfItem();
                params.put("resourceItemId", coreResourceItem == null ? "" : coreResourceItem.getId());params.put("forwardRuleId",ruleId);
            }else {
                params.put("resourceItemId", item.getResourceItemId());
            }
            int count = super.update("updateForwardRuleItem", params);
            if(count > 0) {
                //更新转发规则配置描述
                updateForwardRule(ruleId);
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAllVehs(PagerInfo pagerInfo, String ruleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //获取需要添加的全部车辆Id
//        List<Vehicle> vehs = vehicleService.findBySqlId("findPagerModel", params);
        List<ForwardRuleVehicleModel> vehs = findBySqlId("findAllVehicles", params);
        StringBuilder vehIds = new StringBuilder();
        for (ForwardRuleVehicleModel entry : vehs) {
            vehIds.append(entry.getId()).append(",");
        }
        if (0 < vehIds.length()) {
            //列表选择 类型 添加明细
            insertSelectListItem(ruleId, vehIds.substring(0, vehIds.length() - 1));
        }
    }

    /**
     * 查找规则明细
     *
     * @param ruleIds
     * @return
     */
    @Override
    public List<GroupRuleInfo> findRuleItemsByRuleId(String[] ruleIds) {

        Map<String, Object> params = new HashMap<>();
        params.put("ruleIds", ruleIds);

        List<GroupRuleInfo> ruleInfos = forwardRuleMapper.findRuleItemsByRuleId(params);

        return ruleInfos;
    }

    /**
     * 通过规则ID列表获取权限车辆Id列表
     * @param ruleIds
     * @return
     */
    @Override
    public List<String> getVehicleIdByRuleIds(String[] ruleIds){
        String authSql =  DataAccessKit.getForwardRuleAuthSql(ruleIds, "v");
        List<String> ids = vehicleMapper.findIdsAuthSql(authSql);
        return ids;
    }


    @Override
    public String getForwardRuleResourceDesc(String ruleId) {

        Map<String,Object> params = new HashMap<>();
        params.put("ruleId", ruleId);
        List<ForwardRuleItem> items = forwardRuleMapper.findRuleItemByRuleId(params);
        StringBuffer sb = new StringBuffer();
        Map<String,Object> ruleParams = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        ruleParams.put("id",ruleId);
        ForwardRule rule = unique("findById", ruleParams);
        if (rule != null && rule.getRuleType().equals(Constants.ForwardRuleType.getValue("列表选择"))) {
            if(items != null && items.size() > 0){
                ForwardRuleItem item = items.get(0);
                int vehCount = 0;
                if (!StringUtils.isBlank(item.getVal())){
                    String[] arr = item.getVal().split(",");
                    vehCount = arr.length;
                }
                sb.append("列表选择 ");
                sb.append(vehCount);
                sb.append(" 辆车");
                if(!StringUtils.isEmpty(rule.getNote())){
                    sb.append(", 备注：");
                    sb.append(rule.getNote());
                }
            }else{
                sb.append("列表选择 0 辆车");
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                ForwardRuleItem ri = items.get(i);
                StringBuffer tmpStr = new StringBuffer();
                if (i != 0) {
                    if (ri.getPreLogicOp() != null) {
                        if (ri.getPreLogicOp() == 0) {
                            tmpStr.append(" 与 ");
                        } else {
                            tmpStr.append(" 或 ");
                        }

                    }
                }
                tmpStr.append(ri.get("resourceItemName").toString());
                if (ri.getOp() == null || ri.getOp() == 0) {
                    tmpStr.append(" == ");
                } else {
                    tmpStr.append(" != ");
                }
                tmpStr.append("[");
                String nameDesc = coreResourceItemService.findNameByResItemIdAndIds(ri.getResourceItemId(), ri.getVal());
                if (StringUtil.isNotEmpty(nameDesc)) {
                    tmpStr.append(nameDesc);
                }
                tmpStr.append("]");
                tmpStr.append("  ");
                sb.append(tmpStr.toString());
            }
        }

        return sb.toString();
    }

    @Override
    public Object vehsList(PagerInfo pagerInfo, String ruleId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //获取规则明细
        Map<String,Object> searchParams = new HashMap<>();
        searchParams.put("ruleId", ruleId);
        List<ForwardRuleItem> entries = findBySqlId("findRuleItems", searchParams);
        String val = "";
        if(entries != null && entries.size() > 0) {
            ForwardRuleItem item = entries.get(0);
            val = item.getVal();
        }
        String[] valArr = val.split(",");
        if(valArr.length > 0) {
            //过滤掉已添加的车辆
            params.put("excludeIds", valArr);
        }

        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<ForwardRuleVehicleModel> vehs = findBySqlId("findAllVehicles", params);
            return vehs;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findAllVehicles", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardRuleVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardRuleVehicleModel obj = (ForwardRuleVehicleModel) entry;
                models.add(obj);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /** 多条件查询转发规则及规则配置描述 **/
    @Override
    public Object listRulesAndDesc(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardRule> entries = findBySqlId("pagerModel", params);
            List<ForwardRuleModel> models = new ArrayList();
            for(ForwardRule entry: entries){
                ForwardRule obj = (ForwardRule)entry;
                models.add(ForwardRuleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardRuleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardRule obj = (ForwardRule)entry;
                models.add(ForwardRuleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 车辆删除后，同步更新规则类型为“列表选择”的规则明细val值
     * @param vehIds
     */
    @Override
    public void updateForwardRuleRuleItemVal(String vehIds){
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("ruleType", 2);
        List<ForwardRule> entries = findBySqlId("pagerModel", params);
        if(entries != null && entries.size() > 0){
            for (ForwardRule forwardRule : entries){
                Map<String,Object> itemParams = new HashMap<>();
                itemParams.put("ruleId", forwardRule.getId());
                List<ForwardRuleItem> items = forwardRuleMapper.findRuleItemByRuleId(itemParams);
                if (items != null && items.size() > 0) {
                    ForwardRuleItem item = items.get(0);
                    String oldVal = item.getVal();
                    String[] vehIdArr = oldVal.split(",");
                    StringBuffer newVal = new StringBuffer();
                    boolean flag = false;
                    for (String vehId : vehIdArr){
                        if (vehIds.contains(vehId)){
                            flag = true;
                        }else {
                            newVal.append(vehId).append(",");
                        }
                    }
                    if(flag){
                        if (newVal.length() > 0) {
                            String newValStr = newVal.substring(0, newVal.length() - 1);
                            Map<String,Object> addParams = new HashMap<>();
                            addParams.put("val", newValStr);
                            addParams.put("forwardRuleId",item.getForwardRuleId());
                            addParams.put("id", item.getId());
                            addParams.put("op",item.getOp());
                            addParams.put("preLogicOp",item.getPreLogicOp());
                            addParams.put("updateTime", DateUtil.getNow());
                            addParams.put("updateBy", ServletUtil.getCurrentUser());
                            if(StringUtils.isBlank(item.getResourceItemId())){
                                CoreResourceItem coreResourceItem = coreResourceItemService.getVehicleSelfItem();
                                addParams.put("resourceItemId", coreResourceItem == null ? "" : coreResourceItem.getId());
                            }else {
                                addParams.put("resourceItemId", item.getResourceItemId());
                            }
                            int count = super.update("updateForwardRuleItem", addParams);
                            if(count > 0) {
                                //更新转发规则配置描述
                                StringBuffer sb = new StringBuffer();
                                int vehCount = 0;
                                if (newValStr.length() > 0){
                                    String[] arr = newValStr.split(",");
                                    vehCount = arr.length;
                                }
                                sb.append("列表选择 ");
                                sb.append(vehCount);
                                sb.append(" 辆车");
                                if(!StringUtils.isEmpty(forwardRule.getNote())){
                                    sb.append(", 备注：");
                                    sb.append(forwardRule.getNote());
                                }
                                forwardRule.setRuleDescription(sb.toString());
                                forwardRule.setUpdateTime(DateUtil.getNow());
                                forwardRule.setUpdateBy(ServletUtil.getCurrentUser());
                                Map<String,Object> ruleParams = new HashMap<>();
                                ruleParams.putAll(MapperUtil.Object2Map(forwardRule));
                                int res = super.updateByMap(ruleParams);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ForwardRule findPlatformDefaultRule(String platformId) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("platformId", platformId);
        return forwardRuleMapper.findPlatformDefaultRule(params);
    }

    @Override
    public ForwardRuleItem findPlatformDefaultRuleItem(String platformId) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
        params.put("platformId", platformId);
        return forwardRuleMapper.findPlatformDefaultRuleItem(params);
    }

}
