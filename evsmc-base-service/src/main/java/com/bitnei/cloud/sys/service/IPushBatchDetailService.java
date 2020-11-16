package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.PushBatchDetailModel;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： PushBatchDetailService接口<br>
 * 描述： PushBatchDetailService接口，在xml中引用<br>
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
 * <td>2019-02-27 19:37:27</td>
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

public interface IPushBatchDetailService extends IBaseService {


    /**
     * 全部查询
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @return
     */
    PushBatchDetailModel get(String id);

    /**
     * 根据batchId和formName获取model
     * @param batchId 批次id
     * @param formName 关联名称
     * @return model
     */
    PushBatchDetailModel getByBatchIdAndFormName(String batchId, String formName);

    /**
     * 根据formId获取model
     * @param formId 关联id
     * @return model
     */
    PushBatchDetailModel getByFormId(String formId);

    /**
     * 新增
     * @param model 新增model
     */
    void insert(PushBatchDetailModel model);

    /**
     * 编辑
     * @param model 编辑model
     */
    void update(PushBatchDetailModel model);

    /**
     * 删除多个
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);


}
