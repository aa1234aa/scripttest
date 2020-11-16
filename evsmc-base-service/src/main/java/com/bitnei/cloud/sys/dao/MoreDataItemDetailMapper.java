package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MoreDataItemDetailMapper {

     List<Map<String,Object>> getVehicleByVin(@Param("vins")String [] vins);

    /**
     * 修改后缀名
     * @param fileName
     * @return
     */
     int updateSysOfflineName(@Param("fileName") String fileName,@Param("id")String id);

    /**
     * 根据车型获取通信协议
     * @param modelId
     * @return
     */
    List<Map<String,Object>> getRoleByModel(@Param("modelId")String  modelId);

    int updateName(@Param("modelId")String id);

}
