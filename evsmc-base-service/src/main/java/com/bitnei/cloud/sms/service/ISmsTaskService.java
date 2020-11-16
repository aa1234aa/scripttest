package com.bitnei.cloud.sms.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sms.domain.SmsTask;
import com.bitnei.cloud.sms.model.SmsTaskModel;
import com.bitnei.cloud.sms.model.SmsTemplateModel;

import java.util.List;
import java.util.Map;


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

public interface ISmsTaskService extends IBaseService {


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
    SmsTaskModel get(String id);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(SmsTaskModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(SmsTaskModel model);

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
     * 详情导出
     *
     * @param pagerInfo 查询参数
     */
    void exportDetails(PagerInfo pagerInfo);

    /**
     * 短信下发分页查询
     * @param pagerInfo
     * @return
     */
    Object receiverPagerModel(PagerInfo pagerInfo);

    /**
     * 短信预览
     *
     * @param model 短信预览
     * @return
     */
    String preview(SmsTaskModel model);

    SmsTemplateModel getSmsTemplate(String templateId);

    void processCallBack(String bizId, List<String> phoneNumber, String taskId);

    void batchInsert(SmsTaskModel model);

    void getImportSearchFile();

    void smsCallBackJob();
}
