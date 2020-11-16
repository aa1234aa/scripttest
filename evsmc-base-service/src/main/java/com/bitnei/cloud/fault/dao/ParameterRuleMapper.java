package com.bitnei.cloud.fault.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.fault.domain.ParameterRule;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ParameterRuleMapper接口<br>
 * 描述： ParameterRuleMapper接口，在xml中引用<br>
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
 * <td>2019-02-27 16:35:01</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface ParameterRuleMapper {


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    ParameterRule findById(String id);


    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(ParameterRule obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    int update(ParameterRule obj);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 查询
     *
     * @param params
     * @return
     */
    List<ParameterRule> pagerModel(Map<String, Object> params);

    /**
     * 统计数量
     *
     * @param params
     * @return
     */
    int count(Map<String, Object> params);

    /**
     * 删除车型后, 删除规则
     *
     * @param params
     */
    void deleteByVehModelId(Map<String, Object> params);

    /**
     * 更新车型
     *
     * @return
     */
    void updateVehModelId(Map<String, Object> params);

    /**
     * 删除车辆型号同步过来的规则
     *
     * @param vehModelId    车辆型号ID
     * @param level         报警级别
     * @param groupName 级别描述， 传值参考：VehicleModelRuleTypeEnum.getType()
     * @param updateBy      修改人
     * @param updateTime    修改时间
     */
    void deleteVehicleModelRule(
        @Param("vehModelId") String vehModelId,
        @Param("level") Integer level,
        @Param("groupName") String groupName,
        @Param("updateBy") String updateBy,
        @Param("updateTime") String updateTime);

    /**
     * 更新type为1的车辆型号通用规则的响应方式
     *
     * @param vehModelId   车辆型号ID
     * @param responseMode 响应方式
     * @param updateBy     修改人
     * @param updateTime   修改时间
     */
    void updateVehicleModelRuleResponseMode(
        @Param("vehModelId") String vehModelId,
        @Param("responseMode") String responseMode,
        @Param("updateBy") String updateBy,
        @Param("updateTime") String updateTime);
}