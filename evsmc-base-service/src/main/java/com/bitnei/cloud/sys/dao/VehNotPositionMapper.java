package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.sys.domain.VehNotPosition;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotPositionMapper接口<br>
* 描述： VehNotPositionMapper接口，在xml中引用<br>
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
* <td>2019-03-05 11:24:01</td>
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
public interface VehNotPositionMapper extends IMapper {

    /**
     * 实时查询
     * @param params
     * @return
     */
    List<VehNotPosition> findByRealTime(Map<String,Object> params);

    /**
     * 通过vid查询
     *
     * @param params
     * @return
     */
    VehNotPosition getByVid(Map<String,Object> params);


    /**
     * 分页查询
     * @param params 查询参数
     * @param rowBounds 分页参数
     * @return 分页结果
     */
    Page<VehNotPosition> pagerModel(@NotNull final Map<String,Object> params, final @NotNull PageRowBounds rowBounds);
}
