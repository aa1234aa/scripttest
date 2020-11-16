package com.bitnei.cloud.fault.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.model.CodeRuleModel;
import com.bitnei.cloud.fault.service.ICodeRuleImportService;
import com.bitnei.cloud.fault.service.ICodeRuleItemService;
import com.bitnei.cloud.fault.service.ICodeRuleService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 故障码规则表<br>
 * 描述： 故障码规则表控制器<br>
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
 * <td>2019-02-25 16:55:47</td>
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
@Api(value = "故障码规则", description = "故障码规则操作", tags = {"故障报警-故障码规则"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/fault")
public class CodeRuleController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "CODERULE";
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
    private ICodeRuleService codeRuleService;

    @Autowired
    private ICodeRuleItemService codeRuleItemService;

    @Autowired
    private ICodeRuleImportService codeRuleImportService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/codeRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        CodeRuleModel codeRule = codeRuleService.get(id);
        codeRule.setItems(codeRuleItemService.list(id, true));
        return ResultMsg.getResult(codeRule);
    }

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询: faultCodeTypeId故障码类型Id, faultName=故障名称, vehModelId=车辆型号id", notes = "多条件查询")
    @PostMapping(value = "/codeRules")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = codeRuleService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/codeRule")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) CodeRuleModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        codeRuleImportService.checkStartPointAndExceptionCodeLength(model.getAnalyzeType(), model.getExceptionCodeLength(), model.getStartPoint(), errorMsg -> {
            throw new BusinessException(errorMsg);
        });
        codeRuleService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @ApiImplicitParam(name = "id", value = "ID值", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "/codeRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) CodeRuleModel demo1, @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        demo1.setId(id);
        codeRuleImportService.checkStartPointAndExceptionCodeLength(demo1.getAnalyzeType(), demo1.getExceptionCodeLength(), demo1.getStartPoint(), errorMsg -> {
            throw new BusinessException(errorMsg);
        });
        codeRuleService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/codeRules/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = codeRuleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/codeRules/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        codeRuleService.export(info);
        return;
    }

    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/codeRules/batchImport/{analyzeType}")
    @ApiImplicitParam(name = "analyzeType", value = "解析方式: 1=按字节解析，2＝按bit位解析", required = true, dataType = "String", paramType = "path")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@PathVariable @NotNull(message = "解析方式不能为空") Integer analyzeType, @RequestParam("file") MultipartFile file) {
        codeRuleImportService.batchImport(analyzeType, file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
    }

    /**
     * 导入模版下载
     *
     * @return
     */
    @ApiOperation(value = "导入模版下载", notes = "导入模版下载")
    @ApiImplicitParam(name = "analyzeType", value = "解析方式: 1=按字节解析，2＝按bit位解析", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/codeRules/importTemplateFile/{analyzeType}")
    @RequiresAuthentication
    public void getImportTemplateFile(@PathVariable Integer analyzeType) {
        codeRuleImportService.getImportTemplateFile(analyzeType);
        return;
    }
}
