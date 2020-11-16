package com.bitnei.cloud.dc.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import com.bitnei.cloud.dc.model.ForwardVehicleModel;
import com.bitnei.cloud.dc.model.PlatformRuleLkModel;
import com.bitnei.cloud.dc.model.PlatformVehicleModel;
import com.bitnei.cloud.dc.service.IForwardVehicleService;
import com.bitnei.cloud.dc.service.IPlatformRuleLkService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 转发车辆<br>
* 描述： 转发车辆控制器<br>
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
* <td>2019-02-21 14:29:14</td>
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
@Api(value = "转发车辆", description = "转发车辆",  tags = {"数据转发-转发车辆"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/dc")
public class ForwardVehicleController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="FORWARDVEHICLE";
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

    @Autowired
    private IForwardVehicleService forwardVehicleService;
    @Autowired
    private IPlatformRuleLkService platformRuleLkService;


    /**
     * 关联自动转发规则
     * @return
     */
    @ApiOperation(value = "关联自动转发规则" , notes = "关联自动转发规则")
    @PostMapping(value = "/forwardVehicles/relateForwardRule")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg relateForwardRule (@RequestBody PlatformRuleLkModel demo){
        int count = platformRuleLkService.insertPlatformRules(demo);
        PlatformRuleLkModel platformRule = null;
        if(count != 0){
            platformRule = forwardVehicleService.insertPlatformVeh(demo);
        }
        return ResultMsg.getResult(platformRule);
    }

    /**
     * 移除转发规则
     * @return
     */
    @ApiOperation(value = "移除转发规则" , notes = "移除转发规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platformId", value = "平台ID", required = true, dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "delRuleIds", value = "移除规则ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")}
    )
    @DeleteMapping(value = "/forwardVehicles/{platformId}/forwardRules/{delRuleIds}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteForwardRule (@PathVariable String platformId, @PathVariable String delRuleIds){
        forwardVehicleService.delPlatformVeh(platformId, delRuleIds);
        return ResultMsg.getResult("移除转发规则成功");
    }

    /**
     * 待转发车辆列表查询
     * @param pagerInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "待转发车辆列表查询" , notes = "待转发车辆列表查询")
    @PostMapping(value = "/forwardVehicles/toBeForwardVehs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findToBeForwardedVeh(@RequestBody PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardVehicleService.findToBeForwardedVeh(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 已转发车辆列表查询
     * @param pagerInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "已转发车辆列表查询" , notes = "已转发车辆列表查询")
    @PostMapping(value = "/forwardVehicles/forwardedVehs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findForwardedVeh(@RequestBody PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardVehicleService.findForwardedVeh(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据计算平台各状态车辆数量
     * @return
     */
    @ApiOperation(value = "根据计算平台各状态车辆数量", notes = "根据计算平台各状态车辆数量")
    @ApiImplicitParam(name = "platformId", value = "平台id", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/forwardVehicles/vehStatusCounts/{platformId}")
    @ResponseBody
    public ForwardVehicleModel countPlatformVehByStatus(@PathVariable String platformId) {
        return forwardVehicleService.countPlatformVehByStatus(platformId);
    }


    /**
     * 车辆状态变更接口
     * @return
     */
    @ApiOperation(value = "车辆状态变更接口", notes = "车辆状态变更接口")
    @PutMapping(value = "/forwardVehicles/status")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg changeVehStatus(@RequestBody ForwardVehicleModel model) {
        forwardVehicleService.changeVehStatus(model);
        return ResultMsg.getResult("车辆状态更新成功");
    }

    /**
     * 获取已关联规则
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "获取已关联规则" , notes = "获取已关联规则")
    @PostMapping(value = "/forwardVehicles/platformRules")
    @ResponseBody
    public ResultMsg findPlatformRule(@RequestBody PagerInfo pagerInfo){
        Object result = platformRuleLkService.findPlatformRule(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 查看关联转发规则后的结果详情
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "查看关联转发规则后的结果详情" , notes = "查看关联转发规则后的结果详情")
    @PostMapping(value = "/forwardVehicles/viewRelateRuleResults")
    @ResponseBody
    public ResultMsg viewRelateRuleResult(@RequestBody PagerInfo pagerInfo){
        Object result = forwardVehicleService.viewRelateRuleResult(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 导出关联转发规则后的结果详情
     * @param params
     * @return
     */
    @ApiOperation(value = "导出关联转发规则后的结果详情" , notes = "导出关联转发规则后的结果详情")
    @GetMapping(value = "/forwardVehicles/exportRelateRuleResults/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void exportRelateRuleResults(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardVehicleService.exportRelateRuleResults(info);
        return ;
    }


    /**
     * 导出待转发车辆列表
     * @return
     */
    @ApiOperation(value = "导出待转发车辆列表" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/forwardVehicles/exportToBeForwardVehs/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void exportToBeForwardVehs(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardVehicleService.exportToBeForwardVehs(info);
        return ;

    }

    /**
     * 导出已转发车辆列表
     * @return
     */
    @ApiOperation(value = "导出已转发车辆列表" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/forwardVehicles/exportForwardedVehs/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void exportForwardedVehs(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardVehicleService.exportForwardedVehs(info);
        return ;

    }


    /**
     * 车辆状态批量变更接口
     * @return
     */
    @ApiOperation(value = "车辆状态批量变更接口", notes = "车辆状态批量变更接口")
    @PutMapping(value = "/forwardVehicles/batchStatus")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg changeBatchVehStatus(@RequestBody ForwardVehicleModel model) {
        ForwardVehicleModel result = forwardVehicleService.changeBatchVehStatus(model);
        return ResultMsg.getResult(result);
    }


    /**
     * 查看确认转发结果详情
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "查看确认转发结果详情" , notes = "查看确认转发结果详情")
    @PostMapping(value = "/forwardVehicles/viewConfirmResults")
    @ResponseBody
    public ResultMsg viewConfirmResults(@RequestBody PagerInfo pagerInfo){
        Object result = forwardVehicleService.viewConfirmResults(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 下载待转发车辆列表导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "下载待转发车辆列表导入查询模板" , notes = "下载待转发车辆列表导入查询模板")
    @GetMapping(value = "/forwardVehicles/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        forwardVehicleService.getImportSearchFile();
        return;
    }

    /**
     * 下载已转发车辆列表导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "下载已转发车辆列表导入查询模板" , notes = "下载已转发车辆列表导入查询模板")
    @GetMapping(value = "/forwardVehicles/importForwardedSearchFile")
    @RequiresAuthentication
    public void getImportForwardedSearchFile(){
        forwardVehicleService.getImportForwardedSearchFile();
        return;
    }

    /**
     * 已添加车辆列表查询
     * @param pagerInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "已添加车辆列表" , notes = "数据转发--转发平台规则配置--已添加车辆" )
    @PostMapping(value = "/forwardVehicles/pagePlatFormVehs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg pagePlatFormVehs(@RequestBody PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardVehicleService.pagePlatFormVehs(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 添加车辆接口
     * @return
     */
    @ApiOperation(value = "添加车辆接口", notes = "添加车辆接口")
    @PostMapping(value = "/forwardVehicles/batchAddPlatFormVehs")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg batchAddPlatFormVehs(@RequestBody PlatformVehicleModel model) {
        PlatformVehicleModel result = forwardVehicleService.addPlatformVehicle(model);
        return ResultMsg.getResult(result);
    }

    /**
     * 查看添加失败列表
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "查看添加失败列表" , notes = "查看添加失败列表" )
    @PostMapping(value = "/forwardVehicles/addFailResult")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg addFailResult(@RequestBody PagerInfo pagerInfo){
        List<ForwardVehicle> result = forwardVehicleService.addFailResult(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 移到黑名单接口
     * @return
     */
    @ApiOperation(value = "移到黑名单", notes = "移到黑名单接口(删除功能)")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/forwardVehicles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    public ResultMsg batchMovePlatFormVehs(@PathVariable String id) {
        forwardVehicleService.moveToBlakList(id);
        return ResultMsg.getResult("删除成功");
    }

    /**
     * 添加全部查询结果
     *
     * @param pagerInfo 添加全部查询结果
     * @return
     */
    @ApiOperation(value = "添加全部查询结果", notes = "添加全部查询结果")
    @PostMapping(value = "/forwardVehicles/batchAddPlatFormVehs/all")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAllResult(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        PlatformVehicleModel result = forwardVehicleService.batchAddPlatformVehicle(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 导出添加车辆列表
     * @return
     */
    @ApiOperation(value = "导出添加车辆列表" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/forwardVehicles/exportPagePlatFormVehs/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void exportPagePlatFormVehs(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardVehicleService.exportPagePlatFormVehs(info);
        return ;

    }
}
