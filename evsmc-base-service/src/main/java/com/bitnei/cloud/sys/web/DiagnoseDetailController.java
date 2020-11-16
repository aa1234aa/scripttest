package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.ExportDiagnoseDto;
import com.bitnei.cloud.sys.model.RemoteDiagnoseDetailModel;
import com.bitnei.cloud.sys.model.RemoteDiagnoseModel;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseDetailService;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseService;
import com.bitnei.cloud.sys.util.FileUtilDown;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 远程诊断明细<br>
 * 描述： 远程诊断明细控制器<br>
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
 * <td>2019-03-25 16:16:48</td>
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
@Api(value = "车辆监控-远程诊断明细", description = "车辆监控-远程诊断明细", tags = {"车辆监控-远程诊断明细"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class DiagnoseDetailController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "REMOTEDIAGNOSEDETAIL";
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

    private final IRemoteDiagnoseDetailService remoteDiagnoseDetailService;
    private final IRemoteDiagnoseService remoteDiagnoseService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/remoteDiagnoseDetails/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        RemoteDiagnoseDetailModel remoteDiagnoseDetail = remoteDiagnoseDetailService.get(id);
        return ResultMsg.getResult(remoteDiagnoseDetail);
    }

    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询，查询条件：diagnoseId（主诊断记录的id）")
    @PostMapping(value = "/remoteDiagnoseDetails")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = remoteDiagnoseDetailService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/remoteDiagnoseDetail")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) RemoteDiagnoseDetailModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        remoteDiagnoseDetailService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/remoteDiagnoseDetails/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) RemoteDiagnoseDetailModel demo1, @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        remoteDiagnoseDetailService.update(demo1);
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
    @DeleteMapping(value = "/remoteDiagnoseDetails/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = remoteDiagnoseDetailService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出诊断明细", notes = "导出诊断明细，参数：明细记录的id")
    @GetMapping(value = "/remoteDiagnoseDetails/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    public void export(@PathVariable String params, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        try {
            String data = URLDecoder.decode(params);
            ExportDiagnoseDto exportDiagnoseDto = new ObjectMapper().readValue(data, ExportDiagnoseDto.class);

            RemoteDiagnoseDetailModel remoteDiagnoseDetailModel =
                    remoteDiagnoseDetailService.get(exportDiagnoseDto.getDiagnoseId());

            RemoteDiagnoseModel remoteDiagnoseModel =
                    remoteDiagnoseService.get(remoteDiagnoseDetailModel.getDiagnoseId());

            List<RemoteDiagnoseDetailModel> result = new LinkedList<>();
            result.add(remoteDiagnoseDetailModel);
            String fileName = "内部编号" + remoteDiagnoseModel.getInterNo() + "远程诊断详情表.xls";

            final String userAgent = request.getHeader("user-agent");

            fileName = FileUtilDown.encodeDownloadFilename(fileName, userAgent);

            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            Map<String, String> headerMap = new LinkedHashMap<String, String>();
            headerMap.put("vin", "vin");
            headerMap.put("licensePlate", "车牌号");
            headerMap.put("diagnoseResult", "诊断结果");
            headerMap.put("uploaTime", "数据上传时间");
            headerMap.put("errorNum", "故障数");
            headerMap.put("rightNum", "正常数");
            headerMap.put("exceptionNum", "异常数");

            headerMap.put("faultName", "故障名称");
            headerMap.put("result", "故障状态");
            remoteDiagnoseDetailService.expFaultDetailInfo("远程诊断表", headerMap, result, response.getOutputStream());
        } catch (Exception e) {
            logger.error("报表数据列异常：" + e);
        }
    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/remoteDiagnoseDetails/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        remoteDiagnoseDetailService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/remoteDiagnoseDetails/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        remoteDiagnoseDetailService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

}
