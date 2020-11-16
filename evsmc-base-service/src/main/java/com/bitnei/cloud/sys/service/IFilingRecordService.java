package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.FilingRecordModel;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： FilingRecordService接口<br>
 * 描述： FilingRecordService接口，在xml中引用<br>
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
 * <td>2019-07-24 16:20:42</td>
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

public interface IFilingRecordService extends IBaseService {


    /**
     * 全部查询
     * @param pagerInfo 分页
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @param id id
     * @return FilingRecordModel
     */
    FilingRecordModel get(String id);

    /**
     * 根据fromId获取
     * @param fromId fromId
     * @return FilingRecordModel
     */
    FilingRecordModel getByFromId(String fromId);

    /**
     * 新增
     * @param model 新增model
     */
    void insert(FilingRecordModel model);

    /**
     * 编辑
     * @param model 编辑model
     */
    void update(FilingRecordModel model);

    /**
     * 删除多个
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);


}
