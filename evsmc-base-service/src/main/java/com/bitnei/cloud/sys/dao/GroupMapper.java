package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.sys.domain.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupMapper接口<br>
* 描述： GroupMapper接口，在xml中引用<br>
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
* <td>2018-11-08 10:40:16</td>
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
public interface GroupMapper {

    /**
     * 保存数据组
     * @param group
     * @return
     */
    int insertDefault(Group group);

    /**
     * 查找组
     * @param userId
     * @return
     */
    Group findByUserId(@Param("userId") String userId);

    /**
     * 通过用户id删除关系组
     * @param userId
     */
    void deleteByUserId(@Param("userId") String userId);
}
