package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.sys.domain.UserPersonalSetting;
import com.bitnei.cloud.sys.model.HiddenColumnModel;
import com.bitnei.cloud.sys.model.PersonalCenterInfoModel;
import com.bitnei.cloud.sys.model.UserFaultResponseModeSettingModel;
import com.bitnei.cloud.sys.service.IPersonalCenterService;
import com.bitnei.cloud.sys.service.IUploadFileService;
import com.bitnei.cloud.sys.service.IUserFaultResponseModeSettingService;
import com.bitnei.cloud.sys.service.IUserService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 核心资源类型<br>
 * 描述： 核心资源类型控制器<br>
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
 * <td>2018-11-05 09:32:22</td>
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
@Api(value = "个人中心", description = "个人中心", tags = {"个人中心"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class PersonalCenterController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "PERSONALCENTER";
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

    @Autowired
    private IPersonalCenterService personalCenterService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserFaultResponseModeSettingService userFaultResponseModeSettingService;

    @Getter
    @Setter
    @ApiModel(value = "UserChangePswModel", description = "用户更改密码")
    static class UserChangePswModel {
        @ApiModelProperty(value = "原密码")
        @NotEmpty(message = "原密码不能为空")
        private String oldPassword;

        @ApiModelProperty(value = "新密码")
        @NotEmpty(message = "新密码不能为空")
        private String newPassword;

        @ApiModelProperty(value = "确认密码")
        @NotEmpty(message = "确认密码不能为空")
        private String confirmPassword;
    }

    @ApiOperation(value = "用户登录相关配置详细信息", notes = "用户登录相关配置详细信息")
    @GetMapping(value = "/personalCenter/generalSetting/{userId}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String userId) {

        UserPersonalSetting userPersonalSetting = personalCenterService.getUserSetting(userId);
        return ResultMsg.getResult(userPersonalSetting);
    }


    /**
     * 获取报警铃声列表
     *
     * @return
     */
    @ApiOperation(value = "报警铃声列表", notes = "报警铃声列表")
    @PostMapping(value = "/personalCenter/alarmList")
    @ResponseBody
    public ResultMsg alarmList() {

        List<Map<String, Object>> map = personalCenterService.alarmRingList();

        return ResultMsg.getResult(map);
    }


    @ApiOperation(value = "获取用户通用报警设置", notes = "获取用户通用报警设置")
    @GetMapping(value = "/personalCenter/userAlarmRing")
    @ResponseBody
    public ResultMsg allowRing() {

        Map<String, Object> ringMap = personalCenterService.getUserAlarmRing();

        return ResultMsg.getResult(ringMap);

    }

    @ApiOperation(value = "用户报警通知系统声音启用禁用设置", notes = "用户报警通知短信启用禁用设置(1是启用，0是禁用)")
    @PutMapping(value = "/personalCenter/userAlarmRing/allowRing/{actionId}")
    @ResponseBody
    public ResultMsg isAllowRing(@PathVariable int actionId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("isAllowRing", actionId);
        personalCenterService.setRingAllowed(params);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));

    }

    @ApiOperation(value = "用户报警通知弹窗启用禁用设置", notes = "用户报警通知短信启用禁用设置(1是启用，0是禁用)")
    @PutMapping(value = "/personalCenter/userAlarmRing/allowDialog/{actionId}")
    @ResponseBody
    public ResultMsg isAllowDialog(@PathVariable int actionId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("isAllowDialog", actionId);
        personalCenterService.setRingAllowed(params);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    @ApiOperation(value = "用户报警通知短信启用禁用设置", notes = "用户报警通知短信启用禁用设置(1是启用，0是禁用)")
    @PutMapping(value = "/personalCenter/userAlarmRing/allowSms/{actionId}")
    @ResponseBody
    public ResultMsg isAllowSms(@PathVariable int actionId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("isAllowSms", actionId);
        personalCenterService.setRingAllowed(params);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));

    }

    @ApiOperation(value = "用户报警铃声个性设置", notes = "用户报警铃声个性设置")
    @PutMapping(value = "/personalCenter/userAlarmRing/setRing/{ringId}")
    @ResponseBody
    public ResultMsg setAlarmRing(@PathVariable String ringId) {

        personalCenterService.setUerAlarmRing(ringId);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));

    }


    @ApiOperation(value = "获取用户登录保持时长", notes = "获取用户登录保持时长(单位:分钟)")
    @GetMapping(value = "/personalCenter/userKeepOnlineTime")
    @ResponseBody
    public ResultMsg getUserKeepConnTime() {

        Map<String, Object> time = personalCenterService.getKeepConnTime();
        return ResultMsg.getResult(time);

    }

    @ApiOperation(value = "设置用户登录保持时长", notes = "设置用户登录保持时长(单位:分钟)")
    @PutMapping(value = "/personalCenter/userKeepOnlineTime/{time}")
    @ResponseBody
    public ResultMsg setUserKeepConnTime(@PathVariable String time) {

        personalCenterService.updateKeepConnTime(time);
        return ResultMsg.getResult("操作成功");

    }

    @ApiOperation(value = "个人中心更改密码", notes = "个人中心更改密码")
    @PostMapping(value = "/personalCenter/userChangePassword")
    @ResponseBody
    public ResultMsg changePassword(@RequestBody @Validated UserChangePswModel userChangePswModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        personalCenterService.changePassword(userChangePswModel.oldPassword, userChangePswModel.newPassword, userChangePswModel.confirmPassword);
        return ResultMsg.getResult("密码修改成功");

    }

    @ApiOperation(value = "当前用户角色列表", notes = "当前用户角色列表")
    @GetMapping(value = "/personalCenter/userRoleList")
    @ResponseBody
    public ResultMsg roleList() {

        List<Map<String, String>> role = personalCenterService.getUserRoleList();
        return ResultMsg.getResult(role);
    }


    @Getter
    @Setter
    @ApiModel(value = "UserChangeRoleModel", description = "用户切换角色")
    static class UserChangeRoleModel {
        @ApiModelProperty(value = "是否设置为默认角色（1:是;0:否）")
        @NotNull(message = "是否设置为默认角色不能为空")
        private Integer isDefaultRole;
        @ApiModelProperty(value = "角色id")
        @NotEmpty(message = "角色Id不能为空")
        private String roleId;
        @ApiModelProperty(value = "密码")
        @NotEmpty(message = "密码不能为空")
        private String password;
    }

    @ApiOperation(value = "用户切换角色", notes = "用户切换")
    @PutMapping(value = "/personalCenter/userChangeRole")
    @ResponseBody
    public ResultMsg changeRole(@RequestBody @Validated UserChangeRoleModel userChangeRoleModel, BindingResult bindingResult) {

        personalCenterService.changRole(userChangeRoleModel.getIsDefaultRole(), userChangeRoleModel.getRoleId(), userChangeRoleModel.getPassword());
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    @ApiOperation(value = "查看个人资料", notes = "查看个人资料")
    @GetMapping(value = "/personalCenter/userInfo")
    @ResponseBody
    public ResultMsg userInfo() {

        Map<String, Object> userInfo = personalCenterService.userInfo();
        return ResultMsg.getResult(userInfo);
    }


    @ApiOperation(value = "更新个人资料", notes = "更新个人资料")
    @PutMapping(value = "/personalCenter/updateInfo/")
    @ResponseBody
    public ResultMsg updateInfo(@RequestBody @Validated PersonalCenterInfoModel personalCenterInfoModel, BindingResult bindingResult) {

        //  personalCenterService.updateInfo(infoId,newInfo);
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        personalCenterService.updateInfo(personalCenterInfoModel);

        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));

    }

    @Getter
    @Setter
    @ApiModel(value = "UserSuggestionModel", description = "用户意见反馈Model")
    static class UserSuggestionModel {
        @ApiModelProperty(value = "收件人邮箱")
        @NotEmpty(message = "收件邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String targetEmail;
        @ApiModelProperty(value = "意见内容")
        @NotEmpty(message = "意见内容不能为空")
        private String suggestionDetail;
        @ApiModelProperty(value = "图片附件id,多个id用英文逗号分隔")
        @Length(max = 100, message = "附件图片最多3张")
        private String attachmentIds;
    }

    @ApiOperation(value = "用户意见反馈", notes = "用户意见反馈")
    @PostMapping(value = "/personalCenter/userSuggestion/")
    @ResponseBody
    public ResultMsg userSuggestion(@RequestBody @Validated UserSuggestionModel userSuggestionModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        personalCenterService.userSuggestionSubmit(userSuggestionModel.targetEmail, userSuggestionModel.suggestionDetail, userSuggestionModel.getAttachmentIds());
        return ResultMsg.getResult("发送成功");

    }


    @ApiOperation(value = "保存隐藏列设置", notes = "保存隐藏列设置")
    @PutMapping(value = "/personalCenter/hiddenColumns")
    @ResponseBody
    public ResultMsg updateInfo(@RequestBody @Validated HiddenColumnModel hiddenColumnModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        personalCenterService.saveHiddenColumn(hiddenColumnModel);

        return ResultMsg.getResult("保存成功");

    }


    @ApiOperation(value = "获取用户隐藏列配置", notes = "获取用户隐藏列配置")
    @GetMapping(value = "/personalCenter/hiddenColumns")
    @ResponseBody
    public ResultMsg hiddenColumns() {
        Map<String, String> configMap = personalCenterService.getAllHiddenConfig();
        return ResultMsg.getObjectResult(configMap);
    }


    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传头像接口", notes = "上传头像接口")
    @PostMapping(value = "/personalCenter/uploadPhoto")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg upload(@RequestParam("file") MultipartFile file) {
        String id = uploadFileService.uploadFile(file);
        userService.resetPhoto(ServletUtil.getCurrentTokenInfo().getUserId(), id);
        return ResultMsg.getResult(id, "上传头像成功", 200);
    }

    @ApiOperation(value = "获取用户故障响应方式设置", notes = "获取用户故障响应方式设置")
    @GetMapping(value = "/personalCenter/faultResponseMode")
    @ResponseBody
    public ResultMsg faultResponseMode() {
        UserFaultResponseModeSettingModel model = userFaultResponseModeSettingService.getByUserId();
        return ResultMsg.getResult(model);

    }

    /**
     * 用户故障响应方式设置
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "用户故障响应方式设置", notes = "用户故障响应方式设置")
    @PostMapping(value = "/personalCenter/faultResponseMode")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UserFaultResponseModeSettingModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        userFaultResponseModeSettingService.setting(model);
        return ResultMsg.getResult("设置成功");
    }

}
