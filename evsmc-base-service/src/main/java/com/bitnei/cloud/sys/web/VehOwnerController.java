package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehOwnerModel;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 个人车主<br>
 * 描述： 个人车主控制器<br>
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
 * <td>2018-10-31 15:36:59</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author zxz
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "个人车主信息", description = "个人车主信息", tags = {"基础数据-用户基础-个人车主信息"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class VehOwnerController {


    /**
     * 模块基础请求前缀 *
     */
    public static final String BASE_AUTH_CODE = "VEHOWNER";
    /**
     * 查看 *
     */
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表 *
     */
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 新增 *
     */
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑 *
     */
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除 *
     */
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出 *
     */
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";
    /**
     * 导入 *
     */
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入 *
     */
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新 *
     */
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";

    @Autowired
    private IVehOwnerService vehOwnerService;

    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehOwners/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        VehOwnerModel vehOwner = vehOwnerService.get(id);
        return ResultMsg.getResult(vehOwner);
    }

    /**
     * 多条件查询
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/vehOwners")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehOwnerService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     * @param demo1 VehOwnerModel
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/vehOwner")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehOwnerModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehOwnerService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 保存
     *
     * @param demo1 VehOwnerModel
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/vehOwners/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehOwnerModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehOwnerService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除
     *
     * @param id ID列表，多个用逗号分隔
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/vehOwners/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = vehOwnerService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 导出
     *
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehOwners/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehOwnerService.export(info);
        return;
    }

    /**
     * 批量导入
     *
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/vehOwners/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {
        vehOwnerService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
    }

    /**
     * 批量更新
     *
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/vehOwners/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {
        vehOwnerService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }

    /**
     * 导入模板下载
     *
     * @return
     */
    @ApiOperation(value = "导入模板下载" , notes = "导入模板下载")
    @GetMapping(value = "/vehOwners/importTemplateFile")
    @RequiresAuthentication
    public void getImportTemplateFile(){
        vehOwnerService.getImportTemplateFile();
        return;
    }
}
