package com.bitnei.cloud.veh.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.veh.model.HistoryStateModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： HistoryStateService接口<br>
 * 描述： HistoryStateService接口，在xml中引用<br>
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
 * <td>2019-03-09 11:23:08</td>
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

public interface IHistoryStateService extends IBaseService {

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
    HistoryStateModel get(String id);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 下载车辆历史状态报表导入查询模板
     */
    void getImportSearchFile();

    /**
     * 离线导出
     *
     * @param pagerInfo 查询参数
     * @return 离线导出任务ID
     */
    @NotNull
    String exportOffline(@NotNull final PagerInfo pagerInfo);

    /**
     * 在线导出
     *
     * @param pagerInfo 查询参数
     * @return 在线导出文件流
     */
    @NotNull
    ResponseEntity<StreamingResponseBody> exportOnline(@NotNull final PagerInfo pagerInfo) throws IOException;

}
