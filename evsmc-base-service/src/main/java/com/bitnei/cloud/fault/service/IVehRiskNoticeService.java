package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.model.SafeRiskModel;
import com.bitnei.cloud.fault.model.SafeRiskUpModel;
import com.bitnei.cloud.fault.model.VehRiskNoticeModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;

import java.util.List;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehRiskNoticeService接口<br>
* 描述： VehRiskNoticeService接口，在xml中引用<br>
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
* <td>2019-07-08 18:07:56</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/

public interface IVehRiskNoticeService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 已处理风险通知全部查询
     * @return
     *  返回所有
     */
    Object historyList(PagerInfo pagerInfo);

    /**
     * 历史处理记录
     * @return
     *  返回所有
     */
    Object findAnnotationsByCode(PagerInfo pagerInfo);

    /**
     * 国家平台管理员意见
     * @return
     *  返回所有
     */
    Object findOpinionsByCode(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     VehRiskNoticeModel get(String id);

    /**
     * 通知状态，风险等级统计
     *
     * @return
     */
    VehRiskNoticeModel getCount();

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(VehRiskNoticeModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(VehRiskNoticeModel model);

    /**
     * 批量阅读
     * @param code  批量阅读
     * @return
     */
    void updateReadByCode(String code);

    /**
     * 批量回复
     * @param code  批量回复
     * @return
     */
    void updateReplyByCode(VehRiskNoticeModel code);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 批量新增
     * @param models 车辆安全风险Model集合
     */
    void batchInsert(List<SafeRiskModel> models);


    /**
     * 批量更新状态
     * @param models
     */
    void batchUpdate(List<SafeRiskUpModel> models);


    /**
     * 当前报警全部查询
     * @return
     *  返回所有
     */
    Object alarmInfoList(PagerInfo pagerInfo);

    /**
     * 定时更新未读状态
     *
     */
    void updateNoticeStatus();


    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 已处理通知导出
     *
     * @param pagerInfo 查询参数
     */
    void historyExport(PagerInfo pagerInfo);
}
