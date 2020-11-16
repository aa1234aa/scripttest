package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.UserFaultResponseModeSetting;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserFaultResponseModeSettingMapper接口<br>
* 描述： UserFaultResponseModeSettingMapper接口，在xml中引用<br>
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
@Mapper
public interface UserFaultResponseModeSettingMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    UserFaultResponseModeSetting findById(Map<String,Object> params);


    /**
     * 通过UserId查询
     *
     * @param params
     * @return
     */
    UserFaultResponseModeSetting findByUserId(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(UserFaultResponseModeSetting obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(UserFaultResponseModeSetting obj);

    /**
     * 查询设置开通接收消息的车辆负责人列表
     * @param params
     * @return
     */
    List<UserFaultResponseModeSetting> notifierStting(Map<String,Object> params);

}
