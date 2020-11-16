package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.AfDbcMapper;
import com.bitnei.cloud.sys.domain.AfDbc;
import com.bitnei.cloud.sys.model.AfDbcModel;
import com.bitnei.cloud.sys.service.IAfDbcService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AfDbcService实现<br>
 * 描述： AfDbcService实现<br>
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
 * <td>2019-03-04 17:10:36</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.AfDbcMapper")
@RequiredArgsConstructor
public class AfDbcService extends BaseService implements IAfDbcService {

    private final AfDbcMapper afDbcMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "dbc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<AfDbc> entries = findBySqlId("pagerModel", params);
            List<AfDbcModel> models = new ArrayList();
            for (AfDbc entry : entries) {
                AfDbc obj = (AfDbc) entry;
                models.add(AfDbcModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AfDbcModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                AfDbc obj = (AfDbc) entry;
                models.add(AfDbcModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public AfDbcModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "dbc");
        params.put("id", id);
        AfDbc entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AfDbcModel.fromEntry(entry);
    }


    @Override
    public void insert(AfDbcModel model) {

        AfDbc obj = new AfDbc();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(AfDbcModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "dbc");

        AfDbc obj = new AfDbc();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "dbc");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "dbc");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<AfDbc>(this, "pagerModel", params, "sys/res/afDbc/export.xls", "DBC文件管理") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "AFDBC" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AfDbcModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AfDbcModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AfDbcModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "AFDBC" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AfDbcModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AfDbcModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AfDbcModel model) {
                update(model);
            }
        }.work();

    }

    public String createDBCFile(String userId, String dbcId, String dbcName, String path) throws IOException {
        String dir = "/tmp/dbc/" + userId + "/";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = dir + dbcName + ".dbc";
        file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        @Cleanup FileOutputStream output = new FileOutputStream(file);
        String content = createDbcContent(dbcId, path);
        output.write(content.getBytes("UTF-8"));
        output.close();
        return filePath;
    }

    /**
     * 拼接DBC文件内容
     *
     * @param dbcId
     * @param path
     * @return
     */
    private String createDbcContent(String dbcId, String path) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_af_dbc", "abc");
        params.put("state", 1);
        params.put("path", path);
        params.put("type", 4);
        List<AfDbc> list = afDbcMapper.getAllByParams(params);

        if (null != list && list.size() > 0) {
            StringBuffer content = new StringBuffer();
            content.append("VERSION \" \"\n");
            content.append("\tNS_DESC_\n");
            content.append("\tCM_\n");
            content.append("\tBA_DEF_\n");
            content.append("\tBA_\n");
            content.append("\tVAL_\n");
            content.append("\tCAT_DEF_\n");
            content.append("\tCAT_\n");
            content.append("\tFILTER\n");
            content.append("\tBA_DEF_DEF_\n");
            content.append("\tEV_DATA_\n");
            content.append("\tENVVAR_DATA_\n");
            content.append("\tSGTYPE_\n");
            content.append("\tSGTYPE_VAL_\n");
            content.append("\tBA_DEF_SGTYPE_\n");
            content.append("\tBA_SGTYPE_\n");
            content.append("\tSIG_TYPE_REF_\n");
            content.append("\tVAL_TABLE_\n");
            content.append("\tSIG_GROUP_\n");
            content.append("\tSIG_VALTYPE_\n");
            content.append("\tSIGTYPE_VALTYPE_\n");
            content.append("\n");
            content.append("BS_:\n");
            content.append("\n");

            Map<String, AfDbc> buMap = new LinkedHashMap<>();
            List<AfDbc> dbcList3 = new ArrayList<AfDbc>();
            List<AfDbc> dbcList4 = new ArrayList<AfDbc>();
            String parantId = "";
            for (AfDbc dbc : list) {
                dbcList4.add(dbc);
                if (!parantId.equals(dbc.getParentId())) {
                    parantId = dbc.getParentId();
                    // 数据项父级报文目录实体
                    Map<String, Object> params1 = DataAccessKit.getAuthMap("sys_af_dbc", "abc");
                    params.put("state", 1);
                    params.put("id", parantId);
                    List<AfDbc> params1Results = afDbcMapper.getAllByParams(params1);
                    AfDbc entity3 = CollectionUtils.isNotEmpty(params1Results) ? params1Results.get(0) : null;
                    // 报文父级ECU目录实体
                    if (null != entity3 && !buMap.containsKey(entity3.getParentId())) {
                        params.put("id", entity3.getParentId());
                        params1Results = afDbcMapper.getAllByParams(params1);
                        AfDbc entity2 = CollectionUtils.isNotEmpty(params1Results) ? params1Results.get(0) : null;
                        if (null != entity2) buMap.put(entity2.getId(), entity2);
                    }
                    dbcList3.add(entity3);
                }
            }
            String bu = "";
            for (String key : buMap.keySet()) {
                bu += " " + buMap.get(key).getName();
            }
            content.append("BU_:" + bu + "\n");
            for (AfDbc dbc : dbcList3) {
                content.append("BO_ " + dbc.getCanReportId() + " " + dbc.getName() + ": " + dbc.getCanByteNum() + " " + buMap.get(dbc.getParentId()).getName() + "\n");
                for (AfDbc dbcEntity : dbcList4) {
                    if (dbc.getId().equals(dbcEntity.getParentId())) {
                        content.append(" SG_ " + dbcEntity.getName() + " : " + dbcEntity.getSignalBeginPosition() + "|" + dbcEntity.getSignalPositionNum()
                                + "@" + dbcEntity.getSignalByteOrder() + "+(" + dbcEntity.getSignalGain() + "," + dbcEntity.getSignalBias() + ")["
                                + dbcEntity.getSignalMinValue() + "|" + dbcEntity.getSignalMaxValue() + "] \"" + dbcEntity.getSignalUnit() + "\" Vector__XXX\n");
                    }
                }
            }
            content.append("\n");
            content.append("\n");
            content.append("BA_DEF_  \"HSBaudrate\" ENUM  \"50000\",\"100000\",\"125000\",\"250000\",\"500000\",\"1000000\";\n");
            content.append("BA_DEF_  \"MSBaudrate\" ENUM  \"50000\",\"100000\",\"125000\",\"250000\",\"500000\",\"1000000\";\n");
            content.append("BA_DEF_  \"LSBaudrate\" ENUM  \"50000\",\"100000\",\"125000\",\"250000\",\"500000\",\"1000000\";\n");
            content.append("BA_DEF_ BO_  \"VFrameFormat\" ENUM  \"StandardCAN\",\"ExtendedCAN\",\"reserved\",\"J1939PG\";\n");
            content.append("\n");
            content.append("\n");
            content.append("BA_ \"HSBaudrate\" 2;\n");
            content.append("BA_ \"MSBaudrate\" 3;\n");
            for (AfDbc dbc : dbcList3) {
                if (!StringUtils.isEmpty(dbc.getCanReportId()) && null != dbc.getCanRadio()) {
                    int rn = 0;
                    if (dbc.getCanRadio().equals(new BigDecimal(1))) {
                        rn = 1;
                    } else if (dbc.getCanRadio().equals(new BigDecimal(2))) {
                        rn = 2;
                    } else if (dbc.getCanRadio().equals(new BigDecimal(3))) {
                        rn = 4;
                    } else if (dbc.getCanRadio().equals(new BigDecimal(9))) {
                        rn = 3;
                    }
                    content.append("BA_ \"VFrameFormat\" BO_ " + dbc.getCanReportId() + " " + rn + ";\n");
                }
            }
            return content.toString();
        }
        return "";
    }


}
