package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.LogModel;
import com.bitnei.cloud.sys.service.ILogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 操作日志controller
 *
 * @author zxz
 */
@Slf4j
@Api(value = "操作日志", description = "操作日志", tags = {"系统设置-操作日志"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class LogController {


    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "LOG";
    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 导出
     **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";

    @Resource
    private ILogService logService;

    /**
     * 多条件查询
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/logs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = logService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/logs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        LogModel log = logService.get(id);
        return ResultMsg.getResult(log);
    }

    /**
     * 导出
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/logs/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        logService.export(info);

    }

    /**
     * 离线下载全部查询结果
     * @param pagerInfo
     */
    @ApiOperation(value = "离线导出" ,notes = "通过excel，离线导出")
    @PostMapping(value = "/logs/exportOffline")
    @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final String taskId = logService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");
    }
}
