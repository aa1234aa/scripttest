package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.common.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;

import java.io.IOException;
import java.util.List;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 单位列表<br>
 * 描述： 单位列表控制器<br>
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
 * <td>2018-11-05 17:33:20</td>
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
@Api(value = "单位列表", description = "单位列表", tags = {"基础数据-用户基础-单位管理"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class UnitController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀 *
     */
    public static final String BASE_AUTH_CODE = "UNIT";
    /**
     * 查看 *
     */
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表 *
     */
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页 *
     */
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
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
    private IUnitService unitService;

    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/units/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        UnitModel unit = unitService.get(id);
        return ResultMsg.getResult(unit);
    }

    /**
     * 根据单位名称获取对象
     */
    @ApiOperation(value = "根据单位名称获取详细信息", notes = "根据单位名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "单位名称", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/units/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name) {
        UnitModel unit = unitService.getByName(name);
        return ResultMsg.getResult(unit);
    }

    /**
     * 根据单位简称获取对象
     */
    @ApiOperation(value = "根据单位简称获取详细信息", notes = "根据单位简称获取详细信息")
    @ApiImplicitParam(name = "nickName", value = "单位简称", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/units/nickName/{nickName}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg nickName(@PathVariable String nickName) {
        UnitModel unit = unitService.getByNickName(nickName);
        return ResultMsg.getResult(unit);
    }

    /**
     * 根据统一社会信用代码获取对象
     */
    @ApiOperation(value = "根据统一社会信用代码获取详细信息", notes = "根据统一社会信用代码获取详细信息")
    @ApiImplicitParam(name = "organizationCode", value = "统一社会信用代码", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/units/organizationCode/{organizationCode}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg organizationCode(@PathVariable String organizationCode) {
        UnitModel unit = unitService.getByOrganizationCode(organizationCode);
        return ResultMsg.getResult(unit);
    }

    /**
     * 多条件查询
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/units")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = unitService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 多条件查询树形结构
     */
    @ApiOperation(value = "多条件查询树形结构", notes = "多条件查询树形结构")
    @PostMapping(value = "/units/tree")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg tree(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = unitService.tree(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     * @param demo1 UnitModel
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/unit")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UnitModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        unitService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 更新
     * @param demo1 UnitModel
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/units/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UnitModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        unitService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除
     * @param id id
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/units/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = unitService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 导出
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/units/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        unitService.export(info);
        return;
    }

    /**
     * 批量导入
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/units/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {
        unitService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PostMapping(value = "/units/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {
        unitService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }

    /**
     * 根据单位名称获取单位列表
     */
    @ApiOperation(value = "根据单位名称获取单位列表 ", notes = "根据单位名称获取单位列表")
    @PutMapping(value = "/units/getListByName")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public List<UnitModel> getListByName(String name) {
        return unitService.getListByName(name);
    }

    /**
     * 根据单位ID获取单位列表
     */
    @ApiOperation(value = "根据单位ID获取单位列表", notes = "根据单位ID获取单位列表,支持模糊查询")
    @PutMapping(value = "/units/getListById")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public List<UnitModel> getListById(String id) {
        return unitService.getListById(id);
    }

    /**
     * 导入模版下载
     *
     * @return
     */
    @ApiOperation(value = "导入模版下载" , notes = "导入模版下载")
    @GetMapping(value = "/units/importTemplateFile")
    @RequiresAuthentication
    public void getImportTemplateFile(){
        unitService.getImportTemplateFile();
        return;
    }
}
