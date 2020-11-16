package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.model.AlarmInfoModel;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.AttentionVehMapper;
import com.bitnei.cloud.sys.domain.AttentionVeh;
import com.bitnei.cloud.sys.model.AttentionVehModel;
import com.bitnei.cloud.sys.service.IAttentionVehService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AttentionVehService实现<br>
 * 描述： AttentionVehService实现<br>
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
 * <td>2019-03-19 18:45:16</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.AttentionVehMapper")
@RequiredArgsConstructor
public class AttentionVehService extends BaseService implements IAttentionVehService {

    private final AttentionVehMapper attentionVehMapper;
    private final String TYPE = "type";
    private final IUserService userService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        Integer type = null;
        if (params.containsKey(TYPE)) {
            type = Integer.valueOf(params.get(TYPE).toString());
        }
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<AttentionVeh> entries = findBySqlId("pagerModel", params);
            List<AttentionVehModel> models = new ArrayList();
            for (AttentionVeh entry : entries) {
                AttentionVeh obj = (AttentionVeh) entry;
                models.add(AttentionVehModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            if (null != type) {
                switch (type) {
                    case 0: {
                        //车辆关注暂不支持此接口查询
                        throw new BusinessException("车辆关注暂不支持此接口查询");
                    }
                    case 1: {
                        List<AlarmInfoModel> models = new ArrayList<>();
                        for (Object entry : pr.getData()) {
                            AttentionVeh obj = (AttentionVeh) entry;
                            if (StringUtils.isNotEmpty(obj.getContent())) {
                                models.add(JSONObject.parseObject(obj.getContent(), AlarmInfoModel.class));
                            }
                        }
                        pr.setData(Collections.singletonList(models));
                        return pr;
                    }
                    case 2: {
                        //暂时没有这个类型
                        break;
                    }
                    default:
                }
            }
            List<AttentionVehModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                AttentionVeh obj = (AttentionVeh) entry;
                models.add(AttentionVehModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public Object getJsonDataList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

        List<String> results = new ArrayList();
        for (Object entry : pr.getData()) {
            AttentionVeh obj = (AttentionVeh) entry;
            results.add(obj.getContent());
        }
        pr.setData(Collections.singletonList(results));
        return pr;
    }

    @Override
    public AttentionVehModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");
        params.put("id", id);
        AttentionVeh entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return AttentionVehModel.fromEntry(entry);
    }


    @Override
    public void insert(AttentionVehModel model) {

        AttentionVeh obj = new AttentionVeh();
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
    public void update(AttentionVehModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");

        AttentionVeh obj = new AttentionVeh();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public int updateAttentionData(String itemId, Integer type, Object item) {

        Map<String, Object> params = new HashMap<>();
        params.put("vehId", itemId);
        if (null != type) {
            params.put("type", type);
        }
        if (null != item) {
            String itemJsonData = JSONObject.toJSONString(item);
            params.put("content", itemJsonData);
        } else {
            params.put("content", "");
        }
        return attentionVehMapper.updateJsonData(params);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_attention_veh", "sav");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<AttentionVeh>(this, "pagerModel", params, "sys/res/attentionVeh/export.xls", "用户关注车辆Lk") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ATTENTIONVEH" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AttentionVehModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AttentionVehModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AttentionVehModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ATTENTIONVEH" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AttentionVehModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AttentionVehModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AttentionVehModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 根据类型获取当前用户关注的所有该类型的数据set<itemId>
     * @param type 0:车辆，1: 故障, 2: 消息
     * @return
     */
    @Override
    public Set<String> getAttentionItems(Integer type) {

        PagerInfo pagerInfo = new PagerInfo();
        Condition userIdCondition = new Condition();
        userIdCondition.setName("userId");
        userIdCondition.setValue(userService.findByUsername(ServletUtil.getCurrentUser()).getId());

        Condition typeCondition = new Condition();
        userIdCondition.setName(TYPE);
        userIdCondition.setValue(type.toString());

        List<Condition> conditions = new ArrayList<>();
        conditions.add(userIdCondition);
        conditions.add(typeCondition);
        pagerInfo.setConditions(conditions);

        List<AttentionVehModel> attentionVehModels = (List<AttentionVehModel>) list(pagerInfo);
        Set<String> itenIdSet = attentionVehModels.stream().map(AttentionVehModel::getVehId)
                .collect(Collectors.toSet());

        return itenIdSet;
    }

}
