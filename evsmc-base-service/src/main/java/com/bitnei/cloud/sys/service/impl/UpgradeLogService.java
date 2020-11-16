package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UpgradeLog;
import com.bitnei.cloud.sys.model.UpgradeLogModel;
import com.bitnei.cloud.sys.service.IUpgradeLogService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UpgradeLogService实现<br>
 * 描述： UpgradeLogService实现<br>
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
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UpgradeLogMapper")
@RequiredArgsConstructor
public class UpgradeLogService extends BaseService implements IUpgradeLogService {

    private final IUserService userService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_upgrade_log", "upl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UpgradeLog> entries = findBySqlId("pagerModel", params);
            List<UpgradeLogModel> models = new ArrayList();
            for (UpgradeLog entry : entries) {
                UpgradeLog obj = (UpgradeLog) entry;
                models.add(UpgradeLogModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UpgradeLogModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                UpgradeLog obj = (UpgradeLog) entry;
                models.add(UpgradeLogModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public UpgradeLogModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_upgrade_log", "upl");
        params.put("id", id);
        UpgradeLog entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return UpgradeLogModel.fromEntry(entry);
    }


    @Override
    public void insert(UpgradeLogModel model) {

        UpgradeLog obj = new UpgradeLog();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void save(Integer action, String taskName, String licensePlate, String desc, String remarks) {

        UpgradeLogModel upgradeLogModel = new UpgradeLogModel();
        upgradeLogModel.setAction(action);
        upgradeLogModel.setTaskName(taskName);
        upgradeLogModel.setLicensePlate(licensePlate);
        upgradeLogModel.setDescription(desc);
        upgradeLogModel.setRemarks(remarks);

        upgradeLogModel.setCreateBy(ServletUtil.getCurrentUser());
        upgradeLogModel.setCreateById(userService.findByUsername(ServletUtil.getCurrentUser()).getId());
        upgradeLogModel.setCreateTime(DateUtil.getNow());

        insert(upgradeLogModel);

    }

    @Override
    public void update(UpgradeLogModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_upgrade_log", "upl");

        UpgradeLog obj = new UpgradeLog();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_upgrade_log", "upl");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_upgrade_log", "upl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<UpgradeLog>(this, "pagerModel", params,
                "sys/res/upgradeLog/export.xls", "远程升级日志") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "UPGRADELOG" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UpgradeLogModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UpgradeLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UpgradeLogModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "UPGRADELOG" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UpgradeLogModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UpgradeLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UpgradeLogModel model) {
                update(model);
            }
        }.work();

    }


}
