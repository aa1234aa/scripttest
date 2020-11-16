package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehicleFilingModel;
import com.bitnei.cloud.sys.service.IVehicleFilingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;


/**
 * 车辆防篡改备案日志controller
 * @author zhouxianzhou
 */
@Slf4j
@Api(value = "数据交换-车辆防篡改备案", description = "数据交换-车辆防篡改备案日志", tags = {"数据交换-车辆防篡改备案日志"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class VehicleFilingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "VEHICLEFILING";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页
     **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增
     **/
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑
     **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除
     **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出
     **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";



    @Resource
    private IVehicleFilingService vehicleFilingService;


    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicleFilings/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        VehicleFilingModel vehicleFiling = vehicleFilingService.get(id);
        return ResultMsg.getResult(vehicleFiling);
    }

    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/vehicleFilings")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo) {

        Object result = vehicleFilingService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/vehicleFiling")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehicleFilingModel model) {
        vehicleFilingService.insert(model);
        return ResultMsg.getResult("OK");
    }


    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/vehicleFilings/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehicleFilingModel model, @PathVariable String id) {
        model.setId(id);
        vehicleFilingService.update(model);
        return ResultMsg.getResult("OK");
    }


    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/vehicleFilings/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = vehicleFilingService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }


    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleFilings/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        @SuppressWarnings("deprecation") String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleFilingService.export(info);
    }



}
