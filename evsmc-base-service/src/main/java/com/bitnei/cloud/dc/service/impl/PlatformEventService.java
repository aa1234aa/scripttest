package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.dao.PlatformInformationMapper;
import com.bitnei.cloud.dc.domain.PlatformInformation;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.dc.domain.PlatformEvent;
import com.bitnei.cloud.dc.model.PlatformEventModel;
import com.bitnei.cloud.dc.service.IPlatformEventService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
* 功能： PlatformEventService实现<br>
* 描述： PlatformEventService实现<br>
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
* <td>2019-01-29 20:37:50</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.dc.dao.PlatformEventMapper" )
public class PlatformEventService extends BaseService implements IPlatformEventService {

    @Autowired
    private PlatformInformationMapper platformInformationMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_event", "env");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<PlatformEvent> entries = findBySqlId("pagerModel", params);
            List<PlatformEventModel> models = new ArrayList();
            for(PlatformEvent entry: entries){
                PlatformEvent obj = (PlatformEvent)entry;
                models.add(PlatformEventModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<PlatformEventModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                PlatformEvent obj = (PlatformEvent)entry;
                models.add(PlatformEventModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public PlatformEventModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_event", "env");
        params.put("id",id);
        PlatformEvent entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PlatformEventModel.fromEntry(entry);
    }


    @Override
    public void connect(PlatformEventModel model) {

        //更新连接状态为已连接
        if (model.getStatus()==0){
            PlatformInformation a = new PlatformInformation();
            a.setId(model.getPid());
            a.setStatus(1);
            platformInformationMapper.updatePlatformStatus(a);
        }
        //插入平台事件
        this.insert(model);
    }

    @Override
    public void timeout(PlatformEventModel model) {

        //更新连接状态为超时
        PlatformInformation a = new PlatformInformation();
        a.setId(model.getPid());
        a.setStatus(2);
        platformInformationMapper.updatePlatformStatus(a);
        //插入平台事件
        this.insert(model);
    }

    @Override
    public void disConnect(PlatformEventModel model) {

        //更新连接状态为断开
        PlatformInformation a = new PlatformInformation();
        a.setId(model.getPid());
        a.setStatus(0);
        platformInformationMapper.updatePlatformStatus(a);
        //插入平台事件
        this.insert(model);
    }


    @Override
    public void insert(PlatformEventModel model) {

        PlatformEvent obj = new PlatformEvent();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(PlatformEventModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_event", "env");

        PlatformEvent obj = new PlatformEvent();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_event", "env");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("dc_platform_event", "env");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<PlatformEvent>(this, "pagerModel", params, "dc/res/platformEvent/export.xls", "平台接入日志") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "PLATFORMEVENT"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<PlatformEventModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PlatformEventModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PlatformEventModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "PLATFORMEVENT"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<PlatformEventModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(PlatformEventModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(PlatformEventModel model) {
                update(model);
            }
        }.work();

    }



}
