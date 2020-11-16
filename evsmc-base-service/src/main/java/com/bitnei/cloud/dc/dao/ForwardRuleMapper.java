package com.bitnei.cloud.dc.dao;

import com.bitnei.cloud.dc.domain.ForwardRuleItem;
import com.bitnei.cloud.sys.domain.GroupRuleInfo;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.dc.domain.ForwardRule;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRuleMapper接口<br>
* 描述： ForwardRuleMapper接口，在xml中引用<br>
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
* <td>2019-02-20 10:32:15</td>
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
public interface ForwardRuleMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    ForwardRule findById(Map<String,Object> params);

    /**
     * 根据名称查询
     *
     * @param params
     * @return
     */
    ForwardRule findByName(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(ForwardRule obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(ForwardRule obj);

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
    List<ForwardRule> pagerModel(Map<String,Object> params);

    /**
     * 通过规则id查询所有明细
     * @param params
     * @return
     */
    List<ForwardRuleItem> findRuleItemByRuleId(Map<String,Object> params);



    /**
     * 通过规则id查询所有明细，供权限sql用
      * @param params
     * @return
     */
    List<GroupRuleInfo> findRuleItemsByRuleId(Map<String, Object> params);

    /**
     *  通过平台id查询默认规则明细
     * @param params
     * @return
     */
    ForwardRuleItem findPlatformDefaultRuleItem(Map<String, Object> params);

    /**
     *  查询平台默认规则
     * @param params
     */
    ForwardRule findPlatformDefaultRule(Map<String, Object> params);

    /**
     *  添加规则明细
     * @param params
     */
    int insertForwardRuleItem(Map<String,Object> params);

    /**
     * 根据规则id删除规则明细
     * @param params
     * @return
     */
    int delRuleItem(Map<String,Object> params);
}
