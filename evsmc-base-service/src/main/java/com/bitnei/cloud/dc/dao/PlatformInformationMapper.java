package com.bitnei.cloud.dc.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.dc.domain.PlatformInformation;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformInformationMapper接口<br>
* 描述： PlatformInformationMapper接口，在xml中引用<br>
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
* <td>2019-01-29 19:29:35</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Mapper
public interface PlatformInformationMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    PlatformInformation findById(Map<String,Object> params);

    /**
     * 根据名称查询
     *
     * @param params
     * @return
     */
    PlatformInformation findByName(Map<String,Object> params);

    /**
     * 根据唯一识别码查询
     *
     * @param params
     * @return
     */
    PlatformInformation findByCdkey(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(PlatformInformation obj);

    /**
     * 更改连接状态
     * @param obj
     * @return
     */
    int updatePlatformStatus(PlatformInformation obj);


    /**
     * 更新
     * @param obj
     * @return
     */
    int update(PlatformInformation obj);

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
    List<PlatformInformation> pagerModel(Map<String,Object> params);
}
