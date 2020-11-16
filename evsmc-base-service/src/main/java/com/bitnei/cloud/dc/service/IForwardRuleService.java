package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.dc.domain.ForwardRuleItem;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.ForwardRule;
import com.bitnei.cloud.dc.model.ForwardRuleModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.GroupRuleInfo;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRuleService接口<br>
* 描述： ForwardRuleService接口，在xml中引用<br>
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

public interface IForwardRuleService extends IBaseService {


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
     ForwardRuleModel get(String id);


    /**
     * 根据名称获取
     *
     * @return
     */
     ForwardRuleModel getByName(String name);


    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(ForwardRuleModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(ForwardRuleModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 新增规则明细
     * @param demo  新增规则明细
     * @return
     */
    void insertRuleItems(ForwardRuleModel demo);

    /**
     * 获取已选车辆列表
     * @param pagerInfo
     * @return
     */
    Object selectedVehs(PagerInfo pagerInfo, String ruleId);

    /**
     * 移除已选车辆
     * @param ruleId
     * @param delVehIds
     * @return
     */
    int deleteVehs(String ruleId, String delVehIds);

    /**
     * 添加全部车辆
     * @param pagerInfo  添加全部车辆
     * @return
     */
    void addAllVehs(PagerInfo pagerInfo, String ruleId);

    /**
     * 查找规则明细
     * @param ruleIds
     * @return
     */
    List<GroupRuleInfo> findRuleItemsByRuleId(String[] ruleIds);


    /**
     * 通过规则ID判断车辆Id列表
     * @param ruleIds
     * @return
     */
    List<String> getVehicleIdByRuleIds(String[] ruleIds);

    /**
     * 获取规则描述
     * @param ruleId
     * @return
     */
    String getForwardRuleResourceDesc(String ruleId);

    /**
     * 添加车辆列表
     * @param pagerInfo
     * @param ruleId
     * @return
     */
    Object vehsList(PagerInfo pagerInfo, String ruleId);

    /**
     * 多条件查询转发规则及规则配置描述
     */
    Object listRulesAndDesc(PagerInfo pagerInfo);

    /**
     * 车辆删除后，同步更新规则类型为“列表选择”的规则明细val值
     * @param vehIds
     */
    void updateForwardRuleRuleItemVal(String vehIds);


    void insertSelectListItem(String ruleId, String addVehIds);

    /**
     *  查询平台默认规则
     * @param platformId
     */
    ForwardRule findPlatformDefaultRule(String platformId);

    /**
     *  通过平台id查询默认规则明细
     * @param platformId
     * @return
     */
    ForwardRuleItem findPlatformDefaultRuleItem(String platformId);
}
