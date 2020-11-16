package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.InstructTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructTaskMapper接口<br>
 * 描述： InstructTaskMapper接口，在xml中引用<br>
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
 * <td>2019-03-07 10:26:27</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Mapper
public interface InstructTaskMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    InstructTask findById(Map<String, Object> params);


    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(InstructTask obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    int update(InstructTask obj);

    /**
     * 删除
     *
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     *
     * @param params
     * @return
     */
    List<InstructTask> pagerModel(Map<String, Object> params);

    List<InstructTask> queryUpgradeEffectiveTaskList(Map<String, Object> params);

    List<InstructTask> querySimilarUpgradeTask(Map<String, Object> params);

    List<InstructTask> queryControlEffectiveTaskList(Map<String, Object> params);
}
