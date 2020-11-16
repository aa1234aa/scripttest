package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.VehicleRealStatus;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleRealStatusService接口<br>
* 描述： VehicleRealStatusService接口，在xml中引用<br>
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
* <td>2019-03-16 14:55:45</td>
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

public interface IVehicleRealStatusService extends ICommonBaseService {




    List<Map<String,Object>> listSimple(Map<String,Object> param);

    Map<String,String> findAreaByCode(String code);
    List<Map<String,String>> findAreaByCodes(List<String> list);
    /**
     * 根据id获取
     *
     * @return
     */
     VehicleRealStatusModel get(String id);

    /**
     * 获取某辆车的静态+实时数据
     * @param vehicleId
     * @param dataNos
     * @return
     */
     VehicleRealStatusModel getByVehicleId(String vehicleId,String  dataNos);

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(VehicleRealStatusModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(VehicleRealStatusModel model);

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

    void batchUpdate(String sqlId,List<Object> params);

    /**
     * 获取车辆的地图气泡信息，纯电动车
     * @param vehId
     * @return
     */
    void updateSingle(String sqlId,Object params);
    PowerOneMakerModel getMakerForPowerOne(String vehId);

    /**
     * 获取车辆的地图气泡信息，增程式电动车
     * @param vehId
     * @return
     */
    PowerTwoMakerModel getMakerForPowerTwo(String vehId);

    /**
     * 获取车辆的地图气泡信息，燃料电动汽车
     * @param vehId
     * @return
     */
    PowerThreeMakerModel getMakerForPowerThree(String vehId);

    /**
     * 获取车辆的地图气泡信息，混合动力车
     * @param vehId
     * @return
     */
    PowerFourMakerModel getMakerForPowerFour(String vehId);

    /**
     * 获取车辆的地图气泡信息，插电式混合动力汽车
     * @param vehId
     * @return
     */
    PowerFiveMakerModel getMakerForPowerFive(String vehId);

    /**
     * 获取车辆的地图气泡信息，传统燃油车
     * @param vehId
     * @return
     */
    PowerSixMakerModel getMakerForPowerSix(String vehId);

    VehicleRealStatusModel getByVehicleId(String vehicleId);

    List<Map<String,Object>> vehOnMap(PagerInfo pagerInfo);
    List<Map<String,Object>> vehOnMap(PagerInfo pagerInfo, boolean ignorePermission);
    void exportOffline(PagerInfo pagerInfo);
}
