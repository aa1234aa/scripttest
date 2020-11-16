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
import com.bitnei.cloud.sys.model.UnitTypeModel;
import com.bitnei.cloud.sys.service.IUnitTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 单位类型<br>
* 描述： 单位类型控制器<br>
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
* <td>2018-12-20 11:48:35</td>
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
@Api(value = "单位类型", description = "单位类型",  tags = {"系统设置-单位类型"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class UnitTypeController{


    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="UNITTYPE";
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
    private IUnitTypeService unitTypeService;


     /**
     * 根据id获取对象
     *
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/unitTypes/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        UnitTypeModel unitType = unitTypeService.get(id);
        return ResultMsg.getResult(unitType);
    }



    /**
     * 根据名称获取对象
     *
     */
    @ApiOperation(value = "根据名称获取详细信息" , notes = "根据名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/unitTypes/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        UnitTypeModel unitType = unitTypeService.getByName(name);
        return ResultMsg.getResult(unitType);


    }
    /**
     * 根据编码获取对象
     *
     */
    @ApiOperation(value = "根据编码获取详细信息" , notes = "根据编码获取详细信息")
    @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/unitTypes/code/{code}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg code(@PathVariable String code){

        UnitTypeModel unitType = unitTypeService.getByCode(code);
        return ResultMsg.getResult(unitType);


    }

     /**
     * 多条件查询
     *
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/unitTypes")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = unitTypeService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param model UnitTypeModel
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/unitType")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UnitTypeModel model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        unitTypeService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param model UnitTypeModel
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/unitTypes/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UnitTypeModel model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        unitTypeService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id id
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/unitTypes/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){
        String[] ids = StringUtils.split(id, ",");
        for (String i : ids) {
            if(unitTypeService.findByUnitTypeIdCount(i) > 0){
                return ResultMsg.getResult(null, "该单位类型已被使用，不能删除。", 300);
            }
        }
        int count = unitTypeService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/unitTypes/export")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        unitTypeService.export(info);
        return ;

    }


    /**
     * 批量导入
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/unitTypes/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        unitTypeService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/unitTypes/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        unitTypeService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 单位类型树结构
     * @param pagerInfo PagerInfo
     */
    @ApiOperation(value = "多条件查询树结构", notes = "多条件查询树结构")
    @PostMapping(value = "/unitTypes/tree")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg tree(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = unitTypeService.tree(pagerInfo);
        return ResultMsg.getResult(result);
    }


}
