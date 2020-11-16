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
import com.bitnei.cloud.sys.model.VehicleOperModel;
import com.bitnei.cloud.sys.service.IVehicleOperService;
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
@Api(value = "车辆运营使用列表", description = "车辆运营使用列表相关Api",  tags = {"基础数据-车辆信息-车辆运营信息"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class VehicleOperController {

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
    private IVehicleOperService vehicleOperService;


    @ApiOperation(value = "车辆运营使用详情" , notes = "根据ID获取车辆运营使用详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehicleOpers/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        VehicleOperModel model = vehicleOperService.get(id);
        return ResultMsg.getResult(model);
    }

    @ApiOperation(value = "车辆运营使用列表" , notes = "车辆运营使用列表分页多条件查询")
    @PostMapping(value = "/vehicleOpers")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public ResultMsg vehicleSells(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehicleOperService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    @ApiOperation(value = "新增车辆运营使用信息", notes = "新增车辆基本运营使用信息")
    @PostMapping(value = "/vehicleOper")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehicleOperModel model, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehicleOperService.save(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    @ApiOperation(value = "更新车辆运营使用信息", notes = "更新车辆运营使用信息" )
    @PutMapping(value = "/vehicleOpers/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehicleOperModel model,
                            @PathVariable String id, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        model.setId(id);
        vehicleOperService.save(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    @ApiOperation(value = "删除运营使用信息", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/vehicleOpers/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = vehicleOperService.deleteMulti(id);
        return ResultMsg.getResult(String.format("删除成功，共删除了%d条记录!", count));
    }



    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleOpers/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleOperService.export(info);
        return ;
    }


    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/vehicleOpers/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){
        vehicleOperService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PostMapping(value = "/vehicleOpers/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){
        vehicleOperService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }


    /**
     * 批量导入模版下载
     *
     * @return
     */
    @ApiOperation(value = "批量导入模版" , notes = "批量导入模版")
    @GetMapping(value = "/vehicleOpers/importTemplateFile")
    @RequiresAuthentication
    public void getImportTemplateFile(){
        vehicleOperService.getImportTemplateFile();
        return;
    }


    /**
     * 批量更新模板下载
     * @return
     */
    @ApiOperation(value = "批量更新模板" ,notes = "批量更新模板，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleOpers/batchUpdateTemplateFile/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void getBatchUpdateTemplateFile(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleOperService.getBatchUpdateTemplateFile(info);
        return ;

    }

    /**
     * 导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "导入查询模板" , notes = "导入查询模板")
    @GetMapping(value = "/vehicleOpers/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        vehicleOperService.getImportSearchFile();
        return;
    }


}
