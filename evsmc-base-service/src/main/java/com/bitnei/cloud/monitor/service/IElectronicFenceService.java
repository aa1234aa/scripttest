package com.bitnei.cloud.monitor.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.monitor.domain.ElectronicFence;
import com.bitnei.cloud.monitor.model.ElectronicFenceModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ElectronicFenceService接口<br>
 * 描述： ElectronicFenceService接口，在xml中引用<br>
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
 * <td>2019-05-17 11:04:12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */

public interface IElectronicFenceService extends IBaseService {


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
    ElectronicFenceModel get(String id);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(ElectronicFenceModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(ElectronicFenceModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);

    /**
     * 分页查询未关联电子围栏的车辆
     *
     * @param pagerInfo 分页参数
     * @param fenceId   围栏id
     * @return
     */
    Object queryVehsNotLk(PagerInfo pagerInfo, String fenceId);

    /**
     * 分页查询已关联电子围栏的车辆
     *
     * @param pagerInfo 分页参数
     * @param fenceId   围栏id
     * @return
     */
    Object queryVehsLk(PagerInfo pagerInfo, String fenceId);

    /**
     * 添加围栏关联车辆
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     */
    void insertVehLk(PagerInfo pagerInfo);

    /**
     * 删除围栏关联车辆信息
     *
     * @param pagerInfo 参数：电子围栏id、车辆uuid
     * @param id        电子围栏id
     * @param vehVids   车辆uuid
     */
    void deleteVehLk(PagerInfo pagerInfo, String id, String vehVids);

    /**
     * 下载电子围栏关联车辆导入查询模版
     */
    void getImportSearchFile();


    //<editor-fold desc="无用代码">

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);


    /**
     * 批量导入
     *
     * @param file 文件
     */
    void batchImport(MultipartFile file);


    /**
     * 批量更新
     *
     * @param file 文件
     */
    void batchUpdate(MultipartFile file);
    //</editor-fold>
}
