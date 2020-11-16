package com.bitnei.cloud.sys.dao;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.CoreExtendTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreExtendTemplateMapper接口<br>
* 描述： CoreExtendTemplateMapper接口，在xml中引用<br>
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
* <td>2019-07-31 14:07:09</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Mapper
public interface CoreExtendTemplateMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    CoreExtendTemplate findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(CoreExtendTemplate obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(CoreExtendTemplate obj);

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
    List<CoreExtendTemplate> pagerModel(Map<String,Object> params);

    /**
     * 通过表名查询
     * @param table
     * @return
     */
    @Cached(expire = 60*10, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.LOCAL)
    List<CoreExtendTemplate> findByTable(final @Param("table") String table);
}
