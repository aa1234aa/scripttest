package com.bitnei.cloud.dc.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.dc.model.*;
import com.bitnei.cloud.dc.service.*;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.common.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 平台转发配置<br>
* 描述： 平台转发配置控制器<br>
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
* <td>2019-02-12 14:46:42</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Api(value = "平台转发配置", description = "平台转发配置",  tags = {"数据转发-平台转发配置"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/dc")
public class ForwardPlatformController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="FORWARDPLATFORM";
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
    /** 新增数据项 **/
    public static final String AUTH_ADDDATA = BASE_AUTH_CODE +"_ADDDATA";
    /** 删除数据项 **/
    public static final String AUTH_DELETEDATA = BASE_AUTH_CODE +"_DELETEDATA";
    /** 列表 **/
    public static final String AUTH_LISTDATA = BASE_AUTH_CODE +"_LISTDATA";

    @Autowired
    private IForwardPlatformService forwardPlatformService;
    @Autowired
    private IPlatformDataLkService platformDataLkService;
    @Autowired
    private IDataItemService dataItemService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/forwardPlatforms/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        ForwardPlatformModel forwardPlatform = forwardPlatformService.get(id);
        return ResultMsg.getResult(forwardPlatform);
    }



    /**
     * 根据平台名称获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据平台名称获取详细信息" , notes = "根据平台名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "平台名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/forwardPlatforms/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        ForwardPlatformModel forwardPlatform = forwardPlatformService.getByName(name);
        return ResultMsg.getResult(forwardPlatform);


    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/forwardPlatforms")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardPlatformService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/forwardPlatform")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) ForwardPlatformModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        forwardPlatformService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/forwardPlatforms/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) ForwardPlatformModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        forwardPlatformService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/forwardPlatforms/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = forwardPlatformService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    /*@ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/forwardPlatforms/export")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardPlatformService.export(info);
        return ;

    }*/



    /**
     * 保存
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增数据项", notes = "新增数据项信息")
    @PostMapping(value = "/forwardPlatforms/addPlatformData")
    @ResponseBody
    @RequiresPermissions(AUTH_ADDDATA)
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg addPlatformData(@RequestBody PlatformDataLkModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        platformDataLkService.insertPlatformItems(demo1);
        return ResultMsg.getResult("新增数据项成功");
    }

    /**
     * 删除
     * @param demo1
     * @return
     */
    @ApiOperation(value = "删除数据项", notes = "删除数据项信息")
    @PostMapping(value = "/forwardPlatforms/deletePlatformData")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETEDATA)
    @SLog(action = "删除数据项")
    public ResultMsg deletePlatformData(@RequestBody PlatformDataLkModel demo1, BindingResult bindingResult){

        int count = platformDataLkService.deletePlatformItemLks(demo1);
        return ResultMsg.getResult(String.format("删除数据项成功，共删除了%d条记录", count));
    }


    /**
     * 根据转发平台ID获取数据项列表
     *
     * @return
     */
    @ApiOperation(value = "根据转发平台ID获取数据项列表", notes = "根据转发平台ID获取数据项列表")
    @ApiImplicitParam(name = "platformId", value = "转发平台id", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/forwardPlatforms/getListByPlatformId/{platformId}")
    @RequiresPermissions(AUTH_LISTDATA)
    @ResponseBody
    public List<DataItemTempModel> getListByPlatformId(String platformId) {
        return dataItemService.getListByPlatformId(platformId);
    }


    /**
     * 根据转发平台ID获取分页数据项列表
     *
     * @return
     */
    @ApiOperation(value = "根据转发平台ID获取分页数据项列表", notes = "根据转发平台ID获取分页数据项列表")
    @PostMapping(value = "/forwardPlatforms/getPageListByPlatformId")
    @RequiresPermissions(AUTH_LISTDATA)
    @ResponseBody
    public ResultMsg getPageListByPlatformId(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        Object result = dataItemService.getPageListByPlatformId(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据通讯协议查询树形列表
     * @return
     */
    @ApiOperation(value = "根据通讯协议查询树形列表", notes = "根据通讯协议查询树形列表")
    @PostMapping(value = "/forwardPlatforms/tree")
    @ResponseBody
    @RequiresPermissions(AUTH_LISTDATA)
    public ResultMsg tree(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = platformDataLkService.tree(pagerInfo);
        return ResultMsg.getResult(result);
    }


}
