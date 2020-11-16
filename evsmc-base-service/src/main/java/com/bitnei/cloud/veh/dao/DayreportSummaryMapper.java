package com.bitnei.cloud.veh.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.veh.model.IdleVehicleCountModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportSummaryMapper接口<br>
* 描述： DayreportSummaryMapper接口，在xml中引用<br>
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
* <td>2019-03-11 09:40:45</td>
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
public interface DayreportSummaryMapper extends IMapper {

    /**
     * 游标查询, 必须在事务中使用
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args 查询参数
     * @return 结果集
     */
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    <TArgs> Cursor<IdleVehicleCountModel> idleVehicleDetails(@Nullable final TArgs args);

    /**
     * 流式查询
     * xml 配置中必须加上 resultOrdered="true"
     *
     * @param args    查询参数
     * @param handler 结果集
     */
    <TArgs> void idleVehicleDetails(@Nullable final TArgs args, final @NotNull ResultHandler<IdleVehicleCountModel> handler);

    /**
     * 分页查询
     *
     * @param args 查询参数
     * @param pageRowBounds 分页参数
     * @return 结果集
     */
    <TArgs> Page<IdleVehicleCountModel> idleVehicleDetails(@Nullable final TArgs args, @NotNull final PageRowBounds pageRowBounds);
}
