package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.PowerDevice;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PowerDeviceMapper接口<br>
* 描述： PowerDeviceMapper接口，在xml中引用<br>
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
* <td>2018-12-10 17:03:09</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Mapper
public interface PowerDeviceMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    PowerDevice findById(Map<String,Object> params);

    /**
     * 根据发电装置编码查询
     *
     * @param params
     * @return
     */
    PowerDevice findByCode(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(PowerDevice obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(PowerDevice obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<PowerDevice> pagerModel(Map<String,Object> params);

    Integer findByBatteryModelIdCount(String id);

    Integer findByGeneratorModelIdCount(String id);
}
