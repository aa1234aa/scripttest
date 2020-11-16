package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.CodeType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeTypeMapper接口<br>
 * 描述： CodeTypeMapper接口，在xml中引用<br>
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
 * <td>2019-02-25 10:22:15</td>
 * <td>hzr</td>
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
public interface CodeTypeMapper {


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    CodeType findById(String id);


    /**
     * 插入
     *
     * @param obj
     * @return
     */
    int insert(CodeType obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    int update(CodeType obj);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 查询
     *
     * @param params
     * @return
     */
    List<CodeType> pagerModel(Map<String, Object> params);

    /**
     * 通过名称查询
     *
     * @param name
     * @return
     */
    CodeType findByName(String name);

    /**
     * 通过CODE查询
     *
     * @param code
     * @return
     */
    CodeType findByCode(String code);

    /**
     * 通过LenCode查询
     *
     * @param lenCode
     * @return
     */
    CodeType findByLenCode(String lenCode);

    /**
     * 获取所有故障种类编码字符串
     * @return
     */
    String getAllFaultTypeCode();

}
