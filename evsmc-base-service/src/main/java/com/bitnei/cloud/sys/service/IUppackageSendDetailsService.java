package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UppackageSendDetails;
import com.bitnei.cloud.sys.model.UppackageSendDetailsModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleUpgradeLogSumDto;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageSendDetailsService接口<br>
* 描述： UppackageSendDetailsService接口，在xml中引用<br>
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
* <td>2019-03-05 15:53:14</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/

public interface IUppackageSendDetailsService extends IBaseService {


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
     UppackageSendDetailsModel get(String id);

    /**
     * 如果对象不存在，就返回null
     * @param id
     * @return
     */
    UppackageSendDetailsModel getOrNull(String id);


    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(UppackageSendDetailsModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(UppackageSendDetailsModel model);

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

    void resetState(String taskId, String remark, String vin);

    List<UppackageSendDetailsModel> getAllByTaskId(String taskId);

    String initUppackageArgs(UppackageSendDetailsModel uppackageSendDetailsModel,
                                    String uppackageId, String protocolType);

    Integer getImplementUpgradeCountByVin(String vin);

    PagerResult vehicleUpgradeDetails(PagerInfo pagerInfo);

    List<VehicleUpgradeLogSumDto> importSearchVehicleUpgradeDetails(List<String> vins, List<String> innerNos,
                                                                    List<String> licensePlates, List<String> iccids);

    void exportVehicleUpgradeLog(PagerInfo pagerInfo);

    PagerResult getAllVehUpgradeLogs();
}
