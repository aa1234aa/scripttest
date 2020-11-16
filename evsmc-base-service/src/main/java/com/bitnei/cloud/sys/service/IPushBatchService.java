package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.*;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： PushBatchService接口<br>
 * 描述： PushBatchService接口，在xml中引用<br>
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
 * <td>2019-02-27 19:36:18</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */

public interface IPushBatchService extends IBaseService {


    /**
     * 全部查询
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @return model
     */
    PushBatchModel get(String id);


    /**
     * 新增
     *
     * @param model 新增model
     */
    void insert(PushBatchModel model);

    /**
     * 编辑
     * @param model 编辑model
     */
    void update(PushBatchModel model);

    /**
     * 批量新增
     *
     * @param model model
     */
    void batchInsert(PushModel model);

    /**
     * 批量更新
     * @param model model
     */
    void batchUpdate(VehFeedbackModel model);

    /**
     * 全部批次号详情列表
     * @return 返回所有
     */
    Object pushBatchDetails(PagerInfo pagerInfo);

    /**
     * 修改批次号详情
     * @param model model
     */
    void pushBatchDetailUpdate(VehAuditModel model);


}
