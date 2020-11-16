package com.bitnei.cloud.fault.dao;

import com.bitnei.cloud.fault.domain.CodeRuleItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleItemMapper接口<br>
* 描述： CodeRuleItemMapper接口，在xml中引用<br>
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
* <td>2019-02-25 18:10:28</td>
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
public interface CodeRuleItemMapper {


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    CodeRuleItem findById(String id);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(CodeRuleItem obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(CodeRuleItem obj);

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
    List<CodeRuleItem> pagerModel(Map<String,Object> params);

    /**
     * 根据规则id删除
     * @param id
     */
    void deleteById(String id);

    /**
     * 根据故障种类ID获取所有故障码项集合
     * @param faultCodeTypeId
     * @return
     */
    List<CodeRuleItem> getByFaultCodeTypeId(String faultCodeTypeId);

}
