package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： OwnerPeopleService接口<br>
 * 描述： OwnerPeopleService接口，在xml中引用<br>
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
 * <td>2018-11-02 15:19:04</td>
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

public interface IOwnerPeopleService extends IBaseService {

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
    OwnerPeopleModel get(String id);

    /**
     * 根据ownerName获取
     * @param ownerName 名称
     * @return
     */
    OwnerPeople findByOwnerName(String ownerName);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(OwnerPeopleModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(OwnerPeopleModel model);

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
     * 自定义参数查询联系人
     *
     * @param map
     * @return
     */
    OwnerPeople queryOwnerPeopleByParam(Map<String, Object> map);

    /**
     * 批量导入模板
     */
    void getImportTemplateFile();
}
