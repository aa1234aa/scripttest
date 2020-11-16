package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.AlarmInfoMapper;
import com.bitnei.cloud.fault.dao.AlarmProcessMapper;
import com.bitnei.cloud.fault.domain.AlarmInfo;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.domain.AlarmProcess;
import com.bitnei.cloud.fault.model.AlarmProcessModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.IAlarmProcessService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmProcessService实现<br>
 * 描述： AlarmProcessService实现<br>
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
 * <td>2019-03-04 17:13:13</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.AlarmProcessMapper")
public class AlarmProcessService extends BaseService implements IAlarmProcessService {

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Autowired
    private AlarmProcessMapper alarmProcessMapper;

    @Autowired
    private AlarmInfoMapper alarmInfoMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("fault_alarm_process", "fap");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<AlarmProcess> entries = findBySqlId("pagerModel", params);
            List<AlarmProcessModel> models = new ArrayList<>();
            for (AlarmProcess entry : entries) {
                models.add(AlarmProcessModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AlarmProcessModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                AlarmProcess obj = (AlarmProcess) entry;
                models.add(AlarmProcessModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public void insert(AlarmProcessModel model) {
        long now = System.currentTimeMillis();
        AlarmProcess obj = new AlarmProcess();
        BeanUtils.copyProperties(model, obj);
        obj.setCreateTime(DateUtil.utcToStr(now, DateUtil.FULL_ST_FORMAT));
        obj.setCreateBy(ServletUtil.getCurrentUser());

        String[] faultAlarmIdArray = model.getFaultAlarmIds().split(",");
        String[] beginTimes = model.getFaultBeginTimes().split(",");
        int res = 0;
        // 设置再次提醒时间
        long time = obj.getAgainRemindStatus();
        if (0 < time) {
            time = now + time * 60L * 1000L;
            String againRemindTime = DateUtil.utcToStr(time, DateUtil.FULL_ST_FORMAT);
            obj.setAgainRemindTime(againRemindTime);
        }
        for (String faultAlarmId : faultAlarmIdArray) {
            obj.setId(UtilHelper.getUUID());
            obj.setFaultAlarmId(faultAlarmId);
            obj.setFaultBeginTime(beginTimes[res]);
            super.insert(obj);
            res++;
        }
        alarmInfoService.updateProcesStatus(faultAlarmIdArray, model.getProcesStatus());
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void pushAlarmInfo() {
        log.info("开始--推送过去一分钟需要再次提醒的报警信息");
        // 1.获取过 去一分钟需要再次提醒的报警信息ID集合
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 60L * 1000L;
        String endTimeStr = DateUtil.utcToStr(endTime, DateUtil.FULL_ST_FORMAT);
        String beginTimeStr = DateUtil.utcToStr(beginTime, DateUtil.FULL_ST_FORMAT);
        log.info("获取再次提醒时间为:[{}-{}]的报警ID集合", beginTimeStr, endTimeStr);
        List<String> alarmIdList = alarmProcessMapper.findFaultAlarmId(beginTimeStr, endTimeStr);
        // 2.循环获取获取报警信息详情
        Map<String, Object> params = new HashMap<>();
        AlarmInfo alarmInfo;
        for (String alramId : alarmIdList) {
            params.put("id", alramId);
            alarmInfo = alarmInfoMapper.findById(params);
            if (null != alarmInfo) {
                // 3.调用推送提醒
                log.info("开始再次推送报警[ID:{}]提醒信息", alramId);
                alarmInfoService.sendFaultAlarmInfo(alarmInfo);
            } else {
                log.info("查无报警信息[ID:{}], 应为当前报警已结束", alramId);
            }
        }
        log.info("结束--推送过去一分钟需要再次提醒的报警信息");
    }

    @Override
    public Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> findAllByTableName(String tableName) {

        List<AlarmProcess> alarmProcessList = alarmProcessMapper.findAllByTableName(tableName);
        return convertOriDataToMap(alarmProcessList);
    }

    @Override
    public Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> findByTableNameAndFaultId(
            String tableName, List<String> faultAlarmIds) {

        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("faultAlarmIds", faultAlarmIds);
        List<AlarmProcess> alarmProcessList = alarmProcessMapper.findByTableNameAndFaultId(params);
        return convertOriDataToMap(alarmProcessList);
    }

    /**
     * 转化原始的list为大数据接口使用的map
     * @param alarmProcessList
     * @return
     */
    private Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> convertOriDataToMap(
            List<AlarmProcess> alarmProcessList) {

        Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> alarmProcessMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(alarmProcessList)) {
            alarmProcessList.stream().forEach(alarmProcess -> {
                com.bitnei.cloud.common.client.model.AlarmProcess bigDataAlarmProcess = new com.bitnei.cloud.common.client.model.AlarmProcess();
                BeanUtils.copyProperties(alarmProcess, bigDataAlarmProcess);
                bigDataAlarmProcess.setProcessStatus(alarmProcess.getProcesStatus());
                if (alarmProcessMap.containsKey(alarmProcess.getFaultAlarmId())) {
                    alarmProcessMap.get(alarmProcess.getFaultAlarmId()).add(bigDataAlarmProcess);
                } else {
                    alarmProcessMap.put(alarmProcess.getFaultAlarmId(), Lists.newArrayList(bigDataAlarmProcess));
                }
            });
        }
        return alarmProcessMap;
    }

    @Override
    public void deleteBatch(String tableName, List<AlarmInfoHistory> alarmInfoHistoryList) {
        alarmProcessMapper.deleteBatch(tableName, alarmInfoHistoryList);
    }

    @Override
    public void deleteByFaultAlarmId(String faultBeginTime, String faultAlarmId) {
        alarmProcessMapper.deleteByFaultAlarmId(faultBeginTime, faultAlarmId);
    }

}
