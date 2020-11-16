package com.bitnei.cloud.monitor.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.monitor.domain.ElectronicFence;
import com.bitnei.cloud.monitor.model.ElectronicFenceModel;
import com.bitnei.cloud.monitor.service.IElectronicFenceService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.commons.datatables.PagerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 电子围栏<br>
 * 描述： 电子围栏控制器<br>
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
 * <td>2019-05-17 11:04:12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "电子围栏报警规则设置", description = "电子围栏报警规则设置", tags = {"故障报警-报警提醒规则-电子围栏报警规则设置"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/monitor")
public class ElectronicFenceController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀 *
     */
    public static final String BASE_AUTH_CODE = "ELECTRONICFENCE";
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
     * 删除围栏关联车辆信息 *
     */
    public static final String AUTH_DELETE_VEH_LK = BASE_AUTH_CODE + "_DELETE_VEH_LK";
    /**
     * 删除围栏关联车增加 *
     */
    public static final String AUTH_ADD_VEH_LK = BASE_AUTH_CODE + "_ADD_VEH_LK";
    /**
     * 围栏关联车列表查询 *
     */
    public static final String AUTH_VEH_LK_LIST = BASE_AUTH_CODE + "_VEH_LK_LIST";
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
    private IElectronicFenceService electronicFenceService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/electronicFences/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {
        ElectronicFenceModel electronicFence = electronicFenceService.get(id);
        return ResultMsg.getResult(electronicFence);
    }

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/electronicFences")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = electronicFenceService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/electronicFence")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) ElectronicFenceModel demo1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        electronicFenceService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/electronicFences/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) ElectronicFenceModel demo1, @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        electronicFenceService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 删除围栏
     *
     * @param id 围栏id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/electronicFences/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {
        int count = electronicFenceService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 分页查询未关联电子围栏的车辆
     *
     * @return
     */
    @ApiOperation(value = "分页查询未关联电子围栏的车辆", notes = "分页查询未关联电子围栏的车辆")
    @PostMapping(value = "/electronicFences/queryVehsNotLk")
    @ResponseBody
    @RequiresPermissions(AUTH_VEH_LK_LIST)
    public ResultMsg queryVehsNotLk(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = electronicFenceService.queryVehsNotLk(pagerInfo, null);
        return ResultMsg.getResult(result);
    }

    /**
     * 分页查询已关联电子围栏的车辆
     *
     * @return
     */
    @ApiOperation(value = "分页查询已关联电子围栏的车辆", notes = "分页查询已关联电子围栏的车辆")
    @PostMapping(value = "/electronicFences/queryVehsLk")
    @ResponseBody
    @RequiresPermissions(AUTH_VEH_LK_LIST)
    public ResultMsg queryVehsLk(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = electronicFenceService.queryVehsLk(pagerInfo, null);
        return ResultMsg.getResult(result);
    }

    /**
     * 添加围栏关联车辆
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     * @return
     */
    @ApiOperation(value = "添加围栏关联车辆", notes = "添加围栏关联车辆")
    @PostMapping(value = "/electronicFences/insertVehLk")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD_VEH_LK)
    @SLog(action = "添加围栏绑定车辆")
    public ResultMsg insertVehLk(@RequestBody @Validated PagerInfo pagerInfo) {
        electronicFenceService.insertVehLk(pagerInfo);
        return ResultMsg.getResult("绑定成功");
    }

    /**
     * 删除围栏关联车辆
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     * @return
     */
    @ApiOperation(value = "删除围栏关联车辆", notes = "删除围栏关联车辆")
    @PostMapping(value = "/electronicFences/deleteFenceVehLk")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE_VEH_LK)
    @SLog(action = "删除围栏关联车辆")
    public ResultMsg deleteFenceVehLk(@RequestBody @Validated PagerInfo pagerInfo) {
        electronicFenceService.deleteVehLk(pagerInfo, null, null);
        return ResultMsg.getResult("删除成功");
    }

    /**
     * 下载车辆导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "电子围栏关联车辆导入查询模版", notes = "电子围栏关联车辆导入查询模版")
    @GetMapping(value = "/electronicFences/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile() {
        electronicFenceService.getImportSearchFile();
        return;
    }


    //<editor-fold desc="无用代码">

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/electronicFences/export/{param}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {
        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        electronicFenceService.export(info);
        return;
    }

    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/electronicFences/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {
        electronicFenceService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));
    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/electronicFences/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {
        electronicFenceService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }
    //</editor-fold>
}
