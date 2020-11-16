package com.bitnei.cloud.veh.dao;

import com.bitnei.cloud.common.handler.IMapper;
import com.bitnei.cloud.veh.model.HistoryStateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HistoryStateMapper接口<br>
* 描述： HistoryStateMapper接口，在xml中引用<br>
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
* <td>2019-03-09 11:23:08</td>
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
public interface HistoryStateMapper extends IMapper {

    void updateVehPosition(List<HistoryStateModel> list);

    /**
     * 在单条SQL中更新一个分区表中的多条记录
     * @param table 分区表物理表名
     * @param rows 待更新的记录
     * @return 被更新的行数
     */
    long updateVehPositionInOneShardingTable(
        @Param("table") @NotNull final String table,
        @Param("rows") @NotNull final List<HistoryStateModel> rows);

    /**
     * 通过多条SQL语句, 每条SQL批量更一张分区表中的多条记录
     * @param tables 待更新的记录, key 为分区表物理表名, value 为表中待更新的记录
     */
    void updateVehPositionInManyShardingTables(
        @Param("tables") @NotNull final Map<String, List<HistoryStateModel>> tables);
}
