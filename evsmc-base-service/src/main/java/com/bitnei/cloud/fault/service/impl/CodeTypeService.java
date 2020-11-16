package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.HexUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.fault.dao.CodeTypeMapper;
import com.bitnei.cloud.fault.domain.CodeType;
import com.bitnei.cloud.fault.model.CodeTypeModel;
import com.bitnei.cloud.fault.service.ICodeTypeService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeTypeService实现<br>
 * 描述： CodeTypeService实现<br>
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
 * <td>2019-02-25 10:22:15</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.CodeTypeMapper")
public class CodeTypeService extends BaseService implements ICodeTypeService {

    @Resource
    private CodeTypeMapper codeTypeMapper;

    private static final List<String> GBITEMS = Lists.newArrayList("2809", "2924", "2805", "2922");


    @Override
    public Object list(PagerInfo pagerInfo) {
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<CodeType> entries = findBySqlId("pagerModel", params);
            List<CodeTypeModel> models = new ArrayList<>();
            for (CodeType entry : entries) {
                models.add(CodeTypeModel.fromEntry(entry));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<CodeTypeModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                CodeType obj = (CodeType) entry;
                models.add(CodeTypeModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public CodeTypeModel get(String id) {
        CodeType entry = codeTypeMapper.findById(id);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return CodeTypeModel.fromEntry(entry);
    }


    @Override
    public void insert(CodeTypeModel model) {
        // 统一格式化十六进制码
        model.setTypeCode(HexUtil.format(model.getTypeCode()));
        CodeType oldCodeType = codeTypeMapper.findByName(model.getName());
        if (oldCodeType != null) {
            throw new BusinessException("名称已经存在!");
        }
        oldCodeType = codeTypeMapper.findByCode(model.getTypeCode());
        if (oldCodeType != null) {
            throw new BusinessException("类型编码已经存在!");
        }
        oldCodeType = codeTypeMapper.findByLenCode(model.getLenCode());
        if (oldCodeType != null) {
            throw new BusinessException("lenCode已经存在!");
        }
        CodeType obj = new CodeType();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = codeTypeMapper.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(CodeTypeModel model) {
        CodeTypeModel oldModel = get(model.getId());
        if (oldModel != null && GBITEMS.contains(oldModel.getTypeCode())) {
            throw new BusinessException("国标32960项：[2809、2924、2805、2922] 不能修改");
        }

        // 统一格式化十六进制码
        model.setTypeCode(HexUtil.format(model.getTypeCode()));

        CodeType oldeCodeType = codeTypeMapper.findByName(model.getName());
        if (oldeCodeType != null && !model.getId().equals(oldeCodeType.getId())) {
            throw new BusinessException("名称已经存在!");
        }
        oldeCodeType = codeTypeMapper.findByCode(model.getTypeCode());
        if (oldeCodeType != null && !model.getId().equals(oldeCodeType.getId())) {
            throw new BusinessException("类型编码已经存在!");
        }
        oldeCodeType = codeTypeMapper.findByLenCode(model.getLenCode());
        if (oldeCodeType != null && !model.getId().equals(oldeCodeType.getId())) {
            throw new BusinessException("lenCode已经存在!");
        }

        CodeType obj = new CodeType();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        int res = codeTypeMapper.update(obj);
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
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            CodeTypeModel model = get(id);
            if (model != null && GBITEMS.contains(model.getTypeCode())) {
                throw new BusinessException("国标32960项：[2809、2924、2805、2922] 不能删除");
            }
            int r = codeTypeMapper.delete(id);
            count += r;
        }
        return count;
    }

    @Override
    public String getAllFaultTypeCode() {
        return codeTypeMapper.getAllFaultTypeCode();
    }

    @Override
    public CodeTypeModel findByName(String name){
            CodeType entry = codeTypeMapper.findByName(name);
            if (entry == null) {
                throw new BusinessException("对象已不存在");
            }
            return CodeTypeModel.fromEntry(entry);
    }
}
