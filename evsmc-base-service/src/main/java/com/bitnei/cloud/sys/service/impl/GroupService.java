package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.Column;
import com.bitnei.cloud.common.api.ResultListMsg;
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
import com.bitnei.cloud.sys.dao.CoreResourceItemMapper;
import com.bitnei.cloud.sys.dao.GroupMapper;
import com.bitnei.cloud.sys.dao.UserGroupMapper;
import com.bitnei.cloud.sys.domain.CoreResourceItem;
import com.bitnei.cloud.sys.domain.Group;
import com.bitnei.cloud.sys.model.GroupModel;
import com.bitnei.cloud.sys.service.IGroupService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupService实现<br>
* 描述： GroupService实现<br>
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
* <td>2018-11-08 10:40:16</td>
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
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.GroupMapper" )
public class GroupService extends BaseService implements IGroupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserGroupMapper userGroupMapper;
    @Resource
    private GroupMapper groupMapper;


   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_group", "gr");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<Group> entries = findBySqlId("pagerModel", params);
            List<GroupModel> models = new ArrayList();
            for(Group entry: entries){
                Group obj = (Group)entry;
                models.add(GroupModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<GroupModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                Group obj = (Group)entry;
                models.add(GroupModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public GroupModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_group", "gr");
        params.put("id",id);
        Group entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return GroupModel.fromEntry(entry);
    }

    /**
     * 根据用户获取组
     *
     * @param userId
     */
    @Override
    public GroupModel getByUserId(String userId) {
        Group g = groupMapper.findByUserId(userId);
        if (g == null){
            throw new BusinessException("对象已不存在");
        }
        return GroupModel.fromEntry(g);
    }


    @Override
    public GroupModel getByName(String name){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_group", "gr");
        params.put("name",name);
        Group entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return GroupModel.fromEntry(entry);
    }


    @Override
    public void insert(GroupModel model) {

        Group obj = new Group();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateBy(ServletUtil.getCurrentUser());
        obj.setCreateTime(DateUtil.getNow());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(GroupModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_group", "gr");

        Group obj = new Group();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        obj.setUpdateTime(DateUtil.getNow());
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
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_group", "gr");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("report_demo1", "d");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<Group>(this, "pagerModel", params, "sys/res/group/export.xls", "数据权限组管理") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "GROUP"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<GroupModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(GroupModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(GroupModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "GROUP"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<GroupModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(GroupModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(GroupModel model) {
                update(model);
            }
        }.work();

    }
     @Override
    public String[] getAllUsers(String groupId){
         String[] empty = new String[1];

        List<String> userIdList = userGroupMapper.getGroupUsers(groupId);
         if(userIdList==null||userIdList.size()<1) {

               empty[0]="该资源组未分配用户";

             return empty;
         }
         else {
             String[] userAll = userIdList.toArray(new String[userIdList.size()]);
             return userAll;
         }


    }

     @Override
     @Transactional(rollbackFor = Exception.class)
   public int saveGroupUsers(String groupId,String userIds){
          if(groupId==null||groupId.equals("")||userIds.length()<1) {
              throw new BusinessException("资源组或用户不能为空");
          }
          userGroupMapper.clearGroupUser(groupId);
          int count = 0;
          String[] useridArray = userIds.split(",");

         Map<String,Object> map = new HashMap<>();
         map.put("groupid",groupId);
         map.put("userIds",useridArray);
         map.put("createTime",ServletUtil.getCurrentUser());
         map.put("createBy",DateUtil.getNow());
         userGroupMapper.saveGroudUsers(map);
        return count;
    }

    @Override
    public int removeGroupUsers(String groupId,String userIds){
        if(groupId==null||groupId.equals("")||userIds.length()<1)
            return 0;
        int count = 0;
        String[] userIdArray = userIds.split(",");

        Map<String,Object> map = new HashMap<>();
        map.put("groupid",groupId);
        map.put("userIds",userIdArray);

        userGroupMapper.removeGroupUsers(map);

        return userIdArray.length;

    }


}
