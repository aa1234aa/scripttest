package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.model.NotifierVehicleLkModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： NotifierVehicleLkService接口<br>
* 描述： NotifierVehicleLkService接口，在xml中引用<br>
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
* <td>2019-03-06 17:37:36</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/

public interface INotifierVehicleLkService extends IBaseService {


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
     NotifierVehicleLkModel get(String id);

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    int insert(NotifierVehicleLkModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(NotifierVehicleLkModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 根据用户id删除分配的车辆
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 批量新增
     * @param pagerInfo 查询条件
     */
    void insertBatch(PagerInfo pagerInfo);
}
