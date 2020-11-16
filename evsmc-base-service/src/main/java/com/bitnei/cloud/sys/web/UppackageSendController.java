package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.CheckPasswordDto;
import com.bitnei.cloud.sys.model.UppackageSendModel;
import com.bitnei.cloud.sys.service.IUppackageInfoService;
import com.bitnei.cloud.sys.service.IUppackageSendService;
import com.bitnei.cloud.sys.service.impl.InstructTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 远程升级任务<br>
 * 描述： 远程升级任务控制器<br>
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
 * <td>2019-03-05 14:50:32</td>
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
@Api(value = "远程升级-远程升级任务", description = "远程升级-远程升级任务", tags = {"远程升级-远程升级任务"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class UppackageSendController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InstructTaskService instructTaskService;

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "UPPACKAGESEND";
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

    private final IUppackageSendService uppackageSendService;

    private final IUppackageInfoService uppackageInfoService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/uppackageSends/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        UppackageSendModel uppackageSend = uppackageSendService.get(id);
        return ResultMsg.getResult(uppackageSend);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询")
    @PostMapping(value = "/uppackageSends")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = uppackageSendService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/uppackageSend")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UppackageSendModel demo1,
                         @RequestParam(required = false) String filePath,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        UppackageSendModel model = uppackageSendService.getByTaskName(demo1.getTaskName().trim());
        if (null != model) {
            throw new BusinessException("任务名称已存在");
        }

        //自定义指令的方式，不通过文件路径，改为统一的ftp服务器的方式

        // 自定义升级指令 zxz
//        if (Constant.PROTOCOL_TYPE_CUSTOM.equals(demo1.getProtocolType().toString())) {
//            if (StringUtils.isEmpty(filePath)) {
//                throw new BusinessException("文件路径不能为空");
//            }
//            demo1.setFileName(filePath);
//        } else {
        //校验升级包密码
        uppackageInfoService.checkPassword(new CheckPasswordDto(demo1.getUppackageId(),
                demo1.getUppackagePassword()));
//        }

        //升级状态默认为0
        demo1.setUppackageSendStatus(0);
        uppackageSendService.insert(demo1);

        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/uppackageSends/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UppackageSendModel demo1, @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        uppackageSendService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/uppackageSends/export")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        uppackageSendService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/uppackageSends/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        uppackageSendService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/uppackageSends/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        uppackageSendService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 终止升级任务
     *
     * @param taskId 升级任务id
     * @return
     */
    @ApiOperation(value = "终止升级任务 ", notes = "终止升级任务：taskId就是任务的id")
    @PutMapping(value = "/uppackageSends/shutdownTask/{taskId}")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg shutdownTask(@PathVariable String taskId) {
        try {
            return ResultMsg.getResult(instructTaskService.shutdownTask(taskId));
        } catch (Exception e) {
            log.error("终止升级任务异常：" + e.getMessage());
            return ResultMsg.getResult("终止任务失败");
        }
    }

    /**
     * 终止单辆车的升级任务
     *
     * @param taskId 升级任务id
     * @param vin    车辆vin
     * @return
     */
    @ApiOperation(value = "终止单辆车的升级任务 ", notes = "终止单辆车的升级任务：taskId就是任务的id")
    @PutMapping(value = "/uppackageSends/shutdownTask/{taskId}/{vin}")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg shutdownVehicleTask(@PathVariable String taskId, @PathVariable String vin) {
        try {
            return ResultMsg.getResult(instructTaskService.shutdownVehicleTask(taskId, vin));
        } catch (Exception e) {
            log.error("终止升级任务异常：" + e.getMessage());
            return ResultMsg.getResult("终止任务失败");
        }
    }

    /**
     * 删除升级任务
     *
     * @param taskId 升级任务id
     * @return
     */
    @ApiOperation(value = "删除升级任务 ", notes = "删除升级任务：taskId就是任务的id")
    @DeleteMapping(value = "/uppackageSends/deleteTask/{taskId}")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg deleteTask(@PathVariable String taskId) {
        try {
            return ResultMsg.getResult(instructTaskService.deleteTask(taskId));
        } catch (Exception e) {
            log.error("删除升级任务异常：" + e.getMessage());
            return ResultMsg.getResult("删除升级任务失败");
        }
    }

    /**
     * 开始升级任务
     *
     * @param uppackageSendModel
     * @return
     */
    @ApiOperation(value = "开始升级任务 ", notes = "开始升级任务")
    @PostMapping(value = "/uppackageSends/sendUpdateInstruct")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg sendUpdateInstruct(@RequestBody UppackageSendModel uppackageSendModel) {
        try {
            return ResultMsg.getResult(instructTaskService.startTask(uppackageSendModel));
        } catch (Exception e) {
            log.error("下发升级指令异常：" + e.getMessage());
            return ResultMsg.getResult("任务启动失败");
        }
    }

}
