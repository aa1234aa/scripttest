package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.VehBrand;
import com.bitnei.cloud.sys.model.VehBrandModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.service.IVehSeriesService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehBrandService实现<br>
* 描述： VehBrandService实现<br>
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
* <td>2019-01-22 14:33:02</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehBrandMapper" )
public class VehBrandService extends BaseService implements IVehBrandService {

    @Autowired
    private IVehSeriesService vehSeriesService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<VehBrand> entries = findBySqlId("pagerModel", params);
            List<VehBrandModel> models = Lists.newArrayList();
            for(VehBrand entry: entries){
                models.add(VehBrandModel.fromEntry(entry));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehBrandModel> models = Lists.newArrayList();
            for(Object entry: pr.getData()){
                VehBrand obj = (VehBrand)entry;
                models.add(VehBrandModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

            

    @Override
    public VehBrandModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");
        params.put("id",id);
        VehBrand entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehBrandModel.fromEntry(entry);
    }

    @Override
    public VehBrandModel getByName(String name) {
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");
        params.put("name", name);
        VehBrand entry = unique("findByName", params);
        if (entry == null){
            throw new BusinessException("品牌不存在");
        }
        return VehBrandModel.fromEntry(entry);
    }

    @Override
    public void insert(VehBrandModel model) {

        model.setCode(model.getCode().toUpperCase());
        String msg = validaModel(model);
        if(msg != null) {
            throw new BusinessException(msg);
        }
        VehBrand obj = new VehBrand();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }
    }

    private String validaModel(VehBrandModel model) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");
        params.put("name", model.getName());
        VehBrand entry = unique("findByName", params);
        if(entry != null ) {
            if(StringUtils.isBlank(model.getId()) ||
                    !StringUtils.equals(model.getId(), entry.getId())) {
                return "品牌名称已存在";
            }
        }
        params.put("code", model.getCode());
        VehBrand brand = unique("findByCode", params);
        if(brand != null) {
            if(StringUtils.isBlank(model.getId()) ||
                    !StringUtils.equals(model.getId(), brand.getId())) {
                return "品牌编号已存在";
            }
        }
        if(StringUtils.isNotBlank(model.getEnglistName())){
            params.put("englistName",model.getEnglistName());
            VehBrand vehBrand = unique("findByEnglistName", params);
            if(vehBrand != null) {
                if(StringUtils.isBlank(model.getId()) ||
                        !StringUtils.equals(model.getId(), vehBrand.getId())) {
                    return "英文品牌已存在";
                }
            }
        }
       return null;
    }

    @Override
    public void update(VehBrandModel model) {

        model.setCode(model.getCode().toUpperCase());
        String msg = validaModel(model);
        if(msg != null) {
            throw new BusinessException(msg);
        }
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");
        VehBrand obj = new VehBrand();
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
    * @param ids id集,多个逗号间隔
    * @return 影响行数
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_brand", "brand");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr ) {
            params.put("id",id);
            int r = super.deleteByMap(params);
            count += r;
            // 物理删除品牌下的车型系列
            int i = vehSeriesService.deleteByBrandId(id);
            count += i;
        }
        return count;
    }



            

}
