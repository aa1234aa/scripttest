package com.bitnei.cloud.sms.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sms.model.FieldModel;
import com.bitnei.cloud.sms.model.SmsTaskModel;
import com.bitnei.cloud.sms.model.SmsTemplateModel;
import com.bitnei.cloud.sms.service.ISmsTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 短信下发任务<br>
 * 描述： 短信下发任务控制器<br>
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
 * <td>2019-08-16 09:41:04</td>
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
@Api(value = "短信下发任务", description = "短信下发任务", tags = {"短信下发任务"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sms")
public class SmsTaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public final static String BASE_AUTH_CODE = "SMSTASK";
    /**
     * 查看
     **/
    public final static String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表
     **/
    public final static String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页
     **/
    public final static String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增
     **/
    public final static String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑
     **/
    public final static String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除
     **/
    public final static String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出
     **/
    public final static String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";

    @Autowired
    private ISmsTaskService smsTaskService;


    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/smsTasks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        SmsTaskModel smsTask = smsTaskService.get(id);
        return ResultMsg.getResult(smsTask);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/smsTasks")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo) {
        Object result = smsTaskService.receiverPagerModel(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/smsTask")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) SmsTaskModel model) {
        smsTaskService.insert(model);
        return ResultMsg.getResult("新增成功");
    }

    /**
     * 更新
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/smsTasks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) SmsTaskModel model, @PathVariable String id) {
        smsTaskService.update(model);
        return ResultMsg.getResult("更新成功");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/smsTasks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = smsTaskService.deleteMulti(id);
        return ResultMsg.getResult(String.format("删除成功，共删除了%d条记录", count));
    }

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/smsTasks/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        smsTaskService.export(info);
        return;
    }

    /**
     * 详情导出
     *
     * @return
     */
    @ApiOperation(value = "详情导出", notes = "详情数据导出，传入taskId")
    @GetMapping(value = "/smsTasks/exportDetails/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void exportDetails(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        smsTaskService.exportDetails(info);
        return;
    }

    /**
     * 短信模板接口
     *
     * @return
     */
    @ApiOperation(value = "短信模板接口", notes = "根据ID短信模板接口信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/smsTasks/smsTemplate/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg smsTemplate(@PathVariable String id) {
        SmsTemplateModel smsTemplate = smsTaskService.getSmsTemplate(id);
        return ResultMsg.getResult(smsTemplate);
    }

    /**
     * 短信预览
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "短信预览", notes = "短信预览")
    @PostMapping(value = "/smsTask/preview")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg preview(@RequestBody @Validated({GroupInsert.class}) SmsTaskModel model) {
        return ResultMsg.getResult(smsTaskService.preview(model));
    }

    @ApiOperation(value = "添加全部查询结果", notes = "添加全部查询结果")
    @PostMapping(value = "/smsTask/all")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAllFind(@RequestBody @Validated({GroupInsert.class}) SmsTaskModel model) {
        smsTaskService.batchInsert(model);
        return ResultMsg.getResult("新增成功");
    }

    /**
     * 下载用户导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "下载用户导入查询模板", notes = "下载用户导入查询模板")
    @GetMapping(value = "/smsTask/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile() {
        smsTaskService.getImportSearchFile();
        return;
    }
}
