package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;

public interface IMoreDataItemDetailService {
    void moreExportOffline(PagerInfo pagerInfo);

    Object getDataItem(PagerInfo pagerInfo);

     Object getRule(PagerInfo pagerInfo);
}
