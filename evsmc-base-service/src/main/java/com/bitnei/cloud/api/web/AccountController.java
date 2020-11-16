package com.bitnei.cloud.api.web;

import com.bitnei.cloud.api.model.*;
import com.bitnei.cloud.api.service.IAccountPushLkService;
import com.bitnei.cloud.api.service.IAccountService;
import com.bitnei.cloud.api.service.IAuthorityService;
import com.bitnei.cloud.api.service.IPushDetailService;
import com.bitnei.cloud.api.utils.AesUtils;
import com.bitnei.cloud.api.utils.RsaUtils;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.service.impl.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jodd.util.RandomString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 授权账号<br>
 * 描述： 授权账号控制器<br>
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
@Api(value = "开放平台-授权账号", description = "开放平台-授权账号", tags = {"开放平台-授权账号"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/api")
@RequiredArgsConstructor
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "ACCOUNT";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页
     **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增
     **/
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑
     **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除
     **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出
     **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";
    /**
     * 导入
     **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入
     **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新
     **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";

    private final IAccountService accountService;
    private final IAuthorityService authorityService;
    private final IAccountPushLkService accountPushLkService;
    private final UserService userService;
    private final IPushDetailService pushDetailService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/accounts/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        AccountModel account = accountService.get(id);
        return ResultMsg.getResult(account);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/accounts")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = accountService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/account")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) AccountModel demo1,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        ResultMsg error = validateAccountModel(demo1);
        if (null != error) {
            return error;
        }

        accountService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    private ResultMsg validateAccountModel(AccountModel accountModel) {

        //0:AES 1:RSA
        if (accountModel.getEncryptType().equals(0)) {

            if (StringUtils.isNotEmpty(accountModel.getAppKey()) &&
                    (accountModel.getAppKey().length() < 2 || accountModel.getAppKey().length() > 1024)) {
                return ResultMsg.getResult(null, "AES密钥长度为2-1024个字符", -1);
            }
        } else if (accountModel.getEncryptType().equals(1)) {

            if (StringUtils.isNotEmpty(accountModel.getRsaPriKey()) &&
                    (accountModel.getRsaPriKey().length() < 2 || accountModel.getRsaPriKey().length() > 1024)) {
                return ResultMsg.getResult(null, "RSA私钥长度为2-1024个字符", -1);
            }

            if (StringUtils.isNotEmpty(accountModel.getRsaPubKey()) &&
                    (accountModel.getRsaPubKey().length() < 2 || accountModel.getRsaPubKey().length() > 1024)) {
                return ResultMsg.getResult(null, "RSA公钥长度为2-1024个字符", -1);
            }
        }
        return null;
    }

    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/accounts/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) AccountModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        ResultMsg error = validateAccountModel(demo1);
        if (null != error) {
            return error;
        }

        accountService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/accounts/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = accountService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }

    /**
     * 根据类型获取token、key或AES秘钥
     *
     * @return
     */
    @ApiOperation(value = "根据类型获取token、key或AES秘钥", notes = "根据类型获取token、key或AES秘钥")
    @ApiImplicitParam(name = "type", value = "类型 0:令牌 1:签名密钥 2:AES密钥",
            dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/accounts/getEncodeInfo")
    @ResponseBody
    @RequiresAuthentication
    public StringRespModel getEncodeInfo(@RequestParam String type) {

        String result = "";
        switch (type) {
            //token
            case "0":
                result = RandomString.getInstance().randomBase64(32);
                break;
            //sign_secret签名秘钥，生成方式待定
            case "1":
                result = RandomString.getInstance().randomBase64(10);
                break;
            //AES秘钥
            case "2":
                result = AesUtils.getAesCode(128);
                break;
            default:
                break;
        }
        return StringRespModel.build(result);
    }

    /**
     * 获取rsa秘钥对
     *
     * @return
     */
    @ApiOperation(value = "获取rsa秘钥对", notes = "获取rsa秘钥对")
    @GetMapping(value = "/accounts/getRsaKeyPair")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg getRsaKeyPair() {

        return ResultMsg.getResult(RsaUtils.getRsaKeyPair());
    }

    @ApiOperation(value = "获取接口授权列表，参数accountId，apiName，applicationName，isAuthorize（0为未授权，1为已授权）",
            notes = "获取接口授权列表，参数accountId，apiName，applicationName，isAuthorize（0为未授权，1为已授权）")
    @PostMapping(value = "/authorityApis")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg getAuthorityApis(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        Object result = authorityService.getAuthorityApis(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "获取已授权的推送列表，参数accountId，pushName，applicationName",
            notes = "获取已授权的推送列表，参数accountId，pushName，applicationName")
    @PostMapping(value = "/authorityPushes")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg getAuthorityPushes(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = accountPushLkService.getAuthorityPushes(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 接口授权
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "接口授权", notes = "接口授权")
    @PostMapping(value = "/addApiAuthority")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addApiAuthority(@RequestBody @Validated({GroupInsert.class}) AuthorityModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        authorityService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 根据账号id和接口明细id取消授权
     *
     * @param accountId
     * @param apiDetailId
     * @return
     */
    @ApiOperation(value = "根据账号id和接口明细id取消授权", notes = "根据账号id和接口明细id取消授权")
    @ApiImplicitParams({@ApiImplicitParam(name = "accountId", value = "账号id", required = true, dataType = "String",
            paramType = "path"), @ApiImplicitParam(name = "apiDetailId", value = "接口明细id", required = true,
            dataType = "String", paramType = "path")})
    @DeleteMapping(value = "/delApiAuthority/{accountId}/{apiDetailId}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg delApiAuthority(@PathVariable String accountId, @PathVariable String apiDetailId) {

        int count = authorityService.delApiAuthority(accountId, apiDetailId);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 新增推送配置
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增推送配置", notes = "新增推送配置")
    @PostMapping(value = "/addAccountPushLk")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAccountPushLk(@RequestBody @Validated({GroupInsert.class}) AccountPushLkModel demo1,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        accountPushLkService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "编辑推送配置", notes = "编辑推送配置")
    @PutMapping(value = "/updateAccountPushLk/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg updateAccountPushLk(@RequestBody @Validated({GroupUpdate.class}) AccountPushLkModel demo1,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        accountPushLkService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除推送配置
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除推送配置", notes = "删除推送配置")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delAccountPushLk/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg delAccountPushLk(@PathVariable String id) {

        int count = accountPushLkService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "多条件查询")
    @PostMapping(value = "/users")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg getUsers(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = userService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 获取推送项列表
     *
     * @return
     */
    @ApiOperation(value = "获取推送项列表，查询参数：name（推送名称），applicationName（应用服务名称）",
            notes = "获取推送项列表，查询参数：name（推送名称），applicationName（应用服务名称）")
    @PostMapping(value = "/getPushDetails")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg getPushDetails(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = pushDetailService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据应用唯一编码和账号token获取授权信息
     *
     * @return
     */
    @ApiOperation(value = "根据应用唯一编码和账号token获取授权信息", notes = "根据应用唯一编码和账号token获取授权信息")
    @GetMapping(value = "/applications/authorizations")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg getAuthorizations(@RequestParam String applicationCode,
                                       @RequestParam String token) {

        AccountAuthorizationsDTO dto = accountService.getAuthorizations(applicationCode, token);
        return ResultMsg.getResult(dto);
    }

    /**
     * 根据用户id赋予数据权限令牌
     *
     * @return
     */
    @ApiOperation(value = "网关需要的不通过登录而赋予的用户令牌", notes = "根据用户id赋予数据权限令牌")
    @GetMapping(value = "/users/token/{id}")
    @ResponseBody
    public ResultMsg getDataAuthToken(@PathVariable String id){
        TokenInfo token = accountService.grantUserToken(id);
        return ResultMsg.getResult(token);
    }
}
