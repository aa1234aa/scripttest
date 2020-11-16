package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.DataItemDetail;
import com.bitnei.cloud.sys.model.DataItemDetailModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleModel;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemDetailService接口<br>
* 描述： DataItemDetailService接口，在xml中引用<br>
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
* <td>2019-03-16 16:09:17</td>
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

public interface IDataItemDetailService extends ICommonBaseService {




    /**
     * 根据id获取
     *
     * @return
     */
     DataItemDetailModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(DataItemDetailModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(DataItemDetailModel model);

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

    List<DataItem> parseDataNos(String ids);

    List<Map<String,String>> dataItemsSlimming(List<Map<String, String>> datas,
                                               Map<String, String> keyMapper,
                                               boolean translate,
                                               boolean asc,boolean needTermTime);
    List<Map<String, String>> G6DataItemsSlimming(List<Map<String, String>> datas,
                                                         Map<String, String> keyMapper,
                                                  boolean translate, boolean asc,boolean needTermTime);

    Object findDataItemTreeByRuleId(PagerInfo pagerInfo);
    List<Map<String,String>> findDataItemListByRuleId(PagerInfo pagerInfo);

    VehicleModel findVehicle(PagerInfo pagerInfo);
    List<Map<String,String>> customFaults(VehicleModel vehicleModel);
     void exportOffline(PagerInfo pagerInfo);

     boolean isG6(Map<String,Object> params);

}
