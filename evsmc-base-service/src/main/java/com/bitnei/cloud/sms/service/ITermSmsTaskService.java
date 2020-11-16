package com.bitnei.cloud.sms.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sms.model.SmsTaskModel;
import com.bitnei.cloud.sms.model.TermSmsTaskModel;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： SmsTaskService接口<br>
 * 描述： SmsTaskService接口，在xml中引用<br>
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
 * <td>2019-08-16 09:41:04</td>
 * <td>joinly</td>
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

public interface ITermSmsTaskService extends IBaseService {


    /**
     * 全部查询
     *
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    TermSmsTaskModel get(String id);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(TermSmsTaskModel model);


    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(TermSmsTaskModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 批量新增
     *
     * @param model 新增model
     * @return
     */
    void batchInsert(TermSmsTaskModel model);

    void processAddAllMsgCallBack();

    /**
     * 联通短信发送
     */
    void sendUniComSms();

    /***
     *  联通短信状态
     */
    void processUniComSmsCallBack();

    /**
     * 详情导出
     *
     * @param pagerInfo 查询参数
     */
    void exportDetails(PagerInfo pagerInfo);
}
