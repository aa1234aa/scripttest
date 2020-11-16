package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UppackageInfo;
import com.bitnei.cloud.sys.model.CheckPasswordDto;
import com.bitnei.cloud.sys.model.UppackageInfoModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageInfoService接口<br>
* 描述： UppackageInfoService接口，在xml中引用<br>
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
* <td>2019-03-04 14:05:24</td>
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

public interface IUppackageInfoService extends IBaseService {


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
     UppackageInfoModel get(String id);


    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(UppackageInfoModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(UppackageInfoModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void editPassword(UppackageInfoModel model);

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

    Map upload(Map<String, Object> result, MultipartFile file, String userName);

    Map upload(Map<String, Object> result, File file, String userName);

    /**
     * 检验升级包密码，检验不通过抛业务异常
     * @param checkPasswordDto
     * @return
     */
    Boolean checkPassword(CheckPasswordDto checkPasswordDto);
}
