package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.VehicleInfoModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleWithOnlineStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehicleService接口<br>
 * 描述： VehicleService接口，在xml中引用<br>
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
 * <td>2018-12-12 17:40:52</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */

public interface IVehicleService extends IBaseService {


    /**
     * 车辆列表查询
     *
     * @param pagerInfo PagerInfo
     * @return Object
     */
    Object vehicleList(PagerInfo pagerInfo);

    /**
     * 车辆列表查询
     *
     * @param pagerInfo PagerInfo
     * @param extraMap  参数列表
     * @return Object
     */
    Object vehicleList(PagerInfo pagerInfo, Map<String, Object> extraMap);

    /**
     * 车辆列表
     *
     * @param pagerInfo PagerInfo
     * @return PagerResult
     */
    PagerResult vehicleListByBaseInfo(PagerInfo pagerInfo);

    /**
     * 车辆列表
     *
     * @param vins          list<String>
     * @param innerNos      list<String>
     * @param licensePlates list<String>
     * @param iccids        list<String>
     * @return List<VehicleModel>
     */
    List<VehicleModel> vehicleListByBaseInfoList(List<String> vins, List<String> innerNos,
                                                 List<String> licensePlates, List<String> iccids);

    /**
     * 全部查询 <br>
     * 未做数据权限控制
     *
     * @param params 参数列表
     * @return List<VehicleModel>
     */
    List<VehicleModel> list(Map<String, Object> params);

    /**
     * 根据id获取
     *
     * @param id id
     * @return VehicleModel
     */
    VehicleModel get(String id);


    /**
     * 根据车架号获取
     *
     * @param vin vin
     * @return VehicleModel
     */
    VehicleModel getByVin(String vin);

    /**
     * 不存在则返回null
     *
     * @param vin vin
     * @return VehicleModel
     */
    VehicleModel getByVinOrNull(String vin);

    /**
     * 不存在则返回null，校验权限
     * @param vin
     * @return
     */
    VehicleModel getByVinValidateAuth(String vin);

    /**
     * 根据vin获取车辆信息以及车辆状态
     *
     * @param vin vin
     * @return VehicleWithOnlineStatus
     */
    VehicleWithOnlineStatus getVehicleWithStatusByVin(String vin);

    /**
     * 根据系统内部编码获取
     *
     * @param uuid uuid
     * @return VehicleModel
     */
    VehicleModel getByUuid(String uuid);

    /**
     * 根据车牌号获取
     *
     * @param licensePlate 车牌号
     * @return VehicleModel
     */
    VehicleModel getByLicensePlate(String licensePlate);

    /**
     * 根据内部编号获取
     *
     * @param interNo 内部编号
     * @return VehicleModel
     */
    VehicleModel getByInterNo(String interNo);

    /**
     * 根据运营内部编号获取
     *
     * @param operInterNo 运营内部编号
     * @return VehicleModel
     */
    VehicleModel getByOperInterNo(String operInterNo);


    /**
     * 新增
     *
     * @param model 新增model
     */
    void insert(VehicleModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    void update(VehicleModel model);

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

    /**
     * 国家平台数据导出
     *
     * @param pagerInfo 查询参数
     */
    void nationExport(PagerInfo pagerInfo);

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
     * 生成车辆内部编号,临+随机7位英文与数字组合，英文字符大写，唯一
     *
     * @return 内部编号
     */
    String generateInterNo();


    /**
     * 查询用户的权限车辆id
     *
     * @param userId 用户ID
     * @return 车辆ID列表
     */
    List<String> getVehicleIdsByUserId(String userId);

    /**
     * 根据vin获取车辆的实时在线状态
     *
     * @param vin vin
     * @return 1:在线 2:离线
     */
    Integer getVehicleOnlineStatus(String vin);

    List<Vehicle> findByVins(List<String> vins);

    Map<String, Integer> getVehicleOnlineStatusMap(List<String> vins);

    /**
     * 下载车辆导入查询模板
     */
    void getImportSearchFile();

    /**
     * 生成导入模板文件
     */
    void getImportTemplateFile();

    /**
     * 生成批量更新模板文件
     *
     * @param pagerInfo PagerInfo
     */
    void getBatchUpdateTemplateFile(PagerInfo pagerInfo);

    /**
     * 离线导出
     *
     * @param pagerInfo PagerInfo
     * @return 任务id
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);

    /**
     * 给大数据那边提供单独格式的车辆列表接口
     *
     * @param vid        车辆VID
     * @param updateTime 车辆更新时间
     * @return
     */
    List<VehicleInfoModel> vehicles(String vid, String updateTime);

    /**
     * 查询全部车辆基础信息集合供外部关联使用
     *
     * @param pagerInfo 查询参数
     * @return
     */
    List<Vehicle> findAllForLk(PagerInfo pagerInfo);

    /**
     * 分页查询车辆基础信息供外部关联使用
     *
     * @param pagerInfo
     * @return
     */
    PagerResult findForLk(PagerInfo pagerInfo);

    /**
     * 用户权限车辆
     * @param userId
     * @param pagerInfo
     * @return
     */
    Object userVehicles(String userId, PagerInfo pagerInfo);

    /**
     * 非用户权限车辆
     * @param userId
     * @param pagerInfo
     * @return
     */
    Object userVehiclesNot(String userId, PagerInfo pagerInfo);

    /**
     * 根据id查询芯片型号id
     * @param id 车辆id
     * @return 芯片型号id
     */
    String getChipModelId(String id);

    /**
     * 查询车辆销售信息
     * @param id
     * @return
     */
    Map<String, Object> getSellInfo(String id);

    VehicleModel getByIdSimple(String id);

}
