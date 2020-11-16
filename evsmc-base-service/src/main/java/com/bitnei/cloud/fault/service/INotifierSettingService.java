package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.model.NotifierSettingModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： NotifierSettingService接口<br>
 * 描述： NotifierSettingService接口，在xml中引用<br>
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
 * <td>2019-03-06 11:31:31</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */

public interface INotifierSettingService extends IBaseService {


    /**
     * 全部查询
     *
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /***
     * 车辆－负责人查询
     * @param pagerInfo
     * @return
     */
    Object notifiers(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    NotifierSettingModel get(String id);


    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(NotifierSettingModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(NotifierSettingModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);

    /**
     * 同步负责人账号下的车辆
     */
    void syncNotifierVehicle();

    NotifierSettingModel getNotifierByUserIdAndVehicleId(String vehicleId);

    /**
     * 批量导入
     *
     * @param file 文件
     */
    void batchImport(MultipartFile file);

    /**
     * 导入模版下载
     */
    void getImportTemplateFile();

    /**
     * 查询车辆报警所需推送负责人列表
     * @param params 查询参数
     * @return
     */
    List<String> findForAlarm(Map<String,Object> params);

    /**
     * 查询车辆安全风险通知报警所需推送负责人用户列表
     * @param params 查询参数
     * @return user集合
     */
    List<String> findForRisk(Map<String,Object> params);
}
