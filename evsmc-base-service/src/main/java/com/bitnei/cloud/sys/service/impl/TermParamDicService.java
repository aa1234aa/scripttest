package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.TermParamDic;
import com.bitnei.cloud.sys.model.TermParamDicModel;
import com.bitnei.cloud.sys.service.ITermParamDicService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import it.sauronsoftware.base64.Base64;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermParamDicService实现<br>
 * 描述： TermParamDicService实现<br>
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
 * <td>2019-03-07 15:39:13</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.TermParamDicMapper")
public class TermParamDicService extends BaseService implements ITermParamDicService {

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<TermParamDic> entries = findBySqlId("pagerModel", params);
            List<TermParamDicModel> models = new ArrayList();
            for (TermParamDic entry : entries) {
                TermParamDic obj = (TermParamDic) entry;
                models.add(TermParamDicModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<TermParamDicModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                TermParamDic obj = (TermParamDic) entry;
                models.add(TermParamDicModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    public List<TermParamDicModel> getInternalOrCustomerList(Integer paramType, Integer isSetup) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");
        params.put("paramType", paramType);
        params.put("state", 1);
        if (null != isSetup) {
            params.put("isSetup", isSetup);
        }

        List<TermParamDic> entries = findBySqlId("pagerModel", params);
        List<TermParamDicModel> models = new ArrayList();
        for (TermParamDic entry : entries) {
            TermParamDic obj = (TermParamDic) entry;
            models.add(TermParamDicModel.fromEntry(obj));
        }
        return models;
    }

    @Override
    public List<TermParamDicModel> getAll() {
        List<TermParamDic> entries = findBySqlId("pagerModel", new HashMap<>());
        List<TermParamDicModel> models = new ArrayList();
        for (TermParamDic entry : entries) {
            TermParamDic obj = (TermParamDic) entry;
            models.add(TermParamDicModel.fromEntry(obj));
        }
        return models;
    }

    @Override
    public TermParamDicModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");
        params.put("id", id);
        TermParamDic entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return TermParamDicModel.fromEntry(entry);
    }


    @Override
    public void insert(TermParamDicModel model) {

        TermParamDic obj = new TermParamDic();
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
    public void update(TermParamDicModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");

        TermParamDic obj = new TermParamDic();
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_dic", "tpd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<TermParamDic>(this, "pagerModel", params, "sys/res/termParamDic/export.xls", "终端参数字典") {
        }.work();

        return;


    }

//    public void  offlineExport(PagerInfo pagerInfo){
//        new OfflineExportHandler( "pagerModel" , params, "sys/res/termParamDic/export.xls", "终端参数字典", Mapper.class, Entity.clas);
//    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "TERMPARAMDIC" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<TermParamDicModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(TermParamDicModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(TermParamDicModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "TERMPARAMDIC" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<TermParamDicModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(TermParamDicModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(TermParamDicModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 对数据参数项64位编码
     *
     * @param dicts 数据参数项
     * @param rule  切割规则
     */
    public String base64Encoding(String dicts, String rule) {
        String[] split = dicts.split(rule);
        StringBuilder newDicts = new StringBuilder();
        for (String dict : split) {
            String[] res = dict.split(":");
            String key = res[0];
            String value = res[1];
            if ("5207".equals(key) || "5208".equals(key) || "5205".equals(key) || "5215".equals(key)) {
                value = Base64.encode(value, "UTF-8");
            }
            String format = String.format("%s:%s,", key, value);
            newDicts.append(format);

        }
        return newDicts.substring(0, newDicts.length() - 1);
    }

}
