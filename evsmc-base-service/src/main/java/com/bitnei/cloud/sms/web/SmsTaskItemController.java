package com.bitnei.cloud.sms.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sms.model.SmsTaskItemDetailModel;
import com.bitnei.cloud.sms.model.SmsTaskItemModel;
import com.bitnei.cloud.sms.service.ISmsTaskItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.bitnei.cloud.common.constant.Version;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 短信下发任务明细<br>
* 描述： 短信下发任务明细控制器<br>
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
* <td>2019-08-17 15:45:24</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Slf4j
@Api(value = "短信下发任务明细", description = "短信下发任务明细",  tags = {"短信下发任务明细"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sms")
public class SmsTaskItemController {

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="SMSTASKITEMS";
    /** 查看 **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE +"_DETAIL";
    /** 列表 **/
    public static final String AUTH_LIST = BASE_AUTH_CODE +"_LIST";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";
    @Autowired
    private ISmsTaskItemService smsTaskItemService;

     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/smsTaskItems/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        SmsTaskItemDetailModel smsTaskItems = smsTaskItemService.get(id);
        return ResultMsg.getResult(smsTaskItems);
    }


     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/smsTaskItems")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = smsTaskItemService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/smsTaskItems/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        smsTaskItemService.export(info);
        return ;

    }

    /**
     * 终端指令详情页面的 车辆列表
     *
     * @return
     */
    @ApiOperation(value = "终端指令详情页车辆列表", notes = "终端指令详情页面的车辆列表")
    @PostMapping(value = "/smsTaskItems/findSmsVehicle")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findSmsVehicle(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = smsTaskItemService.findSmsVehicle(pagerInfo);
        return ResultMsg.getResult(result);
    }
}
