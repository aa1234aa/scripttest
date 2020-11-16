package com.bitnei.cloud.monitor.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.monitor.domain.ElectronicFence;

import java.util.Map;
import java.util.List;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ElectronicFenceMapper接口<br>
 * 描述： ElectronicFenceMapper接口，在xml中引用<br>
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
 * <td>2019-05-17 11:04:12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface ElectronicFenceMapper {

    /**
     * 通过ID查询
     *
     * @param params 查询参数
     * @return
     */
    ElectronicFence findById(Map<String, Object> params);

    /**
     * 插入
     *
     * @param obj 插入参数
     * @return
     */
    int insert(ElectronicFence obj);

    /**
     * 更新
     *
     * @param obj 更新参数
     * @return
     */
    int update(ElectronicFence obj);

    /**
     * 删除
     *
     * @param params 删除参数
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     *
     * @param params 查询参数
     * @return
     */
    List<ElectronicFence> pagerModel(Map<String, Object> params);

    /**
     * 查询围栏基本信息通过围栏名称
     *
     * @param params 查询参数
     * @return
     */
    List<Map<String, Object>> queryBaseMessByName(Map<String, Object> params);
}
