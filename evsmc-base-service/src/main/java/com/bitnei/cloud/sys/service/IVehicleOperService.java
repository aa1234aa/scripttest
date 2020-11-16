package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleOperModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 车辆运营使用service接口
 */
public interface IVehicleOperService extends IBaseService {


    /**
     * 根据id获取
     * @param id id
     * @return VehicleOperModel
     */
    VehicleOperModel get(String id);

    /**
     * insert Or update 车辆销售信息
     * @param model VehicleOperModel
     */
    void save(VehicleOperModel model);

    /**
     * 分页查询车辆销售列表
     * @param pagerInfo 分页对象
     */
    Object list(PagerInfo pagerInfo);


    /**
     * 删除
     * @param ids id列表,多个逗号间隔
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
     * 批量更新
     *
     * @param file 文件
     */
    void batchUpdate(MultipartFile file);

    /**
     * 批量导入模板
     */
    void getImportTemplateFile();

    /**
     * 批量更新模板
     * @param pagerInfo PagerInfo
     */
    void getBatchUpdateTemplateFile(PagerInfo pagerInfo);

    /**
     * 导入查询模板
     */
    void getImportSearchFile();

    int updateInspectStatusOutOfPeriodByVins(List<String> vins);
}
