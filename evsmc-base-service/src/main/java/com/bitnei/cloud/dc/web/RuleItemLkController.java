package com.bitnei.cloud.dc.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.dc.model.DataItemTempModel;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.dc.model.RuleItemLkModel;
import com.bitnei.cloud.dc.service.IRuleItemLkService;
import com.bitnei.cloud.common.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
* 功能： 协议数据项中间表<br>
* 描述： 协议数据项中间表控制器<br>
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
* <td>2019-01-31 17:34:33</td>
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
@Api(value = "协议数据项", description = "协议数据项",  tags = {"协议管理-协议数据项"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/dc")
public class RuleItemLkController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="RULEITEMLK";
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

    @Autowired
    private IRuleItemLkService ruleItemLkService;
    @Autowired
    private IDataItemService dataItemService;



    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/ruleItemLk")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody RuleItemLkModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
//        ruleItemLkService.insert(demo1);
        ruleItemLkService.insertRuleItems(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 删除
     * @param ruleId
     * @param delItemIds
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleId", value = "协议ID", required = true, dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "delItemIds", value = "删除数据项ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")}
    )
    @DeleteMapping(value = "/ruleItemLk/delete/{ruleId}/{delItemIds}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String ruleId, @PathVariable String delItemIds){

        int count = ruleItemLkService.deleteRuleItemLks(ruleId, delItemIds);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    /*@ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/ruleItemLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) RuleItemLkModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        ruleItemLkService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }*/




    /**
     * 删除
     * @param id
     * @return
     */
    /*@ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/ruleItemLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = ruleItemLkService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }*/


    /**
     * 根据协议ID获取数据项列表
     *
     * @return
     */
    @ApiOperation(value = "根据协议ID获取数据项列表", notes = "根据协议ID获取数据项列表")
    @ApiImplicitParam(name = "ruleId", value = "协议id", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/ruleItemLks/getListByRuleId/{ruleId}")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public List<DataItemTempModel> getListByRuleId(@PathVariable String ruleId) {
        return dataItemService.getListByRuleId(ruleId);
    }


    /**
     * 根据协议ID获取分页数据项列表
     *
     * @return
     */
    @ApiOperation(value = "根据协议ID获取分页数据项列表", notes = "根据协议ID获取分页数据项列表")
    @PostMapping(value = "/ruleItemLks/getPageListByRuleId")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public ResultMsg getPageListByRuleId(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        Object result = dataItemService.getPageListByRuleId(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据协议类型查询树形列表
     * @return
     */
    @ApiOperation(value = "根据协议类型查询树形列表", notes = "根据协议类型查询树形列表")
    @PostMapping(value = "/ruleItemLks/tree")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg tree(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = ruleItemLkService.tree(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 根据协议ID获取分页数据项列表－－新
     *
     * @return
     */
    @ApiOperation(value = "获取协议数据项", notes = "获取协议数据项")
    @PostMapping(value = "/ruleItemLks/listPageListByRuleId")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public ResultMsg listPageListByRuleId(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = dataItemService.listPageListByRuleId(pagerInfo);
        return ResultMsg.getResult(result);
    }
}
