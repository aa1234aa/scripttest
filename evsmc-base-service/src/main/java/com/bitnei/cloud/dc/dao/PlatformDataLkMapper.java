package com.bitnei.cloud.dc.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.dc.domain.PlatformDataLk;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformDataLkMapper接口<br>
* 描述： PlatformDataLkMapper接口，在xml中引用<br>
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
* <td>2019-02-12 17:08:40</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Mapper
public interface PlatformDataLkMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    PlatformDataLk findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(PlatformDataLk obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(PlatformDataLk obj);

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
    List<PlatformDataLk> pagerModel(Map<String,Object> params);
}
