package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.InstructTaskModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.SendInstructDto;
import com.bitnei.cloud.sys.model.SendRuleParamsDto;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructTaskService接口<br>
* 描述： InstructTaskService接口，在xml中引用<br>
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
* <td>2019-03-08 15:34:48</td>
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

public interface IInstructTaskService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     InstructTaskModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(InstructTaskModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(InstructTaskModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    List<InstructTaskModel> queryUpgradeEffectiveTaskList(Integer instructType, String instructId);

    void sendRule(SendRuleParamsDto sendRuleParamsDto);

    String sendInstruct(SendInstructDto sendInstructDto);
}