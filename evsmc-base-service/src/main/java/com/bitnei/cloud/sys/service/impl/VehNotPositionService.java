package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.MybatisOfflineExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.VehNotPositionMapper;
import com.bitnei.cloud.sys.domain.VehNotPosition;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.model.VehNotPositionModel;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.service.IVehNotPositionService;
import com.bitnei.commons.util.MapperUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehNotPositionService实现<br>
* 描述： VehNotPositionService实现<br>
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
* <td>2019-03-05 11:24:01</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.VehNotPositionMapper" )
public class VehNotPositionService extends BaseService implements IVehNotPositionService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    @Autowired
    private VehNotPositionMapper vehNotPositionMapper;
    @Autowired
    private IOfflineExportService offlineExportService;
    @Autowired
    private IUserService userService;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
       //历史加载页面时不显示数据
       if (pagerInfo.getConditions() == null ||pagerInfo.getConditions().size() == 0){
           return new ArrayList<>();
       }
       //判断时间是否合理
       if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
           long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
           long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
           if (endTime - beginTime < 0){
               throw new BusinessException("请输入正确的时间");
           }
           else if (endTime - beginTime >= (long)86400000*30){
               throw new BusinessException("查询时间跨度不能超过30天");
           }
       }
       else{
           throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
       }
       //前端查询传过来的时间（天）
       if (params.get("days") != null && !String.valueOf(params.get("days")).equals("")){
           String days = String.valueOf(params.get("days"));
           if(!days.matches("^[0-9]+([.][0-9])?$")){
               throw new BusinessException("定位异常天数请输入正确的数值，并且小数点后只能保留一位");
           }
           if (StringUtil.isNotEmpty(days)){
               days = Float.valueOf(days) * (24 * 60 * 60)+"";
           }
           params.put("days",days);
       }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<VehNotPosition> entries = findBySqlId("pagerModel", params);
            List<VehNotPositionModel> models = new ArrayList();
            for(VehNotPosition entry: entries){
                VehNotPosition obj = (VehNotPosition)entry;
                VehNotPositionModel data = VehNotPositionModel.fromEntry(obj);
                data.setDays(data.getSeconds() == null ? null : this.secondsToDays(data.getSeconds()));
                data.setShowLastUploadTime(data.getLastUploadTime() == null ? null : DateUtil.getDate(data.getLastUploadTime().getTime()));
                data.setShowRegainUploadTime(data.getRegainUploadTime() == null ? null : DateUtil.getDate(data.getRegainUploadTime().getTime()));
                models.add(data);
            }
            return models;

        }
        //分页查询
        else{
            // 构建分页参数
            final PageRowBounds pageRowBounds = PageRowBoundsUtil.fromPagerInfo(pagerInfo);
            // 进行分页查询
            final Page<VehNotPosition> page = vehNotPositionMapper.pagerModel(params, pageRowBounds);

            final PagerResult pr = new PagerResult();
            // 设置结果集
            pr.setData(page.stream().map(
                    p -> {

                        VehNotPositionModel model = VehNotPositionModel.fromEntry(p);
                        model.setDays(model.getSeconds() == null ? null : this.secondsToDays(model.getSeconds()));
                        model.setShowLastUploadTime(model.getLastUploadTime() == null ? null : DateUtil.getDate(model.getLastUploadTime().getTime()));
                        model.setShowRegainUploadTime(model.getRegainUploadTime() == null ? null : DateUtil.getDate(model.getRegainUploadTime().getTime()));
                        return model;

                    }).collect(Collectors.toList()));
            // 设置记录总数
            pr.setTotal(page.getTotal());

            return pr;
        }
    }

    @Override
    public Object realTimeList(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !String.valueOf(params.get("beginTime")).equals("") && params.get("endTime")!=null && !params.get("endTime").equals("")){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
        }
        //前端查询传过来的时间（天）
        if (params.get("days") != null && !String.valueOf(params.get("days")).equals("")){
            String days = String.valueOf(params.get("days"));
            if(!days.matches("^[0-9]+([.][0-9])?$")){
                throw new BusinessException("定位异常天数请输入正确的数值，并且小数点后只能保留一位");
            }
            if (StringUtil.isNotEmpty(days)){
                days = Float.valueOf(days) * (24 * 60 * 60)+"";
            }
            params.put("days",days);
        }
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<VehNotPosition> entries = findBySqlId("findByRealTime", params);
            List<VehNotPositionModel> models = new ArrayList();
            for(VehNotPosition entry: entries){
                VehNotPosition obj = (VehNotPosition)entry;
                VehNotPositionModel data = VehNotPositionModel.fromEntry(obj);
                data.setDays(data.getSeconds() == null ? null : this.secondsToDays(data.getSeconds()));
                data.setShowLastUploadTime(data.getLastUploadTime() == null ? null : DateUtil.getDate(data.getLastUploadTime().getTime()));
                models.add(data);
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("findByRealTime", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<VehNotPositionModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                VehNotPosition obj = (VehNotPosition)entry;
                VehNotPositionModel data = VehNotPositionModel.fromEntry(obj);
                data.setDays(data.getSeconds() == null ? null : this.secondsToDays(data.getSeconds()));
                data.setShowLastUploadTime(data.getLastUploadTime() == null ? null : DateUtil.getDate(data.getLastUploadTime().getTime()));
                models.add(data);
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public String secondsToDays(String second) {

        //数据库查询的离线时间（秒）
        float seconds = Float.valueOf(second);
        int day = 0,hours = 0;
        //组装前台显示是离线时间（天-小时）
        String newDay = "";

        day = (int) (seconds/(60*60*24));
        //不足一天的秒数化为小时数，并舍弃不足1小时的时间
        hours = (int) (seconds%(60*60*24)) / (60*60);
        if (day!=0&&hours!=0){
            newDay = day+"天"+hours+"小时";
        }
        else if(day!=0 && hours==0){
            newDay = day+"天";
        }
        else if(day==0 && hours!=0){
            newDay = hours+"小时";
        }
        else{
            newDay = "小于1小时";
        }
        return newDay;
    }


    @Override
    public VehNotPositionModel get(String vid) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_not_position", "vnp");
        params.put("vid",vid);
        VehNotPosition entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehNotPositionModel.fromEntry(entry);
    }

    @Override
    public VehNotPositionModel getByVid(String vid) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_not_position", "vnp");
        params.put("uuid",vid);
        VehNotPosition entry = unique("getByVid", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return VehNotPositionModel.fromEntry(entry);
    }


    @Override
    public void insert(VehNotPositionModel model) {

        VehNotPosition obj = new VehNotPosition();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
//        String id = UtilHelper.getUUID();
//        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
//            id = model.getId();
//        }
//        obj.setId(id);
//        obj.setCreateTime(Timestamp.valueOf(DateUtil.getNow()));
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(VehNotPositionModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_not_position", "vnp");

        VehNotPosition obj = new VehNotPosition();
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
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_veh_not_position", "vnp");

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
        if (pagerInfo.getConditions() == null || pagerInfo.getConditions().size() == 0){
            throw new BusinessException("列表无数据，请输入查询条件");
        }
        else{
            //判断时间是否合理
            if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
                long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
                long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
                if (endTime - beginTime < 0){
                    throw new BusinessException("请输入正确的时间");
                }
                else if (endTime - beginTime >= (long)86400000*30){
                    throw new BusinessException("查询时间跨度不能超过30天");
                }
            }
            else{
                throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
            }
            //前端查询传过来的时间（天）
            if (params.get("days") != null && !String.valueOf(params.get("days")).equals("")){
                String days = String.valueOf(params.get("days"));
                if(!days.matches("^[0-9]+([.][0-9])?$")){
                    throw new BusinessException("定位异常天数请输入正确的数值，并且小数点后只能保留一位");
                }
                if (StringUtil.isNotEmpty(days)){
                    days = Float.valueOf(days) * (24 * 60 * 60)+"";
                }
                params.put("days",days);
            }
            new ExcelExportHandler<VehNotPosition>(this, "pagerModel", params, "sys/res/vehNotPosition/export.xls", "历史定位异常车辆表") {
                @Override
                public Object process(VehNotPosition entity) {
                    entity.setDays(entity.getSeconds() == null ? null : secondsToDays(entity.getSeconds()));
                    entity.setShowLastUploadTime(entity.getLastUploadTime() == null ? null : DateUtil.getDate(entity.getLastUploadTime().getTime()));
                    entity.setShowRegainUploadTime(entity.getRegainUploadTime() == null ? null : DateUtil.getDate(entity.getRegainUploadTime().getTime()));
                    return super.process(entity);
                }
            }.work();
        }

        return;
    }

    @Override
    public String exportOffline(@NotNull final PagerInfo pagerInfo) {

        if (pagerInfo.getConditions() == null || pagerInfo.getConditions().size() <= 0){
            throw new BusinessException("列表无数据，请输入查询条件");
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !"".equals(String.valueOf(params.get("beginTime"))) && params.get("endTime")!=null && !"".equals(String.valueOf(params.get("endTime")))){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
            else if (endTime - beginTime >= (long)86400000*30){
                throw new BusinessException("查询时间跨度不能超过30天");
            }
        }
        else{
            throw new BusinessException("请输入查询时间段，并且时间跨度不能超过30天");
        }
        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 构建给回调方法的第 3 个参数
        final String exportFilePrefixName = "历史定位异常车辆表";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        // 创建离线导出任务
        return offlineExportService.createTask(
            exportFilePrefixName,
            exportServiceName,
            exportMethodName,
            exportMethodParams
        );
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public void exportOfflineProcessor(
        @NotNull final String taskId,
        @NotNull final String createBy,
        @NotNull final Date createTime,
        @NotNull final String exportFileName,
        @NotNull final String exportMethodParams) throws Exception {

        log.trace("执行离线导出任务:{}", exportFileName);

        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        final UserModel userModel = userService.findByUsername(createBy);
        final String userId = userModel.getId();
        final String tableName = "sys_vehicle";
        final String tableAlias = "sv";
        final String authSql = DataAccessKit.getUserAuthSql(userId, tableName, tableAlias);
        if (authSql != null) {
            builder.put(Constants.AUTH_SQL, authSql);
        }

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);
        ServletUtil.pageInfoToMap(pagerInfo).forEach((k, v) -> {
            if (null != k && null != v) {
                builder.put(k, v);
            }
        });
        final Map<String, Object> params = new HashMap<>();
        params.putAll(builder.build());
        //前端查询传过来的时间（天）
        if (params.get("days") != null && !String.valueOf(params.get("days")).equals("")) {
            String days = String.valueOf(params.get("days"));
            if (!days.matches("^[0-9]+([.][0-9])?$")) {
                throw new ExportErrorException("离线天数请输入正确的数值，并且小数点后只能保留一位");
            }
            if (StringUtil.isNotEmpty(days)) {
                days = Float.valueOf(days) * (24 * 60 * 60) + "";
            }
            params.put("days", days);
        }
        final String excelTemplateFile = "sys/res/vehNotPosition/export.xls";

        MybatisOfflineExportHandler.csv(
            taskId,
            createBy,
            createTime,
            exportFileName,
            excelTemplateFile,
            this.vehNotPositionMapper::pagerModel,
            params,
            this::fromEntityToModel,
            this.vehNotPositionMapper::pagerModel,
            redis,
            ws
        );
    }

    @NotNull
    private List<VehNotPositionModel> fromEntityToModel(final @NotNull List<VehNotPosition> entities) {

        final ArrayList<VehNotPositionModel> models = Lists.newArrayList();

        for (final VehNotPosition entity : entities) {

            final VehNotPositionModel model = new VehNotPositionModel();
            BeanUtils.copyProperties(entity, model);

            model.setLicensePlate(entity.get("licensePlate") == null ? null : entity.get("licensePlate").toString());
            model.setVin(entity.get("vin") == null ? null : entity.get("vin").toString());
            model.setInterNo(entity.get("interNo") == null ? null : entity.get("interNo").toString());
            model.setVehModelId(entity.get("vehModelId") == null ? null : entity.get("vehModelId").toString());
            model.setOperLicenseCityId(entity.get("operLicenseCityId") == null ? null : entity.get("operLicenseCityId").toString());
            model.setOperUnitId(entity.get("operUnitId") == null ? null : entity.get("operUnitId").toString());
            model.setOperAreaId(entity.get("operAreaId") == null ? null : entity.get("operAreaId").toString());
            model.setSerialNumber(entity.get("serialNumber") == null ? null : entity.get("serialNumber").toString());
            model.setIccid(entity.get("iccid") == null ? null : CsvUtil.encode("`" + entity.get("iccid").toString()));
            model.setDays(StringUtils.isNotBlank(model.getSeconds()) ? this.secondsToDays(model.getSeconds()) : null);
            model.setShowLastUploadTime(model.getLastUploadTime() == null ? null : DateUtil.getDate(model.getLastUploadTime().getTime()));
            model.setShowRegainUploadTime(model.getRegainUploadTime() == null ? null : DateUtil.getDate(model.getRegainUploadTime().getTime()));
            model.setVin(entity.get("isDelete") == null || "1".equals(entity.get("isDelete").toString()) ? model.getVin() == null ? "(已删除)" : model.getVin()+"(已删除)" : model.getVin());

            models.add(model);
        }

        DataLoader.loadNames(models);

        return models;
    }

    @Override
    public void realTimeExport(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_vehicle", "sv");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //判断时间是否合理
        if (params.get("beginTime")!=null && !String.valueOf(params.get("beginTime")).equals("") && params.get("endTime")!=null && !params.get("endTime").equals("")){
            long beginTime =  DateUtil.strToDate_ex_full(String.valueOf(params.get("beginTime"))).getTime();
            long endTime = DateUtil.strToDate_ex_full(String.valueOf(params.get("endTime"))).getTime();
            if (endTime - beginTime < 0){
                throw new BusinessException("请输入正确的时间");
            }
        }
        //前端查询传过来的时间（天）
        if (params.get("days") != null && !String.valueOf(params.get("days")).equals("")){
            String days = String.valueOf(params.get("days"));
            if(!days.matches("^[0-9]+([.][0-9])?$")){
                throw new BusinessException("定位异常天数请输入正确的数值，并且小数点后只能保留一位");
            }
            if (StringUtil.isNotEmpty(days)){
                days = Float.valueOf(days) * (24 * 60 * 60)+"";
            }
            params.put("days",days);
        }
        new ExcelExportHandler<VehNotPosition>(this, "findByRealTime", params, "sys/res/vehNotPosition/realTimeExport.xls", "实时定位异常车辆表") {
            @Override
            public Object process(VehNotPosition entity) {
                entity.setDays(entity.getSeconds() == null ? null : secondsToDays(entity.getSeconds()));
                entity.setShowLastUploadTime(entity.getLastUploadTime() == null ? null : DateUtil.getDate(entity.getLastUploadTime().getTime()));
                return super.process(entity);
            }
        }.work();
        return;
    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "VEHNOTPOSITION"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<VehNotPositionModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(VehNotPositionModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(VehNotPositionModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "VEHNOTPOSITION"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<VehNotPositionModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(VehNotPositionModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(VehNotPositionModel model) {
                update(model);
            }
        }.work();

    }



}
