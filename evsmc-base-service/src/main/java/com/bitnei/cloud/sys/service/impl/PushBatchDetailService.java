package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.PushBatchDetail;
import com.bitnei.cloud.sys.model.PushBatchDetailModel;
import com.bitnei.cloud.sys.service.IPushBatchDetailService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushBatchDetailService实现<br>
* 描述： PushBatchDetailService实现<br>
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
* <td>2019-02-27 19:37:27</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.PushBatchDetailMapper" )
public class PushBatchDetailService extends BaseService implements IPushBatchDetailService {

   @Override
    public Object list(PagerInfo pagerInfo) {
       //获取当权限的map
       Map<String, Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");
       params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
       //非分页查询
       if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {
           List<PushBatchDetail> entries = findBySqlId("pagerModel", params);
           List<PushBatchDetailModel> models = new ArrayList<>();
           for (PushBatchDetail entry : entries) {
               models.add(PushBatchDetailModel.fromEntry(entry));
           }
           return models;
       }
       //分页查询
       else {
           PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());
           List<PushBatchDetailModel> models = new ArrayList<>();
           for (Object entry : pr.getData()) {
               PushBatchDetail obj = (PushBatchDetail) entry;
               models.add(PushBatchDetailModel.fromEntry(obj));
           }
           pr.setData(Collections.singletonList(models));
           return pr;
       }
   }



    @Override
    public PushBatchDetailModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");
        params.put("id",id);
        PushBatchDetail entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PushBatchDetailModel.fromEntry(entry);
    }

    @Override
    public PushBatchDetailModel getByBatchIdAndFormName(String batchId, String formName) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");
        params.put("batchId",batchId);
        params.put("formName",formName);
        PushBatchDetail entry = unique("findByBatchIdAndFormName", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return PushBatchDetailModel.fromEntry(entry);
    }

    @Override
    public PushBatchDetailModel getByFormId(String formId) {
        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");
        params.put("formId", formId);
        PushBatchDetail entry = unique("findByFormId", params);
        if(entry != null) {
            return PushBatchDetailModel.fromEntry(entry);
        }
        return null;
    }

    @Override
    public void insert(PushBatchDetailModel model) {

        PushBatchDetail obj = new PushBatchDetail();
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
    public void update(PushBatchDetailModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");

        PushBatchDetail obj = new PushBatchDetail();
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
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_push_batch_detail", "ppbd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }





}
