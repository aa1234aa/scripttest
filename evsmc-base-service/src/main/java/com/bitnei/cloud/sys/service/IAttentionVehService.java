package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.AttentionVeh;
import com.bitnei.cloud.sys.model.AttentionVehModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AttentionVehService接口<br>
* 描述： AttentionVehService接口，在xml中引用<br>
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
* <td>2019-03-19 18:45:16</td>
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

public interface IAttentionVehService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 返回关注数据的json列表数据，只支持分页
     * @param pagerInfo
     * @return
     */
    Object getJsonDataList(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     AttentionVehModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(AttentionVehModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(AttentionVehModel model);

    /**
     * @param itemId 关注的业务对象id
     * @param type 关注的类型
     * @param item 关注的Json数据原始对象
     */
    int updateAttentionData(String itemId, Integer type, Object item);

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

    /**
     * 根据类型获取当前用户关注的所有该类型的数据set<itemId>
     * @param type 0:车辆，1: 故障, 2: 消息
     * @return
     */
    Set<String> getAttentionItems(Integer type);

}
