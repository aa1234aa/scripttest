package com.bitnei.cloud.veh.dao;

import com.bitnei.cloud.veh.domain.HistoryState;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayVehMapper接口<br>
* 描述： DayVehMapper接口，在xml中引用<br>
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
public interface DayVehMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    HistoryState findById(Map<String, Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(HistoryState obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(HistoryState obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<HistoryState> pagerModel(Map<String, Object> params);
}
