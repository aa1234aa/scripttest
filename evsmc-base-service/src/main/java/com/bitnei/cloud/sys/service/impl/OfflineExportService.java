package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.sys.dao.OfflineExportMapper;
import com.bitnei.cloud.sys.domain.OfflineExport;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.commons.util.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： OfflineExportService实现<br>
 * 描述： OfflineExportService实现<br>
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
 * <td>2019-04-03 13:33:38</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OfflineExportService implements IOfflineExportService {

    @Autowired
    private OfflineExportMapper mapper;

    @Autowired
    private WsServiceClient ws;

    private final Environment env;

    private int insert(@NotNull final OfflineExport entity) {

        // todo xzp: 使用 AOP 实现

        entity.setCreateTime(DateUtil.getNow());
        entity.setCreateBy(ServletUtil.getCurrentUser());
        entity.setSystemName(env.getProperty("spring.application.name", "evsmc-base-service"));
        return mapper.insert(entity);
    }

    @NotNull
    @Override
    public String createTask(
        @NotNull final String exportFilePrefixName,
        @NotNull final String exportServiceName,
        @NotNull final String exportMethodName,
        @NotNull final String exportMethodParams) {

        final String createBy = ServletUtil.getCurrentUser();
        final int count = mapper.countDuplicateTask(createBy, exportServiceName,
                env.getProperty("spring.application.name", "evsmc-base-service"));
        if (count > 0) {
            throw new BusinessException("同一账号同一功能只能存在一个进行中的下载任务，直到任务取消或结束，才能再次新建.");
        }

        final String id = UtilHelper.getUUID();

        // 创建离线导出任务
        final OfflineExport entity = new OfflineExport();
        entity.setId(id);
        entity.setExportFileName(
            String.format(
                "%s-%s-%s.csv",
                exportFilePrefixName,
                createBy,
                DateFormatUtils.format(
                    System.currentTimeMillis(),
                    "yyyyMMddHHmmss"
                )
            )
        );
        entity.setStateMachine(OfflineExportStateMachine.CREATED);
        entity.setExportServiceName(exportServiceName);
        entity.setExportMethodName(exportMethodName);
        entity.setExportMethodParams(exportMethodParams);
        entity.setExportNote("");

        final int res = this.insert(entity);
        if (1 != res) {
            throw new BusinessException("创建离线导出任务失败");
        }

        OfflineExportProgress.pushProgress(
            ws,
            id,
            createBy,
            "已创建",
            OfflineExportStateMachine.CREATED,
            0L,
            "离线导出任务创建成功"
        );

        return id;
    }
}
