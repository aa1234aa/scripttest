package com.bitnei.cloud.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.model.AlarmInfoHistoryModel;
import com.bitnei.cloud.fault.model.AlarmInfoModel;
import com.bitnei.cloud.fault.service.IAlarmInfoHistoryService;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.AttentionVehModel;
import com.bitnei.cloud.sys.model.VehicleRealStatusModel;
import com.bitnei.cloud.sys.service.IAttentionVehService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.bitnei.cloud.sys.util.DateUtil;
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
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 用户关注车辆Lk<br>
 * 描述： 用户关注车辆Lk控制器<br>
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
 * <td>2019-03-19 18:45:16</td>
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
@Api(value = "车辆监控-用户关注Lk", description = "车辆监控-用户关注Lk", tags = {"车辆监控-用户关注Lk"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class AttentionVehController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "ATTENTIONVEH";
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

    private final IAttentionVehService attentionVehService;
    private final IUserService userService;
    private final IVehicleRealStatusService vehicleRealStatusService;
    private final IAlarmInfoService alarmInfoService;
    private final IAlarmInfoHistoryService alarmInfoHistoryService;

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/attentionVehs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        AttentionVehModel attentionVeh = attentionVehService.get(id);
        return ResultMsg.getResult(attentionVeh);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询：type：0:车辆，1: 故障, 2: 消息，" +
            "（默认只查询当前登录用户的关注列表）")
    @PostMapping(value = "/attentionVehs")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = attentionVehService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/attentionVeh")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) AttentionVehModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        attentionVehService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/attentionVehs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) AttentionVehModel demo1, @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        attentionVehService.update(demo1);
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
    @DeleteMapping(value = "/attentionVehs/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = attentionVehService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/attentionVehs/export/{param}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        attentionVehService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/attentionVehs/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        attentionVehService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/attentionVehs/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        attentionVehService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }

    /**
     * 关注车辆
     *
     * @param vehId
     * @return
     */
    @ApiOperation(value = "关注车辆", notes = "关注车辆：根据车辆id关注车辆")
    @GetMapping(value = "/addVehAttention/{vehId}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addVehAttention(@PathVariable String vehId) {

        //补充校验车辆是否存在逻辑
        try {
            vehicleRealStatusService.getByVehicleId(vehId);
        } catch (BusinessException e) {
            throw new BusinessException("关注的车辆不存在");
        }

        AttentionVehModel attentionVehModel = new AttentionVehModel();
        attentionVehModel.setVehId(vehId);
        attentionVehModel.setAttentionTime(DateUtil.getNow());

        String userId = userService.findByUsername(ServletUtil.getCurrentUser()).getId();

        //0:车辆，1: 故障, 2: 消息
        attentionVehModel.setType(0);
        attentionVehModel.setUserId(userId);

        attentionVehService.insert(attentionVehModel);
        return ResultMsg.getResult("关注成功");
    }

    /**
     * 取消关注车辆
     *
     * @param vehId
     * @return
     */
    @ApiOperation(value = "取消关注车辆", notes = "取消关注车辆：根据车辆id取消关注")
    @DeleteMapping(value = "/cancelVehAttention/{vehId}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg cancelVehAttention(@PathVariable String vehId) {

        String userId = userService.findByUsername(ServletUtil.getCurrentUser()).getId();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("vehId", vehId);

        attentionVehService.deleteBySqlId("deleteByUserIdAndVehId", params);
        return ResultMsg.getResult("取消关注成功");
    }

    /**
     * 关注
     *
     * @param itemId
     * @param type
     * @return
     */
    @ApiOperation(value = "关注", notes = "关注：根据业务id关注；参数：itemId（业务的id），" +
            "type（关注类型：0:车辆，1: 故障, 2: 消息）、faultBeginTime：如果关注的是历史故障，最好带上故障开始时间（非必填）")
    @GetMapping(value = "/addAttention/{itemId}/{type}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg addAttention(@PathVariable String itemId, @PathVariable Integer type,
                                  @RequestParam(required = false) String faultBeginTime) {

        String attentionJsonData = "";

        //补充校验关注的内容是否存在逻辑
        try {
            switch (type) {
                case 0: {
                    VehicleRealStatusModel model = vehicleRealStatusService.getByVehicleId(itemId);
                    attentionJsonData = JSONObject.toJSONString(model);
                    break;
                }
                case 1: {
                    try {
                        AlarmInfoModel model = alarmInfoService.get(itemId);
                        attentionJsonData = JSONObject.toJSONString(model);
                    } catch (BusinessException e) {
                        //如果实时的没有，就查历史的
                        AlarmInfoHistoryModel model = alarmInfoHistoryService.get(itemId, faultBeginTime);
                        attentionJsonData = JSONObject.toJSONString(model);
                        //如果历史的也没有，直接报错
                    }
                    break;
                }
                case 2: {
                    //暂未实现
                    break;
                }
                default:
            }
        } catch (BusinessException e) {
            throw new BusinessException("关注的内容不存在");
        }

        AttentionVehModel attentionVehModel = new AttentionVehModel();
        attentionVehModel.setVehId(itemId);
        attentionVehModel.setAttentionTime(DateUtil.getNow());

        String userId = userService.findByUsername(ServletUtil.getCurrentUser()).getId();

        //0:车辆，1: 故障, 2: 消息
        attentionVehModel.setType(type);
        attentionVehModel.setUserId(userId);
        attentionVehModel.setContent(attentionJsonData);

        attentionVehService.insert(attentionVehModel);
        return ResultMsg.getResult("关注成功");
    }

    /**
     * 取消关注
     *
     * @param itemId
     * @param type
     * @return
     */
    @ApiOperation(value = "取消关注", notes = "取消关注：根据业务id和类型取消关注：参数：itemId（业务的id）、" +
            "type（关注类型：0:车辆，1: 故障, 2: 消息）")
    @DeleteMapping(value = "/cancelAttention/{itemId}/{type}")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg cancelAttention(@PathVariable String itemId, @PathVariable Integer type) {

        String userId = userService.findByUsername(ServletUtil.getCurrentUser()).getId();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("vehId", itemId);
        params.put("type", type);

        attentionVehService.deleteBySqlId("deleteByUserIdAndVehId", params);
        return ResultMsg.getResult("取消关注成功");
    }

}
