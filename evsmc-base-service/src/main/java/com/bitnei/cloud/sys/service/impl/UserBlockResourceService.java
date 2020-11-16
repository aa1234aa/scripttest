package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.StringUtilExs;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.constant.ResourceConstant;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.dao.GroupMapper;
import com.bitnei.cloud.sys.dao.GroupResourceRuleMapper;
import com.bitnei.cloud.sys.dao.UserBlockResourceMapper;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.*;
import com.bitnei.cloud.sys.model.MoveToBlockListModel;
import com.bitnei.cloud.sys.model.UserBlockResourceModel;
import com.bitnei.cloud.sys.model.UserBlockResourceVehicleModel;
import com.bitnei.cloud.sys.service.IUserBlockResourceService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
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

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserBlockResourceService实现<br>
* 描述： UserBlockResourceService实现<br>
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
* <td>2019-07-04 14:22:54</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UserBlockResourceMapper" )
public class UserBlockResourceService extends BaseService implements IUserBlockResourceService {


    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupResourceRuleMapper groupResourceRuleMapper;
    @Resource
    private UserBlockResourceMapper userBlockResourceMapper;
    @Resource
    private VehicleMapper vehicleMapper;


    @Override
    public UserBlockResourceModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_block_resource", "ubr");
        params.put("id",id);
        UserBlockResource entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return UserBlockResourceModel.fromEntry(entry);
    }




    @Override
    public void insert(UserBlockResourceModel model) {

        UserBlockResource obj = new UserBlockResource();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(UserBlockResourceModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_block_resource", "ubr");

        UserBlockResource obj = new UserBlockResource();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_user_block_resource", "ubr");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        if (params.containsKey("ids")){
            String str = params.get("ids").toString();
            if (StringUtil.isNotEmpty(str)){
                params.put("idList", StringUtilExs.spiltString(str, ","));
            }
        }

        new ExcelExportHandler<UserBlockResourceVehicle>(this, "pagerModelVehicle", params, "sys/res/userBlockResource/exportVehicle.xls", "黑名单车辆") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "USERBLOCKRESOURCE"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UserBlockResourceModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UserBlockResourceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UserBlockResourceModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "USERBLOCKRESOURCE"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UserBlockResourceModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UserBlockResourceModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UserBlockResourceModel model) {
                update(model);
            }
        }.work();

    }


    private Map<String,Object> getAuthSql(final String userId){
        Map<String, Object> params = Maps.newHashMap();
        String authSql = DataAccessKit.getUserAuthSql(userId, "sys_vehicle", "sv", false);
        String currentUserAuthSql = DataAccessKit.getAuthSQL("sys_vehicle", "sv");

        if (StringUtil.isNotEmpty(currentUserAuthSql) && authSql != null && !authSql.equals(currentUserAuthSql)) {
            authSql = String.format("(%s) and (%s)", authSql, currentUserAuthSql);
        }
        params.put("authSQL", authSql);
        return params;
    }

    /**
     * 返回用户黑名单车辆
     *
     * @param pagerInfo
     * @return
     */
    @Override
    public Object listBlockVehicle( PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        if (params.containsKey("ids")){
            String str = params.get("ids").toString();
            if (StringUtil.isNotEmpty(str)){
                params.put("idList", StringUtilExs.spiltString(str, ","));
            }
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<UserBlockResourceVehicle> entries = findBySqlId("pagerModelVehicle", params);
            List<UserBlockResourceVehicleModel> models = new ArrayList();
            for(UserBlockResourceVehicle entry: entries){
                models.add(UserBlockResourceVehicleModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModelVehicle", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UserBlockResourceVehicleModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                UserBlockResourceVehicle ubv = (UserBlockResourceVehicle)entry;
                models.add(UserBlockResourceVehicleModel.fromEntry(ubv));
            }

            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    /**
     * 将车辆移入黑名单
     *
     * @param model
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int moveToBlockList(MoveToBlockListModel model) {
        Group group = groupMapper.findByUserId(model.getUserId());

        Map<String, Object> params = Maps.newHashMap();
        params.put("groupId", group.getId());
        params.put("resourceItemId", ResourceConstant.RESOURCE_ITEM_SELF_ID);
        GroupResourceRule groupResourceRule  = groupResourceRuleMapper.findByResourceItemIdAndGroupId(params);


        String[] tmps = model.getIds().split(",");
        //移除默认组里的数据
        if (groupResourceRule != null){
            Set<String> valSet = groupResourceRule.getValSet();
            for (String t:tmps){
                valSet.remove(t);
            }

            groupResourceRule.setVal(StringUtils.join(valSet, ","));
            groupResourceRule.setUpdateBy(ServletUtil.getCurrentUser());
            groupResourceRule.setUpdateTime(DateUtil.getNow());
            groupResourceRuleMapper.update(groupResourceRule);
        }
        //移入到黑名单
        List<UserBlockResource> userBlockResources = Lists.newArrayList();
        for (String t: tmps){
            UserBlockResource userBlockResource = new UserBlockResource();
            userBlockResource.setId(UtilHelper.getUUID());
            userBlockResource.setCreateBy(ServletUtil.getCurrentUser());
            userBlockResource.setCreateTime(DateUtil.getNow());
            userBlockResource.setUserId(model.getUserId());
            userBlockResource.setResourceItemId(ResourceConstant.RESOURCE_ITEM_SELF_ID);
            userBlockResource.setResourceObjectId(t);
            userBlockResources.add(userBlockResource);
        }

        userBlockResourceMapper.insertList(userBlockResources);
        return tmps.length;
    }

    /**
     * 通过查询条件移入黑名单
     *
     * @param pagerInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int moveToBlockList(PagerInfo pagerInfo) {

        Map<String, Object> params = Maps.newHashMap();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        params.put("authSQL", DataAccessKit.getUserAuthSql(params.get("userId").toString(), "sys_vehicle", "sv", false));

        //查询车辆id
        final Cursor<Vehicle> entityCursor = vehicleMapper.findPagerModel(params);
        List<String> ids = new ArrayList<>();
        Iterator<Vehicle> iterator = entityCursor.iterator();
        while (iterator.hasNext()){
            ids.add(iterator.next().getId());
        }


        //组装黑名单model
        MoveToBlockListModel moveToBlockListModel = new MoveToBlockListModel();
        moveToBlockListModel.setIds(StringUtils.join(ids, ","));
        moveToBlockListModel.setUserId(params.get("userId").toString());

        return moveToBlockList(moveToBlockListModel);
    }


}
