package com.bitnei.cloud.dc.service;

import com.bitnei.cloud.dc.model.PlatformRuleLkModel;
import com.bitnei.cloud.dc.model.PlatformVehicleModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.ForwardVehicle;
import com.bitnei.cloud.dc.model.ForwardVehicleModel;
import com.bitnei.cloud.service.IBaseService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardVehicleService接口<br>
* 描述： ForwardVehicleService接口，在xml中引用<br>
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
* <td>2019-02-21 14:29:14</td>
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

public interface IForwardVehicleService extends IBaseService {


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
     ForwardVehicleModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(ForwardVehicleModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(ForwardVehicleModel model);

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
     * 待转发车辆列表查询
     * @param pagerInfo
     * @return
     */
    Object findToBeForwardedVeh(PagerInfo pagerInfo);

    /**
     * 已转发车辆列表查询
     * @param pagerInfo
     * @return
     */
    Object findForwardedVeh(PagerInfo pagerInfo);

    /**
     * 根据计算平台各状态车辆数量
     * @param platformId
     * @return
     */
    ForwardVehicleModel countPlatformVehByStatus(String platformId);

    /**
     * 车辆状态变更接口
     * @param model
     * @return
     */
    void changeVehStatus(ForwardVehicleModel model);

    /**
     * 关联转发规则后，增加转发车辆
     * @param demo
     * @return
     */
    PlatformRuleLkModel insertPlatformVeh(PlatformRuleLkModel demo);

    /**
     * 移除转发规则后，删除转发车辆
     * @param platformId
     * @param delRuleIds
     * @return
     */
    int delPlatformVeh(String platformId, String delRuleIds);

    /**
     * 查看关联转发规则后的结果详情
     * @param pagerInfo
     * @return
     */
    Object viewRelateRuleResult(PagerInfo pagerInfo);

    /**
     * 导出待转发车辆列表
     * @param pagerInfo 查询参数
     */
    void exportToBeForwardVehs(PagerInfo pagerInfo);

    /**
     * 导出已转发车辆列表
     * @param pagerInfo 查询参数
     */
    void exportForwardedVehs(PagerInfo pagerInfo);

    /**
     * 导出关联转发规则后的结果详情
     * @param pagerInfo 查询参数
     */
    void exportRelateRuleResults(PagerInfo pagerInfo);

    /**
     * 车辆状态批量变更接口
     * @param model
     * @return
     */
    ForwardVehicleModel changeBatchVehStatus(ForwardVehicleModel model);

    /**
     * 查看确认转发结果详情
     * @param pagerInfo
     * @return
     */
    Object viewConfirmResults(PagerInfo pagerInfo);


    /**
     * 设置车辆转发状态（陈鹏）
     *
     * @param vid           车辆uuid
     * @param forwardId     转发配置id
     * @param time          转发时间
     *
     */
    void changeForwardStatus(String vid, String forwardId, String time);

    /**
     * 下载待转发车辆列表导入查询模板
     */
    void getImportSearchFile();

    /**
     * 下载已转发车辆列表导入查询模板
     */
    void getImportForwardedSearchFile();

    /**
     * 定时更新平台转发车辆
     */
    void updateForwardVehicle();

    /**
     * 平台添加的车辆列表
     * @return
     */
    Object pagePlatFormVehs(PagerInfo pagerInfo);

    /**
     * 移到黑名单
     */
    PlatformVehicleModel addPlatformVehicle(PlatformVehicleModel model);

    /**
     * 移到黑名单
     */
    void moveToBlakList(String ids);

    /***
     * 查看添加结果详情
     * @param pagerInfo
     * @return
     */
    List<ForwardVehicle> addFailResult(PagerInfo pagerInfo);

    /**
     * 添加全部查询结果
     * @param pagerInfo
     * @return
     */
    PlatformVehicleModel batchAddPlatformVehicle(PagerInfo pagerInfo);

    /**
     * 导出待转发车辆列表
     * @param pagerInfo 查询参数
     */
    void exportPagePlatFormVehs(PagerInfo pagerInfo);
}
