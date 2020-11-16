package com.bitnei.cloud.fault.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.fault.model.NotifierVehicleLkModel;
import com.bitnei.cloud.fault.service.INotifierSettingService;
import com.bitnei.cloud.fault.service.INotifierVehicleLkService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 故障<br>
* 描述： 故障控制器<br>
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
* <td>2019-03-06 17:37:36</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Slf4j
@Api(value = "报警提醒推送管理", description = "报警提醒推送管理",  tags = {"故障报警-报警提醒推送管理-分配车辆"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/fault")
public class NotifierVehicleLkController{

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="NOTIFIERVEHICLELK";
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
    private INotifierVehicleLkService notifierVehicleLkService;

    @Autowired
    private INotifierSettingService notifierSettingService;

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "负责人－车辆列表-多条件查询" , notes = "负责人车辆列表, 传notifierId 负责人id, 为必填项")
    @PostMapping(value = "/notifierVehicleLks")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = notifierVehicleLkService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "车辆－负责人列表-多条件查询" , notes = "车辆－负责人列表, 传vehicleId 车辆id, 为必填项")
    @PostMapping(value = "/notifierVehicleLks/notifiers")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg notifiers(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = notifierSettingService.notifiers(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "分配车辆", notes = "负责人-报警提醒推送管理-分配车辆: 只传notifierId、vehicleId 两个参数")
    @PostMapping(value = "/notifierVehicleLk")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) NotifierVehicleLkModel demo1, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        notifierVehicleLkService.insert(demo1);
//        return ResultMsg.getResult(notifierVehicleLkService.insert(demo1));
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 分配全部查询车辆
     *
     * @param pagerInfo 查询条件
     * @return
     */
    @ApiOperation(value = "分配全部查询车辆", notes = "传notifierId(通知人id; 多车分配时用 , 分开; 如：value1,value2), vin, licensePlate, operLicenseCity(地区id)")
    @PostMapping(value = "/notifierVehicleLk/all")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAllFind(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        notifierVehicleLkService.insertBatch(pagerInfo);
        return ResultMsg.getResult("分配成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "移除车辆", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/notifierVehicleLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){
        int count = notifierVehicleLkService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }
}
