package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleFilingModel;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 车辆防篡改备案service接口<br>
 * 描述： 车辆防篡改备案service接口，在xml中引用<br>
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
 * <td>2019-07-02 11:25:31</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 * @author zxz
 * @version 1.0
 * @since JDK1.8
 */
public interface IVehicleFilingService extends IBaseService {


    /**
     * 全部查询
     * @param pagerInfo 分页对象
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @param id id
     * @return {@link VehicleFilingModel}
     */
    VehicleFilingModel get(String id);


    /**
     * 新增
     *
     * @param model 新增model
     */
    void insert(VehicleFilingModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    void update(VehicleFilingModel model);

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


}
