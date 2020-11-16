package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.domain.DayreportSummary;
import com.bitnei.cloud.veh.model.DayreportSummaryModel;
import com.bitnei.cloud.service.IBaseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportSummaryService接口<br>
* 描述： DayreportSummaryService接口，在xml中引用<br>
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
* <td>2019-03-11 09:40:45</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/

public interface IDayreportSummaryService extends IBaseService {


    /**
     * 总里程统计
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 里程分布统计
     * @param pagerInfo
     * @return
     */
    Object mileageMonitorList(PagerInfo pagerInfo);

    /**
     * 里程分布统计下的车辆详情
     * @param pagerInfo
     * @return
     */
    Object vehicleDetailsList(PagerInfo pagerInfo);

    /**
     * 单位月里程分布统计下的车辆详情
     * @param pagerInfo
     * @return
     */
    Object vehicleDetailsByUnitList(PagerInfo pagerInfo);

    /**
     * 车辆行驶情况统计
     * @param pagerInfo
     * @return
     */
    Object vehicleTravelList(PagerInfo pagerInfo);

    /**
     * 车辆监控情况统计
     * @param pagerInfo
     * @return
     */
    Object vehicleMonitoringList(PagerInfo pagerInfo);

    /**
     * 车辆详情
     * @param pagerInfo
     * @return
     */
    Object vehicleByMileageAbnormalsList(PagerInfo pagerInfo);

    /**
     * 闲置车辆详情多条件查询
     * @param pagerInfo
     * @return
     */
    Object idleVehicleCountsList(PagerInfo pagerInfo);

    /**
     * 期闲置车辆数&长期闲置车辆比率
     * @param pagerInfo
     * @return
     */
    Object idleAndRatioList(PagerInfo pagerInfo);

    /**
     * 总里程统计导出
     *
     * @param params 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 里程分布统计导出
     * @param pagerInfo
     */
    void mileageMonitorExport(PagerInfo pagerInfo);

    /**
     * 单位月里程分布统计导出
     * @param pagerInfo
     */
    void mileageMonitorByUnitListExport(PagerInfo pagerInfo);

    /**
     * 里程分布统计下的车辆详情导出
     * @param pagerInfo
     */
    void vehicleDetailsExport(PagerInfo pagerInfo);

    /**
     * 单位月里程分布统计下的车辆详情导出
     * @param pagerInfo
     */
    void vehicleDetailsByUnitListExport(PagerInfo pagerInfo);
    /**
     * 车辆行驶情况统计导出
     * @param pagerInfo
     */
    void vehicleTravelExport(PagerInfo pagerInfo);

    /**
     * 车辆闲置情况统计导出
     * @param pagerInfo
     */
    void idleVehicleCountsExport(PagerInfo pagerInfo);

    /**
     * 闲置车辆离线导出
     * @param pagerInfo
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);

    /**
     * 车辆监控情况统计导出
     * @param pagerInfo
     */
    void vehicleMonitoringExport(PagerInfo pagerInfo);

    /**
     * 获取车辆详情
     * @param type
     * @param reportDate
     */
    Object get(String type,String reportDate);

    /**
     * 获取单位车辆统计详情
     * @param operUnitId
     *
     */
    Object findByUnitId(String operUnitId);

    /**
     * 车辆详情导出
     * @param pagerInfo
     */
    void vehicleByMileageAbnormalsExport(PagerInfo pagerInfo);


    /**
     * 下载车辆导入查询模板
     */
    void getImportSearchFile();
}
