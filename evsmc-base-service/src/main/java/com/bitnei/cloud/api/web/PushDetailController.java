package com.bitnei.cloud.api.web;

import com.bitnei.cloud.api.model.PushDetailDTO;
import com.bitnei.cloud.api.model.PushDetailModel;
import com.bitnei.cloud.api.service.IApplicationService;
import com.bitnei.cloud.api.service.IPushDetailService;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Api(value = "开放平台-推送明细", description = "开放平台-推送明细", tags = {"开放平台-推送明细"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/api")
@RequiredArgsConstructor
public class PushDetailController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "PUSHDETAIL";

    /**
     * 新增
     **/
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";

    private final IPushDetailService pushDetailService;
    private final IApplicationService applicationService;

    @ApiOperation(value = "批量注册推送", notes = "批量注册推送")
    @PostMapping(value = "/addPushDetails")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg batchAdd(@RequestBody @Validated({GroupInsert.class}) List<PushDetailDTO> pushDetailDTOS,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        pushDetailDTOS.forEach(it -> {
            //不存在会在getByCode里抛出异常
            applicationService.getByCode(it.getApplicationCode());

            PushDetailModel pushDetailModel = new PushDetailModel();
            BeanUtils.copyProperties(it, pushDetailModel);

            PushDetailModel existModel = pushDetailService.getByApplicationCodeAndCode(it.getApplicationCode(), it.getCode());
            if (null != existModel) {
                pushDetailModel.setId(existModel.getId());
                pushDetailService.update(pushDetailModel);
            } else {
                pushDetailService.insert(pushDetailModel);
            }
        });
        return ResultMsg.getResult("批量注册成功");
    }
}
