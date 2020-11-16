package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.dc.model.DataItemTempModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.model.DataItemModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemService接口<br>
* 描述： DataItemService接口，在xml中引用<br>
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
* <td>2019-01-30 17:28:53</td>
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

public interface IDataItemService extends IBaseService {


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
     DataItemModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(DataItemModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(DataItemModel model);

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
     * 根据协议ID获取数据项列表
     *
     * @param ruleId 查询参数
     */
    List<DataItemTempModel> getListByRuleId(String ruleId);

    /**
     * 根据协议ID获取分页数据项列表
     * @param pagerInfo
     * @return
     */
    Object getPageListByRuleId(PagerInfo pagerInfo);


    /**
     * 根据转发平台ID获取数据项列表
     *
     * @param platformId 查询参数
     */
    List<DataItemTempModel> getListByPlatformId(String platformId);

    /**
     * 根据转发平台ID获取分页数据项列表
     * @param pagerInfo
     * @return
     */
    Object getPageListByPlatformId(PagerInfo pagerInfo);

    /**
     * 根据数据项组查询树形列表
     * @param pagerInfo
     * @return
     */
    Object tree(PagerInfo pagerInfo);

    /**
     * 拷贝数据项
     * @param demo1
     */
    DataItemTempModel copyItems(DataItemTempModel demo1);

    /**
     * 根据数据项名称获取详细信息
     * @param name
     * @param ruleTypeId
     * @return
     */
    DataItemModel getByName(String name, String ruleTypeId);


    /**
     * 导入模板下载
     */
    void getImportTemplateFile();

    /**
     * 根据协议编码查找自定义数据项
     * @param ruleTypeCode
     * @return
     */
    List<DataItemModel> findCustomizeByRuleTypeCode(String ruleTypeCode);

    /**
     * 查询协议类型数据属性
     * @param ruleTypeId
     * @return
     */
    List<DataItem> findRuleTypeDataItems (String ruleTypeId);

    /**
     * 新　根据协议ID获取分页数据项列表
     * @param pagerInfo
     * @return
     */
    Object listPageListByRuleId(PagerInfo pagerInfo);

    /**
     * 根据检测规则ID获取分页数据项列表
     * @param pagerInfo
     * @return
     */
    Object getListByCheckRuleId(PagerInfo pagerInfo);
}
