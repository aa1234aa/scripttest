package com.bitnei.cloud.sms.dao;

import com.bitnei.cloud.sms.domain.SmsTaskItem;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sms.domain.SmsTask;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SmsTaskMapper接口<br>
* 描述： SmsTaskMapper接口，在xml中引用<br>
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
* <td>2019-08-16 09:41:04</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Mapper
public interface SmsTaskMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    SmsTask findById(Map<String, Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(SmsTask obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(SmsTask obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String, Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<SmsTask> pagerModel(Map<String, Object> params);

    /**
     * 短信下发分页查询
     * @param params
     * @return
     */
    List<SmsTask> receiverPagerModel(Map<String, Object> params);

    /**
     * 终端短信指令分页查询
     * @param params
     * @return
     */
    List<SmsTask> termSmsTaskPagerModel(Map<String, Object> params);

    /**
     *  导出查询
     * @param params
     * @return
     */
    List<SmsTaskItem> exportDetails(Map<String, Object> params);


    /**
     * 保存短信发送流水号
     * @param bizId
     * @param id
     */
    void updateBizId(Map<String, Object> params);
}
