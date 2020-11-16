package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.model.ModuleModel;
import com.bitnei.cloud.sys.service.IModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 模块管理<br>
* 描述： 模块管理控制器<br>
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
* <td>2018-12-10 17:33:28</td>
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
@Slf4j
@Api(value = "模块管理", description = "模块管理",  tags = {"模块管理"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class ModuleController{



    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="MODULE";
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
    private IModuleService moduleService;


     /**
     * 根据id获取对象
     *
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/modules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        ModuleModel module = moduleService.get(id);
        return ResultMsg.getResult(module);
    }



    /**
     * 根据编码获取对象
     *
     */
    @ApiOperation(value = "根据编码获取详细信息" , notes = "根据编码获取详细信息")
    @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/modules/code/{code}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg code(@PathVariable String code){

        ModuleModel module = moduleService.getByCode(code);
        return ResultMsg.getResult(module);


    }

     /**
     * 多条件查询
     *
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/modules")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = moduleService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param model ModuleModel
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/module")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) ModuleModel model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        if(model.getIsFun().equals(Constant.TrueAndFalse.FALSE)) {
            if(StringUtils.isBlank(model.getCode())) {
                return ResultMsg.getResult(null, "权限编码不能为空", 300);
            }
        }
        moduleService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param model ModuleModel
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/modules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) ModuleModel model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        moduleService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id ID列表，多个用逗号分隔
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/modules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = moduleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }

    /**
     * 删除
     * @param id id
     */
    @ApiOperation(value = "删除模块树", notes = "删除一个模块,和所有子节点")
    @ApiImplicitParam(name = "id", value = "模块id", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/modules/tree/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除模块和子节点")
    public ResultMsg deleteModule(@PathVariable String id){

        moduleService.deleteModuleById(id);
        return ResultMsg.getResult("删除成功");

    }


    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/modules/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        moduleService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 查询系统所有模块
     *
     */
    @ApiOperation(value = "查询系统所有模块", notes = "查询系统所有模块")
    @PostMapping(value = "/modules/tree")
    @ResponseBody
    public ResultMsg queryAllModules(@RequestBody @Validated PagerInfo pagerInfo) {
        TreeNode modules = moduleService.queryAllModules(pagerInfo);
        return ResultMsg.getResult(modules);
    }

}
