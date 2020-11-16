package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.sys.domain.OwnerPeople;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： OwnerPeopleMapper接口<br>
* 描述： OwnerPeopleMapper接口，在xml中引用<br>
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
* <td>2018-11-02 15:19:04</td>
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
public interface OwnerPeopleMapper {

    /**
     * 个人中心用户更新个人信息
     * @param map
     */
   void personalCenterUpdateInfo(Map<String,Object> map);

    /**
     * 是否已经存在相同的证件号
     * @param cardNo
     * @return
     */
   Set<String> checkCardNo(@Param("cardNo")String cardNo);

    /**
     * 查询单位联系人
     * @param userId
     * @return
     */
   OwnerPeople findOwnerByUserId(@Param("userId")String userId);

    OwnerPeople findOwnerById(@Param("id")String id);
}
