package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.HisdataQuery;
import com.bitnei.cloud.sys.model.HisdataQueryModel;
import com.bitnei.cloud.sys.service.IHisdataQueryService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.commons.datatables.PagerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
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

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 历史数据查询<br>
* 描述： 历史数据查询控制器<br>
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
* <td>2019-03-22 16:01:36</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Slf4j
@Api(value = "历史数据查询", description = "历史数据查询",  tags = {"历史数据查询"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class HisdataQueryController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="HISDATAQUERY";
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
    private IHisdataQueryService hisdataQueryService;


//     /**
//     * 根据id获取对象
//     *
//     * @return
//     */
//    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
//    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
//    @GetMapping(value = "/hisdataQuerys/{id}")
//    @ResponseBody
//    @RequiresPermissions(AUTH_DETAIL)
//    public ResultMsg get(@PathVariable String id){
//
//        HisdataQueryModel hisdataQuery = hisdataQueryService.get(id);
//        return ResultMsg.getResult(hisdataQuery);
//    }




     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "轨迹（历史数据）多条件查询" , notes = "需要的参数：hisdataType=0 历史数据，" +
            " hisdataType=1  整段轨迹，hisdataType=2 分段轨迹,  查询类型(queryType):0-vin,1-内部编号，2-车牌号;查询内容:queryContent；起始时间：beginTime;结束时间：endTime")
    @PostMapping(value = "/hisdataQuerys")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = hisdataQueryService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




//    /**
//    * 保存
//    * @param demo1
//    * @return
//    */
//    @ApiOperation(value = "新增", notes = "新增信息")
//    @PostMapping(value = "/hisdataQuery")
//    @ResponseBody
//    @RequiresPermissions(AUTH_ADD)
//    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) HisdataQueryModel demo1, BindingResult bindingResult){
//
//        if (bindingResult.hasErrors()){
//            return ResultMsg.getSchemaMsg(bindingResult);
//        }
//        hisdataQueryService.insert(demo1);
//        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
//    }
//
//
//
//    /**
//    * 更新
//    * @param demo1
//    * @return
//    */
//    @ApiOperation(value = "更新", notes = "编辑更新信息" )
//    @PutMapping(value = "/hisdataQuerys/{id}")
//    @ResponseBody
//    @RequiresPermissions(AUTH_UPDATE)
//    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) HisdataQueryModel demo1, @PathVariable String id, BindingResult bindingResult){
//
//        if (bindingResult.hasErrors()){
//            return ResultMsg.getSchemaMsg(bindingResult);
//        }
//
//        hisdataQueryService.update(demo1);
//        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
//    }
//
//


//    /**
//     * 删除
//     * @param id
//     * @return
//     */
//    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
//    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
//    @DeleteMapping(value = "/hisdataQuerys/{id}")
//    @ResponseBody
//    @RequiresPermissions(AUTH_DELETE)
//    @SLog(action = "删除")
//    public ResultMsg delete(@PathVariable String id){
//
//        int count = hisdataQueryService.deleteMulti(id);
//        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
//
//
//    }


//    /**
//     * 导出
//     * @return
//     */
//    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
//    @GetMapping(value = "/hisdataQuerys/export/{param}")
//    @RequiresPermissions(AUTH_EXPORT)
//    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
//    public void export(@PathVariable String params) throws IOException {
//
//        String d = URLDecoder.decode(params);
//        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
//
//        hisdataQueryService.export(info);
//        return ;
//
//    }
//
//
//    /**
//     * 批量导入
//     * @return
//     */
//    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
//    @PostMapping(value = "/hisdataQuerys/batchImport")
//    @RequiresPermissions(AUTH_BATCH_IMPORT)
//    @ResponseBody
//    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){
//
//        hisdataQueryService.batchImport(file);
//        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
//
//    }
//
//    /**
//     * 批量更新
//     * @return
//     */
//    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
//    @PutMapping(value = "/hisdataQuerys/batchUpdate")
//    @RequiresPermissions(AUTH_BATCH_UPDATE)
//    @ResponseBody
//    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){
//
//        hisdataQueryService.batchUpdate(file);
//        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
//
//    }



}