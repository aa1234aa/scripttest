package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.dc.model.PlatformVehiclePushLogModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.EcoRespModel;
import org.springframework.web.multipart.MultipartFile;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformVehiclePushLogService接口<br>
* 描述： PlatformVehiclePushLogService接口，在xml中引用<br>
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
* <td>2019-02-19 14:19:06</td>
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

public interface IPlatformVehiclePushLogService extends IBaseService {


    /**
     * 全部查询
     * @param pagerInfo pagerInfo
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     * @param id id
     * @return PlatformVehiclePushLogModel
     */
     PlatformVehiclePushLogModel get(String id);


    /**
     * 新增
     * @param model  新增model
     */
    void insert(PlatformVehiclePushLogModel model);

    /**
     * 新增
     * @param model model
     */
    void add(EcoRespModel model);

    /**
     * 编辑
     * @param model  编辑model
     */
    void update(PlatformVehiclePushLogModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
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


}
