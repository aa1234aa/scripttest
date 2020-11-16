package com.bitnei.cloud.dc.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.model.ForwardRuleModel;
import com.bitnei.cloud.dc.service.IForwardRuleService;
import com.bitnei.cloud.common.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

import java.io.IOException;
import java.net.URLDecoder;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 转发规则<br>
* 描述： 转发规则控制器<br>
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
* <td>2019-02-20 10:32:15</td>
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
@Api(value = "转发规则", description = "转发规则",  tags = {"数据转发-转发规则"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/dc")
public class ForwardRuleController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="FORWARDRULE";
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

    @Autowired
    private IForwardRuleService forwardRuleService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/forwardRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        ForwardRuleModel forwardRule = forwardRuleService.get(id);
        return ResultMsg.getResult(forwardRule);
    }



    /**
     * 根据名称获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据名称获取详细信息" , notes = "根据名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/forwardRules/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        ForwardRuleModel forwardRule = forwardRuleService.getByName(name);
        return ResultMsg.getResult(forwardRule);


    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/forwardRules")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardRuleService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/forwardRule")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) ForwardRuleModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        forwardRuleService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 保存规则明细
     * @param demo
     * @return
     */
    @ApiOperation(value = "新增规则明细", notes = "新增规则明细信息")
    @PostMapping(value = "/forwardRule/addRuleItems")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addRuleItems(@RequestBody @Validated({GroupInsert.class}) ForwardRuleModel demo, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        forwardRuleService.insertRuleItems(demo);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/forwardRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) ForwardRuleModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        forwardRuleService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/forwardRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = forwardRuleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/forwardRules/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        forwardRuleService.export(info);
        return ;

    }

    /**
     * 获取已选车辆列表
     * @return
     */
    @ApiOperation(value = "获取已选车辆列表" , notes = "获取已选车辆列表")
    @PostMapping(value = "/forwardRules/selectedVehs/{ruleId}")
    @ResponseBody
    public ResultMsg selectedVehs(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String ruleId){

        Object result = forwardRuleService.selectedVehs(pagerInfo, ruleId);
        return ResultMsg.getResult(result);
    }

    /**
     * 移除已选车辆
     * @return
     */
    @ApiOperation(value = "移除已选车辆" , notes = "移除已选车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ruleId", value = "转发规则ID", required = true, dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "delVehIds", value = "移除车辆ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")}
    )
    @DeleteMapping(value = "/forwardRules/{ruleId}/vehs/{delVehIds}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteVehs (@PathVariable String ruleId, @PathVariable String delVehIds){
        int count = forwardRuleService.deleteVehs(ruleId, delVehIds);
        return ResultMsg.getResult("移除已选车辆成功");
    }

    /**
     * 保存全部车辆
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "添加全部车辆", notes = "添加全部车辆")
    @PostMapping(value = "/forwardRule/addAllVehs/{ruleId}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAllVehs(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String ruleId){
        forwardRuleService.addAllVehs(pagerInfo, ruleId);
        return ResultMsg.getResult("添加全部车辆成功");
    }

    /**
     * 添加车辆列表
     * @param pagerInfo
     * @return
     */
    @ApiOperation(value = "添加车辆列表", notes = "添加车辆列表")
    @PostMapping(value = "/forwardRule/vehsList/{ruleId}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg vehsList(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String ruleId){

        Object result = forwardRuleService.vehsList(pagerInfo, ruleId);
        return ResultMsg.getResult(result);
    }

    /**
     * 多条件查询转发规则及规则配置描述
     * @return
     */
    @ApiOperation(value = "多条件查询转发规则及规则配置描述" , notes = "多条件查询转发规则及规则配置描述")
    @PostMapping(value = "/listRulesAndDesc")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg listRulesAndDesc(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = forwardRuleService.listRulesAndDesc(pagerInfo);
        return ResultMsg.getResult(result);
    }



}
