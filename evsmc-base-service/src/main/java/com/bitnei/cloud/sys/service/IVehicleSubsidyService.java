package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehicleSubsidyModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Lijiezhou on 2018/12/25.
 */
public interface IVehicleSubsidyService {

    /**
     * 车辆补贴信息查询
     *
     * @param pagerInfo
     * @return
     */
    Object subsidyList(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    VehicleSubsidyModel get(String id);

    /**
     * 编辑补贴
     * @return
     */
    void update(VehicleSubsidyModel model);

    /**
     * 车辆补贴信息导出
     *
     * @param pagerInfo 查询参数
     */
    void subsidyExport(PagerInfo pagerInfo);

    /**
     * 批量更新
     *
     * @param file 文件
     */
    void batchUpdate(MultipartFile file);

    /**
     * 批量更新模板
     * @param pagerInfo PagerInfo
     */
    void getBatchUpdateTemplateFile(PagerInfo pagerInfo);

    /**
     * 导入查询模板
     */
    void getImportSearchFile();

    /**
     * 离线导出
     * @param pagerInfo PagerInfo
     * @return 任务id
     */
    String exportOffline(@NotNull final PagerInfo pagerInfo);

}
