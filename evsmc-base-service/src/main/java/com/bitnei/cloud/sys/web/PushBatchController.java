package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.PushBatchDetailModel;
import com.bitnei.cloud.sys.model.PushModel;
import com.bitnei.cloud.sys.model.VehAuditModel;
import com.bitnei.cloud.sys.model.VehFeedbackModel;
import com.bitnei.cloud.sys.service.IPushBatchDetailService;
import com.bitnei.cloud.sys.service.IPushBatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 国家平台API推送CONTROLLER
 * @author zxz
 */
@Slf4j
@Api(value = "推送批次号列表", description = "推送批次号列表",  tags = {"推送批次号列表"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class PushBatchController {

    @Resource
    private IPushBatchService pushBatchService;

    @Resource
    private IPushBatchDetailService pushBatchDetailService;


    /**
     * 推送批次号列表
     * @return 列表
     */
    @ApiOperation(value = "推送批次号列表" , notes = "批次号列表分页多条件查询")
    @PostMapping(value = "/pushBatchs")
    @ResponseBody
    public ResultMsg pushBatch(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = pushBatchService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 推送批次号新增
     * @return ResultMsg
     */
    @ApiOperation(value = "推送批次号新增", notes = "推送批次号新增")
    @PutMapping(value = "/pushBatchs/batchInsert")
    @ResponseBody
    public ResultMsg batchInsert(@RequestBody PushModel model) {
        pushBatchService.batchInsert(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    /**
     * 推送批次号修改
     * @return ResultMsg
     */
    @ApiOperation(value = "推送批次号修改", notes = "推送批次号修改")
    @PutMapping(value = "/pushBatchs/batchUpdate")
    @ResponseBody
    public ResultMsg batchUpdate(@RequestBody VehFeedbackModel model) {
        pushBatchService.batchUpdate(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 推送批次号详情列表
     * @return 列表
     */
    @ApiOperation(value = "推送批次号详情列表" , notes = "推送批次号详情列表")
    @PostMapping(value = "/pushBatchDetails")
    @ResponseBody
    public ResultMsg pushBatchDetails(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = pushBatchService.pushBatchDetails(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 推送批次号详情修改
     * @return ResultMsg
     */
    @ApiOperation(value = "推送批次号详情修改", notes = "推送批次号详情修改")
    @PutMapping(value = "/pushBatchDetails/update")
    @ResponseBody
    public ResultMsg pushBatchDetailUpdate(@RequestBody VehAuditModel model) {
        pushBatchService.pushBatchDetailUpdate(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 根据formId获取推送批次号详情
     * @return ResultMsg
     */
    @ApiOperation(value = "推送批次号详情", notes = "推送批次号详情")
    @ApiImplicitParam(name = "formId", value = "formId", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/pushBatchDetails/formId/{formId}")
    @ResponseBody
    public ResultMsg getByFormId(@PathVariable String formId) {
        PushBatchDetailModel detailModel = pushBatchDetailService.getByFormId(formId);
        return ResultMsg.getResult(detailModel);
    }




}
