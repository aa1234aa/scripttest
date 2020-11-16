package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.RemoteDiagnose;
import com.bitnei.cloud.sys.model.DiagnoseDto;
import com.bitnei.cloud.sys.model.RemoteDiagnoseModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleModel;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RemoteDiagnoseService接口<br>
* 描述： RemoteDiagnoseService接口，在xml中引用<br>
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
* <td>2019-03-25 16:14:50</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/

public interface IRemoteDiagnoseService extends IBaseService {


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
     RemoteDiagnoseModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(RemoteDiagnoseModel model);

    String insertReturnId(RemoteDiagnoseModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(RemoteDiagnoseModel model);

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
     * @param params 查询参数
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

    ResultMsg diagnose(DiagnoseDto diagnoseDto);
    List<Map<String, String>> dealDataMap(Map<String, String> dataMap, String[] dataItems,
                                          VehicleModel vehicleModel);
    List<Map<String, String>> dealDataMapFast(Map<String, String> dataMap, String[] dataItems,
                                              VehicleModel vehicleModel);

}
