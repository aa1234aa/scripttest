package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehicleSubsidyModel;
import com.bitnei.cloud.sys.service.IVehicleSubsidyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by Lijiezhou on 2018/12/25.
 */
@Slf4j
@Api(value = "车辆补贴信息", description = "车辆补贴相关Api",  tags = {"基础数据-车辆信息-车辆补贴信息"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class VehicleSubsidyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="VEHICLE";
    /** 列表 **/
    public static final String AUTH_LIST = BASE_AUTH_CODE +"_LIST";
    /** 查看 **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE +"_DETAIL";
    /** 编辑 **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE +"_UPDATE";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";

    /** 批量更新 **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE +"_BATCH_UPDATE";

    @Autowired
    private IVehicleSubsidyService vehicleSubsidyService;

    /**
     * 车辆补贴
     */
    @ApiOperation(value = "车辆补贴列表信息" , notes = "车辆补贴查询条件")
    @PostMapping(value = "/vehSubsidies")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg subsidyList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehicleSubsidyService.subsidyList(pagerInfo);
        return ResultMsg.getResult(result);
    }
    /**
     * 编辑车辆补贴
     *
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/vehSubsidies/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg subsidyUpdate(@RequestBody @Validated({GroupUpdate.class}) VehicleSubsidyModel model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehicleSubsidyService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }
    /**
     * 根据id获取对象
     *
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehSubsidies/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        VehicleSubsidyModel vehicleSubsidy = vehicleSubsidyService.get(id);
        return ResultMsg.getResult(vehicleSubsidy);
    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehSubsidies/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void subsidyExport(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleSubsidyService.subsidyExport(info);
        return ;
    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PostMapping(value = "/vehSubsidies/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){
        vehicleSubsidyService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }


    /**
     * 批量更新模板下载
     */
    @ApiOperation(value = "批量更新模板" ,notes = "批量更新模板，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehSubsidies/batchUpdateTemplateFile/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void getBatchUpdateTemplateFile(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleSubsidyService.getBatchUpdateTemplateFile(info);
        return ;

    }

    /**
     * 导入查询模板
     *
     */
    @ApiOperation(value = "导入查询模板" , notes = "导入查询模板")
    @GetMapping(value = "/vehSubsidies/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        vehicleSubsidyService.getImportSearchFile();
        return;
    }


    /**
     * 离线导出
     */
    @ApiOperation(value = "离线导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/vehSubsidies/export/offline")
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        final String taskId = vehicleSubsidyService.exportOffline(pagerInfo);
        return ResultMsg.getResult("任务创建成功");

    }


}
