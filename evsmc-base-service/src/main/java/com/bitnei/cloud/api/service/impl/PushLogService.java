package com.bitnei.cloud.api.service.impl;

import com.bitnei.cloud.api.dao.PushLogMapper;
import com.bitnei.cloud.api.domain.PushLog;
import com.bitnei.cloud.api.model.PushLogModel;
import com.bitnei.cloud.api.service.IPushLogService;
import com.bitnei.cloud.api.utils.AesUtils;
import com.bitnei.cloud.api.utils.RsaUtils;
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
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： PushLogService实现<br>
 * 描述： PushLogService实现<br>
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
 * <td>2019-01-16 15:24:34</td>
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
@Mybatis(namespace = "com.bitnei.cloud.api.dao.PushLogMapper")
@RequiredArgsConstructor
public class PushLogService extends BaseService implements IPushLogService {

    private final PushLogMapper pushLogMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<PushLogModel> entries = findBySqlId("pagerPushLogModel", params);
            return convertEntityToModel(entries);

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerPushLogModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PushLogModel> entries = pr.getData().stream().map(it -> (PushLogModel) it).collect(Collectors.toList());
            List<PushLogModel> models = convertEntityToModel(entries);
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private List<PushLogModel> convertEntityToModel(List<PushLogModel> entities) {
        return entities.stream().peek(model -> {
            //加密方案 0:AES 1:RSA
            if (null != model.getEncryptType()) {
                switch (model.getEncryptType()) {
                    case 0: {
                        try {
                            model.setPushBodyDecoded(AesUtils.decode(model.getPushBodyContent(), model.getAppKey()));
                            model.setRspBodyDecoded(AesUtils.decode(model.getRspBodyContent(), model.getAppKey()));
                        } catch (Exception e) {
                            log.error("AES 解密失败，解密数据：pushLog，id:{}", model.getId());
                           log.error("error", e);
                        }
                        break;
                    }
                    case 1: {
                        try {
                            log.info("pri:{}", model.getRsaPriKey());
                            log.info("pushBody:{}", model.getPushBodyContent());
                            log.info("rspBody:{}", model.getRspBodyContent());
                            model.setPushBodyDecoded(RsaUtils.decryptByPrivateKey(model.getPushBodyContent(),
                                    model.getRsaPriKey()));
                            model.setRspBodyDecoded(RsaUtils.decryptByPrivateKey(model.getRspBodyContent(),
                                    model.getRsaPriKey()));
                        } catch (Exception e) {
                            log.error("RSA 解密失败，解密数据：pushLog，id:{}", model.getId());
                           log.error("error", e);
                        }
                    }
                    default:
                        break;
                }
            }
        }).collect(Collectors.toList());
    }


    @Override
    public PushLogModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");
        params.put("id", id);
        PushLog entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return PushLogModel.fromEntry(entry);
    }


    @Override
    public void insert(PushLogModel model) {

        PushLog obj = new PushLog();
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
    public void update(PushLogModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");

        PushLog obj = new PushLog();
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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<PushLog>(this, "pagerModel", params, "api/res/pushLog/export.xls", "推送日志") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "PUSHLOG" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<PushLogModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PushLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PushLogModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "PUSHLOG" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<PushLogModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PushLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PushLogModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public void updateRePushFlag(String id, Integer rePushFlag) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("api_push_log", "pl");
        params.put("id", id);
        params.put("re_push_flag", rePushFlag);
        int res = pushLogMapper.updateRePushFlag(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }
}
