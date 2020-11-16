package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.veh.domain.DataCheckRecord;
import com.bitnei.cloud.veh.model.DataCheckRecordModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.veh.model.DataCheckResult;
import com.bitnei.cloud.veh.model.DataCheckStatistic;
import com.bitnei.cloud.veh.model.DataCheckStatisticParam;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRecordService接口<br>
* 描述： DataCheckRecordService接口，在xml中引用<br>
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
* <td>2019-09-17 14:11:42</td>
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

public interface IDataCheckRecordService extends IBaseService {


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
     DataCheckRecordModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(DataCheckRecordModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(DataCheckRecordModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

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

    /**
     * 车辆下线检测：开始检测
     * @param vin
     * @return
     */
    DataCheckResult check(String vin);

    /**
     * 检测前校验
     * @param vin
     * @return
     */
    int validateCheck(String vin);

    /**
     * @param param 统计参数
     * @return
     */
    DataCheckStatistic statistic(DataCheckStatisticParam param);

    DataCheckResult getCheckResult(String id);
}
