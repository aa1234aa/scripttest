package com.bitnei.cloud.sms.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sms.dao.SmsTaskItemMapper;
import com.bitnei.cloud.sms.domain.SmsTaskItem;
import com.bitnei.cloud.sms.domain.SmsTaskItemDetail;
import com.bitnei.cloud.sms.domain.SmsVehicle;
import com.bitnei.cloud.sms.model.SmsTaskItemDetailModel;
import com.bitnei.cloud.sms.model.SmsTaskItemModel;
import com.bitnei.cloud.sms.model.SmsVehicleModel;
import com.bitnei.cloud.sms.service.ISmsTaskItemService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.sys.dao.OwnerPeopleMapper;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import org.springframework.beans.BeanUtils;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bitnei.commons.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： SmsTaskItemService实现<br>
 * 描述： SmsTaskItemService实现<br>
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
 * <td>2019-08-17 15:45:24</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sms.dao.SmsTaskItemMapper")
public class SmsTaskItemService extends BaseService implements ISmsTaskItemService {

    @Resource
    private SmsTaskItemMapper smsTaskItemMapper;

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private OwnerPeopleMapper ownerPeopleMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<SmsTaskItem> entries = findBySqlId("pagerModel", params);
            List<SmsTaskItemModel> models = new ArrayList();
            for (SmsTaskItem entry : entries) {
                SmsTaskItem obj = (SmsTaskItem) entry;
                SmsTaskItemModel model = SmsTaskItemModel.fromEntry(obj);
                setConsignee(obj, model);
                models.add(model);
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<SmsTaskItemModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                SmsTaskItem obj = (SmsTaskItem) entry;
                SmsTaskItemModel model = SmsTaskItemModel.fromEntry(obj);
                setConsignee(obj, model);
                if (obj.getReceiverType() != null && obj.getReceiverType().intValue() == 1) {
                    OwnerPeople owner = ownerPeopleMapper.findOwnerById(obj.getReceiverId());
                    model.setUnitName(null != owner ? owner.getUnitName() : "");
                }
                models.add(model);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private void setConsignee(SmsTaskItem item, SmsTaskItemModel model) {
        if (item.getServiceType().intValue() == 1) {
            model.setConsignee(item.getReceiver());
        } else {
            model.setConsignee(item.getVin());
        }
    }


    @Override
    public SmsTaskItemDetailModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");
        params.put("id", id);
        SmsTaskItem entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }

        SmsTaskItemDetailModel smsTaskItemDetailModel = null;
        if (entry.getServiceType().intValue() != 1) {
            SmsTaskItemDetail smsTaskItemDetail = smsTaskItemMapper.getSmsTaskItemDetail(id);
            smsTaskItemDetailModel = new SmsTaskItemDetailModel();
            BeanUtils.copyProperties(smsTaskItemDetail, smsTaskItemDetailModel);

            if (entry.getServiceType().intValue() != 3) {
                //实时故障

                //历史故障

                smsTaskItemDetailModel.setFaultBeginTime(DateUtil.getNow());
            }

        } else {
            smsTaskItemDetailModel = new SmsTaskItemDetailModel();
            BeanUtils.copyProperties(entry, smsTaskItemDetailModel);
        }
        if (entry.getServiceType()!=null && entry.getServiceType().intValue() == 2) {
            //车辆信息
            params = new HashMap<>();
            params.put("id", entry.getVehicleId());
            Vehicle vehicle = vehicleMapper.findById(params);
            if (null != vehicle) {
                smsTaskItemDetailModel.setLicensePlate(vehicle.getLicensePlate());
            }
        }
        return smsTaskItemDetailModel;
    }


    @Override
    public void insert(SmsTaskItemModel model) {

        SmsTaskItem obj = new SmsTaskItem();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(SmsTaskItemModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");

        SmsTaskItem obj = new SmsTaskItem();
        BeanUtils.copyProperties(model, obj);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<SmsTaskItem>(this, "pagerModel", params, "sms/res/smsTaskItems/export.xls", "短信下发任务明细") {
            @Override
            public Object process(SmsTaskItem entity) {
                SmsTaskItemModel model = SmsTaskItemModel.fromEntry(entity);
                setConsignee(entity, model);
                return model;
            }
        }.work();

        return;
    }

    @Override
    public Object findSmsVehicle(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sms_task_item", "gsti");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        PagerResult pr = findPagerModel("findSmsVehicle", params, pagerInfo.getStart(), pagerInfo.getLimit());
        List<SmsVehicleModel> models = new ArrayList();
        for (Object entry : pr.getData()) {
            SmsVehicle obj = (SmsVehicle) entry;
            models.add(SmsVehicleModel.fromEntry(obj));
        }
        pr.setData(Collections.singletonList(models));
        return pr;
    }
}
