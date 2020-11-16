package com.bitnei.cloud.sys.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.InstructSendRuleMapper;
import com.bitnei.cloud.sys.dao.UppackageSendDetailsMapper;
import com.bitnei.cloud.sys.domain.InstructSendRule;
import com.bitnei.cloud.sys.model.InstructSendRuleModel;
import com.bitnei.cloud.sys.service.IInstructSendRuleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructSendRuleService实现<br>
 * 描述： InstructSendRuleService实现<br>
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
 * <td>2019-03-07 10:28:43</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.InstructSendRuleMapper")
@RequiredArgsConstructor
public class InstructSendRuleService extends BaseService implements IInstructSendRuleService {

    private final InstructSendRuleMapper instructSendRuleMapper;
    private final UppackageSendDetailsMapper uppackageSendDetailsMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<InstructSendRule> entries = findBySqlId("pagerModel", params);
            List<InstructSendRuleModel> models = new ArrayList();
            for (InstructSendRule entry : entries) {
                InstructSendRule obj = (InstructSendRule) entry;
                models.add(InstructSendRuleModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<InstructSendRuleModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                InstructSendRule obj = (InstructSendRule) entry;
                models.add(InstructSendRuleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public InstructSendRuleModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id", id);
        InstructSendRule entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return InstructSendRuleModel.fromEntry(entry);
    }


    @Override
    public void insert(InstructSendRuleModel model) {

        InstructSendRule obj = new InstructSendRule();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setUpdateTime(obj.getCreateTime());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public String insertReturnId(InstructSendRuleModel model) {

        InstructSendRule obj = new InstructSendRule();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        return id;
    }

    @Override
    public void update(InstructSendRuleModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_send_rule", "isr");

        InstructSendRule obj = new InstructSendRule();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_send_rule", "isr");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<InstructSendRule>(this, "pagerModel", params,
                "sys/res/instructSendRule/export.xls", "国标控制指令") {
        }.work();

        return;
    }

    @Override
    public void exportRemoteInstruct(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_instruct_send_rule", "isr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<InstructSendRule>(this, "pagerModel", params,
                "sys/res/instructSendCan/export.xls", "远程命令下发") {
        }.work();

        return;
    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "INSTRUCTSENDRULE" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<InstructSendRuleModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructSendRuleModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructSendRuleModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "INSTRUCTSENDRULE" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<InstructSendRuleModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(InstructSendRuleModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(InstructSendRuleModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 校验指令是否可以下发
     *
     * @param vin
     * @return
     */
    @Override
    public boolean checkIsCanSend(String vin) {
        int ruleCount = instructSendRuleMapper.getImplementRuleCountByVin(vin);
        if (ruleCount > 0) {
            log.error("车辆vin:" + vin + "的车辆存在正在执行的国标控制指令");
            return false;
        }
        int vehUppackageTaskCount = uppackageSendDetailsMapper.getImplementUpgradeCountByVin(vin);
        if (vehUppackageTaskCount > 0) {
            log.error("车辆vin:" + vin + "的车辆存在正在执行的升级任务");
            return false;
        }
        return true;
    }

    @Override
    public int getImplementRuleCountByVin(String vin) {
        return instructSendRuleMapper.getImplementRuleCountByVin(vin);
    }

    @Override
    public Integer termLockAnswer(Map<String, String> map) {
        this.termLockAnswerMap(map);
        return update("tremLockChange", map);
    }

    /**
     * map中key的装换
     * @param map
     */
    private void termLockAnswerMap(Map<String,String> map){
        //流水号
        String sessionId = map.getOrDefault("4710046","").toString().trim();
        map.put("sessionId", sessionId);
        //终端应答
        String lockAnswer = map.getOrDefault("4710049", "").toString().trim();
        map.put("lockAnswer", lockAnswer);
        //应答时间
        String answerTime = map.getOrDefault("4710045", "").toString().trim();

        if (!StringUtils.isEmpty(answerTime)) {
            answerTime = DateUtil.formatTime(DateUtil.strToDate_ex(answerTime),DateUtil.FULL_ST_FORMAT);
        }
        map.put("answerTime", answerTime);

    }
}
