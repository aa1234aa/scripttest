package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.model.SocVehicleLogModel;
import com.bitnei.cloud.sys.service.ISocVehicleLogService;
import com.bitnei.cloud.common.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SOC过低车辆<br>
* 描述： SOC过低车辆控制器<br>
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
* <td>2019-03-06 19:00:14</td>
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
@Api(value = "SOC过低车辆", description = "SOC过低车辆",  tags = {"监控中心-异常车辆-SOC过低车辆"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class SocVehicleLogController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="SOCVEHICLELOG";
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
    private ISocVehicleLogService socVehicleLogService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/socVehicleLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        SocVehicleLogModel socVehicleLog = socVehicleLogService.get(id);
        return ResultMsg.getResult(socVehicleLog);
    }

    /**
     * 根据vid获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据VID获取详细信息")
    @ApiImplicitParam(name = "vid", value = "VID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/socVehicleLogs/vid/{vid}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getByVid(@PathVariable String vid){

        SocVehicleLogModel socVehicleLog = socVehicleLogService.getByVid(vid);
        return ResultMsg.getResult(socVehicleLog);
    }


     /**
     * 历史多条件查询
     *
     * @return
     */
    @ApiOperation(value = "历史多条件查询" , notes = "历史多条件查询")
    @PostMapping(value = "/socVehicleLogs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = socVehicleLogService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/socVehicleLog")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) SocVehicleLogModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        socVehicleLogService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/socVehicleLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) SocVehicleLogModel demo1, @PathVariable String id, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        socVehicleLogService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/socVehicleLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = socVehicleLogService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 历史导出
     * @return
     */
    @ApiOperation(value = "历史导出" ,notes = "历史导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/socVehicleLogs/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        socVehicleLogService.export(info);
        return ;

    }

    /**
     * 离线下载全部查询结果
     * @param pagerInfo
     */
    @ApiOperation(value = "离线导出" ,notes = "通过excel，离线导出")
    @PostMapping(value = "/socVehicleLogs/exportOffline")
    @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final String taskId = socVehicleLogService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");
    }

    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/socVehicleLogs/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        socVehicleLogService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/socVehicleLogs/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        socVehicleLogService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }
}
