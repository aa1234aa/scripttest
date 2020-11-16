package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.Unit;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UnitService接口<br>
 * 描述： UnitService接口，在xml中引用<br>
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
 * <td>2018-11-05 17:33:20</td>
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
public interface IUnitService extends IBaseService {

    /**
     * 全部查询
     * @param pagerInfo 分页查询条件
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 列表查询
     * @param map 查询条件
     * @return 集合
     */
    List<UnitModel> findList(Map<String, Object> map);

    /**
     * 全部查询树结构
     * @return
     *  返回所有
     */
    Object tree(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    UnitModel get(String id);

    /**
     * 根据单位名称获取
     *
     * @return
     */
    UnitModel getByName(String name);

    /**
     * 根据单位名称获取单位列表,支持模糊查询
     *
     * @return
     */
    List<UnitModel> getListByName(String name);

    /**
     * 根据单位获取单位列表,支持模糊查询ID
     *
     * @return
     */
    List<UnitModel>  getListById(String id);

    /**
     * 根据单位简称获取
     *
     * @return
     */
    UnitModel getByNickName(String nickName);

    /**
     * 根据统一社会信用代码获取
     *
     * @return
     */
    UnitModel getByOrganizationCode(String organizationCode);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(UnitModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(UnitModel model);

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
     * 自定义参数查询单位信息
     *
     * @param params
     * @return
     */
    Unit queryUnitByParam(Map<String, Object> params);

    /**
     * 1,校验单位名称是否存在<br>
     * 2,校验该单位类型编码是否包含指定code
     * @param name 单位名称
     * @param code 单位类型编码
     * @return 校验通过返回单位id, 否则为NULL
     */
    String validateNameCode(String name, String code);

    /**
     * 批量导入模板
     */
    void getImportTemplateFile();


}
