package com.bitnei.cloud.api.service.impl;

import com.bitnei.cloud.api.dao.AccountMapper;
import com.bitnei.cloud.api.domain.Account;
import com.bitnei.cloud.api.domain.ApiDetail;
import com.bitnei.cloud.api.domain.PushDetail;
import com.bitnei.cloud.api.model.AccountAuthorizationsDTO;
import com.bitnei.cloud.api.model.AccountModel;
import com.bitnei.cloud.api.model.ApiDetailModel;
import com.bitnei.cloud.api.model.PushDetailModel;
import com.bitnei.cloud.api.service.IAccountService;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.JwtUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.UserMapper;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import jodd.util.URLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AccountService实现<br>
 * 描述： AccountService实现<br>
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
 * <td>2019-01-15 16:35:26</td>
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
@Mybatis(namespace = "com.bitnei.cloud.api.dao.AccountMapper")
@RequiredArgsConstructor
public class AccountService extends BaseService implements IAccountService {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Resource
    private UserMapper userMapper;

    private final AccountMapper accountMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {


        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<Account> entries = findBySqlId("pagerModel", params);
            List<AccountModel> models = new ArrayList();
            for (Account entry : entries) {
                Account obj = (Account) entry;
                models.add(AccountModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AccountModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                Account obj = (Account) entry;
                models.add(AccountModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public AccountModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");
        params.put("id", id);
        Account entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AccountModel.fromEntry(entry);
    }


    @Override
    public void insert(AccountModel model) {

        Account obj = new Account();
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
    public void update(AccountModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");

        Account obj = new Account();
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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Account>(this, "pagerModel", params, "module/api/account/export.xls", "授权账号") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ACCOUNT" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AccountModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AccountModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AccountModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ACCOUNT" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AccountModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AccountModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AccountModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public AccountModel findByToken(String token) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_account", "acc");
        params.put("token", token);
        Account entry = unique("findByToken", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AccountModel.fromEntry(entry);
    }

    @Override
    public AccountAuthorizationsDTO getAuthorizations(String applicationCode, String token) {
        token = URLDecoder.decode(token);
        AccountModel accountModel = findByToken(token);
        AccountAuthorizationsDTO dto = new AccountAuthorizationsDTO();
        List<ApiDetail> apiDetails = accountMapper.getApiAuthorizations(accountModel.getId());
        List<PushDetail> pushDetails = accountMapper.getPushAuthorizations(accountModel.getId());
        List<ApiDetailModel> apiDetailModels = apiDetails.stream()
                .filter(it -> it.getApplicationCode().equals(applicationCode))
                .map(ApiDetailModel::fromEntry).collect(Collectors.toList());
        List<PushDetailModel> pushDetailModels = pushDetails.stream()
                .filter(it -> it.getApplicationCode().equals(applicationCode))
                .map(PushDetailModel::fromEntry).collect(Collectors.toList());
        BeanUtils.copyProperties(accountModel, dto);
        dto.setAccountId(accountModel.getId());
        dto.setAuthorities(apiDetailModels);
        dto.setPushes(pushDetailModels);
        return dto;
    }

    @Override
    public TokenInfo grantUserToken(String userId){

        User user = userMapper.personalCenterGetUser(userId);
        if(user!=null){
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(user.getId());
            tokenInfo.setUsername(user.getUsername());
            tokenInfo.setIsAdmin(user.getIsAllPermissions());
            tokenInfo.setRoleId(user.getDefRoleId());
            tokenInfo.setCreateTime(System.currentTimeMillis());
            //令牌有效时间为一天
            tokenInfo.setTimeout(1440 * 1000 * 60);

           // String token = JwtUtil.createJWT(1000 * 60 * 60 * 24 * 10, tokenInfo, jwtSecret);
            return tokenInfo;
        }
        return null;
    }
}
