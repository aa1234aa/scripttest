package com.bitnei.cloud.veh.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.veh.model.DataCheckRuleModel;
import com.bitnei.cloud.veh.service.IDataCheckRuleItemService;
import com.bitnei.cloud.veh.service.IDataCheckRuleService;
import com.bitnei.cloud.common.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 数据质量检测规则<br>
* 描述： 数据质量检测规则控制器<br>
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
* <td>2019-09-16 15:39:53</td>
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
@Api(value = "数据质量检测规则", description = "数据质量检测规则",  tags = {"车辆数据质量检测PC端-数据质量检测规则"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/veh")
public class DataCheckRuleController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="DATACHECKRULE";
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
    private IDataCheckRuleService dataCheckRuleService;
    @Autowired
    private IDataItemService dataItemService;
    @Autowired
    private IDataCheckRuleItemService dataCheckRuleItemService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/dataCheckRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        DataCheckRuleModel dataCheckRule = dataCheckRuleService.get(id);
        return ResultMsg.getResult(dataCheckRule);
    }



    /**
     * 根据规则名称获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据规则名称获取详细信息" , notes = "根据规则名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "规则名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/dataCheckRules/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        DataCheckRuleModel dataCheckRule = dataCheckRuleService.getByName(name);
        return ResultMsg.getResult(dataCheckRule);


    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/dataCheckRules")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = dataCheckRuleService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param model
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/dataCheckRule")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) DataCheckRuleModel model){

        dataCheckRuleService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param model
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/dataCheckRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) DataCheckRuleModel model, @PathVariable String id){


        dataCheckRuleService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/dataCheckRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = dataCheckRuleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 根据检测规则ID获取分页数据项列表
     *
     * @return
     */
    @ApiOperation(value = "根据检测规则ID获取分页数据项列表", notes = "根据检测规则ID获取分页数据项列表")
    @PostMapping(value = "/dataCheckRules/getListByCheckRuleId")
    @ResponseBody
    public ResultMsg getListByCheckRuleId(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        Object result = dataItemService.getListByCheckRuleId(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据通讯协议查询树形列表
     * @return
     */
    @ApiOperation(value = "根据通讯协议查询树形列表", notes = "根据通讯协议查询树形列表")
    @PostMapping(value = "/dataCheckRules/tree")
    @ResponseBody
    public ResultMsg tree(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = dataCheckRuleItemService.tree(pagerInfo);
        return ResultMsg.getResult(result);
    }



}
