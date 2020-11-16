package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.api.ResultSchemaMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.SoftwareVersion;
import com.bitnei.cloud.sys.service.ISystermHelpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-04-12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Api(value = "系统帮助", description = "提供各种系统帮助类的接口",  tags = {"全局-系统帮助"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys/help")
public class SystemHelpController {


    @Autowired
    private ISystermHelpService systermHelpService;

    /**
     * 实时导出
     * @return
     */
    @ApiOperation(value = "系统版本" ,notes = "系统版本")
    @GetMapping(value = "/version")
    @ResponseBody
    public SoftwareVersion version(){
        return systermHelpService.getVersion();
    }


    /**
     * 实时导出
     * @return
     */
    @ApiOperation(value = "查看详细异常信息" ,notes = "查看详细异常信息")
    @ApiImplicitParam(name = "code", value = "异常代码", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/api-docs/error/{code}")
    public void errorDetail(@PathVariable String code){

        systermHelpService.getErrorMessage(code);

    }


    /**
     * 健康状态
     * @return
     */
    @ApiOperation(value = "健康状态" ,notes = "健康状态")
    @GetMapping(value = "/api-docs/health")
    @ResponseBody
    public ResultSchemaMsg health(){
        systermHelpService.health();
        ResultSchemaMsg msg = new ResultSchemaMsg();
        msg.setCode(0);
        msg.setMessage("系统正常");
        msg.setType("schema");

        return msg;
    }
}
