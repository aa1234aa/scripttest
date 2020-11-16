package com.bitnei.cloud.sys.web;


import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.service.IMoreDataItemDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
 * <td>2019-07-01 16:09:17</td>
 * <td>李宇</td>
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
@Api(value = "多车明细数据", description = "多车明细数据", tags = {"多车明细数据"})
@RestController
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class MoreDataItemDetailController {

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


    private final IMoreDataItemDetailService dataItemDetailService;

    @ApiOperation(value = "多车离线导出    字段{ 选择的车辆 ： vin  （多辆车用逗号拼接vin） ,  选择的数据项 ： dataNos  （多个数据项用逗号拼接编号） , 通讯协议id ： ruleId   , 开始时间 ： beginTime  , 结束时间 ： endTime  ,排序  ： order (1是正序，0是倒序。不传默认倒序) } ", notes = "离线导出")
    @PostMapping(value = "/moreDataItemDetails/moreExportOffline")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg moreExportOffline(@RequestBody PagerInfo pagerInfo) {
        dataItemDetailService.moreExportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");
    }

    @ApiOperation(value = "通过车型id，获取通讯协议  字段{ 车型id： modelId  } ", notes = "通过车型id，获取通讯协议 ")
    @PostMapping(value = "/moreDataItemDetails/getRule")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg getRule(@RequestBody PagerInfo pagerInfo){
        Object dataItem = dataItemDetailService.getRule(pagerInfo);
        return  ResultMsg.getResult(dataItem);
    }

    @ApiOperation(value = "通过通讯协议id，获取数据项.  字段{ 通讯协议id： ruleId  } ", notes = "通过通讯协议id，获取数据项 ")
    @PostMapping(value = "/moreDataItemDetails/getDataItem")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg getDataItem(@RequestBody PagerInfo pagerInfo){
        Object dataItem = dataItemDetailService.getDataItem(pagerInfo);
        return  ResultMsg.getResult(dataItem);
    }



}
