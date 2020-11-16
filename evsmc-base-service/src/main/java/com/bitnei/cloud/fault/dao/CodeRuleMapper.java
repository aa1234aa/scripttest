package com.bitnei.cloud.fault.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.fault.domain.CodeRule;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleMapper接口<br>
* 描述： CodeRuleMapper接口，在xml中引用<br>
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
* <td>2019-02-25 16:55:47</td>
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
public interface CodeRuleMapper {


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    CodeRule findById(String id);

 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(CodeRule obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(CodeRule obj);

	/**
     * 删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 查询
     * @param params
     * @return
    */
    List<CodeRule> pagerModel(Map<String,Object> params);

    /**
     * 统计数量（查询表中符合条件的未删除记录数量，可用于查重和统计）
     * @param params
     * @return
     */
    int count(Map<String, Object> params);

    /**
     * 通过故障种类查询
     * @param faultCodeTypeId
     * @return
     */
    List<CodeRule> findByTypeId(String faultCodeTypeId);

    /**
     * 通过名称查询
     * @param faultName
     * @return
     */
    CodeRule findByName(String faultName);

    /**
     * 删除车型后, 删除规则
     * @param params
     */
    void deleteByVehModelId(Map<String,Object> params);

    /**
     * 更新车型
     * @return
     */
    void updateVehModelId(Map<String,Object> params);
}
