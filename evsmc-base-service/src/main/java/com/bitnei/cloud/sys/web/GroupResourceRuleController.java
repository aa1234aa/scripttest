package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.GroupResourceRule;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.IGroupResourceRuleService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.commons.datatables.PagerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 数据权限组资源配置<br>
* 描述： 数据权限组资源配置控制器<br>
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
* <td>2019-01-22 16:30:51</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Api(value = "数据权限组资源配置", description = "数据权限组资源配置",  tags = {"数据权限-资源配置"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class GroupResourceRuleController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="GROUPRESOURCERULE";
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
    /** 删除 **/
    public static final String AUTH_DELETE_LISTMODE = BASE_AUTH_CODE +"_DELETE_LISTMODE";

    /** 删除 **/
    public static final String AUTH_DELETE_LISTMODE_PAGER = BASE_AUTH_CODE +"_DELETE_LISTMODE_PAGER";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";
    /** 导入 **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE +"_IMPORT";
    /** 批量导入 **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE +"_BATCH_IMPORT";
    /** 批量更新 **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE +"_BATCH_UPDATE";

    @Autowired
    private IGroupResourceRuleService groupResourceRuleService;





    /**
     * 获取权限组指定资源类型下的规则
     *
     * @return
     */
    @ApiOperation(value = "获取权限组指定资源类型下的规则" , notes = "获取权限组指定资源类型下的规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path")
    }
    )
    @GetMapping(value = "/groupResourceRules/{groupId}/{resourceTypeId}")
    @ResponseBody
    @RequiresAuthentication
    public ResultListMsg<GroupResourceRuleModel> getRules(@PathVariable String groupId, @PathVariable String resourceTypeId){

        List<GroupResourceRuleModel> modelList = groupResourceRuleService.getRules(groupId, resourceTypeId);
        return ResultMsg.getListResult(modelList);
    }


    /**
     * 保存
     * @param listModel
     * @return
     */
    @ApiOperation(value = "保存规则列表", notes = "保存规则列表")
    @PostMapping(value = "/groupResourceRule")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg saveRules(@RequestBody @Validated({GroupInsert.class}) GroupRuleListModel listModel, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        groupResourceRuleService.saveRules(listModel);
        return ResultMsg.getResult("操作成功");
    }





    /**
     * 选择资源
     *
     * @return
     */
    @ApiOperation(value = "选择资源" , notes = "选择资源")
    @PostMapping(value = "/groupResourceRules/{groupId}/{resourceItemId}/selectResources")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceItemId", value = "资源属性ID", required = true, dataType = "String",paramType = "path")
    }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg selectResources(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String resourceItemId, @PathVariable String groupId){
        return groupResourceRuleService.selectResources( groupId, resourceItemId ,pagerInfo, false);
    }


    /**
     * 资源属性的资源查询
     *
     * @return
     */
    @ApiOperation(value = "资源属性的资源查询" , notes = "资源属性的资源查询")
    @PostMapping(value = "/groupResourceRules/{groupId}/{resourceTypeId}/resources")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源类型ID", required = true, dataType = "String",paramType = "path")
    }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg resources(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String resourceTypeId, @PathVariable String groupId){
        return groupResourceRuleService.resources( groupId, resourceTypeId ,pagerInfo);
    }



    /**
     * 已选资源
     *
     * @return
     */
    @ApiOperation(value = "选择资源(列表模式)" , notes = "选择资源(列表模式)")
    @PostMapping(value = "/groupResourceRules/listMode/{groupId}/{resourceTypeId}/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path")
    }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg listModeSelect(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String resourceTypeId, @PathVariable String groupId){
        return groupResourceRuleService.selectSelfResources(groupId, resourceTypeId ,pagerInfo);
    }


    /**
     * 已选资源
     *
     * @return
     */
    @ApiOperation(value = "已选资源(列表模式)" , notes = "已选资源(列表模式)")
    @PostMapping(value = "/groupResourceRules/listMode/{groupId}/{resourceTypeId}/checked")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path")
        }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg listModeChecked(@PathVariable String groupId, @PathVariable String resourceTypeId, @RequestBody @Validated PagerInfo pagerInfo){
        return groupResourceRuleService.selectListResource(groupId,resourceTypeId ,pagerInfo);
    }



    /**
     * 保存选中
     *
     * @return
     */
    @ApiOperation(value = "保存选中(列表模式)" , notes = "保存选中(列表模式)")
    @PostMapping(value = "/groupResourceRules/listMode")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg listModeSave(@RequestBody @Validated GroupListAddModel addModel){
        GroupListAddModelResp rsp = groupResourceRuleService.listAdd(addModel);
        return ResultMsg.getResult(rsp);
    }



    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    }
    )
    @DeleteMapping(value = "/groupResourceRules/listMode/{groupId}/{resourceTypeId}/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE_LISTMODE)
    @SLog(action = "删除")
    public ResultMsg listModeDeleteById(@PathVariable String groupId, @PathVariable String resourceTypeId, @PathVariable String id){

        int count = groupResourceRuleService.listModeDelete(groupId,  resourceTypeId, id);
        return ResultMsg.getResult("操作成功");


    }


    /**
     * 按查询条件删除
     *
     * @param pagerInfo
     * @param resourceTypeId
     * @param groupId
     * @return
     */
    @ApiOperation(value = "按查询条件删除", notes = "按查询条件删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path"),
    }
    )
    @PostMapping(value = "/groupResourceRules/listMode/removeAll/{groupId}/{resourceTypeId}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE_LISTMODE_PAGER)
    @SLog(action = "删除")
    public ResultMsg listModeDeleteByPager(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String resourceTypeId, @PathVariable String groupId){

        int count = groupResourceRuleService.listModeDeleteByPager(groupId,  resourceTypeId,  pagerInfo);
        return ResultMsg.getResult("操作成功");

    }



    /**
     * 已选资源
     *
     * @return
     */
    @ApiOperation(value = "已选车辆" , notes = "已选车辆")
    @PostMapping(value = "/groupResourceRules/vehicle/{groupId}/{resourceTypeId}/checked")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path")
    }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg groupCheckedVehicles(@PathVariable String groupId, @PathVariable String resourceTypeId, @RequestBody @Validated PagerInfo pagerInfo){
        PagerResult pr =  groupResourceRuleService.groupCheckedVehicles(groupId,resourceTypeId ,pagerInfo);
        return ResultMsg.getResult(pr);
    }



    /**
     * 已选资源
     *
     * @return
     */
    @ApiOperation(value = "未选车辆" , notes = "未选车辆")
    @PostMapping(value = "/groupResourceRules/vehicle/{groupId}/{resourceTypeId}/unchecked")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "resourceTypeId", value = "资源属性ID", required = true, dataType = "String", paramType = "path")
    }
    )
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg groupUnCheckedVehicles(@PathVariable String groupId, @PathVariable String resourceTypeId, @RequestBody @Validated PagerInfo pagerInfo){

        Object result =  groupResourceRuleService.groupUnCheckedVehicles(groupId,resourceTypeId ,pagerInfo);
        return ResultMsg.getResult(result);

    }



}
