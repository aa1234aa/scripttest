package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.NotifierVehicleLkMapper;
import com.bitnei.cloud.fault.domain.NotifierVehicleLk;
import com.bitnei.cloud.fault.model.NotifierVehicleLkModel;
import com.bitnei.cloud.fault.service.INotifierVehicleLkService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierVehicleLkService实现<br>
* 描述： NotifierVehicleLkService实现<br>
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
* <td>2019-03-06 17:37:36</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.NotifierVehicleLkMapper" )
public class NotifierVehicleLkService extends BaseService implements INotifierVehicleLkService {

    @Resource
    private NotifierVehicleLkMapper notifierVehicleLkMapper;

    @Resource
    private IVehicleService vehicleService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_notifier_vehicle_lk", "fnvl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<NotifierVehicleLk> entries = findBySqlId("pagerModel", params);
            List<NotifierVehicleLkModel> models = new ArrayList();
            for(NotifierVehicleLk entry: entries){
                NotifierVehicleLkModel model = NotifierVehicleLkModel.fromEntry(entry);
                Integer count = notifierVehicleLkMapper.allocateCount(entry.getVehicleId());
                model.setAllocateCount(count == null ? 0 : count);
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<NotifierVehicleLkModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                NotifierVehicleLk obj = (NotifierVehicleLk)entry;
                NotifierVehicleLkModel model = NotifierVehicleLkModel.fromEntry(obj);
                Integer count = notifierVehicleLkMapper.allocateCount(obj.getVehicleId());
                model.setAllocateCount(count == null ? 0 : count);
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public NotifierVehicleLkModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_notifier_vehicle_lk", "fnvl");
        params.put("id",id);
        NotifierVehicleLk entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return NotifierVehicleLkModel.fromEntry(entry);
    }


    @Override
    public int insert(NotifierVehicleLkModel model) {
        Map<String, List<String>> map = allocate(model);
        //成功分配的车辆数
        int i = 0;

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> vehicleIds = entry.getValue();
            for (String vehicleId : vehicleIds) {
                NotifierVehicleLk obj = new NotifierVehicleLk();
                obj.setId(UtilHelper.getUUID());
                obj.setVehicleId(vehicleId);
                obj.setNotifierId(entry.getKey());
                obj.setCreateTime(DateUtil.getNow());
                obj.setCreateBy(ServletUtil.getCurrentUser());
                super.insert(obj);
                i++;
            }
        }
        return i;
    }

    private Map<String, List<String>> allocate(NotifierVehicleLkModel model) {
        String[] chars = model.getVehicleId().split(",");
        String[] strs =  model.getNotifierId().split(",");
        Map<String, List<String>> allocateMap = new HashMap<>();
        for (String notifierId : strs) {
            List<NotifierVehicleLk> notifierVehicleLks = notifierVehicleLkMapper.allocateVehicle(notifierId);
            List<String> vehicleIds = new ArrayList<>();
            for (String vehicleId : chars) {
                boolean flag = false;
                for (NotifierVehicleLk notifierVehicleLk : notifierVehicleLks) {
                    if (vehicleId.equals(notifierVehicleLk.getVehicleId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    vehicleIds.add(vehicleId);
                }
            }
            allocateMap.put(notifierId, vehicleIds);
        }
        return allocateMap;
    }

    @Override
    public void update(NotifierVehicleLkModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_notifier_vehicle_lk", "fnvl");

        NotifierVehicleLk obj = new NotifierVehicleLk();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_notifier_vehicle_lk", "fnvl");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }

    /**
     * 删除多个
     * @param userId
     * @return
     */
    @Override
    public void deleteByUserId(String userId) {
        notifierVehicleLkMapper.deleteByUserId(userId);
    }

    /**
     * 批量新增
     * @param pagerInfo 查询参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(final PagerInfo pagerInfo) {
        long beginTime = System.currentTimeMillis();
        String notifierIds = null;
        Iterator<Condition> it = pagerInfo.getConditions().iterator();
        while (it.hasNext()) {
            Condition c = it.next();
            // 检查负责人ID
            if ("notifierId".equals(c.getName())) {
                notifierIds = c.getValue();
                // 删除非用于查询的参数
                it.remove();
                break;
            }
        }
        if (StringUtils.isBlank(notifierIds)) {
            String error = "负责人ID不能为空";
            log.error(error);
            throw new BusinessException(error);
        }

        // 查询获取车辆集合
        final List<Vehicle> vehicleList = fingVehicleForLk(pagerInfo);

        if(CollectionUtils.isEmpty(vehicleList)){
            String error = "查无车辆可分配";
            log.error(error);
            throw new BusinessException(error);
        }

        // 多人分配情况需循环分配
        if (StringUtil.isNotEmpty(notifierIds)){
            String[] tmps = notifierIds.split(",");
            for (String notifierId: tmps){
                batchOperation(notifierId, vehicleList);
            }
        }
        log.info("分配全部查询车辆完成, 总耗时(毫秒):{}", System.currentTimeMillis() - beginTime);
    }

    /**
     * 获取车辆集合
     * @param pagerInfo 查询参数
     * @return 待分配车辆集合
     */
    private List<Vehicle> fingVehicleForLk(final PagerInfo pagerInfo) {
        long beginTime = System.currentTimeMillis();
        log.info("开始获取查询车辆结果集");
        List<Vehicle> list = vehicleService.findAllForLk(pagerInfo);
        log.info("已获得查询车辆结果集, 车辆数:{}, 耗时(ms):{}", list.size(), System.currentTimeMillis() - beginTime);
        return list;
    }

    /**
     * 批量操作分配车辆, 车辆数据较多时长事务处理期间并发可能会出现数据重复或死锁问题, 因此需要加同步锁
     * @param notifierId 推送负责人ID
     * @param vehicleList 分配车辆集合
     */
    private synchronized void batchOperation(final String notifierId, final List<Vehicle> vehicleList) {
        long beginTime = System.currentTimeMillis();
        final String createBy = ServletUtil.getCurrentUser();
        final String createTime = DateUtil.getNow();
        // 分批处理, 计算批处理次数, 5000辆车为一个批次
        final int max_size = 5000;
        final int round = vehicleList.size() % max_size == 0 ? vehicleList.size() / max_size : vehicleList.size() / max_size + 1;
        log.info("开始为负责人({})批量分配车辆, 总批次:{}", notifierId, round);
        Stream.iterate(0, n -> n + 1).limit(round).forEach(i -> {
            List<String> deleteList = Collections.synchronizedList(new ArrayList<>());
            List<NotifierVehicleLk> addList = Collections.synchronizedList(new ArrayList<>());

            long totalSize = (long)i * max_size;
            vehicleList.stream().skip(totalSize).limit(max_size).parallel().forEach(v -> {
                NotifierVehicleLk lk = new NotifierVehicleLk();
                lk.setId(UtilHelper.getUUID());
                lk.setNotifierId(notifierId);
                lk.setVehicleId(v.getId());
                lk.setCreateBy(createBy);
                lk.setCreateTime(createTime);
                addList.add(lk);
                deleteList.add(v.getId());
            });
            notifierVehicleLkMapper.deleteBatch(notifierId, deleteList);
            notifierVehicleLkMapper.insertBatch(addList);
            log.info("已分配完成, 批次:{}", i);
        });
        log.info("已完成所有查询车辆分配, 耗时(ms):{}", System.currentTimeMillis() - beginTime);
    }

}
