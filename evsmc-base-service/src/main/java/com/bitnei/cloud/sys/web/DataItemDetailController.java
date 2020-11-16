package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.service.IDataItemDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 明细数据查询<br>
 * 描述： 明细数据查询控制器<br>
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
 * <td>2019-03-16 16:09:17</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "明细数据查询", description = "明细数据查询", tags = {"明细数据查询"})
@RestController
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class DataItemDetailController {


    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "DATAITEMDETAIL";
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
    /**
     * 导入
     **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入
     **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新
     **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";


    private final IDataItemDetailService dataItemDetailService;


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "明细数据多条件查询", notes = "需要的参数： 查询类型(queryType):0-vin,1-内部编号，2-车牌号;查询内容:queryContent；起始时间：startTime;结束时间：endTime,排序方式：0倒序，1正序,数据项字段dataNos(逗号分隔):2000,2001,2002;数据项名称字段dataNosDesc：最后通讯时间,经度,维度")
    @PostMapping(value = "/dataItemDetails")
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        dataItemDetailService.addQueryCondition(pagerInfo, "limitDay", "30");
        dataItemDetailService.addQueryCondition(pagerInfo, "TRANSLATE", "1");
        String order = dataItemDetailService.getQueryCondition(pagerInfo, "order");
        if(order==null){
            //默认排序方式 正序
            dataItemDetailService.addQueryCondition(pagerInfo, "order", "1");
        }
        dataItemDetailService.addQueryCondition(pagerInfo,"isUI","1");

        Object result = dataItemDetailService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 根据vin/车牌号/内部编号查询对应的数据项
     *
     * @return
     */
    @ApiOperation(value = "根据vin/车牌号/内部编号查询对应的数据项",
            notes = "需要的参数： 查询类型(queryType):0-vin,1-内部编号，2-车牌号;查询内容:queryContent")
    @PostMapping(value = "/listDataItemByVehicle")
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg listDataItemByVehicle(@RequestBody PagerInfo pagerInfo) {

        //明细数据
        Object result = dataItemDetailService.findDataItemTreeByRuleId(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "根据vin/车牌号/内部编号查询对应的数据项",
            notes = "需要的参数： 查询类型(queryType):0-vin,1-内部编号，2-车牌号;查询内容:queryContent")
    @PostMapping(value = "/findDataItemByVehicle")
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findDataItemByVehicle(@RequestBody PagerInfo pagerInfo) {
        //监控Tab
        dataItemDetailService.addQueryCondition(pagerInfo, "changeFaultGroupName", "报警数据");
        dataItemDetailService.addQueryCondition(pagerInfo, "removeParentGroup", "1");
        Object result = dataItemDetailService.findDataItemTreeByRuleId(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/dataItemDetails/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params, HttpServletResponse response) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        String order = dataItemDetailService.getQueryCondition(info, "order");
        if(order==null){
            //默认排序方式 正序
            dataItemDetailService.addQueryCondition(info, "order", "1");
        }
        try {
            dataItemDetailService.export(info);
        } catch (BusinessException ex) {
            throw new ExportErrorException(ex.getMessage());
        } catch (Exception e) {
            throw new ExportErrorException("请正确填写查询参数");
        }


    }


    @ApiOperation(value = "离线导出 ", notes = "通过excel，离线导出")
    @PostMapping(value = "/dataItemDetails/exportOffline")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody PagerInfo pagerInfo) {
        String order = dataItemDetailService.getQueryCondition(pagerInfo, "order");
        if(order==null){
            //默认排序方式 正序
            dataItemDetailService.addQueryCondition(pagerInfo, "order", "1");
        }
        dataItemDetailService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");

    }

}
