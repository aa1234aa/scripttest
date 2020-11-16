package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.client.das.AlarmClient;
import com.bitnei.cloud.common.client.model.GlobalResponse;
import com.bitnei.cloud.common.client.model.Location;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.fault.domain.AlarmInfoHistory;
import com.bitnei.cloud.fault.enums.StopFaultAlarmModeEnum;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.IAlarmInfoMoveService;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.fault.service.IAlarmProcessService;
import com.bitnei.cloud.sys.model.VehicleInfoModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.system.interceptor.YearMonthStrategy;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 历史报警数据迁移业务接口实现类
 */
@Slf4j
@Service
public class AlarmInfoMoveService implements IAlarmInfoMoveService {

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Autowired
    private IAlarmInfoHistoryService alarmInfoHistoryService;

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private YearMonthStrategy yearMonthStrategy;

    @Autowired
    private IAlarmProcessService alarmProcessService;

    @Autowired
    private AlarmClient alarmClient;

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    /**
     * 结束报警方式: 0=自动结束(状态=已结束; 默认), 1=处理结束(状态=已结束 && 处理状态=已处理)
     */
    @Value("${stop.fault.alarm.mode:0}")
    private int stopFaultAlarmMode;

    /**
     * 单次最大处理量
     */
    private static final int ROUND_MAX_SIZE = 5000;

    @Override
    public void move() {
        long startTime = System.currentTimeMillis();
        // 1.查询需结束归入历史报警的数据集合
        Map<String, Object> params = new HashMap<>();
        params.put("faultStatus", 2);
        if (StopFaultAlarmModeEnum.PROCESS_STOP.getValue() == stopFaultAlarmMode) {
            params.put("procesStatus", 3);
        }
        List<AlarmInfoHistory> needStopList = alarmInfoService.findStopAlarm(params);
        if (CollectionUtils.isEmpty(needStopList)) {
            return;
        }
        log.info("已查询需结束归入历史报警的数据集合, 数量: {}, 耗时(ms): {}", needStopList.size(),
                System.currentTimeMillis() - startTime);

        // 2.预先获取所有车辆集合
        long queryVehicleTime = System.currentTimeMillis();
        List<VehicleInfoModel> vehicleInfoModelList = vehicleService.vehicles(null, null);

        log.debug("车辆集合查询结束, 数量:{}, 耗时(ms):{}", vehicleInfoModelList.size(),
                System.currentTimeMillis() - queryVehicleTime);

        final Map<String, VehicleInfoModel> vehicleInfoModelMap =
                vehicleInfoModelList.stream().collect(Collectors.toMap(VehicleInfoModel::getId, item -> item));

        //过滤掉车辆不存在的数据
        needStopList = needStopList.stream()
                .filter(it -> vehicleInfoModelMap.containsKey(it.getVehicleId()))
                .collect(Collectors.toList());

        final ImmutableSet<String> processTables = yearMonthStrategy.getShardingTables("fault_alarm_process");

        // 3.按故障开始时间进行分组处理
        final Map<String, List<AlarmInfoHistory>> groupMap = groupBy(needStopList);
        groupMap.entrySet().parallelStream().forEach(stringListEntry -> {
            final String alarmProcessTableName = "fault_alarm_process_" + stringListEntry.getKey();
            final List<AlarmInfoHistory> alarmInfoHistoryList = stringListEntry.getValue();

            // 4.预分组报警处理记录
            long queryAlarmProcessTime = System.currentTimeMillis();

            List<String> faultAlarmIds = alarmInfoHistoryList.stream()
                    .map(AlarmInfoHistory::getId).collect(Collectors.toList());

            Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> alarmProcessMap = new HashMap<>();

            if (processTables.contains(alarmProcessTableName)) {
                alarmProcessMap = alarmProcessService.findByTableNameAndFaultId(
                        alarmProcessTableName, faultAlarmIds);
            }

            final Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> processMap = alarmProcessMap;

            log.debug("预分组报警处理记录结束, 耗时(ms):{}", System.currentTimeMillis() - queryAlarmProcessTime);

            // 5.避免单次数据量过多造成处理事务过长, 按5000条数据一批次处理
            final int round = calculateRound(alarmInfoHistoryList.size());
            log.info("开始__将当前报警归入大数据历史报警，数据量：{}，拆分批次：{}", alarmInfoHistoryList.size(), round);
            Stream.iterate(0, n -> n + 1).limit(round).parallel().forEach(i -> {
                // 截取数据集
                List<AlarmInfoHistory> historyList = subList(alarmInfoHistoryList, i, round);
                // 推送历史数据至大数据
                boolean result = insert(historyList, vehicleInfoModelMap, processMap);
                log.info("结束批次[{}]__将当前报警归入大数据历史报警，操作结果：{}", i, result);
                if (result) {
                    log.info("即将删除此批次[{}]本地数据", i);
                    alarmInfoHistoryService.deleteBatch("fault_alarm_info", historyList);
                    if (processTables.contains(alarmProcessTableName)) {
                        alarmProcessService.deleteBatch(alarmProcessTableName, historyList);
                    }
                }
            });
        });
        log.info("本次当前报警同步至大数据历史报警结束, 总耗时(ms): {}", System.currentTimeMillis() - startTime);
    }

    @Override
    public void moveHistory() {

        long startTime = System.currentTimeMillis();
        // 1.预先获取所有车辆集合
        List<VehicleInfoModel> vehicleInfoModelList = vehicleService.vehicles(null, null);
        log.info("车辆集合查询结束, 数量:{}, 耗时(ms):{}", vehicleInfoModelList.size(),
                System.currentTimeMillis() - startTime);

        final Map<String, VehicleInfoModel> vehicleInfoModelMap =
                vehicleInfoModelList.stream().collect(Collectors.toMap(VehicleInfoModel::getId, item -> item));

        // 2.获取历史报警分表集合
        final ImmutableSet<String> tables = yearMonthStrategy.getShardingTables("fault_alarm_info_history");

        // 排序分表
        final ImmutableSortedSet<String> sortedTables = ImmutableSortedSet.copyOf(tables);

        // 获取上次执行到的时间节点
        String lastTime = redis.get(5, "MOVE_ALARM_HISTORY_LAST_TIME", "0000-00-00 00:00:00");
        // 获取上次执行到的faultId
        String lastFaultId = redis.get(5, "MOVE_ALARM_HISTORY_LASH_FAULT_ID", "");

        // 处理表集合拿出来备用
        final ImmutableSet<String> processTables = yearMonthStrategy.getShardingTables("fault_alarm_process");

        for (String shardingTable : sortedTables) {

            String processingTable = YearMonthStrategy.getShardingTableAndIdx(
                    "fault_alarm_info_history", lastTime);

            List<AlarmInfoHistory> alarmInfoHistoryList = new ArrayList<>();

            // 比较上次执行是否执行到当前分表
            if (processingTable.compareTo(shardingTable) <= 0) {

                while (true) {

                    redis.set(5, "CURRENT_MOVE_TIME", DateUtil.getNow());

                    alarmInfoHistoryList = alarmInfoHistoryService.findPageByTableName(
                            shardingTable, lastTime, ROUND_MAX_SIZE, lastFaultId);

                    if (CollectionUtils.isEmpty(alarmInfoHistoryList)) {
                        break;
                    }

                    //当前批次位点信息
                    lastTime = alarmInfoHistoryList.get(alarmInfoHistoryList.size() - 1).getFaultBeginTime();
                    lastFaultId = alarmInfoHistoryList.get(alarmInfoHistoryList.size() - 1).getId();

                    //过滤掉车辆不存在的数据
                    alarmInfoHistoryList = alarmInfoHistoryList.stream()
                            .filter(it -> vehicleInfoModelMap.containsKey(it.getVehicleId()))
                            .collect(Collectors.toList());

                    if (CollectionUtils.isEmpty(alarmInfoHistoryList)) {
                        break;
                    }

                    alarmInfoHistoryList.forEach(it -> {
                        String whoCanSeeMe = vehicleInfoModelMap.get(it.getVehicleId()).getWhoCanSeeMe();
                        it.setWhoCanSeeMe(StringUtils.isNotEmpty(whoCanSeeMe) ? whoCanSeeMe : "");
                    });

                    //查询当前处理的ROUND_MAX_SIZE的故障对应的故障处理记录
                    String alarmProcessTableName = shardingTable
                            .replace("fault_alarm_info_history", "fault_alarm_process");

                    List<String> faultAlarmIds = alarmInfoHistoryList.stream()
                            .map(AlarmInfoHistory::getId).collect(Collectors.toList());

                    Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> alarmProcessMap =
                            new HashMap<>();

                    if (processTables.contains(alarmProcessTableName)) {

                        alarmProcessMap = alarmProcessService.findByTableNameAndFaultId(
                                alarmProcessTableName, faultAlarmIds);
                    }

                    // 推送历史数据至大数据
                    boolean result = insert(alarmInfoHistoryList, vehicleInfoModelMap, alarmProcessMap);

                    log.info("分表:{}，偏移时间:{}，偏移id:{}，迁移结果:{}",
                            shardingTable, lastTime, lastFaultId, result);
                    if (result) {
                        // 处理成功，设置redis上次执行位点
                        redis.set(5, "MOVE_ALARM_HISTORY_LAST_TIME", lastTime);
                        redis.set(5, "MOVE_ALARM_HISTORY_LASH_FAULT_ID", lastFaultId);

                        //不再删除同步的原始历史数据
//                        log.info("即将删除此本次偏移:偏移id:{}本地分表数据，分表：{}", faultAlarmIds, shardingTable);
//                        alarmInfoHistoryService.deleteBatch(shardingTable, alarmInfoHistoryList);
//                        if (processTables.contains(alarmProcessTableName)) {
//                            alarmProcessService.deleteBatch(alarmProcessTableName, alarmInfoHistoryList);
//                        }
                    }

                }
            }

            //处理完每个分区要把id复原，再进行下个分表的处理
            lastFaultId = "";
        }

        redis.set(5, "CURRENT_MOVE_END_TIME", DateUtil.getNow());

//        // 3.按分表循环查询历史报警数据
//        tables.parallelStream().forEach(alarmInfoHistoryTableName -> {
//            final List<AlarmInfoHistory> alarmInfoHistoryList =
//                    alarmInfoHistoryService.findAllByTableName(alarmInfoHistoryTableName);
//            if (CollectionUtils.isEmpty(alarmInfoHistoryList)) {
//                return;
//            }
//            // 4.按分表预分组报警处理记录
//            long queryAlarmProcessTime = System.currentTimeMillis();
//            final String alarmProcessTableName = alarmInfoHistoryTableName
//                    .replace("fault_alarm_info_history", "fault_alarm_process");
//
//            final Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> alarmProcessMap =
//                    alarmProcessService.findAllByTableName(alarmProcessTableName);
//            log.debug("按分表预分组报警处理记录结束, 耗时(ms):{}", System.currentTimeMillis() - queryAlarmProcessTime);
//
//            // 5.避免单次数据量过多造成处理事务过长, 按5000条数据一批次处理
//            final int round = calculateRound(alarmInfoHistoryList.size());
//            log.debug("开始__迁移历史报警分表数据，分表：{}，数据量：{}，拆分批次：{}", alarmInfoHistoryTableName,
//                    alarmInfoHistoryList.size(), round);
//
//            Stream.iterate(0, n -> n + 1).limit(round).parallel().forEach(i -> {
//                // 截取数据集
//                List<AlarmInfoHistory> historyList = subList(alarmInfoHistoryList, i, round);
//                // 推送历史数据至大数据
//                boolean result = insert(historyList, vehicleInfoModelMap, alarmProcessMap);
//                log.debug("结束批次[{}]__迁移历史报警分表数据，分表:{}，迁移结果：{}", i, alarmInfoHistoryTableName, result);
//                if (result) {
//                    log.debug("即将删除此批次[{}]本地分表数据，分表：{}", i, alarmInfoHistoryTableName);
//                    alarmInfoHistoryService.deleteBatch(alarmInfoHistoryTableName, historyList);
//                    alarmProcessService.deleteBatch(alarmProcessTableName, historyList);
//                }
//            });
//
//        });

        log.info("本次历史报警数据迁移结束, 总耗时(ms): {}", System.currentTimeMillis() - startTime);
    }

    public static void main(String[] args) {

        String a = "fault_alarm_info_history_201905";
        String b = "fault_alarm_info_history_000000";
        String c = "fault_alarm_info_history_201905";
        String d = "fault_alarm_info_history_201908";

        System.out.println(b.compareTo(a));
        System.out.println(c.compareTo(a));
        System.out.println(a.compareTo(b));
        System.out.println(d.compareTo(c));
    }

    /**
     * 计算批次
     *
     * @param totalSize 总量
     * @return
     */
    private int calculateRound(int totalSize) {
        return totalSize % ROUND_MAX_SIZE == 0 ? (totalSize / ROUND_MAX_SIZE) : (totalSize / ROUND_MAX_SIZE + 1);
    }

    /**
     * 计算截取数据集
     *
     * @param alarmInfoHistoryList 原集合
     * @param index                当前批次
     * @param round                总批次数
     * @return
     */
    private List<AlarmInfoHistory> subList(final List<AlarmInfoHistory> alarmInfoHistoryList,
                                           final int index,
                                           final int round) {
        int fromIndex = index * ROUND_MAX_SIZE;
        int toIndex;
        if ((index + 1) >= round) {
            toIndex = alarmInfoHistoryList.size();
        } else {
            toIndex = (index + 1) * ROUND_MAX_SIZE;
        }
        List<AlarmInfoHistory> historyList = alarmInfoHistoryList.subList(fromIndex, toIndex);
        return historyList;
    }

    /**
     * 新增大数据历史报警
     *
     * @param alarmInfoHistoryList 历史报警集合
     * @return
     */
    private boolean insert(final List<AlarmInfoHistory> alarmInfoHistoryList,
                           final Map<String, VehicleInfoModel> vehicleInfoModelMap,
                           final Map<String, List<com.bitnei.cloud.common.client.model.AlarmProcess>> alarmProcessMap) {
        long time = System.currentTimeMillis();
        boolean result;
        final List<com.bitnei.cloud.common.client.model.AlarmInfoHistory> bigDataList = Collections.synchronizedList(new ArrayList<>());
        alarmInfoHistoryList.stream().parallel().forEach(alarmInfoHistory -> {
            // 1.进行对象实体转换
            com.bitnei.cloud.common.client.model.AlarmInfoHistory bigDataAlarmInfoHistory = convertBigDataModel(alarmInfoHistory);

            // 2.获取并设置处理记录
            if (alarmProcessMap.containsKey(alarmInfoHistory.getId())) {
                bigDataAlarmInfoHistory.setAlarmProcesses(alarmProcessMap.get(alarmInfoHistory.getId()).toArray(new com.bitnei.cloud.common.client.model.AlarmProcess[0]));
            }

            // 3.获取并设置车辆相关信息
            if (vehicleInfoModelMap.containsKey(alarmInfoHistory.getVehicleId())) {
                BeanUtils.copyProperties(vehicleInfoModelMap.get(alarmInfoHistory.getVehicleId()), bigDataAlarmInfoHistory);
            }

            // 重新设置报警数据ID, 避免中间实体转换过程中相同ID属性被覆盖
            bigDataAlarmInfoHistory.setId(alarmInfoHistory.getId());
            bigDataAlarmInfoHistory.setWhoCanSeeMe(alarmInfoHistory.getWhoCanSeeMe().split("\\s+"));

            bigDataList.add(bigDataAlarmInfoHistory);
        });

        try {
            // 调用大数据保存接口
            com.bitnei.cloud.common.client.model.AlarmInfoHistory[] bigDataArray =
                    bigDataList.toArray(new com.bitnei.cloud.common.client.model.AlarmInfoHistory[0]);

            GlobalResponse<Boolean> response = alarmClient.add(bigDataArray);
            result = response.getData();
        } catch (Exception e) {
            log.error("推送历史报警数据至大数据存储异常!", e);
            result = false;
        }
        log.debug("结束推送历史报警数据至大数据存储, 调用结果:{}, 耗时(ms):{}", result, System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 将本地历史报警对象转换成大数据接口对象
     *
     * @param alarmInfoHistory 大数据接口历史报警对象
     * @return
     */
    private com.bitnei.cloud.common.client.model.AlarmInfoHistory convertBigDataModel(AlarmInfoHistory alarmInfoHistory) {
        long time = System.currentTimeMillis();
        if (null == alarmInfoHistory) {
            return null;
        }
        com.bitnei.cloud.common.client.model.AlarmInfoHistory result = new com.bitnei.cloud.common.client.model.AlarmInfoHistory();
        BeanUtils.copyProperties(alarmInfoHistory, result);

        // 对象实体转换, 个别属性差异需手动设置
        if (null != alarmInfoHistory.getLatitude() && null != alarmInfoHistory.getLongitude()) {
            double latitude = new BigDecimal(alarmInfoHistory.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            double longitude = new BigDecimal(alarmInfoHistory.getLongitude()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            Location location = new Location(latitude, longitude);
            result.setFaultLocation(location);
        } else {
            result.setFaultLocation(new Location(0, 0));
        }
        result.setProcessStatus(alarmInfoHistory.getProcesStatus());
        result.setProcessTime(alarmInfoHistory.getProcesTime());
        result.setFaultCreateTime(alarmInfoHistory.getCreateTime());

        log.trace("完成转换大数据历史报警对象, 耗时(ms): {}", System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 按故障开始时间分组
     *
     * @param alarmInfoHistoryList 原数据集
     * @return
     */
    private Map<String, List<AlarmInfoHistory>> groupBy(List<AlarmInfoHistory> alarmInfoHistoryList) {
        long time = System.currentTimeMillis();
        // 按年月分区
        final Map<String, List<AlarmInfoHistory>> groupMap = alarmInfoHistoryList
                .stream()
                .collect(Collectors.groupingBy(alarmInfoHistory -> {
                    try {
                        return DateFormatUtils.format(
                                DateUtils.parseDate(alarmInfoHistory.getFaultBeginTime(), "yyyy-MM-dd HH:mm:ss"), "yyyyMM");
                    } catch (ParseException e) {
                        return "";
                    }
                }));
        log.debug("按故障开始时间分组完成, 耗时(ms): {}", System.currentTimeMillis() - time);
        return groupMap;
    }

}
