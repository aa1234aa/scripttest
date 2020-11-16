package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UpgradeLog;
import com.bitnei.cloud.sys.model.UpgradeLogModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UpgradeLogService接口<br>
* 描述： UpgradeLogService接口，在xml中引用<br>
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
* <td>2019-03-09 09:56:09</td>
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

public interface IUpgradeLogService extends IBaseService {


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
     UpgradeLogModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(UpgradeLogModel model);

    /**
     * @param action 操作类型(10 上传升级包、20 编辑升级包、30 删除升级包、40 创建任务、50 删除任务、60 开始任务、
     *               70 开始车辆升级、80 终止车辆升级、90 强制终止车辆升级、100升级包管理重置操作密码)
     *               （ps：部分操作类型2.3不存在）
     *
     * @param taskName 任务名称
     * @param licensePlate 车牌号，多个用','号分割
     * @param desc 描述 操作类型描述
     * @param remarks 备注 也是车牌号拼接
     */
    void save(Integer action, String taskName, String licensePlate, String desc, String remarks);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(UpgradeLogModel model);

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


}
