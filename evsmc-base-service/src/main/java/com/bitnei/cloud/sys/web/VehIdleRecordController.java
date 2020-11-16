package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.model.VehIdleRecordModel;
import com.bitnei.cloud.sys.service.IVehIdleRecordService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.sys.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import lombok.Data;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 长期离线车辆表<br>
* 描述： 长期离线车辆表控制器<br>
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
* <td>2019-03-06 14:44:04</td>
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
@Api(value = "长期离线车辆表", description = "长期离线车辆表",  tags = {"监控中心-异常车辆-长期离线车辆表"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class VehIdleRecordController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="VEHIDLERECORD";
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
    private IVehIdleRecordService vehIdleRecordService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehIdleRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        VehIdleRecordModel vehIdleRecord = vehIdleRecordService.get(id);
        return ResultMsg.getResult(vehIdleRecord);
    }

    /**
     * 根据vid获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "vid", value = "VID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehIdleRecords/vid/{vid}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getByVid(@PathVariable String vid){

        VehIdleRecordModel vehIdleRecord = vehIdleRecordService.getByVid(vid);
        return ResultMsg.getResult(vehIdleRecord);
    }

    /**
     * 弹窗点击车牌号跳转
     * @param plate
     */
    @ApiImplicitParam(name = "plate", value = "PLATE", required = true, dataType = "String",paramType = "plate")
    @GetMapping(value = "/vehIdleRecords/{plate}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public void realIdleVehList(@PathVariable String plate){

        String idleTimeOut = RedisUtil.timeOutTime();
        if (idleTimeOut == null){
            idleTimeOut = "86400";
            RedisUtil.saveTimeOutTime(idleTimeOut);
        }
        String plates = plate;
        if (plates != null && !"".equals(plates)){
            try {
                plates = new String(plates.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new BusinessException("获取车牌失败");
            }
            //车牌号
            PagerInfo pagerInfo = new PagerInfo();
            Condition condition = new Condition();
            condition.setName("licensePlate");
            condition.setValue(plates);
            pagerInfo.getConditions().add(condition);
        }
    }

    /**
     * 保存离线提醒信息
     *
     * @return
     */
    @Data
    static class AddParam{
        private String offLineTime;
        private String mileagePoor;
        private String whetherFrame;
    }
    @ApiOperation(value = "保存离线提醒信息" , notes = "保存离线提醒信息")
    @PostMapping(value = "/vehIdleRecords/saveReminderTime")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg saveReminderTime(@RequestBody AddParam param,BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Map<String , Object> result = vehIdleRecordService.saveReminderTime(param.offLineTime, param.mileagePoor, param.whetherFrame);
        return ResultMsg.getResult(result);
    }

     /**
     * 历史多条件查询
     *
     * @return
     */
    @ApiOperation(value = "历史多条件查询" , notes = "历史多条件查询")
    @PostMapping(value = "/vehIdleRecords")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehIdleRecordService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 实时多条件查询
     *
     * @return
     */
    @ApiOperation(value = "实时多条件查询" , notes = "实时多条件查询")
    @PostMapping(value = "/vehIdleRecords/realTime")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg realTimeList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehIdleRecordService.realTimeList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 离线车辆弹窗
     *
     * @return
     */
    @ApiOperation(value = "离线车辆弹窗" , notes = "离线车辆弹窗")
    @PostMapping(value = "/vehIdleRecords/popup")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg popupList(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehIdleRecordService.popupList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 获取当前提醒状态
     * @return
     */
    @ApiOperation(value = "获取当前提醒状态" , notes = "获取当前提醒状态")
    @GetMapping(value = "/vehIdleRecords/nowStatus")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public String getNowReminderStatus(){

        return vehIdleRecordService.getNowReminderStatus();
    }

    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/vehIdleRecord")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehIdleRecordModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehIdleRecordService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/vehIdleRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehIdleRecordModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehIdleRecordService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 处理提醒状态
     * @param demo1
     * @return
     */
    @ApiOperation(value = "处理提醒状态", notes = "处理提醒状态,多条记录同时处理时参数id以英文逗号隔开" )
    @PutMapping(value = "/vehIdleRecords/updateRmakAndType/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg updateRmakAndType(@RequestBody @Validated({GroupUpdate.class}) VehIdleRecordModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehIdleRecordService.updateRmakAndType(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/vehIdleRecords/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = vehIdleRecordService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 历史导出
     * @return
     */
    @ApiOperation(value = "历史导出" ,notes = "历史导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehIdleRecords/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehIdleRecordService.export(info);
        return ;

    }

    /**
     * 离线下载全部查询结果
     * @param pagerInfo
     */
    @ApiOperation(value = "离线导出" ,notes = "通过excel，离线导出")
    @PostMapping(value = "/vehIdleRecords/exportOffline")
    @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final String taskId = vehIdleRecordService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");
    }

    /**
     * 实时导出
     * @return
     */
    @ApiOperation(value = "实时导出" ,notes = "实时导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehIdleRecords/realTimeExport/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void realTimeExport(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehIdleRecordService.realTimeExport(info);
        return ;

    }

    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/vehIdleRecords/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        vehIdleRecordService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/vehIdleRecords/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        vehIdleRecordService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }



}
