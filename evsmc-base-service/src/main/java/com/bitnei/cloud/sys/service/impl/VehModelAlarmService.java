package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.service.IParameterRuleService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehModelAlarm;
import com.bitnei.cloud.sys.enums.PowerModeEnum;
import com.bitnei.cloud.sys.model.VehModelAlarmModel;
import com.bitnei.cloud.sys.service.IVehModelAlarmService;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 车辆车型服务实现<br>
 * 描述： 车辆车型服务实现<br>
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
 * <td>2019-02-20 13:45:57</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.VehModelAlarmMapper")
public class VehModelAlarmService extends BaseService implements IVehModelAlarmService {

    @Autowired
    private IParameterRuleService parameterRuleService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model_alarm", "alarm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<VehModelAlarm> entries = findBySqlId("pagerModel", params);
            List<VehModelAlarmModel> models = new ArrayList<>();
            for (VehModelAlarm entry : entries) {
                models.add(VehModelAlarmModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehModelAlarmModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                VehModelAlarm obj = (VehModelAlarm) entry;
                models.add(VehModelAlarmModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public VehModelAlarmModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model_alarm", "alarm");
        params.put("id", id);
        VehModelAlarm entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return VehModelAlarmModel.fromEntry(entry);
    }


    @Override
    public void insert(int powerMode, VehModelAlarmModel model) {
        VehModelAlarm obj = new VehModelAlarm();
        if( model.getBeginThreshold() == null ){
            model.setBeginThreshold(0);
        }
        if( model.getEndThreshold() == null ){
            model.setEndThreshold(0);
        }
        BeanUtils.copyProperties(model, obj);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
        // 处理车辆型号通用报警阈值，生成报警规则
        if (powerMode == PowerModeEnum.MODEL_6.ordinal()) {
            // 传统燃油需要把通用规则禁用掉
            parameterRuleService.disabledVehicleModelRule(model.getId());
        } else {
            parameterRuleService.handleVehicleModelRule(model, null);
        }
    }

    @Override
    public void update(VehModelAlarmModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model_alarm", "alarm");
        VehModelAlarm obj = new VehModelAlarm();
        if( model.getBeginThreshold() == null ){
            model.setBeginThreshold(0);
        }
        if( model.getEndThreshold() == null ){
            model.setEndThreshold(0);
        }
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
    }

    /**
     * 删除多个
     *
     * @param ids ids
     * @return 删除行数
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model_alarm", "alarm");
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
    public void save(int powerMode, VehModelAlarmModel model) {
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_model_alarm", "alarm");
        params.put("id", model.getId());
        VehModelAlarm entry = unique("findById", params);
        if (entry != null) {
            this.update(model);
            // 处理车辆型号通用报警阈值，更新报警规则
            if (powerMode == PowerModeEnum.MODEL_6.ordinal()) {
                // 传统燃油需要把通用规则禁用掉
                parameterRuleService.disabledVehicleModelRule(model.getId());
            } else {
                parameterRuleService.handleVehicleModelRule(model, entry);
            }
        } else {
            this.insert(powerMode, model);
        }
    }
}
