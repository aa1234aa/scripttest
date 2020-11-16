package com.bitnei.cloud.sys.web;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.api.StringObjMsg;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.handler.OfflineExportHandler;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.model.DownloadTaskModel;
import com.bitnei.cloud.sys.service.DownloadTaskService;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 离线导出<br>
 * 描述： 离线导出控制器<br>
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
 * <td>2019-04-03 13:33:38</td>
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
@Api(value = "下载任务管理", tags = {"系统设置 - 个人中心 - 下载任务管理"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class DownloadTaskController {

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "DOWNLOAD_TASK";
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
    /**
     * 下载
     **/
    public static final String AUTH_DOWNLOAD = BASE_AUTH_CODE + "_DOWNLOAD";

    @Value("${" + OfflineExportHandler.EXPORT_DIRECTORY_CONFIG_KEY + "}")
    private String exportDirectory;

    @Autowired
    private DownloadTaskService downloadTaskService;

    @ApiOperation(value = "清空任务", notes = "清空当前用户的任务")
    @ApiResponses({
        @ApiResponse(code = 200, message = "清空成功，共删除了%d条记录", response = StringObjMsg.class)
    })
    @DeleteMapping(value = "/task/downloads")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete() {
        final int count = downloadTaskService.delete();
        return ResultMsg.getResult(String.format("清空成功，共删除了%d条记录", count));
    }

    @ApiOperation(value = "删除任务", notes = "删除当前用户的任务")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "任务标识，多个用逗号分隔", dataTypeClass = String.class, paramType = "path")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "删除成功，共删除了%d条记录", response = StringObjMsg.class)
    })
    @DeleteMapping(value = "/task/downloads/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(
        @PathVariable String id) {

        int count = 0;
        if (StringUtils.isNotBlank(id)) {
            final String[] ids = StringUtils.split(id, ',');
            if (ArrayUtils.isNotEmpty(ids)) {
                count = downloadTaskService.delete(ids);
            }
        }
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));
    }

    /**
     * 下载文件 -> 任务状态已完成，将文件下载到本地
     */
    @ApiOperation(value = "下载文件", notes = "下载导出文件", produces = "text/csv;charset=UTF-8, text/plain;charset=UTF-8")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Range", dataTypeClass = String.class, paramType = "header"),
        @ApiImplicitParam(
            name = "params", value = "文件名参数json字符串，用urlEncoder编码", required = true, dataTypeClass = ExportFileName.class, paramType = "path",
            example = "{\"exportFileName\":\"offline-export.csv\"}"
        )
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "text/csv;charset=UTF-8", response = byte[].class),
        @ApiResponse(code = 404, message = "文件[{exportFileName}]不存在", response = StringObjMsg.class)
    })
    @RequiresPermissions(AUTH_DOWNLOAD)
    @GetMapping(value = "/task/downloads/{params}")
    @ResponseBody
    public ResponseEntity get(
        @PathVariable String params)
        throws UnsupportedEncodingException {

        final ExportFileName args;
        try {
            args = JSON.parseObject(params, ExportFileName.class);
        } catch (final Exception e) {

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    ResultMsg.getResult(
                        null,
                        "非法参数",
                        HttpStatus.BAD_REQUEST.value()
                    )
                );
        }

        if (StringUtils.isBlank(args.exportFileName)) {

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    ResultMsg.getResult(
                        null,
                        "文件未指定",
                        HttpStatus.BAD_REQUEST.value()
                    )
                );
        }

        final String property = Optional.ofNullable(exportDirectory).orElse(".");
        final String createBy = ServletUtil.getCurrentUser();
        final File file = Paths.get(property, createBy, args.exportFileName).toAbsolutePath().toFile();

        if (file.exists()) {
            final HttpHeaders headers = new HttpHeaders();

            // [RFC4180](https://www.ietf.org/rfc/rfc4180.txt)
            headers.setContentType(MediaType.valueOf("text/csv;charset=UTF-8"));

            // [Content-Disposition](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition)
            headers.setContentDisposition(
                ContentDisposition
                    .builder("attachment")
                    .filename(URLEncoder.encode(args.exportFileName, StandardCharsets.UTF_8.name()))
                    .build()
            );

            final FileSystemResource resource = new FileSystemResource(file);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    ResultMsg.getResult(
                        null,
                        String.format("文件[%s]不存在", args.exportFileName),
                        HttpStatus.NOT_FOUND.value()
                    )
                );
        }
    }

    @ApiOperation(value = "任务列表", notes = "当前用户任务列表")
    @ApiImplicitParams({
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "请求成功", response = DownloadTaskListMsg.class, reference = "DownloadTaskModel")
    })
    @PostMapping(value = "/task/downloads")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg post(
        @RequestBody @Validated PagerInfo pagerInfo,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        final PagerResult result = downloadTaskService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "重试或取消")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", required = true, value = "任务标识，多个用逗号分隔", dataTypeClass = String.class, paramType = "path")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "更新成功，共更新了%d条记录", response = StringObjMsg.class)
    })
    @PutMapping(value = "/task/downloads/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg put(
        @PathVariable final String id,
        @RequestBody @Validated final DownloadTaskModel task,
        final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        int count = 0;
        if (StringUtils.isNotBlank(id)) {
            final String[] ids = StringUtils.split(id, ',');
            if (ArrayUtils.isNotEmpty(ids)) {
                final int taskStateCode = NumberUtils.toInt(task.getTaskStateCode(), 0);
                switch (taskStateCode) {
                    case OfflineExportStateMachine.CREATED:
                        downloadTaskService.retry(ids);
                        break;
                    case OfflineExportStateMachine.CANCELED:
                        downloadTaskService.cancel(ids);
                        break;
                    default: break;
                }
                count = ids.length;
            }
        }
        return ResultMsg.getResult(String.format("更新成功，共更新了%d条记录", count));
    }

    private static class DownloadTaskListMsg extends ResultListMsg<DownloadTaskModel> {

    }

    @ApiModel
    @Data
    private static class ExportFileName {

        @ApiModelProperty
        private String exportFileName;
    }
}
