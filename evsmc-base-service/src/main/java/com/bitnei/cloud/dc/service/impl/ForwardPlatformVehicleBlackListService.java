package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.dc.dao.ForwardPlatformVehicleBlackListMapper;
import com.bitnei.cloud.dc.dao.ForwardRuleMapper;
import com.bitnei.cloud.dc.domain.ForwardPlatformVehicleBlackList;
import com.bitnei.cloud.dc.domain.ForwardRule;
import com.bitnei.cloud.dc.model.ForwardPlatformVehicleBlackListModel;
import com.bitnei.cloud.dc.service.IForwardPlatformVehicleBlackListService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ForwardPlatformVehicleBlackListService实现<br>
 * 描述： ForwardPlatformVehicleBlackListService实现<br>
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
 * <td>2019-07-03 14:48:54</td>
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
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.ForwardPlatformVehicleBlackListMapper")
public class ForwardPlatformVehicleBlackListService extends BaseService implements IForwardPlatformVehicleBlackListService {

    @Resource
    private ForwardPlatformVehicleBlackListMapper forwardPlatformVehicleBlackListMapper;

    @Resource
    private ForwardRuleMapper forwardRuleMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<ForwardPlatformVehicleBlackList> entries = findBySqlId("pagerModel", params);
            List<ForwardPlatformVehicleBlackListModel> models = new ArrayList();
            for (ForwardPlatformVehicleBlackList entry : entries) {
                ForwardPlatformVehicleBlackList obj = entry;
                models.add(ForwardPlatformVehicleBlackListModel.fromEntry(obj));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<ForwardPlatformVehicleBlackListModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                ForwardPlatformVehicleBlackList obj = (ForwardPlatformVehicleBlackList) entry;
                models.add(ForwardPlatformVehicleBlackListModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public ForwardPlatformVehicleBlackListModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
        params.put("id", id);
        ForwardPlatformVehicleBlackList entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return ForwardPlatformVehicleBlackListModel.fromEntry(entry);
    }

    @Override
    public void insert(ForwardPlatformVehicleBlackListModel model) {
        ForwardPlatformVehicleBlackList obj = new ForwardPlatformVehicleBlackList();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(ForwardPlatformVehicleBlackListModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");

        ForwardPlatformVehicleBlackList obj = new ForwardPlatformVehicleBlackList();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("dc_forward_platform_vehicle_black_list", "dfpvbl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<ForwardPlatformVehicleBlackList>(this, "pagerModel", params, "dc/res/forwardPlatformVehicleBlackList/export.xls", "转发平台车辆黑名单") {
        }.work();

        return;


    }

    @Override
    public void batchInsert(ForwardPlatformVehicleBlackListModel model) {
        String[] vehIdsArr = model.getVehicleIds().split(",");
        List<ForwardPlatformVehicleBlackList> list = new ArrayList<>();
        for (String vehId : vehIdsArr) {
            ForwardPlatformVehicleBlackList obj = new ForwardPlatformVehicleBlackList();
            obj.setId(UtilHelper.getUUID());
            obj.setPlatformId(model.getPlatformId());
            obj.setVehicleId(vehId);
            obj.setCreateBy(ServletUtil.getCurrentUser());
            obj.setCreateTime(DateUtil.getNow());
            list.add(obj);
        }
        forwardPlatformVehicleBlackListMapper.batchInsert(list);
    }
}
