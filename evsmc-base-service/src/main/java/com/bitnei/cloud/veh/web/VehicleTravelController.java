package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.veh.service.IDayreportSummaryService;
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

import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 日汇总报表<br>
* 描述： 日汇总报表控制器<br>
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
* <td>2019-03-11 09:40:45</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Slf4j
@Api(value = "车辆行驶情况统计", description = "车辆行驶情况统计",  tags = {"统计分析-运营分析-车辆运行统计-车辆行驶情况统计"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class VehicleTravelController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="DAYREPORTSUMMARY";
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
    private IDayreportSummaryService dayreportSummaryService;


     /**
     * 车辆行驶情况统计多条件查询
     *
     * @return
     */
    @ApiOperation(value = "车辆行驶情况统计多条件查询" , notes = "车辆行驶情况统计多条件查询")
    @PostMapping(value = "/vehicleTravels")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = dayreportSummaryService.vehicleTravelList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 车辆行驶情况统计导出
     * @return
     */
    @ApiOperation(value = "车辆行驶情况统计导出" ,notes = "车辆行驶情况统计导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleTravels/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        dayreportSummaryService.vehicleTravelExport(info);
        return ;

    }
}