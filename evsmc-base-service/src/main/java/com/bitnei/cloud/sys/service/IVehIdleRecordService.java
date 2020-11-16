package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehIdleRecordModel;
import com.bitnei.cloud.service.IBaseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehIdleRecordService接口<br>
* 描述： VehIdleRecordService接口，在xml中引用<br>
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
* <td>2019-03-06 14:44:04</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/

public interface IVehIdleRecordService extends IBaseService {


    /**
     * 历史全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 实时全部查询
     * @return
     *  返回所有
     */
    Object realTimeList(PagerInfo pagerInfo);

    /**
     * 离线车辆弹窗
     * @return
     *  返回所有
     */
    Object popupList(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     VehIdleRecordModel get(String id);

    /**
     * 获取当前提醒状态
     * @return
     */
    String getNowReminderStatus();

    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(VehIdleRecordModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(VehIdleRecordModel model);

    /**
     * 异常提醒处理
     * @param model
     */
    void updateRmakAndType(VehIdleRecordModel model);
    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 历史导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 离线导出
     * @param pagerInfo
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);

    /**
     * 实时导出
     *
     * @param pagerInfo 查询参数
     */
    void realTimeExport(PagerInfo pagerInfo);

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
     * 秒钟换天数
     * @param seconds
     * @return
     */
    String  secondsToDays(String seconds);

    /**
     *通过vid查询
     * @param vid
     * @return
     */
    VehIdleRecordModel getByVid(String vid);

    /**
     * 保存离线提醒信息
     * @param offLineTime
     * @param mileagePoor
     * @param whetherFrame
     * @return
     */
    Map<String,Object> saveReminderTime(String offLineTime,String mileagePoor,String whetherFrame);
}
