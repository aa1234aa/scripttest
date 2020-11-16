package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.service.ICommonBaseService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class CommonBaseService extends BaseService implements ICommonBaseService {
    @Override
   public   <T> List<T> convertListResult(PagerInfo pagerInfo,boolean translateWarnDataItem,String limitDay){
       List<T> datas;

       if (translateWarnDataItem){
           addQueryCondition(pagerInfo,"TRANSLATE","1");

       }

       addQueryCondition(pagerInfo,"limitDay",limitDay);
       Object result = list(pagerInfo);
       if (result instanceof PagerResult){
           if (((PagerResult) result).getData().size()>0) {
               datas = (List<T>) ((PagerResult) result).getData().get(0);
           }
           else {
               return null;
           }
       }
       else {
           datas = (List<T>) result;
       }

       return datas;
    }
    public boolean isPager(PagerInfo pagerInfo){
        return pagerInfo.getLimit()!=null&&pagerInfo.getLimit()>0;
    }
    @Override
   public String getQueryCondition(PagerInfo pagerInfo, String field){
       Condition condition= pagerInfo.getConditions().stream().filter(b -> b.getName().equals(field)).findAny().orElse(null);
        if(condition==null||StringUtils.isEmpty(condition.getValue())){
            return "";
        }
        return condition.getValue();

    }
    @Override
    public Condition addQueryCondition(PagerInfo pagerInfo,String field,String value){
        String c= getQueryCondition(pagerInfo,field);
        Condition condition;
        if (StringUtils.isNotBlank(c)){
            condition=pagerInfo.getConditions().stream().filter(b -> b.getName().equals(field)).findAny().get();
            condition.setValue(value);
        }
        else {
            condition=new Condition();
            condition.setName(field);
            condition.setValue(value);
            pagerInfo.getConditions().add(condition);
        }

        return condition;
    }

    @Override
    public   Object list(PagerInfo pagerInfo){
        throw new NotImplementedException();
    }


    public boolean checkDate(PagerInfo pagerInfo){
        String startTime = getQueryCondition(pagerInfo, "beginTime"),

                endTime = getQueryCondition(pagerInfo, "endTime");


        if (org.apache.commons.lang3.StringUtils.isAnyEmpty(startTime, endTime)) {
            throw new BusinessException("起止时间不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.equals(startTime, endTime)) {
            throw new BusinessException("起止时间不能相同");
        }
        //默认最大查询天数
        int day = 1;
        String limitDay = getQueryCondition(pagerInfo, "limitDay");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(limitDay)) {

            day = Integer.valueOf(limitDay);
        }


        double diff = com.bitnei.cloud.sys.util.DateUtil.getTimesDiffEx(1, startTime, endTime);
        if (diff > day || diff < 0) {
            throw new BusinessException(String.format("查询时间不能超过%d天", day));
        }
        return true;
    }
}
