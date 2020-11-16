package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.ParameterManage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ParameterManageMapper接口<br>
 * 描述： ParameterManageMapper接口，在xml中引用<br>
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
 * <td>2019-03-01 09:17:04</td>
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
public interface ParameterManageMapper {


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    ParameterManage findById(String id);


    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(ParameterManage obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    int update(ParameterManage obj);

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
    List<ParameterManage> pagerModel(Map<String, Object> params);

    /**
     *  根据协议, 数据项查询
     * @param params
     * @return
     */
    ParameterManage findByParams(Map<String, Object> params);

    /**
     * 根据参数项ID获取包含关联数据项信息
     * @param params
     * @return
     */
    ParameterManage getDataInfoById(Map<String, Object> params);
}
