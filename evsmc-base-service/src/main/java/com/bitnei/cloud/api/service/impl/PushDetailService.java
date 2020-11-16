package com.bitnei.cloud.api.service.impl;

import com.bitnei.cloud.api.dao.PushDetailMapper;
import com.bitnei.cloud.api.domain.PushDetail;
import com.bitnei.cloud.api.model.PushDetailModel;
import com.bitnei.cloud.api.service.IPushDetailService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： PushDetailService实现<br>
 * 描述： PushDetailService实现<br>
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
 * <td>2019-01-15 17:06:43</td>
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
@Mybatis(namespace = "com.bitnei.cloud.api.dao.PushDetailMapper")
@RequiredArgsConstructor
public class PushDetailService extends BaseService implements IPushDetailService {

    private final PushDetailMapper pushDetailMapper;

    @Override
    public Object findByApplicationCode(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<PushDetail> entries = findBySqlId("findByApplicationCode", params);
            List<PushDetailModel> models = new ArrayList();
            for (PushDetail entry : entries) {
                PushDetail obj = (PushDetail) entry;
                models.add(PushDetailModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findByApplicationCode", params,
                    pagerInfo.getStart(), pagerInfo.getLimit());

            List<PushDetailModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                PushDetail obj = (PushDetail) entry;
                models.add(PushDetailModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<PushDetail> entries = findBySqlId("pagerModel", params);
            List<PushDetailModel> models = new ArrayList();
            for (PushDetail entry : entries) {
                PushDetail obj = (PushDetail) entry;
                models.add(PushDetailModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PushDetailModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                PushDetail obj = (PushDetail) entry;
                models.add(PushDetailModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public PushDetailModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.put("id", id);
        PushDetail entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return PushDetailModel.fromEntry(entry);
    }

    @Override
    public PushDetailModel getByApplicationCodeAndCode(String applicationCode, String code) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.put("applicationCode", applicationCode);
        params.put("code", code);
        PushDetail entry = unique("getByApplicationCodeAndCode", params);
        if (entry == null) {
            return null;
        }
        return PushDetailModel.fromEntry(entry);
    }

    @Override
    public void insert(PushDetailModel model) {

        PushDetail obj = new PushDetail();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(PushDetailModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");

        PushDetail obj = new PushDetail();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<PushDetail>(this, "pagerModel", params, "module/api/pushDetail/export.xls", "推送明细") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "PUSHDETAIL" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<PushDetailModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PushDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PushDetailModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "PUSHDETAIL" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<PushDetailModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PushDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PushDetailModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public List<PushDetailModel> getAllByApplicationCode(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_detail", "push");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        return pushDetailMapper.getAllByApplicationCode(params).stream()
                .map(PushDetailModel::fromEntry).collect(Collectors.toList());
    }

    @Override
    public List<PushDetailModel> getByIds(List<String> ids) {
        return pushDetailMapper.getByIds(ids).stream()
                .map(PushDetailModel::fromEntry).collect(Collectors.toList());
    }
}
