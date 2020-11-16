package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.VehicleFiling;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 车辆防篡改备案Mapper接口<br>
* 描述： 车辆防篡改备案Mapper接口，在xml中引用<br>
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
* <td>2019-07-02 11:25:31</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Mapper
public interface VehicleFilingMapper {


    /**
     * 通过ID查询
     *
     * @param params 查询参数
     * @return {@link VehicleFiling}
     */
    VehicleFiling findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj {@link VehicleFiling}
     * @return 影响行数
     */
    int insert(VehicleFiling obj);

    /**
     * 更新
     * @param obj {@link VehicleFiling}
     * @return 影响行数
     */
    int update(VehicleFiling obj);

	/**
     * 删除
     * @param params 查询参数
     * @return 影响行数
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params 查询参数
     * @return 数据集合
    */
    List<VehicleFiling> pagerModel(Map<String,Object> params);
}
