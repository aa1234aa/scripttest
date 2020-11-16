package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;

/**
 * 车辆异常数据项报表service
 * @author zxz
 * @date 2019/9/17 16:14
 **/
public interface IAbnormalDataItemService {

    /**
     * 新能源车辆异常数据项报表数据查询
     * @param pagerInfo 分页对象
     * @return 数据集
     */
    Object energyList(PagerInfo pagerInfo);

    /**
     * 新能源车辆异常数据项报表数据excel导出
     * @param pagerInfo 分页查询对象
     */
    void energyExport(PagerInfo pagerInfo);

    /**
     * 燃油车辆异常数据项报表数据查询
     * @param pagerInfo 分页对象
     * @return 数据集
     */
    Object fuelList(PagerInfo pagerInfo);

    /**
     * 燃油车辆异常数据项报表数据excel导出
     * @param pagerInfo 分页查询对象
     */
    void fuelExport(PagerInfo pagerInfo);
}
