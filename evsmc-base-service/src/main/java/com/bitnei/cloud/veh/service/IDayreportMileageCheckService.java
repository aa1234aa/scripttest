package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.domain.DayreportMileageCheck;
import com.bitnei.cloud.veh.model.DayreportMileageCheckModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportMileageCheckService接口<br>
* 描述： DayreportMileageCheckService接口，在xml中引用<br>
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
* <td>2019-03-14 16:46:26</td>
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

public interface IDayreportMileageCheckService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);


    /**
     * 根据id获取核查数据详情
     * @param id
     * @return
     */
    DayreportMileageCheckModel dataFindById(String id);


    /**
     * 导出
     *
     * @param params 查询参数
     */
    void export(PagerInfo pagerInfo);


    /**
     * 下载车辆导入查询模板
     */
    void getImportSearchFile();
}