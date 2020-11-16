package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.veh.model.HistoryStateModel;
import com.bitnei.cloud.veh.service.IHistoryStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 车辆历史状态报表<br>
* 描述： 车辆历史状态报表控制器<br>
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
* <td>2019-03-09 11:23:08</td>
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
@Api(value = "车辆历史状态报表", description = "车辆历史状态报表",  tags = {"统计分析-工况分析-车辆历史状态报表"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class HistoryStateController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="HISTORYSTATE";
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
    private IHistoryStateService historyStateService;

     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/historyStates/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        HistoryStateModel historyState = historyStateService.get(id);
        return ResultMsg.getResult(historyState);
    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/historyStates")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = historyStateService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/historyStates/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        final String json = URLDecoder.decode(params, StandardCharsets.UTF_8.name());
        final PagerInfo info = new ObjectMapper().readValue(json, PagerInfo.class);

        historyStateService.export(info);
    }

    /**
     * 下载车辆历史状态报表导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "下载车辆历史状态报表导入查询模板" , notes = "下载车辆历史状态报表导入查询模板")
    @GetMapping(value = "/historyStates/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        historyStateService.getImportSearchFile();
        return;
    }

    /**
     * 离线导出
     */
    @ApiOperation(value = "离线导出", notes = "导出数据，传入参数应和分页查询保持一致", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams(
        @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = MediaType.APPLICATION_JSON_UTF8_VALUE, response = byte[].class)
    })
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/historyStates/export/offline")
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        final String taskId = historyStateService.exportOffline(pagerInfo);
        return ResultMsg.getResult("任务创建成功");

    }

    /**
     * 在线导出
     */
    @ApiOperation(value = "在线导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @ApiImplicitParams(
        @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    )
    @ApiResponse(code = 200, message = "text/csv;charset=UTF-8", response = byte[].class)
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/historyStates/export/online", produces = "text/csv;charset=UTF-8")
    public ResponseEntity<StreamingResponseBody> exportOnlineCsv(
        @RequestBody @Validated PagerInfo pagerInfo)
        throws IOException {

        return historyStateService.exportOnline(pagerInfo);
    }

    /**
     * 在线导出
     */
    @ApiOperation(value = "在线导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @ApiImplicitParams(
        @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    )
    @ApiResponse(code = 200, message = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", response = byte[].class)
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/historyStates/export/online", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public void exportOnlineExcel(
        @RequestBody @Validated PagerInfo pagerInfo)
        throws IOException {

        // TODO: 流式导出 Microsoft Excel (OpenXML) 格式文件
    }
}
