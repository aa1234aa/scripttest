package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UserFaultResponseModeSetting;
import com.bitnei.cloud.sys.model.UserFaultResponseModeSettingModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserFaultResponseModeSettingService接口<br>
* 描述： UserFaultResponseModeSettingService接口，在xml中引用<br>
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
* <td>2019-06-04 16:18:15</td>
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

public interface IUserFaultResponseModeSettingService extends IBaseService {

    /**
     * 根据id获取
     *
     * @return
     */
     UserFaultResponseModeSettingModel get(String id);

    /**
     * 根据用户UserId获取
     *
     * @return
     */
    UserFaultResponseModeSettingModel getByUserId();

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(UserFaultResponseModeSettingModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(UserFaultResponseModeSettingModel model);

    /**
     *  设置响应方式
     * @param model
     */
    void setting(UserFaultResponseModeSettingModel model);
}
