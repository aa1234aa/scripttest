package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.UnitTypeModel;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UnitTypeService接口<br>
 * 描述： UnitTypeService接口，在xml中引用<br>
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
 * <td>2018-12-20 11:48:35</td>
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

public interface IUnitTypeService extends IBaseService {

    /**
     * 树状
     *
     * @param pagerInfo PagerInfo
     * @return Object
     */
    Object tree(PagerInfo pagerInfo);

    /**
     * 全部查询
     * @param pagerInfo PagerInfo
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @param id 名称
     * @return UnitTypeModel
     */
    UnitTypeModel get(String id);

    /**
     * 根据名称获取
     *
     * @param name 名称
     * @return UnitTypeModel
     */
    UnitTypeModel getByName(String name);

    /**
     * 根据编码获取
     *
     * @param code 编码
     * @return UnitTypeModel
     */
    UnitTypeModel getByCode(String code);


    /**
     * 新增
     *
     * @param model 新增model
     */
    void insert(UnitTypeModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    void update(UnitTypeModel model);

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
     * 查询关联单位类型的单位数量
     *
     * @param unitTypeId 单位类型id
     * @return 单位数量
     */
    int findByUnitTypeIdCount(String unitTypeId);


}
