package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.model.AlarmMakerModel;
import com.bitnei.cloud.fault.service.IAlarmInfoService;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.IVehicleRealStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 车辆动态信息<br>
 * 描述： 车辆动态信息控制器<br>
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
 * <td>2019-03-16 14:55:45</td>
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
@Api(value = "车辆监控-车辆动态信息", description = "车辆监控-车辆动态信息", tags = {"车辆监控-车辆动态信息"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class VehicleRealStatusController {


    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "VEHICLEREALSTATUS";
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


    private final IVehicleRealStatusService vehicleRealStatusService;
    private final IAlarmInfoService alarmInfoService;


//    /**
//     * 根据id获取对象
//     *获取详情统一采用车辆ID，该方法暂时废弃
//     * @return
//     */
//    @ApiOperation(value = "车辆实时状态详细信息", notes = "根据ID获取详细信息")
//    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
//    @GetMapping(value = "/vehicleRealStatuss/{id}")
//    @ResponseBody
//    @RequiresPermissions(AUTH_DETAIL)
//    public ResultMsg get(@PathVariable String id) {
//
//        VehicleRealStatusModel vehicleRealStatus = vehicleRealStatusService.get(id);
//        return ResultMsg.getResult(vehicleRealStatus);
//    }


    /**
     * 根据车辆id获取对象
     *
     * @return
     */
    @ApiOperation(value = "车辆实时状态详细信息", notes = "根据车辆ID获取详细信息,数据项dataNos用逗号分隔，也可传-1代表查询所有详细数据项")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "dataNos", value = "dataNos", required = true, dataType = "String", paramType = "path")}

    )
    @GetMapping(value = "/vehicleRealStatussByVId/{id}/{dataNos}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getByVId(@PathVariable String id, @PathVariable String dataNos) {

        VehicleRealStatusModel vehicleRealStatus = vehicleRealStatusService.getByVehicleId(id, dataNos);
        return ResultMsg.getResult(vehicleRealStatus);
    }

    /**
     * 获取实时车辆列表
     *
     * @return
     */
    @ApiOperation(value = "获取实时车辆列表", notes = "获取实时车辆列表，查询条件：licensePlate，vin，interNo，" +
            "vehModelId，powerMode，iccid，termId，operUnitId，onlineStatus，isAttention，offlineTime(离线时长),oper_license_city_id")
    @PostMapping(value = "/vehicleRealStatuss")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        Object result = vehicleRealStatusService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 获取地图上的车辆marker
     *
     * @return
     */
    @ApiOperation(value = "获取地图上的车辆marker", notes = "可以传cityId 只获取某城市下面的车")
    @PostMapping(value = "/vehiclesOnMap")
    @ResponseBody

    public ResultMsg vehiclesOnMap(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        Object result = vehicleRealStatusService.vehOnMap(pagerInfo);
        return ResultMsg.getResult(result);
    }

    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增（该方法暂时保留，供前端查看model注释）", notes = "新增信息（该方法暂时保留，供前端查看model注释）")
    @PostMapping(value = "/vehicleRealStatus")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) VehicleRealStatusModel demo1, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        vehicleRealStatusService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }
//
//


    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/vehicleRealStatuss/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params, HttpServletResponse response) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        try {
            vehicleRealStatusService.export(info);
        } catch (BusinessException ex) {
            throw new ExportErrorException(ex.getMessage());
        } catch (Exception e) {
            throw new ExportErrorException("请正确填写查询参数");
        }


    }


    /**
     * 根据车辆id获取车辆地图maker状态
     *
     * @return
     */
    @ApiOperation(value = "根据车辆id获取车辆地图maker状态", notes = "根据车辆id获取车辆地图maker状态")
    @GetMapping(value = "/vehicleRealStatuss/getMakerInfo/{vehId}/{powerMode}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getMakerInfo(@PathVariable String vehId, @PathVariable Integer powerMode) {

        switch (powerMode) {
            //纯电动汽车(BEV)
            case 1:
                PowerOneMakerModel powerOneMakerModel = vehicleRealStatusService.getMakerForPowerOne(vehId);
                return ResultMsg.getResult(powerOneMakerModel);
            //增程式电动车
            case 2:
                PowerTwoMakerModel powerTwoMakerModel = vehicleRealStatusService.getMakerForPowerTwo(vehId);
                return ResultMsg.getResult(powerTwoMakerModel);
            //燃料电动汽车 3
            case 3:
                PowerThreeMakerModel powerThreeMakerModel = vehicleRealStatusService.getMakerForPowerThree(vehId);
                return ResultMsg.getResult(powerThreeMakerModel);
            //混合动力汽车 4
            case 4:
                PowerFourMakerModel powerFourMakerModel = vehicleRealStatusService.getMakerForPowerFour(vehId);
                return ResultMsg.getResult(powerFourMakerModel);
            //插电式混合动力汽车(PHEV) 5
            case 5:
                PowerFiveMakerModel powerFiveMakerModel = vehicleRealStatusService.getMakerForPowerFive(vehId);
                return ResultMsg.getResult(powerFiveMakerModel);
            //传统燃油车
            case 6:
                PowerSixMakerModel powerSixMakerModel = vehicleRealStatusService.getMakerForPowerSix(vehId);
                return ResultMsg.getResult(powerSixMakerModel);
            default:
                //默认按纯电动的返回
                PowerOneMakerModel defaultModel = vehicleRealStatusService.getMakerForPowerOne(vehId);
                return ResultMsg.getResult(defaultModel);
//                return ResultMsg.getResult(null, "暂时不支持该类型", -1);
        }

    }

    /**
     * 根据报警id获取辆地图maker报警信息
     *
     * @return
     */
    @ApiOperation(value = "根据报警id获取辆地图maker报警信息", notes = "根据报警id获取辆地图maker报警信息")
    @GetMapping(value = "/vehicleRealStatuss/getAlarmMakerInfo/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg getAlarmMakerInfo(@PathVariable String id) {

        AlarmMakerModel alarmMakerModel = alarmInfoService.getAlarmMakerInfo(id);
        return ResultMsg.getResult(alarmMakerModel);
    }

    @ApiOperation(value = "离线导出 ", notes = "通过excel，离线导出")
    @PostMapping(value = "/vehicleRealStatuss/exportOffline")
    @RequiresPermissions(AUTH_EXPORT)
    @ResponseBody
    public ResultMsg exportOffline(@RequestBody PagerInfo pagerInfo) {

        vehicleRealStatusService.exportOffline(pagerInfo);
        return ResultMsg.getResult("已经生成离线任务,请稍后在个人下载中心查看.");

    }

}
