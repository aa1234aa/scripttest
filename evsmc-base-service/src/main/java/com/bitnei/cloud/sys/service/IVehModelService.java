package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehModelModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehModelService接口<br>
* 描述： VehModelService接口，在xml中引用<br>
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
* <td>2018-11-12 14:54:43</td>
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

public interface IVehModelService extends IBaseService {


    /**
     * 全部查询
     * @param pagerInfo {@link PagerInfo}
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 查询列表
     * @param map 查询条件
     * @return List<VehModelModel>
     */
    List<VehModelModel> findList(Map<String, Object> map);

    /**
     * 查询全部
     * @return List<VehModelModel>
     */
    List<VehModelModel> getAll();

    /**
     * 根据id获取
     * @param id id
     * @return VehModelModel
     */
     VehModelModel get(String id);

     /**
     * 根据名称获取
     * @param vehModelName 名称
     * @return VehModelModel
     */
     VehModelModel getByName(String vehModelName);

    /**
     * 新增
     * @param model  新增model
     */
    void insert(VehModelModel model);

    /**
     * 编辑
     * @param model  编辑model
     */
    void update(VehModelModel model);

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
     * 根据一组id查询, 返回一组车辆型号名称。
     * @param vehModelIds ids
     * @return 返回一组车辆型号名称
     */
    Map<String, String> getVehModelModelNames(String vehModelIds);

    /**
     * 批量导入模板
     */
    void getImportTemplateFile();

    /**
     * 查询车辆所属车型信息
     * @param vehicleId 车辆ID
     * @return
     */
    VehModelModel getByVehicleId(String vehicleId);
}
