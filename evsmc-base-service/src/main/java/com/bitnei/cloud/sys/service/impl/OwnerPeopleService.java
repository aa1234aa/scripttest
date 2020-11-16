package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RegexUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： OwnerPeopleService实现<br>
 * 描述： OwnerPeopleService实现<br>
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
 * <td>2018-11-02 15:19:04</td>
 * <td>zxz</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author zxz
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.OwnerPeopleMapper")
public class OwnerPeopleService extends BaseService implements IOwnerPeopleService {

    @Resource
    private IUnitService unitService;
    @Resource
    private DictMapper dictMapper;

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    @Override
    public Object list(PagerInfo pagerInfo) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_owner_people", "op");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if(params.containsKey("ids")) {
            String ids = (String) params.get("ids");
            params.put("ids", StringUtils.split(ids,","));
        }
        // 非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
            List<OwnerPeople> entries = findBySqlId("pagerModel", params);
            List<OwnerPeopleModel> models = new ArrayList<>();
            for (OwnerPeople entry : entries) {
                models.add(OwnerPeopleModel.fromEntry(entry));
            }
            return models;

        }
        // 分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
            List<OwnerPeopleModel> models = new ArrayList<>();
            for (Object entry : pr.getData()) {
                OwnerPeople obj = (OwnerPeople) entry;
                models.add(OwnerPeopleModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 根据id获取
     *
     * @return OwnerPeopleModel
     */
    @Override
    public OwnerPeopleModel get(String id) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_owner_people", "op");
        params.put("id", id);
        OwnerPeople entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return OwnerPeopleModel.fromEntry(entry);
    }

    @Override
    public OwnerPeople findByOwnerName(String ownerName) {
        return unique("findByOwnerName", ownerName);
    }

    /**
     * 新增
     *
     * @param model 新增model
     */
    @Override
    public void insert(OwnerPeopleModel model) {
        OwnerPeople obj = new OwnerPeople();
        BeanUtils.copyProperties(model, obj);
        // 校验证件号码
        String message = checkCardNo(obj.getCardType(), obj.getCardNo(), "");
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        // 单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    @Override
    public void update(OwnerPeopleModel model) {
        // 校验证件号码
        String message = checkCardNo(model.getCardType(), model.getCardNo(), model.getId());
        if (!StringUtils.isEmpty(message)) {
            throw new BusinessException(message);
        }
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_owner_people", "op");
        OwnerPeople obj = new OwnerPeople();
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
     * @param ids id集合,逗号间隔
     * @return 影响行数
     */
    @Override
    public int deleteMulti(String ids) {
        // 获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_owner_people", "op");
        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {
            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;
        }
        return count;
    }

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    @Override
    public void export(PagerInfo pagerInfo) {
        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_owner_people", "op");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<OwnerPeople>(this, "pagerModel", params, "sys/res/ownerPeople/export.xls", "联系人管理") {
        }.work();
    }

    /**
     * 批量导入
     *
     * @param file 文件
     */
    @Override
    public void batchImport(MultipartFile file) {
        String messageType = "OWNERPEOPLE" + WsMessageConst.BATCH_IMPORT_SUFFIX;
        new ExcelBatchHandler<OwnerPeopleModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             * @param model OwnerPeopleModel
             * @return 错误信息集合
             */
            @Override
            public List<String> extendValidate(OwnerPeopleModel model) {
                List<String> errors = Lists.newArrayList();
                // 所属单位
                if(StringUtils.isNotBlank(model.getUnitName())) {
                    try {
                        UnitModel unit = unitService.getByName(model.getUnitName());
                        model.setUnitId(unit.getId());
                    } catch (BusinessException e) {
                        errors.add("所属单位不存在");
                    }
                }
                // 性别
                if(StringUtils.isNotBlank(model.getSexName())) {
                    Map<String, Object> params = ImmutableMap.of("type", "SEX", "name", model.getSexName());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setSex(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("性别填写不正确");
                    }
                }
                // 证件类型
                if(StringUtils.isNotBlank(model.getCardTypeName())) {
                    Map<String, Object> params = ImmutableMap.of("type", "CERT_TYPE", "name", model.getCardTypeName());
                    Dict dict = dictMapper.getByTypeAndName(params);
                    if(dict != null) {
                        model.setCardType(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("证件类型填写不正确");
                    }
                    // 证件号码
                    String error = checkCardNo(model.getCardType(), model.getCardNo(), null);
                    if(StringUtils.isNotBlank(error)) {
                        errors.add(error);
                    }
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model OwnerPeopleModel
             */
            @Override
            public void saveObject(OwnerPeopleModel model) {
                insert(model);
            }
        }.work();

    }

    /**
     * 批量更新
     *
     * @param file 文件
     */
    @Override
    public void batchUpdate(MultipartFile file) {
        String messageType = "OWNERPEOPLE" + WsMessageConst.BATCH_UPDATE_SUFFIX;
        new ExcelBatchHandler<OwnerPeopleModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model OwnerPeopleModel
             * @return 错误信息集合
             */
            @Override
            public List<String> extendValidate(OwnerPeopleModel model) {
                return null;
            }

            /**
             *  保存实体
             * @param model OwnerPeopleModel
             */
            @Override
            public void saveObject(OwnerPeopleModel model) {
                update(model);
            }
        }.work();
    }

    /**
     * 自定义参数查询联系人
     * @param params 参数
     * @return OwnerPeople
     */
    @Override
    public OwnerPeople queryOwnerPeopleByParam(Map<String, Object> params) {
        List<OwnerPeople> list = findBySqlId("pagerModel", params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 校验证件号码
     *
     * @param cardType 证件类型 1:居民身份证 2:士官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证
     * @param cardNo   证件号码
     * @param id       车主id
     * @return 错误信息
     */
    private String checkCardNo(Integer cardType, String cardNo, String id) {
        if (!StringUtils.isEmpty(cardNo)) {
            // 数字和大写字母的正则校验需要加
            boolean bl = cardNo.matches(RegexUtil.NUM_OR_CAPITAL_8_30);
            if (null != cardType) {
                boolean bool = (1 == cardType || 4 == cardType) && (18 != cardNo.length() || !bl);
                boolean b = (2 == cardType) && (8 != cardNo.length() || !bl);
                if (bool) {
                    return "身份证或驾驶证为18位大写英文数字";
                } else if (b) {
                    return "军官证为8位大写英文数字";
                } else if(!bl) {
                    return "证件号码为8-30位数字或大写字母";
                }
            } else if (!bl) {
                return "证件号码为8-30位大写英文数字";
            }
            Map<String, Object> params = Maps.newHashMap();
            params.put("cardNo", cardNo);
            OwnerPeople op = queryOwnerPeopleByParam(params);
            // 如果id不为空则为更新校验
            if (null != op && !StringUtils.equals(op.getId(), id) ) {
                // 如果id为空则表示为添加校验，如果id不为空则表示为更新校验，如何查询结果id不同则表示数据库已存在此证件号，返回错误
                return "证件号码已存在";
            }
        }
        return "";
    }

    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("单位联系人导入模板.xls" , OwnerPeopleModel.class);
    }


}
