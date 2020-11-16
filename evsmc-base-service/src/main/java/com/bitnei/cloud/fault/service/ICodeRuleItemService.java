package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.domain.CodeRuleItem;
import com.bitnei.cloud.fault.model.CodeRuleItemModel;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeRuleItemService接口<br>
* 描述： CodeRuleItemService接口，在xml中引用<br>
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
* <td>2019-02-26 11:18:23</td>
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

public interface ICodeRuleItemService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    List<CodeRuleItemModel> list(String faultCodeRuleId);

    /**
     * 全部查询，可以选择是否包含逻辑删除的数据
     * @param faultCodeRuleId
     * @param containDelete
     * @return
     */
    List<CodeRuleItemModel> list(String faultCodeRuleId, Boolean containDelete);

    List<CodeRuleItemModel> getByTypeCodeAndVehModelId(String typeCode, String vehModelId);
    List<CodeRuleItemModel> getByTypeCodeAndVehModelIdFast(String typeCode, String vehModelId, List<CodeRuleItem> cache);


    /**
     * 根据id获取
     *
     * @return
     */
    CodeRuleItemModel get(String id);

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(CodeRuleItemModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(CodeRuleItemModel model);

    /**
     *  根据规则id 删除。
     * @param faultCodeRuleId
     */
    void deleteByFaultCodeRuleId(String faultCodeRuleId);

    /**
     * 通过故障种类ID获取所有故障码项集合
     * @param faultCodeTypeId
     * @return
     */
    List<CodeRuleItemModel> getByFaultCodeTypeId(String faultCodeTypeId);

    List<CodeRuleItem> getAllFault();

}
