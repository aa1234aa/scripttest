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
import com.bitnei.cloud.sys.model.VehicleSellModel;
import com.bitnei.cloud.sys.service.IVehicleSellService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
@Api(value = "车辆销售列表", description = "车辆销售信息相关Api",  tags = {"基础数据-车辆信息-车辆销售信息"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class VehicleSellController {


    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="VEHICLESELL";
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
    private IVehicleSellService vehicleSellService;



    @ApiOperation(value = "车辆销售详情" , notes = "根据ID获取车辆销售详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehicleSells/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        VehicleSellModel vehicleSellModel = vehicleSellService.get(id);
        return ResultMsg.getResult(vehicleSellModel);
    }

    @ApiOperation(value = "车辆销售列表" , notes = "车辆销售列表分页多条件查询")
    @PostMapping(value = "/vehicleSells")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public ResultMsg vehicleSells(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehicleSellService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    @ApiOperation(value = "新增车辆销售信息", notes = "新增车辆基本销售信息")
    @PostMapping(value = "/vehicleSell")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehicleSellModel model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehicleSellService.save(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    @ApiOperation(value = "更新车辆销售信息", notes = "更新车辆销售信息" )
    @PutMapping(value = "/vehicleSells/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehicleSellModel model,
            @PathVariable String id, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        model.setId(id);
        vehicleSellService.save(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    @ApiOperation(value = "删除销售信息", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/vehicleSells/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = vehicleSellService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleSells/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleSellService.export(info);
        return ;
    }

    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/vehicleSells/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){
        vehicleSellService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PostMapping(value = "/vehicleSells/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){
        vehicleSellService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }

    /**
     * 批量导入模版下载
     *
     * @return
     */
    @ApiOperation(value = "批量导入模版" , notes = "批量导入模版")
    @GetMapping(value = "/vehicleSells/importTemplateFile")
    @RequiresAuthentication
    public void getImportTemplateFile(){
        vehicleSellService.getImportTemplateFile();
        return;
    }


    /**
     * 批量更新模板下载
     * @return
     */
    @ApiOperation(value = "批量更新模板" ,notes = "批量更新模板，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleSells/batchUpdateTemplateFile/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void getBatchUpdateTemplateFile(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleSellService.getBatchUpdateTemplateFile(info);
        return ;

    }

    /**
     * 导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "导入查询模板" , notes = "导入查询模板")
    @GetMapping(value = "/vehicleSells/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        vehicleSellService.getImportSearchFile();
        return;
    }


}
