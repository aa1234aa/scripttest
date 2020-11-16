package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.LogModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Lijiezhou on 2018/12/24.
 */
public interface ILogService {

    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    LogModel get(String id);

    /**
     * 导出
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 离线导出
     * @param pagerInfo
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);
}
