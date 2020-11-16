package com.bitnei.cloud.api.web;

import com.bitnei.cloud.api.model.PushLogDTO;
import com.bitnei.cloud.api.model.PushLogModel;
import com.bitnei.cloud.api.service.IAccountService;
import com.bitnei.cloud.api.service.IPushDetailService;
import com.bitnei.cloud.api.service.IPushLogService;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 推送日志<br>
 * 描述： 推送日志控制器<br>
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
 * <td>2019-01-16 15:24:34</td>
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
@Api(value = "开放平台-推送日志", description = "开放平台-推送日志", tags = {"开放平台-推送日志"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/api")
@RequiredArgsConstructor
public class PushLogController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "PUSHLOG";
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

    private final IPushLogService pushLogService;
    private final IPushDetailService pushDetailService;
    private final IAccountService accountService;


    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/pushLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        PushLogModel pushLog = pushLogService.get(id);
        return ResultMsg.getResult(pushLog);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询，参数支持：isSuccess, beginTime, endTime," +
            "accountName, tokenName, pushName, pushId")
    @PostMapping(value = "/pushLogs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = pushLogService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/pushLog")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    @SneakyThrows
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) PushLogModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
//        AccountModel accountModel = accountService.get(demo1.getAccountId());
//        demo1.setPushBodyContent(RsaUtils.encryptByPublicKey(demo1.getPushBodyContent(), accountModel.getRsaPubKey()));
//        demo1.setRspBodyContent(RsaUtils.encryptByPublicKey(demo1.getRspBodyContent(), accountModel.getRsaPubKey()));
        pushLogService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/pushLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) PushLogModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        pushLogService.update(demo1);
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
    @DeleteMapping(value = "/pushLogs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = pushLogService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/pushLogs/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true,
            dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        pushLogService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/pushLogs/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        pushLogService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/pushLogs/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        pushLogService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 重新发送常量
     */
    private static final Integer RE_PUSH = 1;

    @ApiOperation(value = "重新推送", notes = "重新推送，把re_push_flag置为1")
    @PostMapping(value = "/pushLogs/repush")
    @RequiresPermissions(AUTH_UPDATE)
    @ResponseBody
    public ResultMsg updateRePushFlag(@RequestParam String id, @RequestParam(required = false) Integer rePushFlag) {

        pushLogService.updateRePushFlag(id, null != rePushFlag ? rePushFlag : 1);
        return ResultMsg.getResult("处理成功");
    }

    /**
     * 外部接口需不需要这样传参，有待商榷
     *
     * @param pagerInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "获取推送列表", notes = "获取推送列表")
    @PostMapping(value = "/accessLogs/getPushDetails")
    @RequiresPermissions(AUTH_LIST)
    @ResponseBody
    public ResultMsg getPushDetails(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = pushDetailService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    @ApiOperation(value = "批量保存推送日志", notes = "批量保存推送日志")
    @PostMapping(value = "/addPushLogs")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg batchAdd(@RequestBody @Validated({GroupInsert.class}) List<PushLogDTO> pushLogDTOList,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        pushLogDTOList.forEach(it -> {
            PushLogModel pushLogModel = new PushLogModel();
            BeanUtils.copyProperties(it, pushLogModel);
            pushLogService.insert(pushLogModel);
        });
        return ResultMsg.getResult("批量注册成功");
    }

}
