package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;

public interface ICommonBaseService extends IBaseService {
    <T> List<T> convertListResult(PagerInfo pagerInfo,boolean translateWarnDataItem,String limitDay);
    Object list(PagerInfo pagerInfo);
    Condition addQueryCondition(PagerInfo pagerInfo, String field, String value);
    String getQueryCondition(PagerInfo pagerInfo, String field);
}
