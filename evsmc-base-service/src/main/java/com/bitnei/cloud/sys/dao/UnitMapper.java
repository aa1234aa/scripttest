package com.bitnei.cloud.sys.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UnitMapper接口<br>
* 描述： UnitMapper接口，在xml中引用<br>
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
* <td>2018-11-05 17:33:20</td>
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
public interface UnitMapper {

    /**
     * 根据单位id和单位类型编码查询数量
     * @param params 单位id和单位类型编码
     * @return 数量
     */
    int findByUintIdCodeCount(Map<String,Object> params);

    /**
     * 查询单位名称数
     * @param id
     * @param name
     * @return
     */
    Long countOfName(@Param("id") String id, @Param("name") String name);

    /**
     * 查询单位简称数
     * @param id id
     * @param nickName 单位简称
     * @return
     */
    Long countOfNickName(@Param("id") String id, @Param("nickName") String nickName);

}
