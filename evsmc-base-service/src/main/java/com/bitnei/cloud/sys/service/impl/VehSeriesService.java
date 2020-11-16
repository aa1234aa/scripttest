package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehSeries;
import com.bitnei.cloud.sys.model.VehSeriesModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.service.IVehSeriesService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehSeriesService实现<br>
* 描述： VehSeriesService实现<br>
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
* <td>2019-01-22 14:28:26</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehSeriesMapper" )
public class VehSeriesService extends BaseService implements IVehSeriesService {

    @Autowired
    private IVehBrandService vehBrandService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<VehSeries> entries = findBySqlId("pagerModel", params);
            List<VehSeriesModel> models = new ArrayList<>();
            for(VehSeries entry: entries){
                VehSeries obj = entry;
                models.add(VehSeriesModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehSeriesModel> models = new ArrayList<>();
            for(Object entry: pr.getData()){
                VehSeries obj = (VehSeries)entry;
                models.add(VehSeriesModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

            

    @Override
    public VehSeriesModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");
        params.put("id",id);
        VehSeries entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("品牌车型系列已不存在");
        }
        return VehSeriesModel.fromEntry(entry);
    }

    @Override
    public VehSeriesModel getByName(String name) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");
        params.put("name", name);
        VehSeries entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("品牌车型系列已不存在");
        }
        return VehSeriesModel.fromEntry(entry);
    }



    @Override
    public void insert(VehSeriesModel model) {
        model.setCode(model.getCode().toUpperCase());
        String msg = validaModel(model);
        if(msg != null) {
            throw new BusinessException(msg);
        }
        VehSeries obj = new VehSeries();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    private String validaModel(VehSeriesModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");

        // 验证品牌是否存在
        if(StringUtils.isBlank(model.getId())) {
            try {
                vehBrandService.get(model.getBrandId());
            } catch (BusinessException ex) {
                return "品牌记录不存在";
            }
            // 新增验证同一品牌下车型系列编码是否唯一
            params.put("code", model.getCode());
            params.put("brandId", model.getBrandId());
            VehSeries entry = unique("findByBrandIdAndCode", params);
            if(entry != null) {
                return "该品牌已存在此编号";
            }

        }
        // 验证名称是否唯一
        params.put("name", model.getName());
        VehSeries nameEntity = unique("findByName", params);
        if(nameEntity != null ) {
            if(StringUtils.isBlank(model.getId()) ||
                    !StringUtils.equals(model.getId(), nameEntity.getId())) {
                return "车型系列名称已存在";
            }
        }
        // 验证内部编号是否唯一
        if(StringUtils.isNotBlank(model.getInterNo())) {
            params.put("interNo", model.getInterNo());
            VehSeries interNoEntity= unique("findByInterNo", params);
            if(interNoEntity != null ) {
                if(StringUtils.isBlank(model.getId()) ||
                        !StringUtils.equals(model.getId(), interNoEntity.getId())) {
                    return "车型系列内部编号已存在";
                }
            }
        } else {
            model.setInterNo(null);
        }

        return null;
    }


    @Override
    public void update(VehSeriesModel model) {
        String msg = validaModel(model);
        if(msg != null) {
            throw new BusinessException(msg);
        }
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");
        VehSeries obj = new VehSeries();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }


    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");

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
    public int deleteByBrandId(String brandId) {
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_series", "vseries");
        params.put("brandId", brandId);
        return super.delete("deleteByBrandId", params);
    }
}
