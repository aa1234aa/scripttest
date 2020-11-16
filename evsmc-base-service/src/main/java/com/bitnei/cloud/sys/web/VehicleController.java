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
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.domain.TermModel;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.cloud.sys.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 车辆列表<br>
 * 描述： 车辆列表控制器<br>
 * 授权 : (C) Copyright (c) 2018 <br>
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
 * <td>2018-12-12 17:40:52</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 */
@Slf4j
@Api(value = "车辆列表", description = "车辆列表相关Api", tags = {"基础数据-车辆信息-车辆列表"})
@Controller
@Validated
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class VehicleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "VEHICLE";
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
    private IVehicleService vehicleService;

    @Autowired
    private IVehiclePowerDeviceLkService powerDeviceLkService;

    @Autowired
    private IVehicleDriveDeviceLkService driveDeviceLkService;

    @Autowired
    private IVehicleEngeryDeviceLkService engeryDeviceLkService;

    @Autowired
    private IBatteryDeviceModelService batteryDeviceModelService;

    @Autowired
    private IDriveMotorModelService driveMotorModelService;

    @Autowired
    private ITermModelUnitService termModelUnitService;

    @Autowired
    private ITermModelService termModelService;

    @Autowired
    private IEngineModelService engineModelService;

    @Autowired
    private IEncryptionChipModelService encryptionChipModelService;

    @Autowired
    private IVehModelService vehModelService;

    @Autowired
    private IVehicleOperService vehicleOperService;

    @Autowired
    private IVehicleSellService vehicleSellService;

    @Autowired
    private IVehNoticeService vehNoticeService;

    @Autowired
    private IFilingRecordService filingRecordService;


    /**
     * 车辆列表分页多条件查询
     *
     * @return
     */
    @ApiOperation(value = "车辆列表", notes = "车辆列表分页多条件查询：查询条件：vehModelIds")
    @PostMapping(value = "/vehicles")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg vehicles(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehicleService.vehicleList(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 车辆列表分页多条件查询
     *
     * @return
     */
    @ApiOperation(value = "用户车辆权限车辆列表", notes = "用户车辆权限车辆列表")
    @PostMapping(value = "/vehicles/user/{userId}")
    @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg vehicles(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String userId) {

        Object result = vehicleService.userVehicles(userId, pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 车辆列表分页多条件查询
     *
     * @return
     */
    @ApiOperation(value = "用户车辆权限车辆列表", notes = "用户车辆权限车辆列表")
    @PostMapping(value = "/vehicles/not/user/{userId}")
    @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg vehiclesNot(@RequestBody @Validated PagerInfo pagerInfo, @PathVariable String userId) {

        Object result = vehicleService.userVehiclesNot(userId, pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 推送人分配车辆列表查询
     *
     * @return
     */
    @ApiOperation(value = "推送人分配车辆列表", notes = "推送人分配车辆列表分页多条件查询：查询条件：vehModelIds")
    @PostMapping(value = "/vehicles/findForLk")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg findForLk(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = vehicleService.findForLk(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        VehicleModel vehicle = vehicleService.get(id);
        return ResultMsg.getResult(vehicle);
    }


    /**
     * 根据车架号获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据车架号获取详细信息", notes = "根据车架号获取详细信息")
    @ApiImplicitParam(name = "vin", value = "车架号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/vin/{vin}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg vin(@PathVariable String vin) {

        VehicleModel vehicle = vehicleService.getByVin(vin);
        return ResultMsg.getResult(vehicle);


    }

    /**
     * 根据系统内部编码获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据系统内部编码获取详细信息", notes = "根据系统内部编码获取详细信息")
    @ApiImplicitParam(name = "uuid", value = "系统内部编码", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/uuid/{uuid}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg uuid(@PathVariable String uuid) {

        VehicleModel vehicle = vehicleService.getByUuid(uuid);
        return ResultMsg.getResult(vehicle);


    }

    /**
     * 根据车牌号获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据车牌号获取详细信息", notes = "根据车牌号获取详细信息")
    @ApiImplicitParam(name = "licensePlate", value = "车牌号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/licensePlate/{licensePlate}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg licensePlate(@PathVariable String licensePlate) {

        VehicleModel vehicle = vehicleService.getByLicensePlate(licensePlate);
        return ResultMsg.getResult(vehicle);


    }

    /**
     * 根据内部编号获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据内部编号获取详细信息", notes = "根据内部编号获取详细信息")
    @ApiImplicitParam(name = "interNo", value = "内部编号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/interNo/{interNo}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg interNo(@PathVariable String interNo) {

        VehicleModel vehicle = vehicleService.getByInterNo(interNo);
        return ResultMsg.getResult(vehicle);


    }

    /**
     * 根据运营内部编号获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据运营内部编号获取详细信息", notes = "根据运营内部编号获取详细信息")
    @ApiImplicitParam(name = "operInterNo", value = "运营内部编号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/operInterNo/{operInterNo}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg operInterNo(@PathVariable String operInterNo) {

        VehicleModel vehicle = vehicleService.getByOperInterNo(operInterNo);
        return ResultMsg.getResult(vehicle);


    }


    /**
     * 保存
     *
     * @param vehicleModel
     * @return
     */
    @ApiOperation(value = "新增车辆信息", notes = "新增车辆基本信息")
    @PostMapping(value = "/vehicle")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehicleModel vehicleModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        String[] eds = StringUtils.split(vehicleModel.getEngeryDeviceIds(), ",");
        if (eds != null && eds.length > 50) {
            return ResultMsg.getResult(null, "可充电储能装置最多50个", 300);
        }
        String[] dds = StringUtils.split(vehicleModel.getDriveDeviceIds(), ",");
        if (dds != null && dds.length > 4) {
            return ResultMsg.getResult(null, "驱动装置最多4个", 300);
        }
        String[] pds = StringUtils.split(vehicleModel.getPowerDeviceIds(), ",");
        if (pds != null && pds.length > 4) {
            return ResultMsg.getResult(null, "发电装置最多4个", 300);
        }
        vehicleService.insert(vehicleModel);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param vehicleModel
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息")
    @PutMapping(value = "/vehicles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) VehicleModel vehicleModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehicleService.update(vehicleModel);
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
    @DeleteMapping(value = "/vehicles/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id) {

        int count = vehicleService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicles/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehicleService.export(info);
        return;
    }


    /**
     * 国家平台数据导出
     *
     * @param params 查询参数json字符串，用urlEncoder编码
     * @throws IOException IO异常
     */
    @ApiOperation(value = "国家平台数据导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicles/nationExport/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void nationExport(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);
        vehicleService.nationExport(info);
        return;
    }


    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/vehicles/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        vehicleService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PostMapping(value = "/vehicles/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        vehicleService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));
    }


    @ApiOperation(value = "生成内部编号", notes = "生成车辆内部编号,临+随机7位英文与数字组合，英文字符大写，唯一")
    @GetMapping(value = "/vehicle/generateInterNo")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg generateInterNo() {

        String interNo = vehicleService.generateInterNo();
        Map<String, String> data = ImmutableMap.of("interNo", interNo);
        return ResultMsg.getResult(data);
    }


    @ApiOperation(value = "车辆可充电储能装置列表", notes = "车辆可充电储能装置列表")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/engeryDevices")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg engeryDevices(@PathVariable String id) {
        List<VehicleEngeryDeviceLkModel> models = engeryDeviceLkService.listByVehicleId(id);
        return ResultMsg.getResult(models);
    }

    @ApiOperation(value = "车辆发电装置列表", notes = "车辆发电装置列表")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/powerDevices")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg powerDevices(@PathVariable String id) {
        List<VehiclePowerDeviceLkModel> list = powerDeviceLkService.listByVehicleId(id, null);
        return ResultMsg.getResult(list);
    }


    @ApiOperation(value = "车辆驱动电机装置列表", notes = "车辆驱动电机装置列表")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/driveDevices")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg driveDevices(@PathVariable String id) {
        // 只查询驱动电机类型的驱动装置
        List<VehicleDriveDeviceLkModel> models = driveDeviceLkService.listByVehicleId(id, 1);
        return ResultMsg.getResult(models);
    }


    @ApiOperation(value = "车辆动力蓄电池型号列表", notes = "动力蓄电池型号列表")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/batteryDeviceModels")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg batteryDeviceModels(@PathVariable String id) {
        List<VehicleEngeryDeviceLkModel> list = engeryDeviceLkService.listByVehicleId(id);
        List<BatteryDeviceModelModel> batteryModelList = Lists.newArrayList();
        Map<String, String> map = Maps.newHashMap();
        for (VehicleEngeryDeviceLkModel model : list) {
            if (!map.containsKey(model.getBatteryModelId())) {
                map.put(model.getBatteryModelId(), model.getBatteryModelId());
                if (model.getBatteryModelId() != null) {
                    BatteryDeviceModelModel batteryModel = batteryDeviceModelService.get(model.getBatteryModelId());
                    batteryModelList.add(batteryModel);
                }
            }
        }
        return ResultMsg.getResult(batteryModelList);
    }


    @ApiOperation(value = "车辆驱动电机型号列表", notes = "驱动电机型号列表")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/driveMotorModels")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg driveMotorModels(@PathVariable String id) {
        List<VehicleDriveDeviceLkModel> models = driveDeviceLkService.listByVehicleId(id, null);
        List<DriveMotorModelModel> batteryModelList = Lists.newArrayList();
        Map<String, String> map = Maps.newHashMap();
        for (VehicleDriveDeviceLkModel model : models) {
            if (!map.containsKey(model.getDrvieDeviceId())) {
                map.put(model.getDrvieDeviceId(), model.getDrvieDeviceId());
                if (model.getDriveModelId() != null) {
                    DriveMotorModelModel motorModel = driveMotorModelService.get(model.getDriveModelId());
                    motorModel.setInstallPosition(model.getInstallPosition());
                    batteryModelList.add(motorModel);
                }
            }
        }
        return ResultMsg.getResult(batteryModelList);
    }


    @ApiOperation(value = "车辆终端型号详情", notes = "车辆终端型号详情")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}/termInfo")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg termInfo(@PathVariable String id) {
        VehicleModel model = vehicleService.get(id);
        TermModelUnitModel unitModel = termModelUnitService.get(model.getTermId());
        TermModelModel termModelModel = termModelService.get(unitModel.getSysTermModelId());
        return ResultMsg.getResult(termModelModel);
    }


    /**
     * 下载车辆导入查询模板
     *
     * @return
     */
    @ApiOperation(value = "车辆导入查询模板", notes = "车辆导入查询模板")
    @GetMapping(value = "/vehicles/importSearchFile")
    @RequiresAuthentication
    public void getImportSearchFile() {
        vehicleService.getImportSearchFile();
        return;
    }

    /**
     * 下载车辆导入模板
     *
     * @return
     */
    @ApiOperation(value = "车辆导入模板", notes = "车辆导入模板")
    @GetMapping(value = "/vehicles/importTemplateFile")
    @RequiresAuthentication
    public void getImportTemplateFile() {
        vehicleService.getImportTemplateFile();
        return;
    }


    /**
     * 批量更新模板下载
     *
     * @return
     */
    @ApiOperation(value = "批量更新数据导出", notes = "批量更新数据导出，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicles/batchUpdateTemplateFile/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void getBatchUpdateTemplateFile(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        vehicleService.getBatchUpdateTemplateFile(info);
        return;

    }

    /**
     * 离线导出
     */
    @ApiOperation(value = "离线导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @ApiImplicitParam(name = "pagerInfo", value = "查询参数", required = true, dataTypeClass = PagerInfo.class, paramType = "body")
    @RequiresPermissions(AUTH_EXPORT)
    @PostMapping(value = "/vehicles/export/offline")
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        final String taskId = vehicleService.exportOffline(pagerInfo);
        return ResultMsg.getResult("任务创建成功");

    }

    /**
     * 车辆列表分页多条件查询
     *
     * @return
     */
    @ApiOperation(value = "车辆列表", notes = "<pre>注意，该接口目前只提供给大数据那边使用，web端无需调用。\n" +
        "响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': [{\n" +
        "        'id': '00008021c6874e6fa20020ddebf3c48e',                      // 车辆主键ID\n" +
        "        'vin': 'JMETER11211532353',                                    // 车架号\n" +
        "        'vid': 'f13fac506b9445ffb63931febbc47ba9',                     // VID\n" +
        "        'licensePlate': '京A72697',                                    // 车牌号\n" +
        "        'interNo': '京A72697',                                         // 内部编号\n" +
        "        'vehModelId': 'xxxxxxxxxxxxxxxxxxxx',                          // 车型ID\n" +
        "        'vehModelName': '荣威2019E53轻奢侈款',                          // 车型名称\n" +
        "        'operLicenseCityName': ['广东省', '珠海市', '香洲区'],          // 上牌城市name路径\n" +
        "        'operLicenseCityCode': ['10001', '200001', '300001'],          // 上牌城市code路径\n" +
        "        'operLicenseCityPath': ['xxxxxxx', 'xxxxxxx', 'xxxxxx'] ,      // 上牌城市ID路径\n" +
        "        'operLicenseCityId': '402882d25c0eeeed015c0f5b16c50859' ,      // 上牌城市ID\n" +
        "        'operAreaName': ['广东省', '珠海市', '香洲区'],                 // 运营区域name路径\n" +
        "        'operAreaCode': ['10001', '200001', '300001'],                 // 运营区域code路径\n" +
        "        'operAreaPath': ['xxxxxxx', 'xxxxxxx', 'xxxxxx'],              // 运营区域ID路径\n" +
        "        'operAreaId': '402882d25c0eeeed015c0f5b16c50859',              // 运营区域ID\n" +
        "        'operUseForName': 'xxxxx',                                     // 车辆用途名称\n" +
        "        'operUseFor': '2',                                             // 车辆用途\n" +
        "        'vehNoticeID': 'xxxxXX',                                       // 车辆公告ID\n" +
        "        'vehNoticeName': 'xxxx',                                       // 车辆公告名称\n" +
        "        'vehUnitID': 'XXXXXX',                                         // 车辆厂商ID\n" +
        "        'vehUnitName': '吉利',                                         // 车辆厂商名称\n" +
        "        'operUnitId': 'XXXXXX',                                        // 运营单位ID\n" +
        "        'operUnitName': '吉利',                                        // 运营单位名称\n" +
        "        'createTime': '2019-05-22 06:48:26',                           // 车辆录入时间\n" +
        "        'updateTime': '2019-05-22 06:48:26'                            // 车辆最后修改时间\n" +
        "        'whoCanSeeMe': ['xxxxx', 'xxxxx', 'xxxxx']                    // 权限编码\n" +
        "    }]\n" +
        "}</pre>")
    @PostMapping(value = "/bigdata/vehicles")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vid", value = "车辆VID，不传默认查所有", paramType = "query"),
        @ApiImplicitParam(name = "updateTime", value = "车辆更新时间(yyyy-MM-dd)，不传默认查所有", paramType = "query")
    })
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg bigDataVehicles( String vid,
        @Pattern(regexp = "^$|[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String updateTime) {

        List<VehicleInfoModel> result = vehicleService.vehicles(vid, updateTime);
        return ResultMsg.getResult(result);
    }


    @ApiOperation(value = "根据车辆ID查询芯片型号信息,上报环保平台", notes = "根据车辆ID查询芯片型号信息,上报环保平台,openservice使用")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}/chipModel")
    @ResponseBody
    public ResultMsg chipModel(@PathVariable String id) {
        String chipModelId = vehicleService.getChipModelId(id);
        if(StringUtils.isNotEmpty(chipModelId)) {
            // 检查芯片型号是否上报过  暂时屏蔽,以国六平台芯片型号上报为标准
//            FilingRecordModel rm = filingRecordService.getByFromId(chipModelId);
//            if(rm == null) {
                EncryptionChipModelModel model = encryptionChipModelService.get(chipModelId);
                return ResultMsg.getResult(model);
//            }
        }
        return ResultMsg.getResult(null);
    }


    @ApiOperation(value = "根据车辆ID查询车载终端型号信息,上报环保平台", notes = "根据车辆ID查询车载终端型号信息,上报环保平台,openservice使用")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}/termModel")
    @ResponseBody
    public ResultMsg termModel(@PathVariable String id) {
        TermModel model = termModelService.getByVehicleId(id);
        if(model != null ) {
            // 检查车载终端型号是否上报过
            FilingRecordModel rm = filingRecordService.getByFromId(model.getId());
            if(rm == null) {
                EcoTBoxModel tboxModel = EcoTBoxModel.fromEntry(model);
                return ResultMsg.getResult(tboxModel);
            }
        }
        return ResultMsg.getResult(null);
    }

    @ApiOperation(value = "根据车辆ID查询燃料形式为柴油的发动机型号列表,上报环保平台", notes = "根据车辆ID查询燃料形式为柴油的发动机型号列表,上报环保平台,openservice使用")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/vehicles/{id}/engineModels")
    @ResponseBody
    public ResultMsg engineModels(@PathVariable String id) {
        List<VehicleDriveDeviceLkModel> models = driveDeviceLkService.listByVehicleId(id, 2);
        List<EcoEngineModel> engines = Lists.newArrayList();
        for (VehicleDriveDeviceLkModel model : models) {
            if(StringUtils.isNotBlank(model.getEngineModelId())) {
                EngineModelModel engineModel = engineModelService.get(model.getEngineModelId());
                if(Constant.FUEL_FORM_DIESEL.equals(engineModel.getFuelForm())) {
                    // 检查柴油发动机是否上报过
                    FilingRecordModel rm = filingRecordService.getByFromId(model.getEngineModelId());
                    if(rm == null) {
                        EcoEngineModel entry = EcoEngineModel.fromEntry(engineModel);
                        engines.add(entry);
                    }
                }
            }
        }
        return ResultMsg.getResult(engines);
    }

    @ApiOperation(value = "根据车辆ID查询车型信息,上报环保平台", notes = "根据车辆ID查询车型信息,上报环保平台,openservice使用")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}/vehModel")
    @ResponseBody
    public ResultMsg vehModel(@PathVariable String id) {
        VehicleModel model = vehicleService.get(id);
        if(model != null) {
            VehModelModel m = vehModelService.get(model.getVehModelId());
            if(Constant.POWER_MODE_FUEL.equals(m.getPowerMode())) {
                // 检查车型是否上报过
                FilingRecordModel rm = filingRecordService.getByFromId(m.getId());
                if(rm == null) {
                    // 终端型号&芯片型号
                    TermModel term = termModelService.getByVehicleId(id);
                    EcoTBoxModel tboxModel = EcoTBoxModel.fromEntry(term);
                    // 厂商品牌
                    VehNoticeModel noticeModel = vehNoticeService.get(m.getNoticeId());
                    // 发动机型号
                    String engineModelName = "";
                    List<VehicleDriveDeviceLkModel> ddModels = driveDeviceLkService.listByVehicleId(id, 2);
                    for (VehicleDriveDeviceLkModel dd : ddModels) {
                        if(StringUtils.isNotBlank(dd.getEngineModelId()) &&
                                Constant.FUEL_FORM_DIESEL.equals(dd.getFuelForm())) {
                            engineModelName = dd.getEngineModelName();
                            break;
                        }
                    }
                    EcoVehModel ecoVehModel = EcoVehModel.fromEntry(m, tboxModel, noticeModel, engineModelName);
                    return ResultMsg.getResult(ecoVehModel);
                }
            }
        }
        return ResultMsg.getResult(null);
    }


    @ApiOperation(value = "根据车辆ID查询车辆信息,上报环保平台", notes = "根据车辆ID查询车辆信息,上报环保平台,openservice使用")
    @ApiImplicitParam(name = "id", value = "车辆ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/vehicles/{id}/vehicle")
    @ResponseBody
    public ResultMsg vehicle(@PathVariable String id) {
        VehicleModel model = vehicleService.get(id);
        VehModelModel vehModel = vehModelService.get(model.getVehModelId());
        if(vehModel != null ) {
            if(Constant.POWER_MODE_FUEL.equals(vehModel.getPowerMode())) {
                // 检查车辆是否上报过
                FilingRecordModel rm = filingRecordService.getByFromId(model.getId());
                if(rm == null) {
                    VehicleOperModel operModel = vehicleOperService.get(id);
                    VehicleSellModel sellModel = vehicleSellService.get(id);
                    TermModel term = termModelService.getByVehicleId(id);
                    EcoTBoxModel tboxModel = EcoTBoxModel.fromEntry(term);
                    String iccid = StringUtils.ts(term.get("iccid"));
                    String networkAccessNumber = StringUtils.ts(term.get("networkAccessNumber"));
                    VehicleDriveDeviceLkModel lk = getVehicleDriveDeviceLkModel(id);
                    EcoVehicleModel ecoVehicleModel = EcoVehicleModel.fromEntry(model, vehModel, operModel, sellModel,
                            tboxModel, lk, iccid, networkAccessNumber);
                    return ResultMsg.getResult(ecoVehicleModel);
                } else {
                    return ResultMsg.getResult(null, "车辆不允许重复上报", 200);
                }
            } else {
                return ResultMsg.getResult(null, "动力方式错误", 200);
            }
        }
        return ResultMsg.getResult(null, "车辆数据不存在", 200);
    }


    private VehicleDriveDeviceLkModel getVehicleDriveDeviceLkModel(String id){
        List<VehicleDriveDeviceLkModel> models = driveDeviceLkService.listByVehicleId(id, 2);
        for (VehicleDriveDeviceLkModel model : models) {
            if(Constant.FUEL_FORM_DIESEL.equals(model.getFuelForm())) {
                return model;
            }
        }
        return null;
    }



}
