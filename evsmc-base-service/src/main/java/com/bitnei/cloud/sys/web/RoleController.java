package com.bitnei.cloud.sys.web;

        import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IRoleService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 角色管理<br>
 * 描述： 角色管理控制器<br>
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
 * <td>2018-11-22 10:06:09</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @version 1.0
 *
 * @author chenpeng
 * @since JDK1.8
 */
@Api(value = "角色管理", description = "角色管理",  tags = {"角色管理"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class RoleController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="ROLE";
    /** 查看 **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE +"_DETAIL";
    /** 列表 **/
    public static final String AUTH_LIST = BASE_AUTH_CODE +"_LIST";
    /** 分页 **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE +"_PAGER";
    /** 新增 **/
    public static final String AUTH_ADD = BASE_AUTH_CODE +"_ADD";
    /** 编辑 **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE +"_UPDATE";
    /** 删除 **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE +"_DELETE";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";
    /** 导入 **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE +"_IMPORT";
    /** 批量导入 **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE +"_BATCH_IMPORT";
    /** 批量更新 **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE +"_BATCH_UPDATE";

    @Autowired
    private IRoleService roleService;

    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/roles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        RoleModel role = roleService.get(id);
        return ResultMsg.getResult(role);
    }

    /**
     * 根据角色名称获取对象
     */
    @ApiOperation(value = "根据角色名称获取详细信息" , notes = "根据角色名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "角色名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/roles/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        RoleModel role = roleService.getByName(name);
        return ResultMsg.getResult(role);
    }

    /**
     * 多条件查询
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/roles")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = roleService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     * @param demo1 RoleModel
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/role")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) RoleModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        RoleModel roleModel = roleService.insert(demo1);
        return ResultMsg.getObjectResult(roleModel);
    }

    /**
     * 更新
     * @param demo1 RoleModel
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/roles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) RoleModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        roleService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除
     * @param id ID列表，多个用逗号分隔
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/roles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = roleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/roles/export")
    @RequiresPermissions(AUTH_EXPORT)
    public void export(@RequestBody @Validated PagerInfo info){

        roleService.export(info);

    }


    /**
     * 批量导入
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/roles/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        roleService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/roles/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        roleService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }

    /**
     * 查询关联的账号
     */
    @ApiOperation(value = "查询角色关联的账号" , notes = "根据角色ID查询关联的账号")
    @PostMapping(value = "/roles/queryAssociatedAccount")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public List<UserModel> queryAssociatedAccount(String roleId, String trueName, String loginName, String mobile){
        return roleService.queryAssociatedAccount(roleId,trueName,loginName,mobile);
    }

    /**
     * 查询未关联的账号
     */
    @ApiOperation(value = "查询角色未关联的账号" , notes = "根据角色ID查询未关联的账号")
    @PostMapping(value = "/roles/queryUnassociatedAccount")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public List<UserModel> queryUnassociatedAccount(String roleId, String trueName, String loginName, String mobile){
        return roleService.queryUnassociatedAccount(roleId,trueName,loginName,mobile);
    }

    /**
     * 关联账号
     */
    @Getter
    @Setter
    @ApiModel(value = "BindUser", description = "关联账号Model")
    private static class BindUser {
        @ApiModelProperty(value = "角色id")
        private String roleId;
        @ApiModelProperty(value = "用户列表")
        private List<String> userIds;
    }
    @ApiOperation(value = "关联账号" , notes = "根据角色ID关联账号")
    @PostMapping(value = "/roles/bindUser")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg bindAccount( @RequestBody BindUser bindUser){
        roleService.bindAccount(bindUser.roleId,bindUser.userIds);
        return ResultMsg.getResult("关联账号完成");
    }

    /**
     * 复制角色
     */
    @Getter
    @Setter
    @ApiModel(value = "RoleCopyModel", description = "复制角色Model")
    private static class RoleCopyModel{
        @ApiModelProperty(value = "源角色id")
        private String sourceRoleId;
        @ApiModelProperty(value = "目标角色id")
        private String targetRoleId;
    }
    @ApiOperation(value = "复制角色" , notes = "复制角色")
    @PostMapping(value = "/roles/copyRole")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg copyRole(@RequestBody @Validated({GroupInsert.class}) RoleCopyModel roleCopyModel){
        roleService.copyRole(roleCopyModel.sourceRoleId,roleCopyModel.targetRoleId);
        return ResultMsg.getResult("复制角色完成");
    }

    /**
     * 查询角色模块数据项配置状态
     *
     * @param
     */
//    @ApiOperation(value = "查询角色模块数据项配置状态" , notes = "查询角色模块数据项配置状态")
//    @PostMapping(value = "/roles/queryRoleModuleDataItem")
//    @ResponseBody
//    public List<ModuleDataItemModel> queryRoleModuleDataItemPermission(String roleId,String moduleId){
//        List<ModuleDataItemModel>  models = roleService.queryRoleModuleDataItemPermission(roleId,moduleId);
//        return models;
//    }

    /**
     * 设置角色数据项权限，是否脱敏，是否隐藏
     *
     * @param conf<module_data_id,"isHidden,isSensitive">
     *
     */
//    @ApiOperation(value = "设置角色列权限" , notes = "设置角色数据项权限，是否脱敏，是否隐藏")
//    @PostMapping(value = "/roles/updateRoleModuleDateItem")
//    @ResponseBody
//    public ResultMsg updateRoleModuleDateItemPermission(String roleId,String moduleId,@RequestBody Map<String,String> conf){
//        roleService.updateRoleModuleDateItemPermission(roleId,moduleId,conf);
//        return ResultMsg.getResult("设置角色列权限完成");
//    }
}
