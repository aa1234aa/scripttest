package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.VehicleSellModel;
import org.springframework.web.multipart.MultipartFile;

/**
 * 车辆销售service接口
 */
public interface IVehicleSellService extends IBaseService {

    /**
     * 根据id获取
     * @param id
     * @return
     */
    VehicleSellModel get(String id);

    /**
     * insert Or update 车辆销售信息
     * @param model
     */
    void save(VehicleSellModel model);

    /**
     * 分页查询车辆销售列表
     * @param pagerInfo 分页对象
     * @return
     */
    Object list(PagerInfo pagerInfo);


    /**
     * 删除
     * @param ids id列表,多个逗号间隔
     * @return
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
}
