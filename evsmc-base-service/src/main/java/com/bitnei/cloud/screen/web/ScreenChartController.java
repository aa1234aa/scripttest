package com.bitnei.cloud.screen.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.screen.model.Gps;
import com.bitnei.cloud.screen.model.RunGeo;
import com.bitnei.cloud.screen.protocol.DataTool;
import com.bitnei.cloud.screen.protocol.PositionUtil;
import com.bitnei.cloud.screen.service.ScreenChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表数据
 *
 * @author xuzhijie
 */
@RestController
@Validated
@Api(description = "author：xuzhijie", tags = {"大屏接口-充电热力图&数据统计&轨迹分布数据接口"}, value = "大屏接口")
@RequestMapping("/" + Version.VERSION_V1 + "/screen/chart")
public class ScreenChartController {

    @Autowired
    private ScreenChartService chartService;

    // region 充电热力图

    /**
     * 充电热力图-获取充电统计信息
     *
     * @param areaId 区域ID
     * @param date   日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': {\n" +
        "        'chargeCount': 22787,          // 充电次数\n" +
        "        'chargeDuration': 25064.72,    // 充电时长(h)\n" +
        "        'chargeSum': 367411.16         // 充电量(Kw.h)\n" +
        "        'chargeVehicleCount': 8128     // 充电车辆数\n" +
        "    }\n" +
        "}</pre>", value = "【充电热力图】获取充电统计信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "区域ID", required = true, paramType = "query"),
        @ApiImplicitParam(name = "date", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    })
    @RequestMapping(value = "/chargeInfo", method = RequestMethod.GET)
    public ResultMsg chargeInfo(
        @NotNull(message = "区域ID不能为空") String areaId,
        @NotNull(message = "日期不能为空") @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        return ResultMsg.getResult(chartService.getChargeInfo(areaId, date));
    }

    /**
     * 充电热力图-获取车辆充电位置
     *
     * @param areaId 区域ID
     * @param date   日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': [{\n" +
        "        'wgLat': 39.832053175963914,  // 纬度（百度坐标系）\n" +
        "        'wgLon': 116.39097490027021   // 经度（百度坐标系）\n" +
        "    }]\n" +
        "}</pre>", value = "【充电热力图】获取车辆充电位置")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "区域ID", required = true, paramType = "query"),
        @ApiImplicitParam(name = "date", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    })
    @RequestMapping(value = "/chargeGeo", method = RequestMethod.GET)
    public ResultMsg chargeGeo(
        @NotNull(message = "区域ID不能为空") String areaId,
        @NotNull(message = "日期不能为空") @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        List<Gps> result = new ArrayList<>();
        chartService.getChargeGeo(areaId, date).forEach(geo -> {
            // 转换为百度坐标系
            Gps gcj02 = PositionUtil.gps84ToBd09(geo.getWgLat(), geo.getWgLon());
            if (gcj02 == null) {
                return;
            }
            result.add(gcj02);
        });
        return ResultMsg.getResult(result);
    }

    // endregion

    // region 轨迹分布热力图

    /**
     * 轨迹分布热力图
     *
     * @param areaId 区域ID
     * @param date   日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': [{\n" +
        "        'wgLat': 39.832053175963914,  // 纬度（百度坐标系）\n" +
        "        'wgLon': 116.39097490027021   // 经度（百度坐标系）\n" +
        "     }]\n" +
        "}</pre>", value = "轨迹分布热力图")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "区域ID", required = true, paramType = "query"),
        @ApiImplicitParam(name = "date", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    })
    @RequestMapping(value = "/runGeo", method = RequestMethod.GET)
    public ResultMsg runGeo(
        @NotNull(message = "区域ID不能为空") String areaId,
        @NotNull(message = "日期不能为空") @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        List<RunGeo> result = new ArrayList<>();
        chartService.getRunGeo(areaId, date).forEach(geo -> {
            // 转换为百度坐标系
            Gps gcj02 = PositionUtil.gps84ToBd09(geo.getLat(), geo.getLon());
            if (gcj02 == null) {
                return;
            }
            geo.setLon(gcj02.getWgLon());
            geo.setLat(gcj02.getWgLat());
            result.add(geo);
        });
        return ResultMsg.getResult(result);
    }

    // endregion

    // region 数据统计

    /**
     * 客户销量情况分布
     * --------------------------------
     * 获取车辆数排名前十的运营单位的车辆数和活跃车辆数
     *
     * @param date 日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': [{\n" +
        "        'unitId': '402882d25d0dd80f015d1083ed9b021d',   // 运营单位ID\n" +
        "        'unitName': '私人',                             // 运营单位名称\n" +
        "        'vehicleCount': 151968,                         // 车辆数\n" +
        "        'onlineCount': 1977                             // 活跃车辆数\n" +
        "     }]\n" +
        "}</pre>", value = "【数据统计】客户销量情况分布")
    @ApiImplicitParam(name = "date", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    @RequestMapping(value = "/vehicleCountByUnit", method = RequestMethod.GET)
    public ResultMsg vehicleCountByUnit(
        @NotNull @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        return ResultMsg.getResult(chartService.getVehicleCountByUnit(date));
    }

    /**
     * 车辆充电时段分布
     *
     * @param date 日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': {\n" +
        "        'chargeVehicleCount1': 32633,  // 00:00-02:00充电车辆数(以此类推)\n" +
        "        'chargeVehicleCount2': 23018,\n" +
        "        'chargeVehicleCount3': 13553,\n" +
        "        'chargeVehicleCount4': 13783,\n" +
        "        'chargeVehicleCount5': 17346,\n" +
        "        'chargeVehicleCount6': 22020,\n" +
        "        'chargeVehicleCount7': 25972,\n" +
        "        'chargeVehicleCount8': 24287,\n" +
        "        'chargeVehicleCount9': 22860,\n" +
        "        'chargeVehicleCount10': 24949,\n" +
        "        'chargeVehicleCount11': 26431,\n" +
        "        'chargeVehicleCount12': 32506\n" +
        "    }\n" +
        "}</pre>", value = "【数据统计】车辆充电时段分布")
    @ApiImplicitParam(name = "date", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    @RequestMapping(value = "/chargeVehicleCountDateHistogram", method = RequestMethod.GET)
    public ResultMsg chargeVehicleCountDateHistogram(
        @NotNull @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        return ResultMsg.getResult(chartService.getChargeVehicleCount(date));
    }

    /**
     * SOC充电起始状态
     *
     * @param startDate 日期(yyyy-MM-dd)
     * @param endDate   日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': {\n" +
        "        'chargeSoc0': 9261,  // 充电起始SOC状态0-10%(以此类推)\n" +
        "        'chargeSoc1': 34099,\n" +
        "        'chargeSoc2': 32832,\n" +
        "        'chargeSoc3': 16960,\n" +
        "        'chargeSoc4': 193,\n" +
        "        'chargeSoc5': 202,\n" +
        "        'chargeSoc6': 199,\n" +
        "        'chargeSoc7': 176,\n" +
        "        'chargeSoc8': 169,\n" +
        "        'chargeSoc9': 314\n" +
        "     ]\n" +
        "}</pre>", value = "【数据统计】SOC充电起始状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startDate", value = "日期(yyyy-MM-dd)", required = true, paramType = "query"),
        @ApiImplicitParam(name = "endDate", value = "日期(yyyy-MM-dd)", required = true, paramType = "query")
    })
    @RequestMapping(value = "/socChargeStartState", method = RequestMethod.GET)
    public ResultMsg socChargeStartState(
        @NotNull @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String startDate,
        @NotNull @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "日期格式不合法") String endDate) throws BusinessException {

        return ResultMsg.getResult(chartService.getSocChargeStartState(startDate, endDate));
    }

    /**
     * 车型平均单车行驶里程
     *
     * @param date 日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': [{\n" +
        "        'vehModelName': 'BYD7008BEV1', // 车型\n" +
        "        'kilometreList': [{\n" +
        "            'day': 1,              // 日期\n" +
        "            'avgKilometre': 563    // 累计里程\n" +
        "        },\n" +
        "        {\n" +
        "            'day': 2,\n" +
        "            'avgKilometre': 408\n" +
        "        }]\n" +
        "    }]\n" +
        "}</pre>", value = "【数据统计】车型平均单车行驶里程")
    @ApiImplicitParam(name = "date", value = "日期(yyyy-MM)", required = true, paramType = "query")
    @RequestMapping(value = "/dailyAvgKilometre", method = RequestMethod.GET)
    public ResultMsg dailyAvgKilometre(
        @NotNull @Pattern(regexp = "[0-9]{4}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        return ResultMsg.getResult(DataTool.subList(chartService.getDailyAvgKilometre(date), 20));
    }

    /**
     * 单车日行驶里程
     *
     * @param unitId       单位ID
     * @param areaId       区域ID
     * @param vin          车架号
     * @param licensePlate 车辆牌号
     * @param date         日期(yyyy-MM-dd)
     * @return
     * @throws BusinessException
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': [{\n" +
        "        'licensePlate': '京N0B830',    // 车牌号\n" +
        "        'kilometreList': [{\n" +
        "            'day': 1,                  // 日期\n" +
        "            'avgKilometre': 43         // 里程\n" +
        "        },\n" +
        "        {\n" +
        "            'day': 2,\n" +
        "            'avgKilometre': 70\n" +
        "        }]\n" +
        "    }]\n" +
        "}</pre>", value = "【数据统计】单车日行驶里程")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "unitId", value = "单位ID", paramType = "query"),
        @ApiImplicitParam(name = "areaId", value = "区域ID", paramType = "query"),
        @ApiImplicitParam(name = "vin", value = "车架号", paramType = "query"),
        @ApiImplicitParam(name = "licensePlate", value = "车辆牌号", paramType = "query"),
        @ApiImplicitParam(name = "date", value = "日期(yyyy-MM)", paramType = "query")
    })
    @RequestMapping(value = "/vehicleDailyKilometre", method = RequestMethod.GET)
    public ResultMsg vehicleDailyKilometre(
        String unitId,
        String areaId,
        String vin,
        String licensePlate,
        @Pattern(regexp = "[0-9]{4}-[0-9]{2}", message = "日期格式不合法") String date) throws BusinessException {

        return ResultMsg.getResult(chartService.vehicleDailyKilometre(unitId, areaId, vin, licensePlate, date));
    }

    // endregion

    // region 其他


    /**
     * 获取所有运营单位
     *
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': [{\n" +
        "        'unitId': '402882d25d0dd80f015d1083ed9b021d',   // 运营单位ID\n" +
        "        'unitName': '私人',                             // 运营单位名称\n" +
        "        'vehicleCount': 151968,                         // 车辆数\n" +
        "        'onlineCount': 1977                             // 活跃车辆数\n" +
        "     }]\n" +
        "}</pre>", value = "获取所有运营单位")
    @RequestMapping(value = "/unitList", method = RequestMethod.GET)
    public ResultMsg unitList() {
        return ResultMsg.getResult(chartService.unitList());
    }

    // endregion
}