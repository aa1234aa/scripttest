package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;

/**
 * Created by Lijiezhou on 2019/2/21.
 */
public interface IForwardRecordService extends IBaseService {

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
}
