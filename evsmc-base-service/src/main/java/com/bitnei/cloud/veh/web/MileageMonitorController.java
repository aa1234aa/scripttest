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
@Api(value = "里程分布统计", description = "里程分布统计",  tags = {"统计分析-运营分析-里程分布统计"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class MileageMonitorController {

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
     * 里程分布统计多条件查询
     *
     * @return
     */
    @ApiOperation(value = "里程分布统计多条件查询" , notes = "参数需传：endTime指定某一天，默认前一天  可单传：operUnitId")
    @PostMapping(value = "/mileageMonitors")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg mileageMonitorList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = dayreportSummaryService.mileageMonitorList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 里程分布统计导出
     * @return
     */
    @ApiOperation(value = "里程分布统计导出，默认时间是前一天" ,notes = "里程分布统计导出数据，参数需传：endTime  可单传：operUnitId")
    @GetMapping(value = "/mileageMonitors/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export2(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        dayreportSummaryService.mileageMonitorExport(info);
        return ;

    }

    /**
     * 统计车辆详情多条件查询
     *
     * @return
     */
    @ApiOperation(value = "车辆详情多条件查询" , notes = "参数需传：operUnitId,endTime--格式为年月日,data1,data2 tip: <0.05,data1 不传,data2:500;0.05~1,data1-data2:500-10000;1~2,data1-data2:10000-20000;>=3data1:30000,data2不传————单位车辆统计详情参数需传:operUnitId,endTime,data1,data2  提示：传入的日期是报表时间年月 yyyy-mm格式")
    @PostMapping(value = "/vehicleDetails")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg vehicleDetailsList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = dayreportSummaryService.vehicleDetailsList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 里程分布统计下的车辆详情导出
     * @return
     */
    @ApiOperation(value = "车辆详情导出" ,notes = "车辆详情导出数据，需传入参数：operUnitId,endTime年月日,data1,data2——————单位下需传参数：operUnitId，endTime 年月，data1,data2")
    @GetMapping(value = "/mileageMonitors/vehicleDetailsExport/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export3(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        dayreportSummaryService.vehicleDetailsExport(info);
        return ;

    }

    /**
     * 统计单位月车辆详情多条件查询
     *
     * @return
     */
//    @ApiOperation(value = "单位-车辆详情" , notes = "参数需传:type=1:≥3万;type=2:2~3万;type=3:1~2万;type=4:0.05~1万;type=5:＜0.05万;operUnitId,endTime  提示：目前转入的日期得是yyyy-mm-dd 后期会修复为传入yyyy-mm就行")
//    @PostMapping(value = "/vehicleDetailsByUnit")
//    @ResponseBody
//    @RequiresPermissions(AUTH_LIST)
//    public ResultMsg vehicleDetailsByUnitList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
//
//
//        if (bindingResult.hasErrors()){
//            return ResultMsg.getSchemaMsg(bindingResult);
//        }
//        Object result = dayreportSummaryService.vehicleDetailsByUnitList(pagerInfo);
//        return ResultMsg.getResult(result);
//    }
    /**
     * 获取车辆详细信息
     *
     * @return
     */

//    @ApiImplicitParam(name = "operUnitId", value = "根据运营单位获取所有月份详情", required = true, dataType = "String",paramType = "path")
//    @ApiOperation(value = "单位-所有月份里程分布统计详情" , notes = "需传入参数：operUnitId")
//    @GetMapping(value = "/mileageMonitors/{operUnitId}")
//    @ResponseBody
//    @RequiresPermissions(AUTH_DETAIL)
//    public ResultMsg findByUnitId(@PathVariable String operUnitId){
//        Object result =  dayreportSummaryService.findByUnitId(operUnitId);
//        return ResultMsg.getResult(result);
//    }




    /**
     * 运营单位月里程分布统计导出
     * @return
     */
//    @ApiOperation(value = "单位-里程分布导出" ,notes = "单位所有月车辆里程分布统计导出数据，需传参数：operUnitId")
//    @GetMapping(value = "/mileageMonitorByUnit/export/{params}")
//    @RequiresPermissions(AUTH_EXPORT)
//    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
//    public void mileageMonitorByUnitListExport(@PathVariable String params) throws IOException {
//
//        String d = URLDecoder.decode(params);
//        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
//
//        dayreportSummaryService.mileageMonitorByUnitListExport(info);
//        return ;
//
//    }




    /**
     * 单位月里程分布统计下的车辆详情导出
     * @return
     */
//    @ApiOperation(value = "单位-车辆导出" ,notes = "需传参数：operUnitId，endTime，type")
//    @GetMapping(value = "/vehicleDetailsByUnit/export/{params}")
//    @RequiresPermissions(AUTH_EXPORT)
//    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
//    public void export4(@PathVariable String params) throws IOException {
//
//        String d = URLDecoder.decode(params);
//        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
//
//        dayreportSummaryService.vehicleDetailsByUnitListExport(info);
//        return ;
//
//    }
}
