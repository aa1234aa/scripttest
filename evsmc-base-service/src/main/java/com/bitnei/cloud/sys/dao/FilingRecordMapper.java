package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.FilingRecord;

import java.util.Map;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： FilingRecordMapper接口<br>
 * 描述： FilingRecordMapper接口，在xml中引用<br>
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
 * <td>2019-07-24 16:20:42</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface FilingRecordMapper {


    /**
     * 通过ID查询
     *
     * @param params 查询参数
     * @return FilingRecord
     */
    FilingRecord findById(Map<String, Object> params);

    /**
     * 通过fromID查询
     *
     * @param params 查询参数
     * @return FilingRecord
     */
    FilingRecord findByFromId(Map<String, Object> params);

    /**
     * 插入
     * @param obj {@link FilingRecord}
     * @return 影响行数
     */
    int insert(FilingRecord obj);

    /**
     * 更新
     *
     * @param obj {@link FilingRecord}
     * @return 影响行数
     */
    int update(FilingRecord obj);

    /**
     * 删除
     * @param params 参数map
     * @return 影响行数
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     * @param params 查询参数
     * @return 数据集合
     */
    List<FilingRecord> pagerModel(Map<String, Object> params);
}
