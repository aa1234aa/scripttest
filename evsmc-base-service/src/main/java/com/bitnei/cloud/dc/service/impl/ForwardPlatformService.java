package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.dao.ForwardPlatformVehicleBlackListMapper;
import com.bitnei.cloud.dc.dao.ForwardRuleMapper;
import com.bitnei.cloud.dc.domain.ForwardPlatform;
import com.bitnei.cloud.dc.domain.ForwardRule;
import com.bitnei.cloud.dc.domain.PlatformRuleLk;
import com.bitnei.cloud.dc.model.ForwardPlatformModel;
import com.bitnei.cloud.dc.model.PlatformRuleLkModel;
import com.bitnei.cloud.dc.service.IForwardPlatformService;
import com.bitnei.cloud.dc.service.IPlatformDataLkService;
import com.bitnei.cloud.dc.service.IPlatformRuleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardPlatformService实现<br>
* 描述： ForwardPlatformService实现<br>
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
* <td>2019-02-12 14:46:42</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.ForwardPlatformMapper" )
public class ForwardPlatformService extends BaseService implements IForwardPlatformService {

    @Resource(name = "ctoRedisKit")
    private RedisKit redisKit;

    private static final int CAR_COUNT_DB = 1;
    @Autowired
    private IPlatformDataLkService platformDataLkService;
    @Autowired
    private IPlatformRuleLkService platformRuleLkService;

    @Value("${forward.platform.check.ip:10.11.6.1}")
    private String checkIp;

    @Resource
    private ForwardRuleMapper forwardRuleMapper;

    @Resource
    private ForwardPlatformVehicleBlackListMapper forwardPlatformVehicleBlackListMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<ForwardPlatform> entries = findBySqlId("pagerModel", params);
            List<ForwardPlatformModel> models = new ArrayList();
            for(ForwardPlatform entry: entries){
                ForwardPlatform obj = (ForwardPlatform)entry;
                ForwardPlatformModel model = ForwardPlatformModel.fromEntry(obj);
                // 获取平台连接状态
                String onlineState = redisKit.hget(CAR_COUNT_DB, "XNY.PLAT_ONLINE_STATE", obj.getId());
                if(onlineState != null){
                    model.setConnectStatus(Integer.parseInt(onlineState));
                }
                models.add(model);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardPlatformModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                ForwardPlatform obj = (ForwardPlatform)entry;
                ForwardPlatformModel model = ForwardPlatformModel.fromEntry(obj);
                // 获取平台连接状态
                String onlineState = redisKit.hget(CAR_COUNT_DB, "XNY.PLAT_ONLINE_STATE", obj.getId());
                if(onlineState != null){
                    model.setConnectStatus(Integer.parseInt(onlineState));
                }
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public ForwardPlatformModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        params.put("id",id);
        ForwardPlatform entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        ForwardPlatformModel model = ForwardPlatformModel.fromEntry(entry);
        // 获取平台连接状态
        String onlineState = redisKit.hget(CAR_COUNT_DB, "XNY.PLAT_ONLINE_STATE", id);
        if(onlineState != null){
            model.setConnectStatus(Integer.parseInt(onlineState));
        }
        return model;
    }


    @Override
    public ForwardPlatformModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        params.put("name",name);
        ForwardPlatform entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        ForwardPlatformModel model = ForwardPlatformModel.fromEntry(entry);
        // 获取平台连接状态
        String onlineState = redisKit.hget(CAR_COUNT_DB, "XNY.PLAT_ONLINE_STATE", model.getId());
        if(onlineState != null){
            model.setConnectStatus(Integer.parseInt(onlineState));
        }
        return model;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ForwardPlatformModel model) {
        //测试ip及端口检查
        checkTestIp(model.getHost());
        ForwardPlatform obj = new ForwardPlatform();
        BeanUtils.copyProperties(model, obj);
        //限制 IP + 端口 唯一
        //获取当权限的map
        Map<String,Object> params = new HashMap<>();
        params.put("host", obj.getHost());
        params.put("port", obj.getPort());
        Integer fpCount = unique("findByHostPortCount", params);
        if(fpCount > 0){
            throw new BusinessException(String.format("新增失败，%s 已存在", obj.getHost() + " + " +obj.getPort()));
        }

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

        //生成默认的规则
        addDefaultRule(id, obj.getName());
    }

    @Override
    public void update(ForwardPlatformModel model) {
        //测试ip及端口检查
        checkTestIp(model.getHost());
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");

        ForwardPlatform obj = new ForwardPlatform();
        BeanUtils.copyProperties(model, obj);
        //限制 IP + 端口 唯一
        //获取当权限的map
        Map<String,Object> validParams = new HashMap<>();
        validParams.put("host", obj.getHost());
        validParams.put("port", obj.getPort());
        validParams.put("id", obj.getId());
        Integer fpCount = unique("findByHostPortCount", validParams);
        if(fpCount > 0){
            throw new BusinessException(String.format("更新失败，%s 已存在", obj.getHost() + " + " +obj.getPort()));
        }
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");

        Map<String,Object> ruleParams = DataAccessKit.getAuthMap("dc_forward_rule", "fr");

        Map<String,Object> blackParams = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){

            List<PlatformRuleLk> platformRuleLks = platformRuleLkService.listPlatformRule(id);
            if (CollectionUtils.isNotEmpty(platformRuleLks) && platformRuleLks.size() > 1) {
                throw new BusinessException("该转发平台存在关联转发规则，不允许删除", 300);
            }
            PlatformRuleLk platformRuleLk = platformRuleLks.get(0);
            //不是默认规则
            if (platformRuleLk.getDefaultRule() != null && platformRuleLk.getDefaultRule().intValue() == 0) {
                throw new BusinessException("该转发平台存在关联转发规则，不允许删除", 300);
            }

            //1、删除平台
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
            //2、删除平台转发数据项
            Map<String,Object> diParams = DataAccessKit.getAuthMap("dc_platform_data_lk", "lk");
            diParams.put("platformId", id);
            platformDataLkService.delete("deleteByPlatformId", diParams);

            //3、删除平台与默认规则关系
            platformRuleLkService.deleteMulti(platformRuleLk.getId());
            //4、删除默认规则
            ruleParams.put("id", platformRuleLk.getForwardRuleId());
            ruleParams.put("ruleId", platformRuleLk.getForwardRuleId());
            forwardRuleMapper.delete(ruleParams);
            //5、删除默认规则明细
            forwardRuleMapper.delRuleItem(ruleParams);
            //6、删除黑名单车辆
            blackParams.put("platformId", id);
            forwardPlatformVehicleBlackListMapper.deleteByPlatformId(blackParams);
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_forward_platform", "fp");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardPlatform>(this, "pagerModel", params, "dc/res/forwardPlatform/export.xls", "平台转发配置") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "FORWARDPLATFORM"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<ForwardPlatformModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ForwardPlatformModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ForwardPlatformModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "FORWARDPLATFORM"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<ForwardPlatformModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(ForwardPlatformModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(ForwardPlatformModel model) {
                update(model);
            }
        }.work();

    }

    private void checkTestIp(String targetIp) {
        String[] testIpArr = checkIp.replaceAll("，", ",").split(",");
        for(String ip : testIpArr) {
            if (targetIp.equals(ip)) {
                throw new BusinessException("配置的目的地址为内部测试地址");
            }
        }
    }

    private void addDefaultRule(String platFormId, String platformName) {
        //1、默认规则
        ForwardRule model = new ForwardRule();
        model.setName(platformName + "-默认规则");
        //默认是
        model.setStatus(1);
        //默认列表
        model.setRuleType(1);
        //默认规则
        model.setDefaultRule(1);
        model.setNote("添加平台时自动生成");
        String defaultRuleId = UtilHelper.getUUID();
        model.setId(defaultRuleId);
        model.setCreateTime(DateUtil.getNow());
        model.setCreateBy(ServletUtil.getCurrentUser());
        forwardRuleMapper.insert(model);

        //2、规则明细
        Map<String,Object> params = new HashMap<>();
        params.put("resourceItemId", "");
        params.put("forwardRuleId", defaultRuleId);
        params.put("val", "");
        params.put("createTime", DateUtil.getNow());
        params.put("createBy", ServletUtil.getCurrentUser());
        forwardRuleMapper.insertForwardRuleItem(params);

        //3、增加平台与规则关系
        PlatformRuleLkModel demo = new PlatformRuleLkModel();
        demo.setPlatformId(platFormId);
        demo.setAddRuleIds(defaultRuleId);
        platformRuleLkService.insertPlatformRules(demo);
    }
}
