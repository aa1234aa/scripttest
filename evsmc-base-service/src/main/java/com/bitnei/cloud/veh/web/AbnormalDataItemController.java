package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.veh.service.IDayreportFuelItemAbnormalService;
import com.bitnei.cloud.veh.service.IDayreportItemAbnormalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 车辆异常数据项报表Controller
 * @author zhouxianzhou
 * @date 2019/9/17 14:14
 **/
@Slf4j
@Api(value = "车辆异常数据项报表", description = "车辆异常数据项报表",  tags = {"统计分析-异常分析-车辆异常数据项报表"})
@RestController
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class AbnormalDataItemController {


    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="ABNORMALDATAITEM";

    /** 列表 **/
    public static final String AUTH_LIST = BASE_AUTH_CODE +"_LIST";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";

    @Resource
    private IDayreportItemAbnormalService dayreportItemAbnormalService;
    @Resource
    private IDayreportFuelItemAbnormalService dayreportFuelItemAbnormalService;



    @ApiOperation(value = "新能源车辆异常数据项报表" , notes = "新能源车辆异常数据项报表")
    @PostMapping(value = "/AbnormalDataItems/energy")
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg energyList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = dayreportItemAbnormalService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "新能源车辆异常数据项报表导出" ,notes = "新能源车辆异常数据项报表导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/AbnormalDataItems/energy/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void energyExport(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        dayreportItemAbnormalService.export(info);
        return ;
    }

    @ApiOperation(value = "新能源车辆异常数据项报表离线导出", notes = "导出数据，传入参数应和分页查询保持一致", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams(@ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body"))
    @ApiResponses({@ApiResponse(code = 200, message = MediaType.APPLICATION_JSON_UTF8_VALUE, response = byte[].class)})
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/AbnormalDataItems/energy/export/offline")
    public ResultMsg energyExportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final String taskId = dayreportItemAbnormalService.exportOffline(pagerInfo);
        return ResultMsg.getResult("任务创建成功");
    }

    @ApiOperation(value = "燃油车辆异常数据项报表" , notes = "燃油车辆异常数据项报表")
    @PostMapping(value = "/AbnormalDataItems/fuel")
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg fuelList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = dayreportFuelItemAbnormalService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "燃油车辆异常数据项报表导出" ,notes = "燃油车辆异常数据项报表导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/AbnormalDataItems/fuel/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void fuelExport(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        dayreportFuelItemAbnormalService.export(info);
        return ;
    }

    @ApiOperation(value = "燃油车辆异常数据项报表导出离线导出", notes = "导出数据，传入参数应和分页查询保持一致", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams(@ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body"))
    @ApiResponses({@ApiResponse(code = 200, message = MediaType.APPLICATION_JSON_UTF8_VALUE, response = byte[].class)})
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/AbnormalDataItems/fuel/export/offline")
    public ResultMsg fuelExportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final String taskId = dayreportFuelItemAbnormalService.exportOffline(pagerInfo);
        return ResultMsg.getResult("任务创建成功");
    }



}
