package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelExportExHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.dao.ForwardPlatformVehicleBlackListMapper;
import com.bitnei.cloud.dc.dao.ForwardVehicleMapper;
import com.bitnei.cloud.dc.domain.*;
import com.bitnei.cloud.dc.model.*;
import com.bitnei.cloud.dc.service.IForwardPlatformService;
import com.bitnei.cloud.dc.service.IForwardRuleService;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.dc.service.IPlatformRuleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.base.Joiner;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
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
* 功能： ForwardVehicleService实现<br>
* 描述： ForwardVehicleService实现<br>
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
* <td>2019-02-21 14:29:14</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.ForwardVehicleMapper" )
public class ForwardVehicleService extends BaseService implements IForwardVehicleService {

    @Autowired
    private IForwardRuleService forwardRuleService;
    @Autowired
    private IForwardPlatformService forwardPlatformService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private ForwardVehicleMapper forwardVehicleMapper;
    @Autowired
    private IPlatformRuleLkService platformRuleLkService;
    @Resource
    private RedisKit webRedisKit;

    @Resource
    private ForwardPlatformVehicleBlackListMapper forwardPlatformVehicleBlackListMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardVehicle> entries = findBySqlId("pagerModel", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public ForwardVehicleModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.put("id", id);
        ForwardVehicle entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return ForwardVehicleModel.fromEntry(entry);
    }




    @Override
    public void insert(ForwardVehicleModel model) {

        ForwardVehicle obj = new ForwardVehicle();
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
    public void update(ForwardVehicleModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");

        ForwardVehicle obj = new ForwardVehicle();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");

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
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardVehicle>(this, "pagerModel", params, "dc/res/forwardVehicle/export.xls", "转发车辆") {
        }.work();

        return;


    }

    @Override
    public Object findToBeForwardedVeh(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardVehicle> entries = findBySqlId("toBeForwardedVehPagerModel", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("toBeForwardedVehPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object findForwardedVeh(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardVehicle> entries = findBySqlId("forwardedVehPagerModel", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("forwardedVehPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public ForwardVehicleModel countPlatformVehByStatus(String platformId) {
        //获取当权限的map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("platformId", platformId);
        ForwardVehicle entry = unique("countPlatformVehByStatus", params);
        return ForwardVehicleModel.fromEntry(entry);
    }

    @Override
    public void changeVehStatus(ForwardVehicleModel model) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.put("id", model.getId());
        ForwardVehicle entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }else {
            ForwardVehicle obj = new ForwardVehicle();
            int res = 0;
            if (model.getConfirmStatus() != null) {
                model.setConfirmTime(DateUtil.getNow());
                model.setConfirmBy(ServletUtil.getCurrentUser());
                BeanUtils.copyProperties(model, obj);
                params.putAll(MapperUtil.Object2Map(obj));
                res = super.update("changeConfirmStatus", params);
            } else if (model.getPushStatus() != null) {
                if(model.getPushStatus().equals(Constants.PushStatus.getValue("待推送"))){
                    model.setPushTime(null);
                    model.setErrorMessage(null);
                }else if(model.getPushStatus().equals(Constants.PushStatus.getValue("推送中"))) {
                    model.setPushTime(DateUtil.getNow());
                    model.setErrorMessage(null);
                }else {
                    model.setPushTime(entry.getPushTime());
                }
                BeanUtils.copyProperties(model, obj);
                params.putAll(MapperUtil.Object2Map(obj));
                res = super.update("changePushStatus", params);
            } else if (model.getForwardStatus() != null) {
                if(model.getForwardStatus().equals(Constants.ForwardStatus.getValue("已转发"))){
                    model.setForwardFirstTime(DateUtil.getNow());
                }else{
                    model.setForwardFirstTime(entry.getForwardFirstTime());
                }
                BeanUtils.copyProperties(model, obj);
                params.putAll(MapperUtil.Object2Map(obj));
                res = super.update("changeForwardStatus", params);
            }
            if (res == 0 ){
                throw new BusinessException("更新失败");
            }
        }
    }


    /** 关联转发规则后，增加转发车辆 **/
    @Override
    public PlatformRuleLkModel insertPlatformVeh(PlatformRuleLkModel demo){
        ForwardRuleModel rule = forwardRuleService.get(demo.getAddRuleIds());
        if (rule == null){
            throw new BusinessException("转发规则已不存在");
        }
        String platformId = demo.getPlatformId();
        //获取平台信息
        Map<String,Object> platParams = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        platParams.put("id",platformId);
        ForwardPlatform platform = forwardPlatformService.unique("findById", platParams);
        //静态数据推送平台类型, 普通平台的车辆推送状态设为空
        Integer plateFormType = platform == null?null:platform.getStaticForwardPlatform();
        Integer pushStatus = 1;
        if(plateFormType == null || plateFormType.equals(Constants.StaticForwardPlatform.getValue("无"))){
            pushStatus = null;
        }

        //获取平台已存在车辆
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.put("platformId", platformId);
//        List<ForwardVehicle> existVehs = findBySqlId("pagerModel", params);
        List<ForwardVehicle> existVehs = findBySqlId("findPlatformVehs", params);
        //待转发车辆Map
        StringBuffer unFWVeh = new StringBuffer();
        //已转发Map
        StringBuffer fWVeh = new StringBuffer();

        //手动添加的车辆
        List<ForwardVehicle> manualVehs = new ArrayList<>();

        for(ForwardVehicle vehicle : existVehs){
            if(vehicle.getForwardStatus().equals(Constants.ForwardStatus.getValue("待转发"))) {
                unFWVeh.append(vehicle.getVehicleId()).append(",");
            }else if (vehicle.getForwardStatus().equals(Constants.ForwardStatus.getValue("已转发"))) {
                fWVeh.append(vehicle.getVehicleId()).append(",");
            }
            //手动添加的车辆
            if (vehicle.getAddMode().intValue() == 2) {
                manualVehs.add(vehicle);
            }
        }
        String unFWVehIds="";
        String fWVehIds="";
        if (0 < unFWVeh.length()) {
            unFWVehIds = unFWVeh.substring(0, unFWVeh.length() - 1);
        }
        if (0 < fWVeh.length()) {
            fWVehIds = fWVeh.substring(0, fWVeh.length() - 1);
        }
        String[] vehIdArr;
        if(rule.getRuleType().equals(Constants.ForwardRuleType.getValue("列表选择"))){
            if (rule.getRuleItemList() != null && rule.getRuleItemList().size() > 0) {
                ForwardRuleItemModel itemModel = rule.getRuleItemList().get(0);
                String val = itemModel.getVal();
                vehIdArr = val.split(",");
            }else{
                vehIdArr = "".split(",");
            }
        }else{
            //属性公式 获取车辆Id
            List<String> vehIds = forwardRuleService.getVehicleIdByRuleIds(rule.getId().split(","));
            String[] array = new String[vehIds.size()];
            vehIdArr = vehIds.toArray(array);
        }
        //保存车辆
        PlatformRuleLkModel model = insertVehs(platformId, pushStatus, vehIdArr, unFWVehIds, fWVehIds, manualVehs);
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    public PlatformRuleLkModel insertVehs (String platformId, Integer pushStatus, String[] valArr
                , String unFWVehIds, String fWVehIds, List<ForwardVehicle> manualVehs) {
        PlatformRuleLkModel model = new PlatformRuleLkModel();
        int unFWRepeatVehNum = 0;
        int repeatVehNum = 0;
        StringBuffer unFWRepeatVehIds = new StringBuffer();
        StringBuffer repeatVehIds = new StringBuffer();
        StringBuffer blankVehIds = new StringBuffer();
        int blankVehNum = 0;
//        int sucVeh = 0;
        StringBuffer sucVehIds = new StringBuffer();

        //黑名单车辆
        String currBlankVehIds = findBlanVehicleForPlatform(platformId);
        List<ForwardVehicle> currRuleVehs = new ArrayList<>();

        ForwardVehicleModel vehModel = null;
        List<ForwardVehicleModel> list = new ArrayList<>();
        for(String vehId : valArr){
            if(unFWVehIds.contains(vehId)){
                unFWRepeatVehNum += 1;
                unFWRepeatVehIds.append(vehId).append(",");
            } else if(fWVehIds.contains(vehId)){
                repeatVehNum += 1;
                repeatVehIds.append(vehId).append(",");
            } else if (currBlankVehIds.contains(vehId)) {
                blankVehNum += 1;
                blankVehIds.append(vehId).append(",");
            } else {
                vehModel = new ForwardVehicleModel();
                String id = UtilHelper.getUUID();
                vehModel.setId(id);
                vehModel.setPlatformId(platformId);
                vehModel.setVehicleId(vehId);
                vehModel.setPushStatus(pushStatus);
                vehModel.setConfirmStatus(Constants.ConfirmStatus.getValue("待确认"));
                vehModel.setForwardStatus(Constants.ForwardStatus.getValue("待转发"));
                vehModel.setCreateTime(DateUtil.getNow());
                vehModel.setCreateBy(ServletUtil.getCurrentUser());
                //规则添加
                vehModel.setAddMode(1);
                list.add(vehModel);
//                insert(vehModel);
//                sucVeh += 1;
                sucVehIds.append(vehId).append(",");
            }

            //找到与手动添加的车辆,并修改成 规则添加
            for (ForwardVehicle forwardVehicle : manualVehs) {
                if (vehId.equals(forwardVehicle.getVehicleId())) {
                    currRuleVehs.add(forwardVehicle);
                    break;
                }
            }
        }

        //把添加方式更新为 规则添加。
        for (ForwardVehicle forwardVehicle : currRuleVehs) {
            forwardVehicleMapper.updateAddModeToRule(forwardVehicle.getId());
            //同时从默认规则中删除
            deleteDefaultRuleVehicle(null, forwardVehicle.getPlatformId(), forwardVehicle.getVehicleId());
        }

        if(list != null && list.size() > 0) {
            forwardVehicleMapper.insertBatchVeh(list);
        }
        model.setRuleVehTotalNum(valArr.length);
        model.setUnFWRepeatVehNum(unFWRepeatVehNum);
        if (0 < unFWRepeatVehIds.length()) {
            model.setUnFWRepeatVehIds(unFWRepeatVehIds.substring(0, unFWRepeatVehIds.length() - 1));
        }
        model.setRepeatVehNum(repeatVehNum);
        if (0 < repeatVehIds.length()) {
            model.setRepeatVehIds(repeatVehIds.substring(0, repeatVehIds.length() - 1));
        }
        model.setSucVehNum(list.size());
        if (0 < sucVehIds.length()) {
            model.setSucVehIds(sucVehIds.substring(0, sucVehIds.length() - 1));
        }
        //黑名单车辆
        model.setBlankVehNum(blankVehNum);
        if (0 < blankVehIds.length()) {
            model.setBlankVehIds(blankVehIds.substring(0, blankVehIds.length() - 1));
        }

        return model;
    }

    /**
     * 移除转发规则后，删除转发车辆
     */
    @Override
    public int delPlatformVeh(String platformId, String delRuleIds){
        int delCount = platformRuleLkService.deletePlatformRules(platformId, delRuleIds);
        if(delCount != 0){
            //获取平台转发车辆
            Map<String, Object> vehParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
            vehParams.put("platformId", platformId);
            List<String> fvIds = findBySqlId("findIds", vehParams);

            //获取平台关联的规则
            Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
            params.put("platformId", platformId);
            List<PlatformRuleLk> entries = platformRuleLkService.findBySqlId("pagerModel", params);
            StringBuffer delRule = new StringBuffer();
            StringBuffer addRule = new StringBuffer();
            if (entries != null && entries.size() > 0) {
                for (PlatformRuleLk pr : entries) {
                    if (pr.getRuleStatus() == 0) {
                        delRule.append(pr.getForwardRuleId()).append(",");
                    } else {
                        addRule.append(pr.getForwardRuleId()).append(",");
                    }
                }
            }else {
                //平台无关联规则，则清除所有车辆
                Map<String,Object> delParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                delParams.put("platformId", platformId);
                super.delete("deleteVehs", delParams);
            }
            //规则的启用状态为是
            if (addRule.length() > 0) {
                //获取规则的车辆id
                String addRuleIds = addRule.substring(0, addRule.length() - 1);
                List<String> vehList = forwardRuleService.getVehicleIdByRuleIds(addRuleIds.split(","));
                String vehListStr = Joiner.on(",").join(vehList);

                //如果平台车辆不在规则里，则删除转发车辆
                StringBuffer delIds = new StringBuffer();
                for (String fvId : fvIds) {
                    if (!vehListStr.contains(fvId)) {
                        delIds.append(fvId).append(",");
                    }
                }
                //删除车辆
                if (delIds.length() > 0) {
                    String delIdsStr = delIds.substring(0, delIds.length() - 1);
                    Map<String, Object> delParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                    delParams.put("platformId", platformId);
                    delParams.put("vehIds", delIdsStr.split(","));
                    int count = super.delete("deleteVehs", delParams);
                }
            }
        }
        return delCount;
    }

    /**
     * 查看关联转发规则后的结果详情
     * @param pagerInfo
     * @return
     */
    @Override
    public Object viewRelateRuleResult(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //与待转发车辆重复Ids
        String unFWRepeatVehIds = params.get("unFWRepeatVehIds")==null?"":params.get("unFWRepeatVehIds").toString();
        String[] unFWRepeatVehIdsArr = unFWRepeatVehIds.split(",");
        params.put("unFWRepeatVehIds", unFWRepeatVehIdsArr);

        //与已转发车辆重复Ids
        String repeatVehIds = params.get("repeatVehIds")==null?"":params.get("repeatVehIds").toString();
        String[] repeatVehIdsArr = repeatVehIds.split(",");
        params.put("repeatVehIds", repeatVehIdsArr);

        //成功车辆IDs
        String sucVehIds = params.get("sucVehIds")==null?"":params.get("sucVehIds").toString();
        String[] sucVehIdsArr = sucVehIds.split(",");
        params.put("sucVehIds", sucVehIdsArr);

        //平台Id
        String platformId = params.get("platformId")==null?"":params.get("platformId").toString();
        //关联规则Id
        String ruleId = params.get("ruleId")==null?"":params.get("ruleId").toString();

        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardVehicle> entries = findBySqlId("relateRuleResultPagerModel", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            String json = new JsonSerializer().deep(true).serialize(models);
            String key = "viewRelateRuleResult.prId."+ platformId + "-" +ruleId;
            webRedisKit.set(key, json);
//            ServletUtil.getRequest().getSession().setAttribute("viewRelateRuleResult", models);
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("relateRuleResultPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }

            List<ForwardVehicle> entries = findBySqlId("relateRuleResultPagerModel", params);
            List<ForwardVehicleModel> exportModels = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                exportModels.add(ForwardVehicleModel.fromEntry(obj));
            }
            String json = new JsonSerializer().deep(true).serialize(exportModels);
            String key = "viewRelateRuleResult.prId."+ platformId + "-" +ruleId;
            webRedisKit.set(key, json);
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public void exportToBeForwardVehs(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardVehicle>(this, "toBeForwardedVehPagerModel", params, "dc/res/forwardVehicle/toForwardedVehsExport.xls", "待转发车辆") {
        }.work();

        return;
    }

    @Override
    public void exportForwardedVehs(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardVehicle>(this, "forwardedVehPagerModel", params, "dc/res/forwardVehicle/forwardedVehsExport.xls", "已转发车辆") {
        }.work();

        return;
    }

    @Override
    public void exportRelateRuleResults(PagerInfo pagerInfo) {
        List<Object> list = new ArrayList<>();
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //平台Id
        String platformId = params.get("platformId")==null?"":params.get("platformId").toString();
        //关联规则Id
        String ruleId = params.get("ruleId")==null?"":params.get("ruleId").toString();
        String key = "viewRelateRuleResult.prId."+ platformId + "-" +ruleId;
        String json = webRedisKit.get(key, "");
        List<ForwardVehicleModel> models = new JsonParser().map("values", ForwardVehicleModel.class).parse(json, List.class);
        for (ForwardVehicleModel m: models){
            list.add(m);
        }

        new ExcelExportExHandler<ForwardVehicleModel>("dc/res/forwardVehicle/relateRuleResultsExport.xls", list, "关联转发规则结果详情") {
        }.work();

        return;
    }

    @Override
    public ForwardVehicleModel changeBatchVehStatus(ForwardVehicleModel model) {
        ForwardVehicleModel result = new ForwardVehicleModel();
        String ids = model.getIds();
        String[] idsArr = ids.split(",");
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        //获取已确认车辆
        params.put("ids", idsArr);
        params.put("confirmStatus", Constants.ConfirmStatus.getValue("已确认"));
        List<ForwardVehicle> entries = findBySqlId("pagerModel", params);
        Map<String, ForwardVehicle> vehMap = new HashMap<String, ForwardVehicle>();
        StringBuffer repeatIds = new StringBuffer();
        for(ForwardVehicle entry: entries){
            vehMap.put(entry.getId(), entry);
            repeatIds.append(entry.getVehicleId()).append(",");
        }
        result.setRepeatVehNum(entries.size());
        if (repeatIds.length() > 0) {
            result.setRepeatIds(repeatIds.substring(0, repeatIds.length() - 1));
        }

        int res = 0;
        StringBuffer sucIds = new StringBuffer();
        for (String id : idsArr){
            ForwardVehicle fv = vehMap.get(id);
            if(fv == null){
                params.clear();
                params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                params.put("id", id);
                ForwardVehicle obj = unique("findById", params);
                if (obj != null) {
                    obj.setConfirmTime(DateUtil.getNow());
                    obj.setConfirmStatus(Constants.ConfirmStatus.getValue("已确认"));
                    obj.setConfirmBy(ServletUtil.getCurrentUser());
                    params.putAll(MapperUtil.Object2Map(obj));
                    res += super.update("changeConfirmStatus", params);
                    sucIds.append(obj.getVehicleId()).append(",");
                }
            }
        }
        result.setConfirmedVeh(res);
        if(0 < sucIds.length()) {
            result.setSucIds(sucIds.substring(0, sucIds.length() - 1));
        }
        return result;
    }

    @Override
    public Object viewConfirmResults(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //重复确认Ids
        String repeatVehIds = params.get("repeatIds")==null?"":params.get("repeatIds").toString();
        String[] repeatVehIdsArr = repeatVehIds.split(",");
        params.put("repeatIds", repeatVehIdsArr);

        //成功车辆IDs
        String sucVehIds = params.get("sucIds")==null?"":params.get("sucIds").toString();
        String[] sucVehIdsArr = sucVehIds.split(",");
        params.put("sucIds", sucVehIdsArr);

        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardVehicle> entries = findBySqlId("confirmResultPagerModel", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("confirmResultPagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 设置车辆转发状态
     *
     * @param vid           车辆uuid
     * @param forwardId     转发配置id
     * @param time          转发时间
     */
    @Override
    public void changeForwardStatus(String vid, String forwardId, String time) {

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", vid);
        Vehicle vehicle =  vehicleMapper.findByUuid(params);
        if (vehicle == null){
            log.warn("车辆不存在,UUID：" + vid);
            return;
        }
        forwardVehicleMapper.changeForwardStatus(vehicle.getId(), forwardId, time);


    }

    /**
     * 下载待转发车辆列表导入查询模板
     */
    @Override
    public void getImportSearchFile() {
        EasyExcel.renderImportSearchDemoFile("待转发车辆列表导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

    /**
     * 下载已转发车辆列表导入查询模板
     */
    @Override
    public void getImportForwardedSearchFile() {
        EasyExcel.renderImportSearchDemoFile("已转发车辆列表导入查询模板.xls", "VIN", new String[]{"LSB123214124214"});
    }

    /**
     * 定时更新平台转发车辆
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForwardVehicle(){
        //获取平台信息
        Map<String,Object> platParams = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        List<ForwardPlatform> platformList = forwardPlatformService.findBySqlId("pagerModel", platParams);
        for(ForwardPlatform platform : platformList){
            //获取平台转发车辆
            Map<String, Object> vehParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
            vehParams.put("platformId", platform.getId());
            List<String> fvIds = findBySqlId("findIds", vehParams);
            String fvIdsStr = Joiner.on(",").join(fvIds);

            //获取平台关联的规则
            Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_rule", "fr");
            params.put("platformId", platform.getId());
            List<PlatformRuleLk> entries = platformRuleLkService.findBySqlId("pagerModel", params);
            StringBuffer delRule = new StringBuffer();
            StringBuffer addRule = new StringBuffer();
            if (entries != null && entries.size() > 0) {
                for (PlatformRuleLk pr : entries) {
                    if (pr.getRuleStatus() == 0) {
                        delRule.append(pr.getForwardRuleId()).append(",");
                    } else {
                        addRule.append(pr.getForwardRuleId()).append(",");
                    }
                }
            }else {
                //平台无关联规则，则清除所有车辆
                Map<String,Object> delParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                delParams.put("platformId", platform.getId());
                super.delete("deleteVehs", delParams);
            }
            //规则的启用状态为是
            if (addRule.length() > 0) {
                //获取规则的车辆id
                String addRuleIds = addRule.substring(0, addRule.length() - 1);
                List<String> vehList = forwardRuleService.getVehicleIdByRuleIds(addRuleIds.split(","));
                String vehListStr = Joiner.on(",").join(vehList);

                //如果平台车辆不在规则里，则删除转发车辆
                StringBuffer delIds = new StringBuffer();
                for (String fvId : fvIds) {
                    if (!vehListStr.contains(fvId)) {
                        delIds.append(fvId).append(",");
                    }
                }
                //删除车辆
                if (delIds.length() > 0) {
                    String delIdsStr = delIds.substring(0, delIds.length() - 1);
                    Map<String, Object> delParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                    delParams.put("platformId", platform.getId());
                    delParams.put("vehIds", delIdsStr.split(","));
                    int count = super.delete("deleteVehs", delParams);
                }

                //平台黑名单的车辆
                params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
                params.put("platformId", platform.getId());
                List<ForwardPlatformVehicleBlackList> blackLists = forwardPlatformVehicleBlackListMapper.listPage(params);

                //如果规则的车辆 不在平台里，则增加转发车辆
                List<ForwardVehicleModel> list = new ArrayList<>();
                for (String vehId : vehList) {
                    boolean unExistVhe = !fvIdsStr.contains(vehId);
                    boolean unBlackVhe = !checkBlackVeh(blackLists, vehId);
                    //新加的车辆(不在转发列表及黑名单中)
                    if (unExistVhe && unBlackVhe) {
                        Integer plateFormType = platform == null ? null : platform.getStaticForwardPlatform();
                        Integer pushStatus = 1;
                        if (plateFormType == null || plateFormType.equals(Constants.StaticForwardPlatform.getValue("无"))) {
                            pushStatus = null;
                        }
                        ForwardVehicleModel vehModel = new ForwardVehicleModel();
                        //单元测试指定id，如果是单元测试，那么就不使用uuid
                        String id = UtilHelper.getUUID();
                        vehModel.setId(id);
                        vehModel.setPlatformId(platform.getId());
                        vehModel.setVehicleId(vehId);
                        vehModel.setPushStatus(pushStatus);
                        vehModel.setConfirmStatus(Constants.ConfirmStatus.getValue("待确认"));
                        vehModel.setForwardStatus(Constants.ForwardStatus.getValue("待转发"));
                        vehModel.setCreateTime(DateUtil.getNow());
                        vehModel.setCreateBy(ServletUtil.getCurrentUser());
                        //规则添加
                        vehModel.setAddMode(1);
                        list.add(vehModel);
                    }
                }
                if (list != null && list.size() > 0) {
                    forwardVehicleMapper.insertBatchVeh(list);
                }
            }else{
                //平台所关联的规则都禁用时，则清除所有车辆
                if (delRule.length() > 0) {
                    Map<String,Object> delParams = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
                    delParams.put("platformId", platform.getId());
                    super.delete("deleteVehs", delParams);
                }
            }
        }
    }

    @Override
    public Object pagePlatFormVehs(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){
            List<ForwardVehicle> entries = findBySqlId("pagePlatFormVehs", params);
            List<ForwardVehicleModel> models = new ArrayList();
            for(ForwardVehicle entry: entries){
                ForwardVehicle obj = entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagePlatFormVehs", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardVehicle obj = (ForwardVehicle)entry;
                models.add(ForwardVehicleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformVehicleModel addPlatformVehicle(PlatformVehicleModel model) {

        //平台的默认规则
        ForwardRule forwardRule = forwardRuleService.findPlatformDefaultRule(model.getPlatformId());
        if (forwardRule == null) {
            throw new BusinessException("平台的默认规则不存在");
        }

        int blackNum = 0;
        int repeatNum = 0;
        StringBuilder blackIds = new StringBuilder();
        StringBuilder repeatIds = new StringBuilder();
        List<String> newVehs = new ArrayList<>();
        List<ForwardVehicleModel> newPlatFormVehs = new ArrayList<>();
        List<String> currVehs = new ArrayList<>();
        ForwardVehicleModel vehModel = null;

        //获取平台信息
        Map<String,Object> platParams = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        platParams.put("id", model.getPlatformId());
        ForwardPlatform platform = forwardPlatformService.unique("findById", platParams);
        //静态数据推送平台类型, 普通平台的车辆推送状态设为空
        Integer plateFormType = platform == null ? null : platform.getStaticForwardPlatform();
        Integer pushStatus = 1;
        if(plateFormType == null || plateFormType.equals(Constants.StaticForwardPlatform.getValue("无"))){
            pushStatus = null;
        }

        //过滤黑名单车辆
        String[] vehIdsArr = model.getVehicleIds().split(",");
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
        params.put("vehicleIds", vehIdsArr);
        params.put("platformId", model.getPlatformId());
        List<ForwardPlatformVehicleBlackList> blackLists = forwardPlatformVehicleBlackListMapper.pagerModel(params);
        outer:for (String vehId: vehIdsArr) {
            for (ForwardPlatformVehicleBlackList black : blackLists) {
                if (vehId.equals(black.getVehicleId())) {
                    blackIds.append(vehId).append(",");
                    blackNum++;
                    continue outer;
                }
            }
            newVehs.add(vehId);
        }

        //去重 获取平台已存在车辆
        params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.put("platformId", model.getPlatformId());
        List<ForwardVehicle> existVehs = findBySqlId("findPlatformVehs", params);
        layer:for (String vehId : newVehs) {
            for (ForwardVehicle forwardVehicle : existVehs) {
                if (vehId.equals(forwardVehicle.getVehicleId())){
                    repeatIds.append(vehId).append(",");
                    repeatNum++;
                    continue layer;
                }
            }
            vehModel = new ForwardVehicleModel();
            vehModel.setId(UtilHelper.getUUID());
            vehModel.setPlatformId(model.getPlatformId());
            vehModel.setVehicleId(vehId);
            vehModel.setPushStatus(pushStatus);
            vehModel.setConfirmStatus(Constants.ConfirmStatus.getValue("待确认"));
            vehModel.setForwardStatus(Constants.ForwardStatus.getValue("待转发"));
            vehModel.setCreateTime(DateUtil.getNow());
            vehModel.setCreateBy(ServletUtil.getCurrentUser());
            vehModel.setAddMode(2);

            currVehs.add(vehId);
            newPlatFormVehs.add(vehModel);
        }

        if (CollectionUtils.isNotEmpty(newPlatFormVehs)) {
            //把车辆加到平台中
            forwardVehicleMapper.insertBatchVeh(newPlatFormVehs);

            //车辆同时加到平台的默认规则中。
            String addVehIdStr = Joiner.on(",").join(currVehs);
            forwardRuleService.insertSelectListItem(forwardRule.getId(), addVehIdStr);
        }

        PlatformVehicleModel returnModel = new PlatformVehicleModel();
        if (0 < blackIds.length()) {
            returnModel.setBlackIds(blackIds.substring(0, blackIds.length() - 1));
        }
        if (0 < repeatIds.length()) {
            returnModel.setRepeatIds(repeatIds.substring(0, repeatIds.length() - 1));
        }
        returnModel.setBlackNum(blackNum);
        returnModel.setRepeatNum(repeatNum);
        returnModel.setSuccessNum(newPlatFormVehs.size());
        return returnModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveToBlakList(String ids) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        String[] idsArr = ids.split(",");
        params.put("ids", idsArr);
        List<ForwardVehicle> forwardVehicles = forwardVehicleMapper.pagerModel(params);
        List<ForwardPlatformVehicleBlackList> list = new ArrayList<>();
        for (ForwardVehicle forwardVehicle : forwardVehicles) {
            //1、规则方式, 2、手动方式
            if (forwardVehicle.getAddMode().intValue() == 1) {
                ForwardPlatformVehicleBlackList obj = new ForwardPlatformVehicleBlackList();
                obj.setId(UtilHelper.getUUID());
                obj.setPlatformId(forwardVehicle.getPlatformId());
                obj.setVehicleId(forwardVehicle.getVehicleId());
                obj.setCreateBy(ServletUtil.getCurrentUser());
                obj.setCreateTime(DateUtil.getNow());
                list.add(obj);
            }
            //手动方式, 直接删除
            deleteDefaultRuleVehicle(forwardVehicle.getId(), forwardVehicle.getPlatformId(), forwardVehicle.getVehicleId());
        }
        //添加到黑名单的车辆(规则添加的)
        if (CollectionUtils.isNotEmpty(list)) {
            forwardPlatformVehicleBlackListMapper.batchInsert(list);
        }
    }

    @Override
    public List<ForwardVehicle> addFailResult(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //黑名单车辆IDs
        String blackIds = params.get("blackIds")==null?"":params.get("blackIds").toString();
        String[] blackIdsArr = blackIds.split(",");
        params.put("blackIds", blackIdsArr);

        //重复添加Ids
        String repeatVehIds = params.get("repeatIds")==null?"":params.get("repeatIds").toString();
        String[] repeatVehIdsArr = repeatVehIds.split(",");
        params.put("repeatIds", repeatVehIdsArr);

        List<ForwardVehicle> datas = forwardVehicleMapper.addFailResult(params);
        List<ForwardVehicleModel> models = new ArrayList();
        for(ForwardVehicle entry: datas){
            models.add(ForwardVehicleModel.fromEntry(entry));
        }
        return datas;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlatformVehicleModel batchAddPlatformVehicle(PagerInfo pagerInfo) {
        PlatformVehicleModel model = new PlatformVehicleModel();

        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        final Cursor<Vehicle> entityCursor = vehicleMapper.findPagerModel(params);
        List<String> addVals = new ArrayList<>();
        Iterator<Vehicle> iterator = entityCursor.iterator();
        while (iterator.hasNext()){
            addVals.add(iterator.next().getId());
        }
        if(CollectionUtils.isEmpty(addVals)) {
            throw new BusinessException("转发车辆为空");
        }
        model.setVehicleIds(StringUtils.join(addVals, ","));
        model.setPlatformId(String.valueOf(params.get("platformId")));
        model = addPlatformVehicle(model);
        return model;
    }

    private String findBlanVehicleForPlatform(String platformId) {
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
        params.put("platformId", platformId);
        List<ForwardPlatformVehicleBlackList> blackLists = forwardPlatformVehicleBlackListMapper.pagerModel(params);
        //平台黑名单的车
        List<String> list = new ArrayList<>();
        for (ForwardPlatformVehicleBlackList black : blackLists) {
            list.add(black.getVehicleId());
        }
        String vehIdStr = "";
        if (CollectionUtils.isNotEmpty(list)) {
            vehIdStr = Joiner.on(",").join(list);
        }
        return vehIdStr;
    }

    private boolean checkBlackVeh(List<ForwardPlatformVehicleBlackList> blackLists, String vehicleId) {
        boolean flag = false;
        if (CollectionUtils.isEmpty(blackLists)) {
            return flag;
        }
        for (ForwardPlatformVehicleBlackList black : blackLists) {
            if (black.getVehicleId().equals(vehicleId)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean checkDefualtRule(ForwardRuleItem ruleItem, String vehicleId) {
        boolean flag = ruleItem != null && StringUtils.isNotBlank(ruleItem.getVal()) && ruleItem.getVal().contains(vehicleId);
        return flag;
    }

    @Override
    public void exportPagePlatFormVehs(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_vehicle", "fv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardVehicle>(this, "pagePlatFormVehs", params, "dc/res/forwardVehicle/pagePlatFormVehs.xls", "已添加车辆") {
        }.work();

        return;
    }

    private void deleteDefaultRuleVehicle(String id, String platformId, String vehicleId) {
        ForwardRule forwardRule = forwardRuleService.findPlatformDefaultRule(platformId);
        if (forwardRule != null) {
            //手动方式的直接删除
            if(StringUtils.isNotBlank(id)) {
                deleteMulti(id);
            }
            forwardRuleService.deleteVehs(forwardRule.getId(), vehicleId);
        } else {
            throw new BusinessException("平台默认规则不存在,删除失败");
        }

    }

}
