package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.FilingRecord;
import com.bitnei.cloud.sys.model.FilingRecordModel;
import com.bitnei.cloud.sys.service.IFilingRecordService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： FilingRecordService实现<br>
 * 描述： FilingRecordService实现<br>
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
 * <td>2019-07-24 16:20:42</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.FilingRecordMapper")
public class FilingRecordService extends BaseService implements IFilingRecordService {

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_filing_record", "sfr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<FilingRecord> entries = findBySqlId("pagerModel", params);
            List<FilingRecordModel> models = Lists.newArrayList();
            for (FilingRecord entry : entries) {
                models.add(FilingRecordModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<FilingRecordModel> models = Lists.newArrayList();
            for (Object entry : pr.getData()) {
                FilingRecord obj = (FilingRecord) entry;
                models.add(FilingRecordModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public FilingRecordModel get(String id) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_filing_record", "sfr");
        params.put("id", id);
        FilingRecord entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return FilingRecordModel.fromEntry(entry);
    }

    @Override
    public FilingRecordModel getByFromId(String fromId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_filing_record", "sfr");
        params.put("fromId", fromId);
        params.put("fromStatus", Constant.TrueAndFalse.TRUE);
        FilingRecord entry = unique("findByFromId", params);
        return FilingRecordModel.fromEntry(entry);
    }

    @Override
    public void insert(FilingRecordModel model) {

        FilingRecord obj = new FilingRecord();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(FilingRecordModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_filing_record", "sfr");
        FilingRecord obj = new FilingRecord();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_filing_record", "sfr");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }





}
