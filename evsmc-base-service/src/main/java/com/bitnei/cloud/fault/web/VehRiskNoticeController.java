package com.bitnei.cloud.fault.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.model.SafeRiskModel;
import com.bitnei.cloud.fault.model.SafeRiskUpModel;
import com.bitnei.cloud.fault.model.VehRiskNoticeModel;
import com.bitnei.cloud.fault.service.IVehRiskNoticeService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 当前国家平台车辆风险通知<br>
* 描述： 当前国家平台车辆风险通知控制器<br>
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
* <td>2019-07-08 18:07:56</td>
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
@Api(value = "当前国家平台车辆风险通知", description = "当前国家平台车辆风险通知",  tags = {"当前国家平台车辆风险通知"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/fault")
public class VehRiskNoticeController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="VEHRISKNOTICE";
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
    private IVehRiskNoticeService vehRiskNoticeService;

     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/vehRiskNotices/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        VehRiskNoticeModel vehRiskNotice = vehRiskNoticeService.get(id);
        return ResultMsg.getResult(vehRiskNotice);
    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/vehRiskNotices")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = vehRiskNoticeService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "已处理风险通知多条件查询" , notes = "已处理风险通知多条件查询")
    @PostMapping(value = "/vehRiskNotices/history")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg historyList(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = vehRiskNoticeService.historyList(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
    * 保存
    * @param model
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/vehRiskNotice")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehRiskNoticeModel model){

        vehRiskNoticeService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
    * 更新
    * @param model
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/vehRiskNotices/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehRiskNoticeModel model, @PathVariable String id){


        vehRiskNoticeService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 批量阅读
     * @param code
     * @return
     */
    @ApiOperation(value = "批量阅读", notes = "批量阅读更新状态,多条记录用英文逗号隔开" )
    @PutMapping(value = "/vehRiskNotices/updateReadByCode/{code}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg updateReadByCode(@PathVariable String code){

         vehRiskNoticeService.updateReadByCode(code);
        return ResultMsg.getResult("批量阅读成功");
    }

    /**
     * 批量回复
     * @param code
     * @return
     */
    @ApiOperation(value = "批量回复", notes = "批量回复,多条code记录用英文逗号隔开,有附件上传时，需给fileId赋值" )
    @PutMapping(value = "/vehRiskNotices/updateReplyByCode/{code}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg updateReplyByCode(@RequestBody @Validated({GroupUpdate.class}) VehRiskNoticeModel model, @PathVariable String code){

        vehRiskNoticeService.updateReplyByCode(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 历史处理记录
     *
     * @return
     */
    @ApiOperation(value = "历史处理记录" , notes = "历史处理记录(需传参code)")
    @PostMapping(value = "/findAnnotationsByCode")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findAnnotationsByCode(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = vehRiskNoticeService.findAnnotationsByCode(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "通知状态，风险等级统计" , notes = "获取通知状态，风险等级统计详情")
    @GetMapping(value = "/vehRiskNotices")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getCount(){

        VehRiskNoticeModel vehRiskNotice = vehRiskNoticeService.getCount();
        return ResultMsg.getResult(vehRiskNotice);
    }

    /**
     * 国家平台管理员意见
     *
     * @return
     */
    @ApiOperation(value = "国家平台管理员意见" , notes = "国家平台管理员意见(需传参code)")
    @PostMapping(value = "/findOpinionsByCode")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findOpinionsByCode(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = vehRiskNoticeService.findOpinionsByCode(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/vehRiskNotices/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = vehRiskNoticeService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    @ApiOperation(value = "批量新增,国家平台风险消息通知信息接收API", notes = "批量新增安全风险通知,openService使用")
    @PostMapping(value = "/vehRiskNotice/batchInsert")
    @ResponseBody
    public ResultMsg batchInsert(@RequestBody @Validated List<SafeRiskModel> models) {
        vehRiskNoticeService.batchInsert(models);
        return ResultMsg.getResult("请求成功");
    }


    @ApiOperation(value = "批量更新状态", notes = "批量更新状态")
    @PostMapping(value = "/vehRiskNotice/batchUpdate")
    @ResponseBody
    public ResultMsg batchUpdate(@RequestBody @Validated List<SafeRiskUpModel> models) {
        vehRiskNoticeService.batchUpdate(models);
        return ResultMsg.getResult("请求成功");
    }


    /**
     * 平台内当前报警多条件查询
     *
     * @return
     */
    @ApiOperation(value = "平台内关联报警多条件查询" , notes = "平台内关联报警多条件查询，必须传vin和firstNoticeTime；isNow为1时查询当前警报否则查询历史报警")
    @PostMapping(value = "/vehRiskNotice/alarmInfoList")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg alarmInfoList(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = vehRiskNoticeService.alarmInfoList(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehRiskNotices/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehRiskNoticeService.export(info);
        return ;

    }

    /**
     * 已处理通知导出
     * @return
     */
    @ApiOperation(value = "已处理通知导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehRiskNotices/history/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void historyExport(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehRiskNoticeService.historyExport(info);
        return ;

    }
}
