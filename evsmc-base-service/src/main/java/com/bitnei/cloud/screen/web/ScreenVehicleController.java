package com.bitnei.cloud.screen.web;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.screen.service.ScreenVehicleService;
import com.bitnei.cloud.sys.model.HomePageModel;
import com.bitnei.cloud.sys.service.IHomePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


/**
 * 车辆数据
 *
 * @author xuzhijie
 */
@Slf4j
@RestController
@Validated
@Api(description = "author：xuzhijie", tags = {"大屏接口-首页数据"}, value = "大屏接口")
@RequestMapping("/" + Version.VERSION_V1 + "/screen/vehicle")
public class ScreenVehicleController {
    private static final String ALL_AREA_ID = "0";

    @Autowired
    private ScreenVehicleService vehicleService;

    @Autowired
    private IHomePageService homePageService;


    // region 车辆分布排名

    /**
     * 车辆分布排名 - 顶部统计
     * --------------------------------
     * 获取全国监控和在线车辆数
     *
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code':200,\n" +
        "    'data': {\n" +
        "        'totalCarCount': 11111,      // 总车辆数\n" +
        "        'monitoringCarCount': 95087, // 监控车辆数\n" +
        "        'onlineCarCount': 27218      // 在线车辆数\n" +
        "    }\n" +
        "}</pre>", value = "获取全国监控和在线车辆数")
    @RequestMapping(value = "/carCount", method = RequestMethod.GET)
    public ResultMsg carCount() {
        HomePageModel model = (HomePageModel) homePageService.list(new PagerInfo(), true);

        Map<String, Integer> map = new HashMap<>(16);
        map.put("totalCarCount", model.getTotal());
        map.put("monitoringCarCount", model.getOnlined());
        map.put("onlineCarCount", model.getVehOnline());
        return ResultMsg.getResult(map);
    }

    /**
     * 车辆分布排名，中间地图聚合，单车日行驶里程统计，共用一个接口
     * --------------------------------
     * 获取所有省份总车辆数和在线车辆数
     * 注：前端截取，到时候看是否加limit参数，不然查所有， 数据量有点大
     *
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': [{\n" +
        "        'id': '110',               // 区域ID\n" +
        "        'name': '北京市',          // 区域名称\n" +
        "        'totalCarCount': 63648,    // 总车辆数\n" +
        "        'onlineCarCount': 5493     // 在线车辆数\n" +
        "    },\n" +
        "    {\n" +
        "        'id': '181',\n" +
        "        'name': '广东省',\n" +
        "        'totalCarCount': 19839,\n" +
        "        'onlineCarCount': 1394\n" +
        "    }]\n" +
        "}</pre>", value = "获取所有省份总车辆数和在线车辆数")
    @RequestMapping(value = "/provinceCarCount", method = RequestMethod.GET)
    public ResultMsg provinceCarCount() {
        return ResultMsg.getResult(vehicleService.getProvinceCarCount(vehicleService.getProvinceArea()));
    }

    // endregion

    // region 节能减排

    /**
     * 节能减排数据
     * --------------------------------
     * 获取某个区域碳减排信息
     * 注：据国栋说节能减排油耗量【13l~15l】是由web端配置的
     *
     * @param areaId 区域ID
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': {\n" +
        "        'mil': 5138.3,          // 里程\n" +
        "        'milUnit': '公里',       // 里程单位 公里、万公里\n" +
        "        'oil': 1839.6,         // 耗油量\n" +
        "        'oilUnit': '升',        // 耗油量单位 升、万升\n" +
        "        'ele': 5446.9,         // 耗电量\n" +
        "        'eleUnit': '度',        // 耗电量单位 度、万度\n" +
        "        'co2': 3.2,            // 碳减排\n" +
        "        'co2Unit': '吨'         // 碳减排单位 吨、万吨\n" +
        "    }\n" +
        "}</pre>", value = "获取某个区域碳减排信息")
    @ApiImplicitParam(name = "areaId", value = "区域ID(默认查全国)")
    @RequestMapping(value = "/getEmissionReductionByArea", method = RequestMethod.GET)
    public ResultMsg getEmissionReductionByArea(String areaId) {
        if (StringUtils.isBlank(areaId)) {
            areaId = ALL_AREA_ID;
        }
        return ResultMsg.getResult(vehicleService.getEmissionReductionByArea(areaId));
    }

    // endregion

    // region 地图聚合

    /**
     * 中间地图聚合
     * --------------------------------
     * 获取可监控车辆的经纬度
     *
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': [{\n" +
        "        'vid': '30b91ce3-f1ba-4ccc-b439-a59d91eb604f',  // 车辆vid\n" +
        "        'licensePlate': '京Q7KQ70',                     // 车牌号\n" +
        "        'lat': 39.91003475697299,                       // 纬度(火星坐标系)\n" +
        "        'lon': 116.4164556083337,                       // 经度(火星坐标系)\n" +
        "        'status': 2                                     // 状态 1：在线、2：离线、3：充电、4：故障\n" +
        "    }]\n" +
        "}</pre>", value = "获取可监控车辆的经纬度")
    @RequestMapping(value = "/getGeo", method = RequestMethod.GET)
    public ResultMsg getGeo() {
        return ResultMsg.getResult(vehicleService.getGeo());
    }

    /**
     * 中间地图车辆查找
     * --------------------------------
     * 筛选获取车辆信息
     *
     * @param vin          车架号
     * @param licensePlate 车牌号
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': [{\n" +
        "            'vin': 'LVXMAZAA2HS956216',\n" +
        "            'licensePlate': '赣SHS956216'\n" +
        "        },\n" +
        "        {\n" +
        "            'vin': 'LVXMAZAA6HS957014',\n" +
        "            'licensePlate': '赣SHS957014'\n" +
        "        }\n" +
        "    ]\n" +
        "}</pre>", value = "筛选获取车辆信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vin", value = "车架号", paramType = "query"),
        @ApiImplicitParam(name = "licensePlate", value = "车牌号", paramType = "query")
    })
    @RequestMapping(value = "/getVehicleInfo", method = RequestMethod.GET)
    public ResultMsg getVehicleInfo(String vin, String licensePlate) {
        return ResultMsg.getResult(vehicleService.getVehicleInfo(vin, licensePlate));
    }

    // endregion

    //region 单车监控


    /**
     * 中间地图点击车辆弹出来监控界面
     * --------------------------------
     * 车辆信息卡-单车监控
     *
     * @param vid 车辆ID
     * @return
     */
    @ApiOperation(notes = "<pre>响应示例：\n" +
        "{\n" +
        "    'code': 200,\n" +
        "    'data': {\n" +
        "        '2003': [             // 可充电储能装置电压\n" +
        "            3.78,\n" +
        "            3.78\n" +
        "        ],\n" +
        "        '2103': [             // 可充电储能装置温度\n" +
        "            15,\n" +
        "            17\n" +
        "        ],\n" +
        "        '2201': 64.1,         // 车速\n" +
        "        '2202': 1895,         // 累计行驶里程\n" +
        "        '2203': '自动D挡',    // 档位\n" +
        "        '2213': 1,            // 动力方式 1：纯电动汽车、2：增程式电动车、3：燃料电动汽车、4：混合动力汽车、5：插电式混合动力汽车\n" +
        "        '2615': 66,           // SOC 单位：%\n" +
        "        '2401': '启动状态',   // 发动机状态 1：启动状态、2：关闭状态、254：异常、255：无效\n" +
        "        '2411': 6000,         // 曲轴转速\n" +
        "        '2413': 10,           // 燃料消耗率\n" +
        "        'vehicleStatus': '行驶中',      // 车辆状态【静止、行驶中】\n" +
        "        'address': 'xxx省xxx市xxxx路',  // 当前位置\n" +
        "        'powerLossState': '耗电状态',   // 驱动电机状态【耗电状态、关闭状态】\n" +
        "        'maxRotatingSpeed': 0,         // 驱动电机转速\n" +
        "        'maxRotatingTorque': 0,        // 驱动电机转矩\n" +
        "        '60033': 80,                   // 反应剂余量 0%~100%\n" +
        "        '60038': '80',               // 发动机冷却液温度\n" +
        "        '60039': '80',                 // 油箱液位 %\n" +
        "        '60028': '12',                // 发动机摩擦扭矩\n" +
        "        '60029': 3000,                 // 发动机转速\n" +
        "        '60035': '80',               // SCR 入口温度\n" +
        "        '60036': '80',               // SCR 出口温度\n" +
        "        '2121': '工作',                // 高压DC-DC状态  状态为：工作/断开\n" +
        "        '2114': [                     // 燃料电池温度值列表\n" +
        "            3,\n" +
        "            21\n" +
        "        ],\n" +
        "        '2110': 3000,                 // 燃料电池电压\n" +
        "        '2111': 3000,                 // 燃料电池电流\n" +
        "        '2117': 4000,                 // 氢气最高浓度\n" +
        "        '2302': '40',               // 电机控制器温度\n" +
        "        '2304': '40',               // 电机温度\n" +
        "    }\n" +
        "}</pre>", value = "单车监控")
    @ApiImplicitParam(name = "vid", value = "车辆ID", required = true, paramType = "query")
    @RequestMapping(value = "/getVehicleItem", method = RequestMethod.GET)
    public ResultMsg getVehicleItem(@NotNull(message = "车辆VID不能为空") String vid) {
        return ResultMsg.getResult(vehicleService.getVehicleItem(vid));
    }
    // endregion
}
