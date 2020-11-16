package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.ModuleDataGroup;
import com.bitnei.cloud.sys.model.ModuleDataGroupModel;
import com.bitnei.cloud.sys.service.IModuleDataGroupService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.commons.datatables.PagerModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
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

import java.util.HashMap;
import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 角色资源组<br>
* 描述： 角色资源组控制器<br>
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
* <td>2018-11-22 11:48:45</td>
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
@Api(value = "角色资源组", description = "角色资源组",  tags = {"角色资源组"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class ModuleDataGroupController{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="MODULEDATAGROUP";
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
    private IModuleDataGroupService moduleDataGroupService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/moduleDataGroups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        ModuleDataGroupModel moduleDataGroup = moduleDataGroupService.get(id);
        return ResultMsg.getResult(moduleDataGroup);
    }



    /**
     * 根据名称获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据名称获取详细信息" , notes = "根据名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/moduleDataGroups/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        ModuleDataGroupModel moduleDataGroup = moduleDataGroupService.getByName(name);
        return ResultMsg.getResult(moduleDataGroup);
    

    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/moduleDataGroups")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = moduleDataGroupService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


   

    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/moduleDataGroup")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) ModuleDataGroupModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        moduleDataGroupService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/moduleDataGroups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) ModuleDataGroupModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        moduleDataGroupService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/moduleDataGroups/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = moduleDataGroupService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/moduleDataGroups/export")
    @RequiresPermissions(AUTH_EXPORT)
    public void export(@RequestBody @Validated PagerInfo info){

        moduleDataGroupService.export(info);
        return ;

    }


    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/moduleDataGroups/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        moduleDataGroupService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/moduleDataGroups/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        moduleDataGroupService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }



}
