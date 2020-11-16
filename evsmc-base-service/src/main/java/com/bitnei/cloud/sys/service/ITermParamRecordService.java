package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.TermParamRecordModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermParamRecordService接口<br>
 * 描述： TermParamRecordService接口，在xml中引用<br>
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
 * <td>2019-03-07 15:28:19</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */

public interface ITermParamRecordService extends IBaseService {


    /**
     * 全部查询
     *
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    TermParamRecordModel get(String id);

    ResultMsg getParamsDetail(PagerInfo pagerInfo);

    TermParamRecordModel findByVin(String vin);


    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    int insert(TermParamRecordModel model);

    /**
     * 新增或更新
     *
     * @param model
     */
    void insertOrUpdate(TermParamRecordModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(TermParamRecordModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);

    /**
     * 导出
     *
     * @param params 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 导出参数详情记录列表
     * @param pagerInfo
     */
    void exportParamsDetail(PagerInfo pagerInfo);

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
     * 全量参数查询
     *
     * @param vehId 车牌号
     * @return 返回线程对象
     */
    void allQuery(String vehId, String userName, Integer vehState, String remark, String describes);

    TermParamRecordModel save(VehicleModel vehicleModel, String userName, Integer vehState,
                              String operation, String describes);

    List<VehicleModel> save(List<String> vehIds, String userName, String remark, String describes);

    Object verification(JSONObject jsonObject);
}
