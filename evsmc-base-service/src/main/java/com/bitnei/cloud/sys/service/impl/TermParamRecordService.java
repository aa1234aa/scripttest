package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.PagerModel;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.compent.kafka.service.KafkaSender;
import com.bitnei.cloud.compent.kafka.service.LocalThreadFactory;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.dao.TermParamRecordMapper;
import com.bitnei.cloud.sys.domain.TermParamRecord;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.ITermModelUnitService;
import com.bitnei.cloud.sys.service.ITermParamDicService;
import com.bitnei.cloud.sys.service.ITermParamRecordService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import it.sauronsoftware.base64.Base64;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： TermParamRecordService实现<br>
 * 描述： TermParamRecordService实现<br>
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
 * <td>2019-03-07 15:28:19</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.TermParamRecordMapper")
@RequiredArgsConstructor
public class TermParamRecordService extends BaseService implements ITermParamRecordService {

    private final IVehicleService vehicleService;
    private final ITermParamDicService termParamDicService;
    private final TermModelService termModelService;
    private final ITermModelUnitService termModelUnitService;
    private final KafkaSender kafkaSender;
    private final RealDataClient realDataClient;
    private final ThreadPoolExecutor instructTaskExecutor;
    private final TermParamRecordMapper termParamRecordMapper;
    private final ScheduledExecutorService paramsSearchScheduleExecutor;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        List<TermParamDicModel> dicModels = termParamDicService.getAll();
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<TermParamRecord> entries = findBySqlId("pagerModel", params);
            List<TermParamRecordModel> models = new ArrayList();
            for (TermParamRecord entry : entries) {
                TermParamRecord obj = (TermParamRecord) entry;
                models.add(TermParamRecordModel.fromEntry(obj));
            }
            return addParamDicToResult(models, dicModels);

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<TermParamRecordModel> models = new ArrayList();
            for (Object entry : pr.getData()) {
                TermParamRecord obj = (TermParamRecord) entry;
                models.add(TermParamRecordModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(addParamDicToResult(models, dicModels)));
            return pr;
        }
    }

    private List<JSONObject> addParamDicToResult(List<TermParamRecordModel> models,
                                                 List<TermParamDicModel> dicModels) {

        //显示调用数据渲染
        DataLoader.loadNames(models);

        return models.stream().map(it -> {

            String json = it.getParamValues();
            JSONObject recordObj = JSONObject.fromObject(it);

            JSONObject dictObj = new JSONObject();
            if (StringUtils.isNotEmpty(json)) {
                //解析json
                dictObj = JSONObject.fromObject(json);
            }

            for (TermParamDicModel dicModel : dicModels) {
                //将原来的对象转成jsonobj,加入其它参数
                String key = dicModel.getReceiveCode();
                Object value = dictObj.get(dicModel.getReceiveCode());
                //5107硬件版本，5108固件版本，5105管理平台域名，5115公共平台域名
                //这种硬编码看起来真难受，有时间优化下
                if ((StringUtils.isNotEmpty(key)) &&
                        ("5107".equals(key) || "5108".equals(key) || "5105".equals(key) || "5115".equals(key))
                        && (null != value && StringUtils.isNotEmpty(value.toString()))) {
                    try {
                        value = Base64.decode(value.toString(), "UTF-8").trim();
                    } catch (Exception e) {

                    }

                }
                recordObj.put(dicModel.getCode(), value);
            }
            return recordObj;
        }).collect(Collectors.toList());

    }

    @Override
    public TermParamRecordModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("id", id);
        TermParamRecord entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return TermParamRecordModel.fromEntry(entry);
    }

    @Override
    public TermParamRecordModel findByVin(String vin) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.put("vin", vin);
        TermParamRecord entry = unique("findByVin", params);
        if (entry == null) {
            return null;
        }
        return TermParamRecordModel.fromEntry(entry);
    }

    @Override
    public ResultMsg getParamsDetail(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        Object id = params.get("id");
        if (null == id) {
            return ResultMsg.getResult(null, "查询记录id不存在", -1);
        }

        PagerModel pm = new PagerModel();

        TermParamRecordModel model = get(id.toString());
        //参数json
        String paramsValue = model.getParamValues();

        //把所有参数拿出来匹配
        List<TermParamDicModel> allParams = termParamDicService.getAll();
        Map<String, TermParamDicModel> allParamsMap = allParams.stream()
                .filter(it -> StringUtils.isNotEmpty(it.getReceiveCode()))
                .collect(Collectors.toMap(TermParamDicModel::getReceiveCode, it -> it));

        if (StringUtils.isNotEmpty(paramsValue) && paramsValue.contains("{")) {

            JSONArray rows = new JSONArray();

            JSONObject dicObj = JSONObject.fromObject(paramsValue);
            Iterator keys = dicObj.keys();
            while (keys.hasNext()) {
                Object code = keys.next();

                Map<String, Object> map = new HashMap<>();
                map.put("code", code);

                String value = dicObj.get(code.toString()).toString();

                //如果参数不包含这个code
                if (!allParamsMap.containsKey(code.toString())) {
                    continue;
                }

                JSONObject obj = JSONObject.fromObject(allParamsMap.get(code.toString()));
                if ("5107".equals(code) || "5108".equals(code) || "5105".equals(code) || "5115".equals(code)) {
                    if (StringUtils.isNotEmpty(value)) {
                        try {
                            value = Base64.decode(value, "UTF-8").trim();
                        } catch (Exception e) {

                        }
                    }
                }
                obj.put("value", value);
                rows.add(obj);
            }

            //计算开始位置和结束位置
            int i1 = rows.size() / pagerInfo.getLimit() + 1;   //总页数

            //开始下标 ,结束下标
            int startIndex = pagerInfo.getStart(), endIndex = pagerInfo.getStart() + pagerInfo.getLimit();
            if (startIndex > rows.size() || endIndex > rows.size()) {
                startIndex = (i1 - 1) * pagerInfo.getLimit();
                endIndex = rows.size();
            }
            pm.setRows(rows.subList(startIndex, endIndex));
            pm.setTotal(rows.size());
            return pm.toResultMsg(pagerInfo.getLimit(), pagerInfo.getStart());
        }

        pm.setRows(new ArrayList());
        pm.setTotal(0L);
        return pm.toResultMsg(pagerInfo.getLimit(), pagerInfo.getStart());
    }

    @Override
    public int insert(TermParamRecordModel model) {

        TermParamRecord obj = new TermParamRecord();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        obj.setUpdateTime(com.bitnei.cloud.sys.util.DateUtil.getNow());
        obj.setUpdateUser(ServletUtil.getCurrentUser());
        return super.insert(obj);
    }

    @Override
    public void insertOrUpdate(TermParamRecordModel model) {
        //一个车查询记录只能有一条，用vin区分
        if (StringUtils.isNotEmpty(model.getVin())) {
            TermParamRecordModel recordModel = findByVin(model.getVin());
            if (null != recordModel) {
                model.setId(recordModel.getId());
            }
        }
        if (null != model.getId()) {
            update(model);
        } else {
            insert(model);
        }
    }

    @Override
    public void update(TermParamRecordModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_record", "tpr");

        TermParamRecord obj = new TermParamRecord();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
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

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_term_param_record", "tpr");

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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_vehicle", "v");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<TermParamRecord>(this, "pagerModel", params,
                "sys/res/termParamRecord/export.xls", "终端参数记录") {
        }.work();

        return;
    }

    @Override
    public void exportParamsDetail(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        Object id = params.get("id");
        if (null == id) {
            throw new BusinessException("导出失败，导出参数id不存在");
        }

        TermParamRecordModel model = get(id.toString());
        //参数json
        String paramsValue = model.getParamValues();

        //把所有参数拿出来匹配
        List<TermParamDicModel> allParams = termParamDicService.getAll();
        Map<String, TermParamDicModel> allParamsMap = allParams.stream()
                .filter(it -> StringUtils.isNotEmpty(it.getReceiveCode()))
                .collect(Collectors.toMap(TermParamDicModel::getReceiveCode, it -> it));

        if (StringUtils.isNotEmpty(paramsValue) && paramsValue.contains("{")) {

            List<Object> detailDtos = new ArrayList<>();

            JSONObject dicObj = JSONObject.fromObject(paramsValue);
            Iterator keys = dicObj.keys();
            while (keys.hasNext()) {
                Object code = keys.next();

                Map<String, Object> map = new HashMap<>();
                map.put("code", code);

                String value = dicObj.get(code.toString()).toString();

                //如果参数不包含这个code
                if (!allParamsMap.containsKey(code.toString())) {
                    continue;
                }

                JSONObject obj = JSONObject.fromObject(allParamsMap.get(code.toString()));
                if ("5107".equals(code) || "5108".equals(code) || "5105".equals(code) || "5115".equals(code)) {
                    if (StringUtils.isNotEmpty(value)) {
                        try {
                            value = Base64.decode(value, "UTF-8").trim();
                        } catch (Exception e) {

                        }
                    }
                }
                obj.put("value", value);

                TermParamDetailDto termParamDetailDto = new TermParamDetailDto();
                termParamDetailDto.setDataSize((Integer) obj.get("dataSize"));
                termParamDetailDto.setDataType(obj.get("dataType").toString());
                termParamDetailDto.setName(obj.get("name").toString());
                termParamDetailDto.setValue(obj.get("value").toString());
                detailDtos.add(termParamDetailDto);

            }
            DataLoader.loadNames(detailDtos);
            String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
            String srcFile = srcBase + "sys/res/termParamRecord/export_detail.xls";
            ExcelData ed = new ExcelData();
            ed.setTitle("导出参数详情");
            ed.setExportTime(DateUtil.getNow());
            ed.setData(detailDtos);
            String outName = String.format("%s-导出-%s.xls", "导出参数详情", DateUtil.getShortDate());
            EasyExcel.renderResponse(srcFile, outName, ed);
        }

        return;
    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "TERMPARAMRECORD" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<TermParamRecordModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(TermParamRecordModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(TermParamRecordModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "TERMPARAMRECORD" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<TermParamRecordModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(TermParamRecordModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(TermParamRecordModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 查询命令
     */
    public static final int QUERY = 1;

    /**
     * 设置命令
     */
    public static final int SET = 2;

    /**
     * 全量参数查询
     *
     * @return 返回线程对象
     */
    @Override
    public void allQuery(String vehId, String userName, Integer vehState, String operation, String describes) {
        List<TermParamDicModel> all = termParamDicService.getAll();
        //组织数据发送
        StringBuilder dicts = new StringBuilder();
        for (int i = 0; i < all.size(); i++) {
            TermParamDicModel entity = all.get(i);
            dicts.append(entity.getCode() + "|");
        }
        VehicleModel vehicleModel = vehicleService.get(vehId);
        save(vehicleModel, userName, vehState, operation, describes);

        cmd(QUERY, Collections.singletonList(vehId), dicts.substring(0, dicts.length() - 1), userName, vehState);
    }

    /**
     * 保存查询记录
     *
     * @param vehicleModel
     * @param userName
     * @param vehState
     * @return
     */
    @Override
    public TermParamRecordModel save(VehicleModel vehicleModel, String userName, Integer vehState,
                                     String operation, String describes) {
        //通过车辆id获取到所有车辆数据,并保存到参数表中

        TermParamRecordModel entity = findByVin(vehicleModel.getVin());
        if (null == entity) entity = new TermParamRecordModel();
        entity.setLicensePlate(vehicleModel.getLicensePlate());
        entity.setVin(vehicleModel.getVin());
        entity.setSendTime(DateUtil.getNow());
        entity.setUpdateTime(DateUtil.getNow());
        entity.setUpdateUser(userName);
        TermModelUnitModel termModelUnitModel = termModelUnitService.getOrNull(vehicleModel.getTermId());
        entity.setTermCode(null != termModelUnitModel ? termModelUnitModel.getSerialNumber() : "");
        entity.setVehicleTypeValue(vehicleModel.getVehTypeName());
        entity.setOperatingArea(vehicleModel.getOperAreaName());
        entity.setOperatingUnit(vehicleModel.getOperUnitName());
        entity.setHistoryOnlineState(vehState);
        entity.setReceiveState(0);
        entity.setState(null);
        entity.setResponseTime(null);
        entity.setOperation(operation);
        entity.setDescribes(describes);
        entity.setErrorMessage("");
        insertOrUpdate(entity);
        return entity;
    }

    /**
     * 保存查询记录，保存的车辆状态为车辆实时状态
     *
     * @param vehIds
     * @return 返回保存到库里的车辆数据
     */
    @Override
    public List<VehicleModel> save(List<String> vehIds, String userName, String operation, String describes) {
        //对车辆id切割,获得所有车辆id
        List<VehicleModel> list = new ArrayList<>();
        //通过车辆id获取到所有车辆数据,并保存到参数表中
        for (String id : vehIds) {
            VehicleModel vehicleModel = vehicleService.get(id);
            list.add(vehicleModel);

            int vehState = vehicleService.getVehicleOnlineStatus(vehicleModel.getVin());

            save(vehicleModel, userName, vehState, operation, describes);
        }
        return list;
    }

    /**
     * 校验参数
     * @param jsonObject
     * @return
     */
    @Override
    public Object verification(JSONObject jsonObject) {
        Map<String, Object> param = new HashMap<>();
        if (null != jsonObject.get("5202")){
            int index = Integer.parseInt(jsonObject.get("5202").toString());
            if (index < 0 || index > 60000){
                param.put("error", "车载终端时间周期错误，范围0-60000");
                return param;
            }
        }
        if (null != jsonObject.get("5210")){
            int index = Integer.parseInt(jsonObject.get("5210").toString());
            if (index < 1 || index > 600){
                param.put("error", "终端应答超时时间错误，范围1-600");
                return param;
            }
        }
        if (null != jsonObject.get("5211")){
            int index = Integer.parseInt(jsonObject.get("5211").toString());
            if (index < 1 || index > 600){
                param.put("error", "平台应答超时时间错误，范围1-600");
                return param;
            }
        }
        if (null != jsonObject.get("5214")){
            int index = Integer.parseInt(jsonObject.get("5214").toString());
            if (index < 1 || index > 240){
                param.put("error", "登入时间间隔错误，范围1-240");
                return param;
            }
        }
        if (null != jsonObject.get("5216")){
            int index = Integer.parseInt(jsonObject.get("5216").toString());
            if (index < 1 || index > 65531){
                param.put("error", "公共平台端口错误，范围1-65531");
                return param;
            }
        }
        if (null != jsonObject.get("5203")){
            int index = Integer.parseInt(jsonObject.get("5203").toString());
            if (index < 1 || index > 600){
                param.put("error", "正常上报时间周期错误，范围1-600");
                return param;
            }
        }
        if (null != jsonObject.get("5204")){
            int index = Integer.parseInt(jsonObject.get("5204").toString());
            if (index < 1 || index > 60000){
                param.put("error", "报警上报时间周期错误，范围1-60000");
                return param;
            }
        }
        if (null != jsonObject.get("5206")){
            int index = Integer.parseInt(jsonObject.get("5206").toString());
            if (index < 1 || index > 65531){
                param.put("error", "管理平台端口错误，范围1-65531");
                return param;
            }
        }
        if (null != jsonObject.get("5209")){
            int index = Integer.parseInt(jsonObject.get("5209").toString());
            if (index < 1 || index > 240){
                param.put("error", "终端心跳发送周期错误，范围1-240");
                return param;
            }
        }
        param.put("success", "200");
        return param;
    }

    /**
     * 判断是否非空,取对象名
     *
     * @param obj       对象
     * @param type      返回类型
     * @param fieldName 获取的字段名
     * @return
     */
    private <T> T getName(String obj, Class<T> type, String fieldName) {
        if (StringUtils.isNotEmpty(obj)) {
            Class<?> clazz = obj.getClass();
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (ReflectiveOperationException e) {
                log.info("从车辆表插入查询记录表数据出错");
               log.error("error", e);
            }
        }
        return null;
    }

    /**
     * 发送命令
     *
     * @param type     1, 查询 2,设置
     * @param vehIds
     * @param dict
     * @param userName
     * @param vehHisState 如果外层有传这个值，以这个值的车辆状态为准，只适用于车辆数量为1辆的情况，慎用
     * @return
     */
    @SneakyThrows
    public ResultMsg cmd(int type, List<String> vehIds, String dict, String userName, Integer vehHisState) {
        Integer vehState;
        //遍历车辆id
        for (String vehId : vehIds) {
            VehicleModel entity = vehicleService.get(vehId);
            // 车辆是否在线
            if (null != vehHisState) {
                vehState = vehHisState;
            } else {
                vehState = vehicleService.getVehicleOnlineStatus(entity.getVin());
            }

            //状态1则为在线在线
            if (vehState == 1) {
                String vehType = entity.getVehTypeId();
                if (StringUtils.isEmpty(vehType)) {
                    log.info("================车牌号：" + entity.getLicensePlate() + "，VIN码：" +
                            entity.getVin() + "车辆种类为空");
                }
                if (type == QUERY) { //判断1 发送查询命令
                    //在线就发送指令
                    try {
                        String cmd = String.format("SUBMIT 1 %s GETARG {VID:%s,VTYPE:%s,5001:%s,5002:%s}",
                                entity.getVin(), entity.getUuid(), vehType,
                                com.bitnei.cloud.sys.util.DateUtil.getKafkaDataSyncTime(), dict);
                        log.info(cmd);
                        kafkaSender.send(SysDefine.kafkaDataCtrlTopic, "GETARG", cmd);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else if (type == SET) { //判断其他, 发送设置命令
                    dict = termParamDicService.base64Encoding(dict, ",");
                    String cmd = String.format("SUBMIT 1 %s SETARG {VID:%s,VTYPE:%s,5201:%s,%s}", entity.getVin(),
                            entity.getUuid(), vehType, com.bitnei.cloud.sys.util.DateUtil.getKafkaDataSyncTime(), dict);
                    log.info(cmd);
                    kafkaSender.sendSync(SysDefine.kafkaDataCtrlTopic, "SETARG", cmd);
                }
            } else {
                //不在线,直接失败
                TermParamRecordModel entity1 = findByVin(entity.getVin());
                entity1.setReceiveState(1);
                entity1.setHistoryOnlineState(vehState);
                entity1.setUpdateUser(userName);
                entity1.setUpdateTime(DateUtil.getNow());
                entity1.setResponseTime(DateUtil.getNow());
                entity1.setState(TermParamRecordModel.State.error);
                entity1.setErrorMessage("车辆离线，下发指令失败");
                update(entity1);
            }
        }

        //循环查询表, 查看kafka反馈状态是否更新

        int flag = 0;

        Map<String, ResultMsg> resultMap = new HashMap<>();

        while (flag < 3) {

            //TODO 终端参数查询 这里的delay时间可能需要修改
            ScheduledFuture<Map<String, ResultMsg>> scheduledFuture = paramsSearchScheduleExecutor.schedule(() -> {

                Map<String, ResultMsg> resultMsgMap = new HashMap<>();
                for (String vehId : vehIds) {

                    TermParamRecordModel entity = findByVehId(vehId);
                    if (entity.getReceiveState() == 1) {
                        //判断状态, 成功,失败,超时状态
                        if (TermParamRecordModel.State.success == entity.getState()) {
                            resultMsgMap.put(vehId, ResultMsg.getResult(
                                    entity.getId(), "参数设置|参数查询成功", 200));
                        } else {
                            if (TermParamRecordModel.State.error == entity.getState()) {
                                resultMsgMap.put(vehId, ResultMsg.getResult(
                                        null, "参数设置|参数查询错误", 400));
                            }
                            if (TermParamRecordModel.State.timeout == entity.getState()) {
                                entity.setErrorMessage("参数设置|参数查询响应超时");
                                update(entity);
                                resultMsgMap.put(vehId, ResultMsg.getResult(
                                        null, "参数设置超时", 408));
                            }
                        }
                    }
                }

                return resultMsgMap;
            }, 10 * 1000L, TimeUnit.MILLISECONDS);

            resultMap = scheduledFuture.get();
            if (vehIds.size() == 1 && resultMap.size() == 1) {
                return resultMap.get(resultMap.keySet().toArray()[0]);
            }

            flag++;
        }

        for (String vehId : vehIds) {
            if (!resultMap.containsKey(vehId)) {
                //如果3次没查到, 判断参数查询超时
                TermParamRecordModel entity = findByVehId(vehId);
                //如果设置响应已经有错误原因了，就不需要设置超时了原因了
                if (StringUtils.isEmpty(entity.getErrorMessage())) {
                    entity.setErrorMessage("参数设置|参数查询响应超时");
                    update(entity);
                }
            }
        }

        if (vehIds.size() == 1) {
            return ResultMsg.getResult(null, "参数设置|参数查询响应超时", 408);
        }
        return null;
    }

    /**
     * 通过车辆id查询车辆参数查询记录
     *
     * @param vehId
     * @return
     */
    public TermParamRecordModel findByVehId(String vehId) {
        VehicleModel sysVehicleEntity = vehicleService.get(vehId);
        return findByVin(sysVehicleEntity.getVin());
    }
}
