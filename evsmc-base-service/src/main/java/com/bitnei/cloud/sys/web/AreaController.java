package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.Area;
import com.bitnei.cloud.sys.model.AreaModel;
import com.bitnei.cloud.sys.service.IAreaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 行政区域<br>
 * 描述： 行政区域控制器<br>
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
 * <td>2018-12-27 09:27:18</td>
 * <td>lijiezhou</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author lijiezhou
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "行政区域", description = "行政区域", tags = {"系统设置-行政区域"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class AreaController {


    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "AREA";
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

    @Resource
    private IAreaService areaService;


    /**
     * 根据id获取对象
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/areas/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        AreaModel area = areaService.get(id);
        return ResultMsg.getResult(area);
    }


    /**
     * 多条件查询
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/areas")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = areaService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 查询所有区域
     */
    @ApiOperation(value = "查询指定所有区域", notes = "查询指定所有区域")
    @PostMapping(value = "/areas/tree")
    @ResponseBody
    public ResultMsg queryAllModules(@RequestBody @Validated PagerInfo pagerInfo) {
        TreeNode modules = areaService.queryAllArea(pagerInfo);
        return ResultMsg.getResult(modules);
    }


    /**
     * 树结构
     *
     */
    @ApiOperation(value = "树形结构", notes = "树形结构")
    @ApiImplicitParam(name = "parentId", value = "父节点id", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/areas/tree/{parentId}")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg tree(@PathVariable String parentId) {

        Object result = areaService.tree(parentId);
        return ResultMsg.getResult(result);
    }


    /**
     * 根据名称获取对象
     *
     */
    @ApiOperation(value = "根据名称获取详细信息", notes = "根据名称获取详细信息")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/areas/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name) {

        AreaModel area = areaService.getByName(name);
        return ResultMsg.getResult(area);


    }

    /**
     * 保存
     *
     * @param demo1 AreaModel
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/area")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) AreaModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        areaService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1 AreaModel
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/areas/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) AreaModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        areaService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }


    /**
     * 删除
     *
     * @param id ID列表，多个用逗号分隔
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/areas/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        String[] arr = id.split(",");
        for (String i : arr) {
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", i);
            List<Area> list = areaService.findBySqlId("findByParentId", params);
            if (CollectionUtils.isNotEmpty(list)) {
                return ResultMsg.getResult(null, "该节点存在子节点，不可删除", 300);
            }
        }
        int count = areaService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/areas/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        areaService.export(info);
        return;

    }


    /**
     * 批量导入
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/areas/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        areaService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/areas/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        areaService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }


}
