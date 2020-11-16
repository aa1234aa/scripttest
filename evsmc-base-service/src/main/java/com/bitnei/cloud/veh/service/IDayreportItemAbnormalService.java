package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.veh.model.DayreportItemAbnormalModel;
import org.jetbrains.annotations.NotNull;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportItemAbnormalService接口<br>
* 描述： DayreportItemAbnormalService接口，在xml中引用<br>
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
* <td>2019-09-20 09:28:41</td>
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
public interface IDayreportItemAbnormalService extends IBaseService {


    /**
     * 全部查询
     * @param pagerInfo 分页查询对象
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @param id id
     * @return 新能源车辆异常数据项报表Model
     */
     DayreportItemAbnormalModel get(String id);

    /**
     * 导出
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 离线导出
     * @param pagerInfo 查询参数
     * @return 离线导出任务ID
     */
    @NotNull
    String exportOffline(@NotNull final PagerInfo pagerInfo);



}
