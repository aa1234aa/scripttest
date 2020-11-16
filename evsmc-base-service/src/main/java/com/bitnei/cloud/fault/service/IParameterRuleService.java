package com.bitnei.cloud.fault.service;

import com.bitnei.cloud.fault.domain.ParameterRule;
import com.bitnei.cloud.fault.model.ParameterRuleModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.VehModelAlarm;
import com.bitnei.cloud.sys.model.VehModelAlarmModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ParameterRuleService接口<br>
* 描述： ParameterRuleService接口，在xml中引用<br>
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
* <td>2019-02-27 16:35:01</td>
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

public interface IParameterRuleService extends IBaseService {


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
     ParameterRuleModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(ParameterRuleModel model);

    /**
     * 新增返回id
     * @param model  新增model
     * @return
     */
    String insertReturnId(ParameterRuleModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(ParameterRuleModel model);

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
     * 统计数量
     * @param params
     * @return
     */
    int count(Map<String, Object> params);

    /**
     * 删除车型后, 删除规则
     * @param vehModelId
     */
    void deleteByVehModelId(String vehModelId);

    /**
     * 将车辆型号里的通用规则同步到参数异常规则
     *
     * @param newModel 新数据
     * @param oldModel 旧数据
     */
    void handleVehicleModelRule(VehModelAlarmModel newModel, VehModelAlarm oldModel);

    /**
     * 将车辆型号里的通用规则同步到参数异常规则
     *
     * @param vehModelId 车辆型号ID
     */
    void disabledVehicleModelRule(String vehModelId);
}
