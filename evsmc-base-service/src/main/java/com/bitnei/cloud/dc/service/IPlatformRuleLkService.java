package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.PlatformRuleLk;
import com.bitnei.cloud.dc.model.PlatformRuleLkModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformRuleLkService接口<br>
* 描述： PlatformRuleLkService接口，在xml中引用<br>
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
* <td>2019-02-21 14:30:57</td>
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

public interface IPlatformRuleLkService extends IBaseService {

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(PlatformRuleLkModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(PlatformRuleLkModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 关联自动转发规则
     * @param demo
     * @return
     */
    int insertPlatformRules(PlatformRuleLkModel demo);

    /**
     * 移除转发规则
     * @param platformId
     * @param delRuleIds
     * @return
     */
    int deletePlatformRules(String platformId, String delRuleIds);

    /**
     * 获取已关联规则
     * @param pagerInfo
     * @return
     */
    Object findPlatformRule(PagerInfo pagerInfo);

    /**
     * 获取平台规则
     * @param platformId
     * @return
     */
    List<PlatformRuleLk> listPlatformRule(String platformId);
}
