package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.common.util.I18nUtil;;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Group;
import com.bitnei.cloud.sys.model.GroupModel;
import com.bitnei.cloud.sys.service.IGroupService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.commons.datatables.PagerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import jodd.util.URLDecoder;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 数据权限组管理<br>
* 描述： 数据权限组管理控制器<br>
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
* <td>2018-11-08 10:40:16</td>
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
@Api(value = "数据权限组管理", description = "数据权限组管理",  tags = {"数据权限组管理"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class GroupController{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="GROUP";
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
    private IGroupService groupService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/groups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        GroupModel group = groupService.get(id);
        return ResultMsg.getResult(group);
    }



    /**
     * 根据组名获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据组名获取详细信息" , notes = "根据组名获取详细信息")
    @ApiImplicitParam(name = "name", value = "组名", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/groups/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        GroupModel group = groupService.getByName(name);
        return ResultMsg.getResult(group);


    }

    /**
     * 根据组名获取对象
     *
     * @return
     */
    @ApiOperation(value = "查询用户默认组" , notes = "查询用户默认组")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/groups/user/{userId}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg getByUserId(@PathVariable String userId){

        GroupModel group = groupService.getByUserId(userId);
        return ResultMsg.getResult(group);


    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/groups")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = groupService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }






    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/group")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) GroupModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        groupService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/groups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) GroupModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        groupService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/groups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = groupService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/groups/export")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        groupService.export(info);
        return ;

    }


    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/groups/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        groupService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/groups/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        groupService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    @Getter
    @Setter
    @ApiModel(value = "UserGroupModel", description = "资源组和用户Model")
    static class UserGroupModel{
        @ApiModelProperty(value = "资源组id")
        @NotEmpty(message = "资源组id不能为空")
        private String groupId;
        @ApiModelProperty(value = "用户列表，多个用逗号分隔")
        @NotEmpty(message = "用户列表不能为空")
        private String userIds;
    }

    @ApiOperation(value = "资源组用户列表" , notes = "根据ID获取资源组分配给用户列表")
    @GetMapping(value = "/groups/groupUsers/{groupId}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getAllUsers(@PathVariable String groupId){

        String[] userIds = groupService.getAllUsers(groupId);
        return ResultMsg.getResult(userIds);
    }

    @ApiOperation(value = "保存资源组用户" , notes = "列表的用户赋予资源组权限")
    @PostMapping(value = "/groups/saveGroupUsers")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg saveGroudUser(@RequestBody @Validated UserGroupModel userGroupModel,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        int result = groupService.saveGroupUsers(userGroupModel.getGroupId(),userGroupModel.getUserIds());
        return ResultMsg.getResult(String.format("保存成功，共增加了%d条记录", result));
    }


    @ApiOperation(value = "移除资源组用户" , notes = "列表的用户取消资源组权限")
    @PostMapping(value = "/groups/removeGroupUser")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg removeGroudUser(@RequestBody @Validated UserGroupModel userGroupModel,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        int result = groupService.removeGroupUsers(userGroupModel.getGroupId(),userGroupModel.getUserIds());

            return ResultMsg.getResult(String.format("删除成功，共删除了%d条记录", result));

    }






}
