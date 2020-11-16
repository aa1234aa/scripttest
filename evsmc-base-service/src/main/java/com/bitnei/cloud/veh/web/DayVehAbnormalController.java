package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.veh.service.IDayVehAbnormalService;
import com.bitnei.cloud.veh.service.IDayVehDriveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by Lijiezhou on 2019/2/20.
 */
@Slf4j
@Api(value = "车辆异常数据日报", description = "车辆异常数据日报",  tags = {"统计分析-燃油车统计报表-车辆异常数据日报"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class DayVehAbnormalController extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IDayVehAbnormalService dayVehAbnormalService;

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/dayVehAbnormals")
    @ResponseBody
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        Object result = dayVehAbnormalService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/dayVehAbnormals/export/{params}")
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        dayVehAbnormalService.export(info);
        return ;

    }

    /**
     * 下载车辆导入查询模板
     * @return
     */
    @ApiOperation(value = "下载车辆导入查询模板" , notes = "下载车辆导入查询模板")
    @GetMapping(value = "/dayVehAbnormals/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile(){
        dayVehAbnormalService.getImportSearchFile();
        return;
    }
}
