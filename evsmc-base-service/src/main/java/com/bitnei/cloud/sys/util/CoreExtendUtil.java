package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.common.annotation.ExtendTable;
import com.bitnei.cloud.common.bean.BaseExtendModel;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.CoreExtendValue;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Maps;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.StringUtil;

import java.util.Map;


public class CoreExtendUtil {


    /**
     * 将model转为扩展值
     * @param idVal
     * @param model
     * @return
     */
    public static CoreExtendValue fromModel(final String idVal, BaseExtendModel model){

        ExtendTable extendTable = model.getClass().getAnnotation(ExtendTable.class);

        CoreExtendValue cv = new CoreExtendValue();
        cv.setId(UtilHelper.getUUID());
        cv.setIdVal(idVal);
        cv.setTableName(extendTable.table());


        cv.setJsonValue(new JsonSerializer().deep(true).serialize(model.getExtendColumns()));
        return cv;
    }

    /**
     * 更新扩展值
     * @param model
     */
    public static void updateExtendValue(CoreExtendValue cv, BaseExtendModel model){

        Map<String, Object> oldMap = Maps.newHashMap();
        if (StringUtil.isNotEmpty(cv.getJsonValue())){
            oldMap = new JsonParser().parse(cv.getJsonValue(), Map.class);
        }
        if (model.getExtendColumns() != null && !model.getExtendColumns().isEmpty()){
            oldMap.putAll(model.getExtendColumns());
        }
        cv.setJsonValue(new JsonSerializer().serialize(oldMap));

    }
}
