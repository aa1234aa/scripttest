package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.DeptModel;
import com.bitnei.cloud.sys.model.UserCopyModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.model.UserNamePasswordModel;
import com.bitnei.cloud.sys.service.IDeptService;
import com.bitnei.cloud.sys.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import jodd.util.URLDecoder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;


/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 用户管理<br>
 * 描述： 用户管理控制器<br>
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
 * <td>2018-11-12 10:33:47</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Api(value = "账号管理", description = "账号管理", tags = {"基础数据-用户基础-账号管理"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀 *
     */
    public static final String BASE_AUTH_CODE = "USER";
    /**
     * 查看 *
     */
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表 *
     */
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页 *
     */
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增 *
     */
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑 *
     */
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除 *
     */
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出 *
     */
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";
    /**
     * 导入 *
     */
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入 *
     */
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新 *
     */
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";
    /**
     * 重置密码 *
     */
    private static final String AUTH_RESET_PWD = BASE_AUTH_CODE + "_RESET_PWD";
    /**
     * 查询用户下组织架构
     */
    private static final String AUTH_GET_DEPT = BASE_AUTH_CODE + "_GET_DEPT";

    /**
     * 复制用户的权限和角色
     */
    private static final String AUTH_COPY_GROUP_ROLE = BASE_AUTH_CODE + "_COPY_GROUP_ROLE";

    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;

    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/users/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        UserModel user = userService.get(id);
        return ResultMsg.getResult(user);
    }

    /**
     * 多条件查询
     * @param pagerInfo 分页对象
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/users")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = userService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param demo1 UserModel
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/user")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UserModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        userService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 更新
     * @param demo1 UserModel
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/users/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UserModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        userService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除
     *
     * @param id ID列表，多个用逗号分隔
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/users/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = userService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 导出
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/users/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        userService.export(info);
        return;
    }

    /**
     * 批量导入
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/users/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {
        userService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/users/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {
        userService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }

    /**
     * 重置密码
     * @param userModel 用户名
     */
    @ApiOperation(value = "重置密码", notes = "重置密码，需传入用户名和重置后的密码")
    @PostMapping(value = "/users/resetPwd")
    @ResponseBody
    @RequiresPermissions(AUTH_RESET_PWD)
    @SLog(action = "重置密码")
    public ResultMsg resetPwd(@RequestBody @Validated({GroupInsert.class}) UserNamePasswordModel userModel) {
        userService.resetPwd(userModel.getUsername(), userModel.getPassword());
        return ResultMsg.getResult("重置成功");
    }


    /**
     * 查询联系人单位下组织架构
     * @param opId 联系人id
     */
    @ApiOperation(value = "获取组织架构", notes = "根据联系人id获取权限内组织架构")
    @PostMapping(value = "/users/queryDeptsByOpid")
    @ResponseBody
    @RequiresPermissions(AUTH_GET_DEPT)
    public ResultMsg queryDeptsByOpid(String opId) {
        List<DeptModel> list = deptService.queryDeptsByOpid(opId);
        return ResultMsg.getResult(list);
    }


    /**
     * 复制权限和角色
     * @param userCopyModel 用户复制
     */
    @ApiOperation(value = "复制权限和角色", notes = "原始用户id和目标用户id")
    @PostMapping(value = "/users/copyGroupAndRole")
    @ResponseBody
    @RequiresPermissions(AUTH_COPY_GROUP_ROLE)
    @SLog(action = "复制权限和角色")
    public ResultMsg copyGroupAndRole(@RequestBody @Validated({GroupInsert.class}) UserCopyModel userCopyModel) {
        userService.copyGroupAndRole(userCopyModel.getSourceUserId(), userCopyModel.getTargetUserId());
        return ResultMsg.getResult("复制权限和角色");
    }


    @ApiOperation(value = "管理员解锁", notes = "管理员解除账号冻结")
    @PostMapping(value = "/users/adminUnFrozenUser/{userId}")
    @ResponseBody
    public ResultMsg unlock(@PathVariable String userId) {
        userService.adminUnfrozenUser(userId);
        return ResultMsg.getResult("解除锁定成功");
    }

    @ApiOperation(value = "管理员冻结账号", notes = "管理员冻结用户账号")
    @PostMapping(value = "/users/adminFrozenUser")
    @ResponseBody
    public ResultMsg lock(@RequestBody @Validated FrozenUserModel frozenUserModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        userService.adminFrozenUser(frozenUserModel.getUserId(), frozenUserModel.getReason());
        return ResultMsg.getResult("账号冻结成功");
    }


    @Setter
    @Getter
    @ApiModel(value = "FrozenUserModel", description = "解除账号冻结Model")
    private static class FrozenUserModel {
        @ApiModelProperty(value = "用户id")
        @NotEmpty(message = "用户id不能为空")
        private String userId;
        @ApiModelProperty(value = "冻结原因")
        @NotEmpty(message = "冻结原因不能为空")
        @Length(min = 2, max = 50, message = "冻结原因长度2-50")
        private String reason;


    }


    /**
     * 开通app权限
     * @param
     */
    @ApiOperation(value = "开通app权限", notes = "开通app权限")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "/users/openApp/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg openAppPermissions(@PathVariable String id) {
        userService.openAppPermissions(id);
        return ResultMsg.getResult("用户APP权限已开通");
    }

    /**
     * 关闭app权限
     * @param id
     * @return
     */
    @ApiOperation(value = "关闭app权限", notes = "关闭app权限")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "/users/turnOffApp/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg turnOffAppPermissions(@PathVariable String id) {
        userService.turnOffAppPermissions(id);
        return ResultMsg.getResult("用户APP权限已关闭");
    }

}
