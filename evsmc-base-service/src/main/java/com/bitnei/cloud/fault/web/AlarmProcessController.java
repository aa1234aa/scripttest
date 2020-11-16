package com.bitnei.cloud.fault.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.fault.model.AlarmProcessModel;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.IAlarmProcessService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 故障处理表<br>
 * 描述： 故障处理表控制器<br>
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
 * <td>2019-03-04 17:13:13</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "故障处理表", description = "故障处理表", tags = {"故障报警-故障处理"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/fault")
public class AlarmProcessController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "ALARMPROCESS";
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

    @Autowired
    private IAlarmProcessService alarmProcessService;

    @Autowired
    private IAlarmInfoHistoryService alarmInfoHistoryService;

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询=历史处理记录, 某一故障的id : faultAlarmId及故障的开始时间: faultBeginTime")
    @PostMapping(value = "/alarmProcesss")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Map<String, Object> param = ServletUtil.pageInfoToMap(pagerInfo);
        Object result;
        // 历史报警处理记录需从大数据查询获取
        if (param.containsKey("type") && "2".equals(param.get("type"))) {
            result = alarmInfoHistoryService.getAlarmProcess((String) param.get("faultAlarmId"));
        } else {
            result = alarmProcessService.list(pagerInfo);
        }
        return ResultMsg.getResult(result);
    }


    /**
     * 实时故障处理
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "故障处理", notes = "用type区分实时与历史; type = 1实时故障处理；type = 2 历史故障处理")
    @PostMapping(value = "/alarmProcess")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) AlarmProcessModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        model.checkParamLength();
        String msg = I18nUtil.t("common.addSuccess");
        if (1 == model.getType().intValue()) {
            alarmProcessService.insert(model);
        } else {
            boolean result = alarmInfoHistoryService.alarmProcess(model);
            if (!result) {
                msg = "新增失败";
            }
        }
        return ResultMsg.getResult(msg);
    }
}
