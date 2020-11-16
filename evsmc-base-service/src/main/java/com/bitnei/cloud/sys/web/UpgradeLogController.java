package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.UpgradeLogModel;
import com.bitnei.cloud.sys.model.VehicleUpgradeLogSumDto;
import com.bitnei.cloud.sys.service.IUpgradeLogService;
import com.bitnei.cloud.sys.service.IUppackageSendDetailsService;
import com.bitnei.cloud.sys.util.ExcDownImpTemplatesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 远程升级日志<br>
 * 描述： 远程升级日志控制器<br>
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
 * <td>2019-03-09 09:56:09</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "远程升级日志", description = "远程升级日志", tags = {"远程升级日志"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class UpgradeLogController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "UPGRADELOG";
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
    private IUpgradeLogService upgradeLogService;

    private final IUppackageSendDetailsService uppackageSendDetailsService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/upgradeLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        UpgradeLogModel upgradeLog = upgradeLogService.get(id);
        return ResultMsg.getResult(upgradeLog);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询，条件：taskName，action，licensePlate，" +
            "createBy，beginTime，endTime")
    @PostMapping(value = "/upgradeLogs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = upgradeLogService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/upgradeLog")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UpgradeLogModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        upgradeLogService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/upgradeLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UpgradeLogModel demo1, @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        upgradeLogService.update(demo1);
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
    @DeleteMapping(value = "/upgradeLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = upgradeLogService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/upgradeLogs/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        upgradeLogService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/upgradeLogs/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        upgradeLogService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/upgradeLogs/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        upgradeLogService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 获取车辆升级记录列表
     *
     * @return
     */
    @ApiOperation(value = "获取车辆升级记录列表", notes = "获取车辆升级记录列表：licensePlate，vin，interNo，iccid")
    @PostMapping(value = "/vehicleUpgradeDetails")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg vehicleUpgradeDetails(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        PagerResult pr = uppackageSendDetailsService.vehicleUpgradeDetails(pagerInfo);

        return ResultMsg.getResult(pr);
    }

    @ApiOperation(value = "导入查询车辆升级记录", notes = "导入查询车辆升级记录")
    @PostMapping("/upgradeLogs/uploadSearch")
    @ResponseBody
    @SneakyThrows
    public ResultMsg uploadSearch(@RequestParam("file") MultipartFile file) {

        val mapResult = ExcDownImpTemplatesUtils.initHeaderAndDataToArray(
                file.getInputStream(), 2, 4);

        List<Map<String, String>> dataList = (List<Map<String, String>>) mapResult.get("dataMapList");
        List<String> vins = new ArrayList<>();
        List<String> interNos = new ArrayList<>();
        List<String> licensePlates = new ArrayList<>();
        List<String> iccids = new ArrayList<>();
        dataList.forEach(it -> {
            if (it.containsKey("vin") && null != it.get("vin"))
                vins.add(it.get("vin"));
            if (it.containsKey("interNo") && null != it.get("interNo"))
                interNos.add(it.get("interNo"));
            if (it.containsKey("licensePlate") && null != it.get("licensePlate"))
                licensePlates.add(it.get("licensePlate"));
            if (it.containsKey("iccid") && null != it.get("iccid"))
                iccids.add(it.get("iccid"));
        });

        //如果没有，补充-1，避免sql in查询报错
        if (CollectionUtils.isEmpty(vins)) vins.add("-1");
        if (CollectionUtils.isEmpty(interNos)) interNos.add("-1");
        if (CollectionUtils.isEmpty(licensePlates)) licensePlates.add("-1");
        if (CollectionUtils.isEmpty(iccids)) iccids.add("-1");
        val result = uppackageSendDetailsService.importSearchVehicleUpgradeDetails(
                vins, interNos, licensePlates, iccids);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "导入查询车辆升级记录模板下载", notes = "导入查询车辆升级记录模板下载")
    @GetMapping("/upgradeLogs/downImportTemplate")
    @ResponseBody
    @SneakyThrows
    public void downImportTemplate(HttpServletResponse response) {
        try {
            String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
            String srcFile = srcBase + "sys/res/upgradeLog/VehicleUpgradeLogModel.xls";
            File file = new File(srcFile);

            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(("查询车辆升级记录模板.xls").getBytes(), "iso-8859-1"));
            @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
            @Cleanup BufferedInputStream bis = new BufferedInputStream(fileInputStream);

            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                outputStream.write(buff, 0, bytesRead);
            }
            bis.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
           log.error("error", e);
            logger.error(e.getMessage());
        }
    }

    /**
     * 导出车辆升级记录
     *
     * @return
     */
    @ApiOperation(value = "导出车辆升级记录", notes = "导出车辆升级记录，传入参数应和分页查询保持一致")
    @GetMapping(value = "/upgradeLogs/exportVehicleUpgradeLog/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true,
            dataType = "String", paramType = "path")
    public void exportVehicleUpgradeLog(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        uppackageSendDetailsService.exportVehicleUpgradeLog(info);
        return;

    }
}
