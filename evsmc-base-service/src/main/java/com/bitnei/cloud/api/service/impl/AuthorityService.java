package com.bitnei.cloud.api.service.impl;

import com.bitnei.cloud.api.dao.AuthorityMapper;
import com.bitnei.cloud.api.domain.Authority;
import com.bitnei.cloud.api.model.ApiDetailAuthorityModel;
import com.bitnei.cloud.api.model.AuthorityModel;
import com.bitnei.cloud.api.service.IAuthorityService;
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
import com.github.pagehelper.PageHelper;
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
 * 功能： AuthorityService实现<br>
 * 描述： AuthorityService实现<br>
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
 * <td>2019-01-22 10:39:46</td>
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
@Mybatis(namespace = "com.bitnei.cloud.api.dao.AuthorityMapper")
@RequiredArgsConstructor
public class AuthorityService extends BaseService implements IAuthorityService {

    private final AuthorityMapper authorityMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<Authority> entries = findBySqlId("pagerModel", params);
            List<AuthorityModel> models = new ArrayList();
            for (Authority entry : entries) {
                Authority obj = (Authority) entry;
                models.add(AuthorityModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AuthorityModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                Authority obj = (Authority) entry;
                models.add(AuthorityModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public AuthorityModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");
        params.put("id", id);
        Authority entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AuthorityModel.fromEntry(entry);
    }


    @Override
    public void insert(AuthorityModel model) {

        Authority obj = new Authority();
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
    public void update(AuthorityModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");

        Authority obj = new Authority();
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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }

    /**
     * 根据账号id和接口明细id取消授权
     * @param accountId
     * @param apiDetailId
     * @return
     */
    @Override
    public int delApiAuthority(String accountId, String apiDetailId) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");

        params.put("accountId", accountId);
        params.put("apiDetailId", apiDetailId);
        return authorityMapper.delApiAuthority(params);
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Authority>(this, "pagerModel", params, "module/api/authority/export.xls", "接口授权") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "AUTHORITY" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AuthorityModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AuthorityModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AuthorityModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "AUTHORITY" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AuthorityModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AuthorityModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AuthorityModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public Object getAuthorityApis(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_authority", "auth");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            String accountId = params.get("accountId").toString();
            return authorityMapper.getAuthorityApis(params).stream().peek(it ->
                    it.setIsAuthorize(null != it.getAccountId() && it.getAccountId().equals(accountId)))
                    .collect(Collectors.toList());
        }
        else {
            PageHelper.offsetPage(pagerInfo.getStart(), pagerInfo.getLimit(), false);
            PageHelper.orderBy("auth.create_time desc");
            String accountId = params.get("accountId").toString();
            List<Object> models = authorityMapper.getAuthorityApis(params).stream().peek(it ->
                    it.setIsAuthorize(null != it.getAccountId() && it.getAccountId().equals(accountId)))
                    .collect(Collectors.toList());
            Long count = authorityMapper.countAuthorityApis(params);
            PagerResult pr = new PagerResult();
            pr.setData(Collections.singletonList(models));
            pr.setTotal(count);
            return pr;
        }

    }
}
