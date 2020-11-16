package com.bitnei.cloud.api.service;

import com.bitnei.cloud.api.model.ApiDetailAuthorityModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.api.domain.Authority;
import com.bitnei.cloud.api.model.AuthorityModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AuthorityService接口<br>
* 描述： AuthorityService接口，在xml中引用<br>
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
* <td>2019-01-22 10:39:46</td>
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

public interface IAuthorityService extends IBaseService {


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
     AuthorityModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(AuthorityModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(AuthorityModel model);

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
     * @param
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
     * 根据账户id获取用户当前授权账户列表
     * @param pagerInfo
     * @return
     */
    Object getAuthorityApis(PagerInfo pagerInfo);

    /**
     * 根据账号id和接口明细id取消授权
     * @param accountId
     * @param apiDetailId
     * @return
     */
    int delApiAuthority(String accountId, String apiDetailId);

}
