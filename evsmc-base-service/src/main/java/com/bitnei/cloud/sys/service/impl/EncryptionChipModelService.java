package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.model.ResultMsgModel;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.client.OpenServiceClient;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.dao.EncryptionChipMapper;
import com.bitnei.cloud.sys.domain.EncryptionChipModel;
import com.bitnei.cloud.sys.model.EncryptionChipModelModel;
import com.bitnei.cloud.sys.service.IEncryptionChipModelService;
import com.bitnei.cloud.sys.util.StringUtils;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： EncryptionChipModelService实现<br>
 * 描述： EncryptionChipModelService实现<br>
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
 * <td>2019-07-03 20:06:31</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.EncryptionChipModelMapper")
public class EncryptionChipModelService extends BaseService implements IEncryptionChipModelService {

    @Resource
    private EncryptionChipMapper encryptionChipMapper;
    @Resource
    private OpenServiceClient openServiceClient;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<EncryptionChipModel> entries = findBySqlId("pagerModel", params);
            List<EncryptionChipModelModel> models = new ArrayList<>();
            for (EncryptionChipModel entry : entries) {
                EncryptionChipModel obj = (EncryptionChipModel) entry;
                models.add(EncryptionChipModelModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<EncryptionChipModelModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                EncryptionChipModel obj = (EncryptionChipModel) entry;
                models.add(EncryptionChipModelModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public EncryptionChipModelModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");
        params.put("id", id);
        EncryptionChipModel entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return EncryptionChipModelModel.fromEntry(entry);
    }


    @Override
    public EncryptionChipModelModel getByName(String name) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");
        params.put("name", name);
        EncryptionChipModel entry = unique("findByName", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return EncryptionChipModelModel.fromEntry(entry);
    }


    @Override
    public void insert(EncryptionChipModelModel model) {
        EncryptionChipModel obj = new EncryptionChipModel();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setFilingStatus(Constant.CHIP_FILING_STATUS.UN);
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(EncryptionChipModelModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");

        EncryptionChipModel obj = new EncryptionChipModel();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            Integer ecCount = encryptionChipMapper.findByEncryptionChipModelIdCount(id);
            if (ecCount > 0) {
                throw new BusinessException("存在加密芯片型号已关联加密芯片，不可删除", 300);
            }
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<EncryptionChipModel>(this, "pagerModel", params, "sys/res/encryptionChipModel/export.xls", "加密芯片型号") {
        }.work();

        return;
    }

    @Override
    public void filing(String ids) {
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_encryption_chip_model", "ecm");
        params.put("ids", StringUtils.split(ids,","));
        List<EncryptionChipModel> entries = findBySqlId("pagerModel", params);
        List<EncryptionChipModelModel> models = new ArrayList<>();
        // 过滤出未备案的芯片型号列表, 转换成model
        for (EncryptionChipModel entry : entries) {
            if(entry.getFilingStatus() == null || Constant.CHIP_FILING_STATUS.UN.equals(entry.getFilingStatus())) {
                EncryptionChipModelModel model = EncryptionChipModelModel.fromEntry(entry);
                DataLoader.loadNames(model);
                models.add(model);
            }
        }
        if(models.size() == 0) {
            throw new BusinessException("请选择未备案的芯片型号");
        }
        ResultMsgModel rm = openServiceClient.chipModelUp(models);
        if(rm.getCode() != 0) {
            throw new BusinessException(rm.getMsg());
        }
        if(rm.getData() != null && rm.getData() instanceof Map) {
            // 成功的 map<id,备案编码> 集合
            Map<String, String> map = (Map<String, String>) rm.getData();
            for (EncryptionChipModelModel model : models) {
                if(map.containsKey(model.getId())) {
                    String filingCode = map.get(model.getId());
                    model.setFilingCode(filingCode);
                    model.setFilingStatus(Constant.CHIP_FILING_STATUS.SUCCESS);
                } else {
                    model.setFilingStatus(Constant.CHIP_FILING_STATUS.FAIL);
                }
                this.update(model);
            }
        }
    }


}
