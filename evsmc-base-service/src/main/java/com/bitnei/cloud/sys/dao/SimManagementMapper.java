package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.sys.domain.SimManagement;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SimManagementMapper接口<br>
* 描述： SimManagementMapper接口，在xml中引用<br>
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
* <td>2018-11-05 10:01:32</td>
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
public interface SimManagementMapper {

    /**
     * 根据iccid查询
     * @param params
     * @return
     */
    SimManagement findByIccId(Map<String,Object> params);
}