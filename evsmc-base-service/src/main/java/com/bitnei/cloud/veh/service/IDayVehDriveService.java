package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;

public interface IDayVehDriveService {

    /**
     * 全部查询
     * @return
     *  返回所有
     */
    ResultMsg list(PagerInfo pagerInfo);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 下载车辆导入查询模板
     */
    void getImportSearchFile();
}
