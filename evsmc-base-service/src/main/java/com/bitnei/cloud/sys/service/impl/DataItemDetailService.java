package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.Exception.CancelRequestException;
import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.client.DataReadMode;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.HisDataClient;
import com.bitnei.cloud.common.client.das.RealDataClient;
import com.bitnei.cloud.common.client.model.*;
import com.bitnei.cloud.common.handler.*;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.dc.domain.DataItem;
import com.bitnei.cloud.dc.domain.DataItemGroup;
import com.bitnei.cloud.dc.domain.Rule;
import com.bitnei.cloud.dc.domain.RuleType;
import com.bitnei.cloud.dc.model.DataItemGroupModel;
import com.bitnei.cloud.dc.model.RuleModel;
import com.bitnei.cloud.dc.service.IDataItemGroupService;
import com.bitnei.cloud.dc.service.IDataItemService;
import com.bitnei.cloud.dc.service.IRuleService;
import com.bitnei.cloud.fault.service.ICodeTypeService;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.common.DataItemKey;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.domain.DataItemDetail;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.model.DataItemDetailModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IDataItemDetailService;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseService;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.commons.util.MapperUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.util.ClassLoaderUtil;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DataItemDetailService实现<br>
 * 描述： DataItemDetailService实现<br>
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
 * <td>2019-03-16 16:09:17</td>
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
@RequiredArgsConstructor
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.DataItemDetailMapper")
public class DataItemDetailService extends CommonBaseService implements IDataItemDetailService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    private final IVehicleService vehicleService;

    private final HisDataClient hisDataClient;
    private final RealDataClient realDataClient;
    private final IDataItemService dataItemService;
    private final IDataItemGroupService dataItemGroupService;
    private final IRuleService ruleService;
    private final com.bitnei.cloud.dc.mapper.RuleTypeMapper ruleTypeMapper;

    @Value("${dataItemDetail.timeSizeHour:4}")
    private int timeSizeHour;
    @Value("${dataItemDetail.timeSizeMin:15}")
    private int timeSizeMin;

    @Value("${dataItemDetail.queryMode:1}")
    private int queryMode;

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }



    public static String bytesToHexString(Byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString();
    }



    private static Map<String, String> findFinalParent(Map<String, String> current, Map<String, Map<String, String>> all) {
        if (current.get("parent_id") == null || current.get("id").equals("0")) {
            return null;
        }
        if (current.get("parent_id").equals("0")) {

            return current;
        }
        if (!all.containsKey(current.get("parent_id"))) {

            return null;

        }
        current = all.get(current.get("parent_id"));
        return findFinalParent(current, all);

    }

    public static void main(String[] args) {




    }

    private List<AbstractMap.SimpleEntry<String, String>> splitTime(String startTime, String endTime, PagerInfo pagerInfo) {
        List<AbstractMap.SimpleEntry<String, String>> timeSpan = new ArrayList<>();
        long end = DateUtil.getLongTimeNum(endTime);


        long timeUnit = 0;

        if (1 == 2) {
            //启用分页
            //分钟为单位
            timeUnit = (long)60000 * timeSizeMin;
        } else {
            //小时为单位
            timeUnit = (long)3600000 * timeSizeHour;
        }


        long innerBegin = DateUtil.getLongTimeNum(startTime);
        long innerEnd = innerBegin + timeUnit;

        for (; innerEnd <= end; innerEnd += timeUnit) {
            AbstractMap.SimpleEntry<String, String> v = new AbstractMap.SimpleEntry<>(DateUtil.getDate(innerBegin), DateUtil.getDate(innerEnd));
            timeSpan.add(v);
            innerBegin = innerEnd + 1000;
        }
        if (innerBegin - 1000 < end) {
            AbstractMap.SimpleEntry<String, String> v = new AbstractMap.SimpleEntry<>(DateUtil.getDate(innerBegin), DateUtil.getDate(end));
            timeSpan.add(v);
        }

        return timeSpan;
    }


    private List<AbstractMap.SimpleEntry<String, String>> getDateSpan(PagerInfo pagerInfo) {
        String startTime = getQueryCondition(pagerInfo, "beginTime"),

                endTime = getQueryCondition(pagerInfo, "endTime");

        if (checkDate(pagerInfo)) {
            return splitTime(startTime, endTime, pagerInfo);
        }
        return null;

    }

    public List<DataItem> getDataItemArray(PagerInfo pagerInfo) {
        String dataNosStr = getQueryCondition(pagerInfo, "dataNos");
        if (StringUtils.isBlank(dataNosStr)) {
            throw new BusinessException("请选择数据项");
        }
        //从UI端明细数据项查询为入口发起的接口调用
        String fromUI = getQueryCondition(pagerInfo, "isUI");
        List<DataItem> dataNos;
        if (StringUtils.isBlank(fromUI)) {
            dataNos = parseDataNos(dataNosStr);
        } else {
            dataNos = parseDataNos(dataNosStr, pagerInfo);
        }


        if (dataNos == null || dataNos.size() == 0) {
            throw new BusinessException("请选择数据项");
        } else {
            dataNos = dataNos.stream().filter(distinctByKey(DataItem::getSeqNo)).collect(Collectors.toList());
        }
        return dataNos;

    }


    @Override
    public Object list(PagerInfo pagerInfo) {

        VehicleModel v = findVehicle(pagerInfo);

        checkDate(pagerInfo);

        List<DataItem> dataNos = getDataItemArray(pagerInfo);
        addQueryCondition(pagerInfo, "limitDay", "30");

        String readMode = getQueryCondition(pagerInfo, "dataReadMode");
        String vin = getQueryCondition(pagerInfo, "queryContent");
        Integer dataReadMode = DataReadMode.TRANSLATE;
        if (StringUtils.isNotBlank(readMode)) {
            dataReadMode = Integer.valueOf(readMode);
        }

        Map<String, Object> param = Maps.newHashMap();
        param.put("supportProtocol", v.getSupportProtocol());
        String isG6 = isG6(param) ? "1" : "0";
        String isUI = getQueryCondition(pagerInfo, "isUI");
        boolean needTerm = StringUtils.isNotBlank(isUI) && "1".equals(isUI);
        if (isPager(pagerInfo)) {
            VehicleModel vehicleModel = new VehicleModel();
            PageRequest pageRequest = buildPageRequest(pagerInfo, true, dataNos, vehicleModel);
            String order = getQueryCondition(pagerInfo, "order");
            boolean asc = StringUtils.isBlank(order) || order.equals("1");
            pageRequest.setAsc(asc);
            pageRequest.setReadMode(dataReadMode);

            //获取明细数据
            GlobalResponse<PageResult<Map<String, String>>> resultGlobalResponse =
                    findPageByUUID(pageRequest, vehicleModel.getSupportProtocol());

            if (resultGlobalResponse != null && resultGlobalResponse.getData() != null
                    && CollectionUtils.isNotEmpty(resultGlobalResponse.getData().getData())) {


                Map<String, String> seq = dataNos.stream().collect(Collectors.toMap(DataItem::getSeqNo, i -> i.getSeqNo() == null ? "" : i.getSeqNo()));

                List<Map<String, String>> result;

                if (StringUtils.isBlank(isG6) || isG6.equals("0")) {
                    result = dataItemsSlimming(resultGlobalResponse.getData().getData(), seq,
                            StringUtils.isNotBlank(getQueryCondition(pagerInfo, "TRANSLATE")), asc, needTerm);
                } else {


                    result = G6DataItemsSlimming(resultGlobalResponse.getData().getData(), seq,
                            StringUtils.isNotBlank(getQueryCondition(pagerInfo, "TRANSLATE")), asc, needTerm);
                }

                result.forEach(e -> e.put("vin", vin));

                if (isPager(pagerInfo)) {
                    //启用分页
                    PagerResult pagerResult = new PagerResult();
                    pagerResult.setData(Collections.singletonList(result));
                    pagerResult.setTotal(resultGlobalResponse.getData().getTotalCount());
                    pagerResult.setPagerInfo(pagerInfo);

                    return pagerResult;
                } else {
                    return result;
                }
            }
        } else {

            //获取全部数据项
            List<Map<String, String>> allData = getData(pagerInfo, dataNos, dataReadMode);


            Map<String, String> seq = dataNos.stream().collect(Collectors.toMap(DataItem::getSeqNo, i -> i.getSeqNo() == null ? "" : i.getSeqNo()));


            List<Map<String, String>> result;
            if (StringUtils.isBlank(isG6) || isG6.equals("0")) {
                //国标
                result = dataItemsSlimming(allData, seq,
                        StringUtils.isNotBlank(getQueryCondition(pagerInfo, "TRANSLATE")), true, needTerm);
            } else {
                //国6


                result = G6DataItemsSlimming(allData, seq,
                        StringUtils.isNotBlank(getQueryCondition(pagerInfo, "TRANSLATE")), true, needTerm);
            }

            result.forEach(e -> e.put("vin", vin));

            return result;
        }
        throw new BusinessException("没有数据啦");


    }

    public GlobalResponse<PageResult<Map<String, String>>> findPageByUUID(PageRequest pageRequest,
                                                                          String supportProtocol) {

        if (StringUtils.isEmpty(supportProtocol)) {
            pageRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            return hisDataClient.findPageByUUID(pageRequest);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("supportProtocol", supportProtocol);
        List<RuleType> ruleTypes = ruleTypeMapper.findByRuleId(params);
        if (CollectionUtils.isNotEmpty(ruleTypes)) {
            for (RuleType it : ruleTypes) {
                if (it.getName().contains("国六") || it.getCode().contains("17691")) {
                    pageRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                    return hisDataClient.findPageByUUID(pageRequest);
                }
            }
        }
        pageRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
        return hisDataClient.findPageByUUID(pageRequest);
    }

    @Override
    public boolean isG6(Map<String, Object> params) {
        //TODO 可以省去该方法,查询车辆的时候就关联出协议类型
        boolean ret = false;
        if (params.get("supportProtocol") != null) {
            String supportProtocol = params.get("supportProtocol").toString();
            params.clear();
            params.put("supportProtocol", supportProtocol);
            try {


                List<RuleType> ruleTypes = ruleTypeMapper.findByRuleId(params);
                if (CollectionUtils.isNotEmpty(ruleTypes)) {
                    for (RuleType it : ruleTypes) {
                        if (it.getName().contains("国六") || it.getCode().contains("17691")) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("error", e);
                return false;
            }
        } else if (null != params.get("vin")) {
            try {
                VehicleModel vehicleModel = vehicleService.getByVin(params.get("vin").toString());
                Map<String, Object> param = Maps.newHashMap();
                param.put("supportProtocol", vehicleModel.getSupportProtocol());
                return isG6(param);
            } catch (Exception e) {
                log.error("error", e);
                return false;
            }


        } else if (null != params.get("id")) {
            try {
                VehicleModel vehicleModel = vehicleService.getByIdSimple(params.get("id").toString());
                Map<String, Object> param = Maps.newHashMap();
                param.put("supportProtocol", vehicleModel.getSupportProtocol());
                return isG6(param);
            } catch (Exception e) {
                log.error("error", e);
                return false;
            }


        }
        return ret;
    }

    List<Map<String, String>> getData(PagerInfo pagerInfo, List<DataItem> dataNos, Integer dataReadMode) {

        List<AbstractMap.SimpleEntry<String, String>> dateSpan = getDateSpan(pagerInfo);
        if (null == dateSpan) {
            throw new BusinessException("参数错误");
        }

        CopyOnWriteArrayList<Map<String, String>> mulList = new CopyOnWriteArrayList<>();
        int size = dateSpan.size();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(size, size, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(size));

        VehicleModel v = findVehicle(pagerInfo);
        dateSpan.forEach(q -> {

            executor.execute(() -> {
                for (int i = 0; i < 3; i++) {
                    //重试2次
                    try {
                        PageRequest pageRequest = buildPageRequest(pagerInfo, true, dataNos, v.getUuid());

                        pageRequest.setStartTime(q.getKey());
                        pageRequest.setEndTime(q.getValue());
                        pageRequest.setReadMode(dataReadMode);

                        GlobalResponse<PageResult<Map<String, String>>> resultGlobalResponse =
                                findPageByUUID(pageRequest, v.getSupportProtocol());

                        if (resultGlobalResponse != null && resultGlobalResponse.getData() != null
                                && CollectionUtils.isNotEmpty(resultGlobalResponse.getData().getData())) {
                            mulList.addAll(resultGlobalResponse.getData().getData());
                        }

                        break;
                    } catch (Exception e) {
                        log.error("error:", e);
                    }
                }

            });

        });
        executor.shutdown();

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("error:", e);
                Thread.currentThread().interrupt();
            }
        }

        return mulList;
    }

    @Override
    public DataItemDetailModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_data_item_detail", "d_i_d");
        params.put("id", id);
        DataItemDetail entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("对象已不存在");
        }
        return DataItemDetailModel.fromEntry(entry);
    }


    @Override
    public void insert(DataItemDetailModel model) {

    }

    @Override
    public void update(DataItemDetailModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_data_item_detail", "d_i_d");

        DataItemDetail obj = new DataItemDetail();
        BeanUtils.copyProperties(model, obj);
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
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_data_item_detail", "d_i_d");

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

        String ids = getQueryCondition(pagerInfo, "dataNos");
        addQueryCondition(pagerInfo, "isUI", "1");
        List<DataItem> dataItemList = parseDataNos(ids, pagerInfo);
        //不使用分页
        //从第一页导出
        pagerInfo.setStart(0);
        pagerInfo.setLimit(null);
        List<Map<String, String>> datas = convertListResult(pagerInfo, true, "1");

        Map<String, Object> params = new HashMap<>();

        if (datas == null || datas.size() == 0) {
            throw new BusinessException("无数据");
        }
        new ExcelExportHandler<DataItemDetail>(this, "pagerModel", params, "sys/res/dataItemDetail/export.xlsx", "车辆动态信息") {

            @Override
            public void work() {

                String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
                String srcFile = srcBase + this.excelTemplateFile;

                write(srcFile, dataItemList, datas);


            }
        }.work();
        return;


    }

    private static InputStream getInputStream(String srcFile) {
        try {
            InputStream stream = null;
            if (srcFile.contains("!")) {
                if (!srcFile.startsWith("/com")) {
                    int index = srcFile.indexOf("/com");
                    srcFile = srcFile.substring(index);
                }

                stream = ClassLoaderUtil.getResourceAsStream(srcFile);
            } else {
                stream = new FileInputStream(srcFile);
            }

            return (InputStream) stream;
        } catch (Exception var3) {
            log.error("error", var3);
            return null;
        }
    }

    private void write(String fileName, List<DataItem> dataItems, List<Map<String, String>> dataList) {

        File tmp = null;
        try {

            Workbook workbook = EasyExcel.getWorkBook(fileName);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.createRow(0);

            int titleIndex = 0;
            Map<String, String> hasWrite = Maps.newHashMap();
            for (int i = 0; i < dataItems.size(); ++i) {
                if (StringUtils.isNotBlank(dataItems.get(i).getNote()) && dataItems.get(i).getNote().equals("HIDE")) {
                    continue;
                }
                if (dataItems.get(i).getSeqNo().equals(DataItemKey.G6_ITEM_ServerTime)) {
                    continue;
                }
                if (hasWrite.containsKey(dataItems.get(i).getSeqNo())) {
                    continue;
                }
                hasWrite.put(dataItems.get(i).getSeqNo(), "");
                headerRow.createCell(titleIndex++).setCellValue(dataItems.get(i).getName());
            }
            int rowIndex = 1;

            for (Map<String, String> record : dataList) {
                Row fieldRow = sheet.createRow(rowIndex++);
                int headerIndex = 0;
                Map<String, String> writed = Maps.newHashMap();
                for (int col = 0; col < dataItems.size(); col++) {
                    if (StringUtils.isNotBlank(dataItems.get(col).getNote()) && dataItems.get(col).getNote().equals("HIDE")) {
                        continue;
                    }
                    if (dataItems.get(col).getSeqNo().equals(DataItemKey.G6_ITEM_ServerTime)) {
                        continue;
                    }
                    if (writed.containsKey(dataItems.get(col).getSeqNo())) {
                        continue;
                    }
                    writed.put(dataItems.get(col).getSeqNo(), "");
                    String seq = dataItems.get(col).getSeqNo();
                    if (record != null && record.get(seq) != null) {
                        String val = record.getOrDefault(seq, "");
                        fieldRow.createCell(headerIndex++).setCellValue(val);
                    } else {
                        fieldRow.createCell(headerIndex++).setCellValue("");
                    }

                }
            }
            String suffix = fileName.endsWith(".xlsx") ? ".xlsx" :".xls";
            tmp = File.createTempFile("明细数据导出-", suffix);
            @Cleanup FileOutputStream os = new FileOutputStream(tmp);
            workbook.write(os);
            os.close();
            ResponseStreamWriter.flushFile(tmp, ResponseStreamWriter.MimeEnum.XLS);

        } catch (Exception e) {
            log.error("error:", e);
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
        }
    }

    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "DATAITEMDETAIL" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<DataItemDetailModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataItemDetailModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "DATAITEMDETAIL" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<DataItemDetailModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(DataItemDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(DataItemDetailModel model) {
                update(model);
            }
        }.work();

    }

    private final ICodeTypeService codeTypeService;
    private final IRemoteDiagnoseService remoteDiagnoseService;


    /**
     * @param vehicleModel
     * @param dataItemType 1、国标， 2、国六 （默认是国标）
     * @return
     */
    @Override
    public List<Map<String, String>> customFaults(VehicleModel vehicleModel) {
        List<Map<String, String>> result = new ArrayList<>();
        String dataItemsStr = codeTypeService.getAllFaultTypeCode();
        if (StringUtils.isBlank(dataItemsStr)) {
            return result;
        }
        String[] dataItems = dataItemsStr.split(",");
        if (ArrayUtils.isEmpty(dataItems)) {
            return result;
        }
        try {
            DataRequest dataRequest = new DataRequest();
            dataRequest.setVid(vehicleModel.getUuid());
            dataRequest.setReadMode(DataReadMode.NONE);
            dataRequest.setColumns(Arrays.asList(dataItems));
            if (vehicleModel.isG6()) {
                dataRequest.setRuleType(RuleTypeEnum.GB_T17691.getCode());
            } else {
                dataRequest.setRuleType(RuleTypeEnum.GB_T32960.getCode());
            }

            Map<String, String> realMap = realDataClient.findByUuid(dataRequest);
            if (realMap == null || realMap.size() == 0) {
                return result;
            }
            List<Map<String, String>> mapList = remoteDiagnoseService.dealDataMapFast(realMap, dataItems, vehicleModel);
            if (CollectionUtils.isNotEmpty(mapList)) {
                mapList = mapList.stream().map(f -> {
                    Map<String, String> tmpMap = new HashMap<>();
                    f.forEach((k, v) -> {
                        if (k.equals("\"result\"")) {
                            if (v.equals("\"" + SysDefine.DIAGNBOSE_MODE.NORMAL + "\"")) {
                                tmpMap.put("result", "0");
                            } else if (v.equals("\"" + SysDefine.DIAGNBOSE_MODE.FAULT + "\"")) {
                                tmpMap.put("result", "1");
                            } else if (v.equals("\"" + SysDefine.DIAGNBOSE_MODE.EXCEPTION + "\"")) {
                                //异常也算故障
                                tmpMap.put("result", "1");
                            }
                        }
                        if (k.equals("\"faultName\"")) {
                            tmpMap.put("faultName", v.replace("\"", ""));
                        }

                    });
                    return tmpMap;
                }).collect(Collectors.toList());
                result.addAll(mapList);

            }

        } catch (Exception e) {
            log.error("error:", e);
        }
        return result;

    }

    @Override
    public VehicleModel findVehicle(PagerInfo pagerInfo) {

        String queryContent = getQueryCondition(pagerInfo, "queryContent");
        String queryType = getQueryCondition(pagerInfo, "queryType");

        List<String> queryTypes = Arrays.asList("0", "1", "2");
        if (!queryTypes.contains(queryType) || StringUtils.isBlank(queryContent)) {
            throw new BusinessException("请输入查询车辆信息");
        }
        VehicleModel vehicle = null;
        try {


            if ("0".equals(queryType)) {
                vehicle = vehicleService.getByVin(queryContent);
            } else if ("1".equals(queryType)) {
                vehicle = vehicleService.getByInterNo(queryContent);
            } else {
                vehicle = vehicleService.getByLicensePlate(queryContent);
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
        if (vehicle == null) {
            throw new BusinessException("车辆信息输入有误，请检查");
        }
        return vehicle;

    }

    private String getDefaultRuleId() {
        String ruleId = "-1";
        Map<String, String> params = Maps.newHashMap();
        params.put("name", "国标");
        List<Rule> ruleList = ruleService.findBySqlId("pagerModel", params);
        if (CollectionUtils.isEmpty(ruleList)) {
            params.put("name", "32960");
            ruleList = ruleService.findBySqlId("pagerModel", params);
            if (CollectionUtils.isNotEmpty(ruleList)) {
                ruleId = ruleList.get(0).getId();
            }

        } else {
            ruleId = ruleList.get(0).getId();
        }
        return ruleId;
    }

    private List<DataItem> parseDataNos(String dataItemStr, PagerInfo pagerInfo) {

        if (StringUtils.isBlank(dataItemStr) || dataItemStr.trim().equals("-1")) {
            throw new BusinessException("请选择数据项", -1);
        }
        VehicleModel vehicleModel = findVehicle(pagerInfo);
        if (StringUtils.isBlank(vehicleModel.getSupportProtocol())) {
            vehicleModel.setSupportProtocol(getDefaultRuleId());
            if (StringUtils.isBlank(vehicleModel.getSupportProtocol()) || "-1".equals(vehicleModel.getSupportProtocol())) {
                throw new BusinessException("未找到任何协议数据项");
            }
        }

        return getDataNos(dataItemStr, vehicleModel.getSupportProtocol());
    }

    public List<DataItem> getDataNos(String dataItemStr, String ruleId) {
        Map<String, Object> params = new HashMap<>();
        RuleModel ruleModel = ruleService.get(ruleId);
        if (ruleModel == null) {
            throw new BusinessException("数据项协议不能为空");
        }
        List<String> arr = Arrays.stream(dataItemStr.split(",")).collect(Collectors.toList());

        params.put("itemIds", arr);
        params.put("ruleTypeId", ruleModel.getRuleTypeId());
        List<DataItem> result = dataItemService.findBySqlId("getBySeqNos", params);
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(f -> {
                f.setIndex(arr.indexOf(f.getSeqNo()));
            });
            result.sort(new Comparator<DataItem>() {
                @Override
                public int compare(DataItem o1, DataItem o2) {
                    return o1.getIndex() - o2.getIndex();
                }
            });

        }
        //明细数据查询固定显示最后通讯时间
        if (CollectionUtils.isEmpty(result)) {
            result = new ArrayList<>();
        }


        DataItem dataItem = new DataItem();
        dataItem.setId("vin");
        dataItem.setName("VIN");
        dataItem.setSeqNo("vin");
        result.add(0, dataItem);

        dataItem = new DataItem();
        dataItem.setId(DataItemKey.ServerTime);
        dataItem.setName("服务器接收时间");
        dataItem.setSeqNo(DataItemKey.ServerTime);
        result.add(1, dataItem);

        dataItem = new DataItem();
        dataItem.setId(DataItemKey.RtDateTime);
        dataItem.setName("数据采集时间");
        dataItem.setSeqNo(DataItemKey.RtDateTime);
        result.add(2, dataItem);
        if (!arr.contains(DataItemKey.RtDateTime)) {
            //不需要显示数据采集时间
            dataItem.setNote("HIDE");
        }

//        DataItem dataItemG6 = new DataItem();
//        dataItemG6.setId(DataItemKey.G6_ITEM_60011);
//        dataItemG6.setName("报文时间");
//        dataItemG6.setSeqNo(DataItemKey.G6_ITEM_60011);
//        result.add(dataItemG6);
//        if (!arr.contains(DataItemKey.G6_ITEM_60011)) {
//            //不需要显示数据采集时间
//            dataItemG6.setNote("HIDE");
//        }

        DataItem lastUpdateTime = new DataItem();
        lastUpdateTime.setId(DataItemKey.G6_ITEM_ServerTime);
        lastUpdateTime.setName("数据采集时间");
        lastUpdateTime.setSeqNo(DataItemKey.G6_ITEM_ServerTime);
        result.add(lastUpdateTime);

        DataItem hasQudong = result.stream().filter(f -> !f.getSeqNo().equals(DataItemKey.item2307) &&
                !f.getSeqNo().equals(DataItemKey.ChargeState2301) &&
                f.getSeqNo().startsWith("23")).findAny().orElse(null);
        if (null != hasQudong) {
            //需要读取2308驱动电机总成数据项
            DataItem item2308 = result.stream().filter(f -> f.getSeqNo().equals(DataItemKey.item2308)).findAny().orElse(null);
            if (null == item2308) {
                DataItem di = new DataItem();
                di.setId(DataItemKey.item2308);
                di.setName("驱动电机列表");
                di.setSeqNo(DataItemKey.item2308);
                result.add(di);
                if (!arr.contains(DataItemKey.item2308)) {
                    //不需要显示驱动电机列表
                    di.setNote("HIDE");
                }
            }
        }
        return result;

    }

    @Override
    public List<DataItem> parseDataNos(String dataItemStr) {
        List<DataItem> resultList;
        if (StringUtils.isBlank(dataItemStr) || dataItemStr.trim().equals("-1")) {
            throw new BusinessException("请选择数据项", -1);


        } else if (dataItemStr.trim().startsWith("@ruleId@")) {
            //某辆车关联的数据项
            dataItemStr = dataItemStr.replace("@ruleId@", "");
            if (StringUtils.isBlank(dataItemStr)) {
                dataItemStr = getDefaultRuleId();
            }

            List<Map<String, String>> dataItemList = findDataItemListByRule(dataItemStr, null);

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                resultList = dataItemList.stream().map(q -> {
                    DataItem item = new DataItem();
                    item.setId(q.get("id"));
                    item.setName(q.get("name"));
                    item.setSeqNo(q.get("seq_no"));
                    return item;
                }).collect(Collectors.toList());
            } else {
                throw new BusinessException("车辆未关联协议数据项", -1);
            }
            if (CollectionUtils.isNotEmpty(resultList)) {
                DataItem dataItem = new DataItem();
                dataItem.setId("vin");
                dataItem.setName("VIN");
                dataItem.setSeqNo("vin");
                resultList.add(0, dataItem);

                dataItem = new DataItem();
                dataItem.setId(DataItemKey.ServerTime);
                dataItem.setName("服务器接收时间");
                dataItem.setSeqNo(DataItemKey.ServerTime);
                resultList.add(1, dataItem);

                dataItem = new DataItem();
                dataItem.setId(DataItemKey.RtDateTime);
                dataItem.setName("数据采集时间");
                dataItem.setSeqNo(DataItemKey.RtDateTime);
                resultList.add(2, dataItem);

                DataItem lastUpdateTime = new DataItem();
                lastUpdateTime.setId(DataItemKey.G6_ITEM_ServerTime);
                lastUpdateTime.setName("最后更新时间");
                lastUpdateTime.setSeqNo(DataItemKey.G6_ITEM_ServerTime);
                resultList.add(lastUpdateTime);


            }


        } else if (dataItemStr.startsWith("@seq@")) {

            dataItemStr = dataItemStr.replace("@seq@", "");
            Map<String, String> seqMap = new HashMap<>();
            final List<DataItem> list = new ArrayList<>();
            Arrays.stream(dataItemStr.split(",")).forEach(f -> {
                if (seqMap.containsKey(f)) {
                    return;
                }
                seqMap.put(f, "");
                DataItem dataItem = new DataItem();
                dataItem.setSeqNo(f);
                dataItem.setName(f);
                list.add(dataItem);

            });
            resultList = list;
        } else {
            //由数据项id获取数据项
            Map<String, Object> params = new HashMap<>();
            List<String> arr = Arrays.stream(dataItemStr.split(",")).collect(Collectors.toList());
            params.put("itemIds", arr);

            resultList = dataItemService.findBySqlId("getByIds", params);
            if (CollectionUtils.isNotEmpty(resultList)) {
                resultList.forEach(f -> {
                    f.setIndex(arr.indexOf(f.getSeqNo()));
                });
                resultList.sort(new Comparator<DataItem>() {
                    @Override
                    public int compare(DataItem o1, DataItem o2) {
                        return o1.getIndex() - o2.getIndex();
                    }
                });

            }
            if (CollectionUtils.isEmpty(resultList)) {
                resultList = new ArrayList<>();
            }
            DataItem dataItem = new DataItem();
            dataItem.setId(DataItemKey.RtDateTime);
            dataItem.setName("数据采集时间");
            dataItem.setSeqNo(DataItemKey.RtDateTime);
            resultList.add(dataItem);

            DataItem lastUpdateTime = new DataItem();
            lastUpdateTime.setId(DataItemKey.G6_ITEM_ServerTime);
            lastUpdateTime.setName("数据采集时间");
            lastUpdateTime.setSeqNo(DataItemKey.G6_ITEM_ServerTime);
            resultList.add(lastUpdateTime);

            DataItem g6UpdateTime = new DataItem();
            g6UpdateTime.setId(DataItemKey.ServerTime);
            g6UpdateTime.setName("数据采集时间");
            g6UpdateTime.setSeqNo(DataItemKey.ServerTime);
            resultList.add(g6UpdateTime);

        }
        DataItem hasQudong = resultList.stream().filter(f -> !f.getSeqNo().equals(DataItemKey.item2307) &&
                !f.getSeqNo().equals(DataItemKey.ChargeState2301) &&
                f.getSeqNo().startsWith("23")).findAny().orElse(null);
        if (null != hasQudong) {
            //需要读取2308驱动电机总成数据项
            DataItem item2308 = resultList.stream().filter(f -> f.getSeqNo().equals(DataItemKey.item2308)).findAny().orElse(null);
            if (null == item2308) {
                DataItem dataItem = new DataItem();
                dataItem.setId(DataItemKey.item2308);
                dataItem.setName("驱动电机列表");
                dataItem.setSeqNo(DataItemKey.item2308);
                resultList.add(dataItem);
            }
        }
        return resultList;
    }

    private boolean isWarnDataItem(String key) {
        return (key.startsWith("29") || key.startsWith("3801") || key.startsWith("2804")
                || key.startsWith("2805") || key.startsWith("2808") || key.startsWith("2809")
                || key.startsWith("2801") || key.startsWith("2802") || key.startsWith("2803")
                || key.startsWith("2806") || key.startsWith("2807")
        );
    }

    /**
     * 去掉数据项中的.desc翻译项
     *
     * @param datas     数据项
     * @param keyMapper 转换数据项编码
     * @return
     */
    @Override
    public List<Map<String, String>> dataItemsSlimming(
            List<Map<String, String>> datas,
            Map<String, String> keyMapper,
            boolean translate, boolean asc,
            boolean needTermTime) {
        if (CollectionUtils.isNotEmpty(datas) && datas.size() == 1) {
            Map<String, String> tmp = datas.get(0);
            datas = new ArrayList<>();
            datas.add(tmp);
        }
        datas.removeIf(f -> !f.containsKey(DataItemKey.RtDateTime));
        if (CollectionUtils.isNotEmpty(datas)){
            //TODO 测试需要
            datas.forEach(d->{
               String item2308= d.get("2308");
               if (StringUtils.isNotBlank(item2308)){
                   d.put("2308",item2308+"|"+item2308);
               }
            });
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Stream<Map<String, String>> ret =
                datas.stream().map(m -> {
                    Map<String, String> result = new HashMap<>();
                    Map<String, String> tmpMap = Maps.newHashMap();
                    if (m.get(DataItemKey.ServerTime) != null && !m.get(DataItemKey.ServerTime).contains("-")) {
                        try {
                            m.put(DataItemKey.ServerTime, format1.format(format2.parse(m.get(DataItemKey.ServerTime))));
                        } catch (Exception e) {

                        }
                    }
                    m.forEach((key, value) -> {

                        if (key.endsWith(".desc")) {
                            return;
                        }
                        String descKey = key + ".desc";
                        if (StringUtils.isNotBlank(m.get(descKey))) {
                            if (isWarnDataItem(key)) {
                                if (translate) {
                                    tmpMap.put(key, m.get(descKey));
                                }
                            } else {
                                tmpMap.put(key, m.get(descKey));
                            }


                        }

                        String newKey = key;
                        if (keyMapper != null && StringUtils.isNotBlank(keyMapper.get(key))) {
                            newKey = keyMapper.get(key);
                        }
                        if (key.equals(DataItemKey.item2307) || key.equals(DataItemKey.item2308)
                                || key.equals(DataItemKey.ChargeState2301) || !key.startsWith("23")) {

                            result.put(newKey, StringUtils.isNotBlank(tmpMap.get(key)) ? tmpMap.get(key) : value);

                        }
                        if (StringUtils.isNotBlank(value) && DataItemKey.item2308.equals(key)) {
                            //特殊处理2308驱动电机总成数据项
                            String[] motorCount = value.split("\\|");
                            for (String motor : motorCount) {
                                String[] itemPairs = motor.split(",");
                                Map<String, String> map = Stream.of(itemPairs).collect(Collectors.toMap(k -> k.split(":")[0], k -> k.split(":")[1]));
                                for (String itemPair : itemPairs) {
                                    String[] kv = itemPair.split(":");
                                    if (kv.length != 2 || kv[0].endsWith(".desc")) {
                                        continue;
                                    }
                                    String val = kv[1];
                                    if (map != null && StringUtils.isNotBlank(map.get(kv[0] + ".desc"))) {
                                        val = map.get(kv[0] + ".desc");
                                    }
                                    String newK = kv[0];
                                    if (keyMapper != null && keyMapper.containsKey(kv[0])) {
                                        newK = keyMapper.get(kv[0]);
                                    }
                                    result.put(newK, val);

                                }
                            }
                        }

                    });
                    if (!needTermTime) {
                        if (m.containsKey(DataItemKey.ServerTime) && StringUtils.isNotBlank(m.get(DataItemKey.ServerTime))) {
                            result.put(DataItemKey.RtDateTime, m.get(DataItemKey.ServerTime));
                        }
                    }


                    return result;

                });


        return asc ? ret.sorted(Comparator.comparing(k -> StringUtils.isBlank(k.get(DataItemKey.RtDateTime)) ? "2000" : k.get(DataItemKey.RtDateTime))).collect(Collectors.toList()) :
                ret.sorted(Comparator.comparing(k -> StringUtils.isBlank(((Map<String, String>) k).get(DataItemKey.RtDateTime)) ? "2000" : ((Map<String, String>) k).get(DataItemKey.RtDateTime)).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> G6DataItemsSlimming(
            List<Map<String, String>> datas,
            Map<String, String> keyMapper,
            boolean translate,
            boolean asc,
            boolean needTermTime) {
        if (CollectionUtils.isNotEmpty(datas) && datas.size() == 1) {
            Map<String, String> tmp = datas.get(0);
            datas = new ArrayList<>();
            datas.add(tmp);
        }
        datas.removeIf(f -> !f.containsKey(DataItemKey.RtDateTime));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Stream<Map<String, String>> ret =
                datas.stream().map(m -> {
                    Map<String, String> result = new HashMap<>();
                    Map<String, String> tmpMap = Maps.newHashMap();
                    if (m.get(DataItemKey.G6_ITEM_ServerTime) != null && !m.get(DataItemKey.G6_ITEM_ServerTime).contains("-")) {
                        try {
                            m.put(DataItemKey.G6_ITEM_ServerTime, format1.format(format2.parse(m.get(DataItemKey.G6_ITEM_ServerTime))));
                        } catch (Exception e) {

                        }
                    }
                    if (m.containsKey(DataItemKey.RtDateTime) && StringUtils.isNotBlank(m.get(DataItemKey.RtDateTime))) {
                        String timeVal = m.get(DataItemKey.RtDateTime);
                        if (!timeVal.contains("-")) {
                            //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                            timeVal = DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                        }
                        m.put(DataItemKey.RtDateTime, timeVal);
                    }
                    if (m.containsKey(DataItemKey.item1025) && StringUtils.isNotBlank(m.get(DataItemKey.item1025))) {
                        String timeVal = m.get(DataItemKey.item1025);
                        if (!timeVal.contains("-")) {
                            //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                            timeVal = DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                        }
                        m.put(DataItemKey.item1025, timeVal);
                    }
                    if (m.containsKey(DataItemKey.item1031) && StringUtils.isNotBlank(m.get(DataItemKey.item1031))) {
                        String timeVal = m.get(DataItemKey.item1031);
                        if (!timeVal.contains("-")) {
                            //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                            timeVal = DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                        }
                        m.put(DataItemKey.item1031, timeVal);
                    }
                    if (m.containsKey(DataItemKey.G6_ITEM_60050) && StringUtils.isNotBlank(m.get(DataItemKey.G6_ITEM_60050))) {
                        String timeVal = m.get(DataItemKey.G6_ITEM_60050);
                        if (!timeVal.contains("-")) {
                            //说明时间格式不是2019-07-15 23:59:59 ,需要转换一下
                            timeVal = DateUtil.formatTime(DateUtil.strToDate_ex(timeVal), DateUtil.FULL_ST_FORMAT);
                        }
                        m.put(DataItemKey.G6_ITEM_60050, timeVal);
                    }
                    m.forEach((key, value) -> {

                        if (key.endsWith(".desc")) {
                            return;
                        }
                        String descKey = key + ".desc";
                        if (StringUtils.isNotBlank(m.get(descKey))) {
                            tmpMap.put(key, m.get(descKey));

                        }

                        String extraKey = g6ToGb32960(key);
                        if (StringUtils.isNotBlank(extraKey)) {
                            result.put(extraKey, StringUtils.isNotBlank(tmpMap.get(key)) ? tmpMap.get(key) : value);
                        }
                        String newKey = key;
                        if (keyMapper != null && StringUtils.isNotBlank(keyMapper.get(key))) {
                            newKey = keyMapper.get(key);
                        }
                        result.put(newKey, StringUtils.isNotBlank(tmpMap.get(key)) ? tmpMap.get(key) : value);


                    });
                    if (!needTermTime) {
                        if (m.containsKey(DataItemKey.G6_ITEM_ServerTime) && StringUtils.isNotBlank(m.get(DataItemKey.G6_ITEM_ServerTime))) {
                            //实时数据中使用服务器接收时间代表终端采集时间
                            String timeVal = m.get(DataItemKey.G6_ITEM_ServerTime);

                            result.put(DataItemKey.RtDateTime, timeVal);

                        }
                    }
                    if (StringUtils.isNotBlank(m.get(DataItemKey.G6_ITEM_60041))) {
                        double lng = Double.valueOf(m.get(DataItemKey.G6_ITEM_60041));
                        if (lng < 1) {
                            result.put(DataItemKey.Lng, String.valueOf(lng * 1000000));

                        }
                    }
                    if (StringUtils.isNotBlank(m.get(DataItemKey.G6_ITEM_60042))) {
                        double lat = Double.valueOf(m.get(DataItemKey.G6_ITEM_60042));
                        if (lat < 1) {
                            result.put(DataItemKey.Lat, String.valueOf(lat * 1000000));

                        }
                    }

                    return result;

                });

        final String time = DataItemKey.G6_ITEM_60011;


        return asc ? ret.sorted(Comparator.comparing(k -> k.get(time))).collect(Collectors.toList()) :
                ret.sorted(Comparator.comparing(k -> ((Map<String, String>) k).get(time)).reversed()).collect(Collectors.toList());
    }

    String g6ToGb32960(String seqNo) {
        Map<String, String> mapper = Maps.newHashMap();

        mapper.put(DataItemKey.ServerTime, DataItemKey.ServerTime);
        mapper.put(DataItemKey.G6_ITEM_ServerTime, DataItemKey.ServerTime);
        //mapper.put(DataItemKey.G6_ITEM_60041+".gd",DataItemKey.Lng+".gd");
        // mapper.put(DataItemKey.G6_ITEM_60042+".gd",DataItemKey.Lat+".gd");
        // mapper.put(DataItemKey.G6_ITEM_60025,DataItemKey.VehicleSpeed);
        // mapper.put(DataItemKey.G6_ITEM_60043,DataItemKey.VehicleDistance);
        //mapper.put(DataItemKey.G6_ITEM_60040,DataItemKey.LocateState);


        if (!mapper.containsKey(seqNo)) {
            return null;
        }
        return mapper.get(seqNo);
    }

    @Getter
    @Setter
    class TreeNodeEx extends TreeNode {

        private String seqNo;
        //用于单车监控面板中Tab的显示顺序
        private double index;


    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    DataItemGroup findDataItemGroup(String code, String rtId, List<Map<String, String>> modells) {
        DataItemGroup dataItemGroup = new DataItemGroup();
        dataItemGroup.setId("-1");
        Map<String, String> model = modells.stream().filter(f -> f.get("isGroup").equals("1") && f.get("seq_no").contains(code) && f.get("parent_id").equals(rtId)).findFirst().orElse(null);
        if (null != model) {
            dataItemGroup.setId(model.get("id"));
        }
        return dataItemGroup;
    }

    boolean isGT32960DataItem(String seq_no) {
        if (StringUtils.isBlank(seq_no)) {
            return true;
        }
        Map<String, String> gb = Maps.newHashMap();
        gb.put("7615", "");
        gb.put("7001", "");
        gb.put("7002", "");
        gb.put("7003", "");
        gb.put("7101", "");
        gb.put("7102", "");
        gb.put("7103", "");
        if (gb.containsKey(seq_no)) {
            return true;
        }
        String tmp = seq_no.replace("0x", "").replace("0X", "");
        if (!StringUtils.isNumeric(tmp)) {
            return false;
        }
        if (StringUtils.isNumeric(tmp) && Integer.valueOf(tmp) < 4000) {
            return true;
        }
        if (StringUtils.isNumeric(tmp) && tmp.startsWith("600")) {
            return true;
        }
        return false;
    }

    @Override
    public Object findDataItemTreeByRuleId(PagerInfo pagerInfo) {
        VehicleModel vehicleMode = findVehicle(pagerInfo);

        if (StringUtils.isBlank(vehicleMode.getSupportProtocol())) {
            TreeNode node = new TreeNode();
            return node;
//            String ruleId = getDefaultRuleId();
//            if (StringUtils.isBlank(ruleId) || "-1".equals(ruleId)) {
//                throw new BusinessException("车辆未关联协议");
//            }
//            vehicleMode.setSupportProtocol(ruleId);
        }

        return getDataItem(pagerInfo, vehicleMode.getSupportProtocol());
    }

    public Object getDataItem(PagerInfo pagerInfo, String ruleId) {
        String groupName = getQueryCondition(pagerInfo, "changeFaultGroupName");

        List<Map<String, String>> models = findBySqlId("findTreeList", ruleId);

        if (CollectionUtils.isNotEmpty(models)) {
            //去掉重复数据项
            models = models.stream().filter(distinctByKey(q -> q.get("id"))).collect(Collectors.toList());


        }
        if (StringUtils.isNotBlank(groupName) && models != null) {
            //这几个作为自定义故障,单独处理
            List<String> notShow = Stream.of("3801", "2921", "2922", "2804", "2805", "2923", "2924", "2808", "2809", "2801", "2802", "2803", "2806", "2807")
                    .collect(Collectors.toList());

            models = models.stream().filter(f -> !notShow.contains(f.get("seq_no"))).collect(Collectors.toList());

        }

        Map<String, String> param = new HashMap<>();
        param.put("code", "02");
        param.put("name", "实时数据");
        RuleModel rule = ruleService.get(ruleId);

        param.put("ruleTypeId", rule.getRuleTypeId());
        DataItemGroup rtModel = dataItemGroupService.unique("searchByCode", param);

        param.remove("name");
        String rtId = "-1";
        if (null != rtModel) {
            rtId = rtModel.getId();
        }
        //储能电压
        param.put("code", "08");
        DataItemGroup vModel = findDataItemGroup("08", rtId, models); // dataItemGroupService.unique("searchByCode", param);

        //储能温度
        param.put("code", "09");
        DataItemGroup tModel = findDataItemGroup("09", rtId, models); //dataItemGroupService.unique("searchByCode", param);

        //报警数据
        param.put("code", "07");
        DataItemGroup faultModel = findDataItemGroup("07", rtId, models);// dataItemGroupService.unique("searchByCode", param);

        //极值数据
        param.put("code", "06");
        DataItemGroup maxModel = findDataItemGroup("06", rtId, models); //dataItemGroupService.unique("searchByCode", param);
        Map<String, String> nullGroup = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(models)) {

            Map<String, Map<String, String>> cache = Maps.newHashMap();
            final List<Map<String, String>> modelsTmp = models;
            if (models == null){
                return null;
            }
            Map<String, Map<String, String>> all = models.stream().collect(Collectors.toMap(k -> k.get("id"), v -> v));
            models.forEach(model -> {
                String parentId = model.get("parent_id");
                if (parentId == null || parentId.equals("0") || (rtModel != null && parentId.equals(rtModel.getId()))) {
                    return;
                }
                // Map<String, String> exist = modelsTmp.stream().filter(f -> f.get("id").equals(parentId)).findAny().orElse(null);
                boolean exist = all.containsKey(parentId);
                if (!exist) {

                    if (!cache.containsKey(parentId)) {
                        DataItemGroupModel dataItemGroupModel = dataItemGroupService.get(parentId);
                        if (dataItemGroupModel != null) {
                            Map<String, String> groupMap = Maps.newHashMap();
                            groupMap.put("id", parentId);
                            groupMap.put("name", dataItemGroupModel.getName());
                            groupMap.put("seq_no", dataItemGroupModel.getCode());
                            groupMap.put("parent_id", "0");
                            cache.put(parentId, groupMap);

                        } else {
                            cache.put(parentId, null);
                        }
                    }

                }


            });

            if (cache.size() > 0) {
                cache.forEach((k, v) -> {
                    if (v != null) {
                        modelsTmp.add(v);
                    }
                });
            }

            final Map<String, Map<String, String>> full = models.stream().collect(Collectors.toMap(k -> k.get("id"), v -> v));

            String remove = getQueryCondition(pagerInfo, "removeParentGroup");
            if (StringUtils.isNotBlank(remove)) {
                models.forEach(model -> {
                    if ("1".equals(model.get("isGroup"))) {
                        return;
                    }
                    if (isGT32960DataItem(model.get("seq_no"))) {
                        return;
                    }
                    Map<String, String> current = findFinalParent(model, full);
                    if (null == current) {
                        return;
                    }
                    if (!current.get("id").equals(model.get("id"))) {
                        if (!nullGroup.containsKey(model.get("parent_id"))) {
                            nullGroup.put(model.get("parent_id"), "");
                        }
                        model.put("parent_id", current.get("id"));

                    }

                });
                //去除空的数据项组
                nullGroup.forEach((k, v) -> {
                    if (full.containsKey(k)) {
                        full.get(k).put("parent_id", "0");
                    }
                });
            }
        }

        Object[] flag = new Object[1];
        TreeNode root = new TreeHandler<Map<String, String>>(models) {

            @Override
            protected TreeNode beanToTreeNode(Map<String, String> bean) {
                TreeNodeEx tn = new TreeNodeEx();
                tn.setId(bean.get("id"));
                tn.setParentId(bean.get("parent_id"));
                tn.setSeqNo(bean.get("seq_no").replace("0x", "").replace("0X", ""));
                tn.setLabel(bean.get("name"));
                if (tn.getLabel().contains("自定义")) {
                    //自定义数据项
                    tn.setSeqNo("03_01");
                }
                if (rtModel != null && tn.getParentId() != null && tn.getParentId().equals(rtModel.getId())) {
                    //去掉实时数据这一层
                    tn.setParentId("0");

                    if (tn.getSeqNo().equals("01")) {
                        //整车
                        tn.setSeqNo("02_01");
                        if (flag[0] == null) {
                            TreeNodeEx treeNodeEx = new TreeNodeEx();
                            treeNodeEx.setSeqNo(DataItemKey.RtDateTime);
                            treeNodeEx.setLabel("数据采集时间");
                            treeNodeEx.setId(DataItemKey.RtDateTime);
                            treeNodeEx.setParentId(tn.getId());
                            tn.getChildren().add(treeNodeEx);
                        }
                    }
                    if (tn.getSeqNo().equals("04")) {
                        //发动机数据
                        tn.setSeqNo("02_04");
                    }
                    if (tn.getSeqNo().equals("02")) {
                        //驱动电机数据
                        tn.setSeqNo("02_02");
                    }
                    if (tn.getSeqNo().equals("03")) {
                        //燃料电池数据
                        tn.setSeqNo("02_03");
                    }
                    if (tn.getSeqNo().equals("07")) {
                        //报警数据
                        tn.setSeqNo("02_07");
                    }
                    if (tn.getSeqNo().equals("05")) {
                        //位置数据
                        tn.setSeqNo("02_05");
                    }
                    if (tn.getSeqNo().equals("06")) {
                        //极值数据
                        tn.setSeqNo("02_06");
                    }

                }
                if (vModel != null && tn.getId() != null && tn.getId().equals(vModel.getId())) {
                    tn.setLabel("可充电储能装置");
                    tn.setSeqNo("02_08");
                }
                if (faultModel != null && tn.getId() != null && tn.getId().equals(faultModel.getId())) {
                    if (StringUtils.isNotBlank(groupName)) {
                        //tn.setLabel(groupName);
                    }

                    tn.setSeqNo("02_07");
                }
                if (maxModel != null && tn.getId() != null && tn.getId().equals(maxModel.getId())) {
                    tn.setLabel("极值数据");
                    tn.setSeqNo("02_06");
                }
                if (tModel != null && tn.getParentId() != null && tn.getParentId().equals(tModel.getId())) {
                    if (vModel != null) {
                        //合并可充电储能装置电压/温度数据项
                        tn.setParentId(vModel.getId());
                    }
                }

                return tn;
            }

            @Override
            protected boolean isRoot(TreeNode treeNode) {
                return "0".equals(treeNode.getId());
            }
        }.toTree();

        if (CollectionUtils.isNotEmpty(root.getChildren())) {
            //排序数据项组
            Map<String, Double> orderMap = Maps.newHashMap();
            orderMap.put("02_01", 1d);//整车
            orderMap.put("02_08", 2d);//可充电储能

            orderMap.put("02_04", 3d);//发动机数据
            orderMap.put("02_02", 4d); //驱动电机
            orderMap.put("02_03", 4.1);//燃料电池
            orderMap.put("02_07", 5d);//报警
            orderMap.put("02_05", 6d);//定位
            orderMap.put("02_06", 7d);//极值
            orderMap.put("01", 8d);//车辆登入
            orderMap.put("04", 8.1d);//车辆登出
            orderMap.put("03_01", 9d);//自定义数据项
            orderMap.put("05", 10d);//平台登入
            orderMap.put("06", 11d);//平台登出

            //去掉储能温度这个数据项组
            root.getChildren().removeIf(dataItemGroup -> dataItemGroup.getId() != null && tModel != null && dataItemGroup.getId().equals(tModel.getId()));

            root.getChildren().removeIf(dataItemGroup -> CollectionUtils.isEmpty(dataItemGroup.getChildren()));
            root.getChildren().removeIf(dataItemGroup ->dataItemGroup.getLabel().startsWith("平台"));

            List<TreeNode> treeNodeExList = root.getChildren().stream().filter(d -> ((TreeNodeEx) d).getSeqNo().equals("02_02")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(treeNodeExList)) {
                if (CollectionUtils.isNotEmpty(treeNodeExList.get(0).getChildren())) {
                    //移除驱动电机总成信息字段
                    treeNodeExList.get(0).getChildren().removeIf(i -> ((TreeNodeEx) i).getSeqNo().equals(DataItemKey.item2308));
                }

            }
            root.getChildren().forEach(dataItemGroup -> {
                if (orderMap.containsKey(((TreeNodeEx) dataItemGroup).getSeqNo())) {
                    ((TreeNodeEx) dataItemGroup).setIndex(orderMap.get(((TreeNodeEx) dataItemGroup).getSeqNo()));
                } else {
                    ((TreeNodeEx) dataItemGroup).setIndex(Byte.MAX_VALUE);
                }
                if (CollectionUtils.isNotEmpty(dataItemGroup.getChildren())) {
                    //面板Tab下面的数据项按照数据项编号排序
                    dataItemGroup.setChildren(dataItemGroup.getChildren().stream().sorted(Comparator.comparing(i -> ((TreeNodeEx) i).getSeqNo())).collect(Collectors.toList()));

                }

            });
            root.setChildren(root.getChildren().stream().sorted(Comparator.comparing(i -> ((TreeNodeEx) i).getIndex())).collect(Collectors.toList()));

        }
        return root;

    }

    @Override
    public List<Map<String, String>> findDataItemListByRuleId(PagerInfo pagerInfo) {
        VehicleModel vehicleMode = findVehicle(pagerInfo);
        String groupName = getQueryCondition(pagerInfo, "groupName");
        return findDataItemListByRule(vehicleMode.getSupportProtocol(), groupName);
    }

    private List<Map<String, String>> findDataItemListByRule(String ruleId, String dataItemGroupName) {

        if (StringUtils.isBlank(ruleId)) {
            //默认使用国标
            ruleId = getDefaultRuleId();
        }
        if (StringUtils.isBlank(ruleId) || "-1".equals(ruleId)) {
            throw new BusinessException("车辆未关联有效数据项");
        }
        Map<String, String> param = new HashMap<>();
        param.put("ruleId", ruleId);
        param.put("groupName", dataItemGroupName);
        return findBySqlId("findDataItemList", param);
    }

    private final IOfflineExportService offlineExportService;

    PageRequest buildPageRequest(PagerInfo pagerInfo, boolean skipPage,
                                 List<DataItem> dataNos, VehicleModel v) {

        int size = 50;
        if (pagerInfo.getLimit() != null && pagerInfo.getLimit() > 0) {
            size = pagerInfo.getLimit();
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setAsc(true);

        pageRequest.setColumns(dataNos.stream().map(DataItem::getSeqNo).collect(Collectors.toList()));
        String beginTimeStr = getQueryCondition(pagerInfo, "beginTime");
        String endTimeStr = getQueryCondition(pagerInfo, "endTime");
        pageRequest.setStartTime(beginTimeStr);
        pageRequest.setEndTime(endTimeStr);
        pageRequest.setPageNo(pagerInfo.getStart() / size + 1);
        pageRequest.setSkipPage(skipPage);
        VehicleModel vehicle = findVehicle(pagerInfo);
        BeanUtils.copyProperties(vehicle, v);
        pageRequest.setVid(vehicle.getUuid());

        if (!isPager(pagerInfo)) {
            //全部数据
            pageRequest.setPageSize(Integer.MAX_VALUE);
            pageRequest.setGetCount(false);
        } else {
            //分页
            pageRequest.setPageSize(pagerInfo.getLimit());
            pageRequest.setGetCount(true);
        }
        return pageRequest;
    }

    PageRequest buildPageRequest(PagerInfo pagerInfo, boolean skipPage, List<DataItem> dataNos, String uuid) {

        int size = 50;
        if (pagerInfo.getLimit() != null && pagerInfo.getLimit() > 0) {
            size = pagerInfo.getLimit();
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setAsc(true);

        pageRequest.setColumns(dataNos.stream().map(DataItem::getSeqNo).collect(Collectors.toList()));
        String beginTimeStr = getQueryCondition(pagerInfo, "beginTime");
        String endTimeStr = getQueryCondition(pagerInfo, "endTime");
        pageRequest.setStartTime(beginTimeStr);
        pageRequest.setEndTime(endTimeStr);
        pageRequest.setPageNo(pagerInfo.getStart() / size + 1);
        pageRequest.setSkipPage(skipPage);

        pageRequest.setVid(uuid);

        if (!isPager(pagerInfo)) {
            //全部数据
            pageRequest.setPageSize(Integer.MAX_VALUE);
            pageRequest.setGetCount(false);
        } else {
            //分页
            pageRequest.setPageSize(pagerInfo.getLimit());
            pageRequest.setGetCount(true);
        }
        return pageRequest;
    }

    @Override
    public void exportOffline(PagerInfo pagerInfo) {
        //校验参数begin
        addQueryCondition(pagerInfo, "isUI", "1");
        List<DataItem> dataNos = getDataItemArray(pagerInfo);

        //离线下载方式最多下载30天
        addQueryCondition(pagerInfo, "limitDay", "30");
        // List<AbstractMap.SimpleEntry<String, String>> dateSpan = getDateSpan(pagerInfo);
        checkDate(pagerInfo);
        VehicleModel v = findVehicle(pagerInfo);
        //校验参数end

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";

        // 透传给回调方法的第 3 个参数
        final String exportFilePrefixName = "数据项明细导出";

        // 透传给回调方法的第 4 个参数, 如果是非字符串, 需要序列化一下.
        final String exportMethodParams = JSON.toJSONString(pagerInfo);

        offlineExportService.createTask(
                exportFilePrefixName,
                exportServiceName,
                exportMethodName,
                exportMethodParams
        );
    }

    @Override
    public void exportOfflineProcessor(
            @NotNull final String taskId,
            @NotNull final String createBy,
            @NotNull final Date createTime,
            @NotNull final String exportFileName,
            @NotNull final String exportMethodParams) throws Exception {

        final PagerInfo pagerInfo = JSON.parseObject(exportMethodParams, PagerInfo.class);

        addQueryCondition(pagerInfo, "isUI", "1");
        final List<DataItem> dataNos = getDataItemArray(pagerInfo);

        Map<String, String> map = dataNos.stream().collect(Collectors.toMap(DataItem::getSeqNo, DataItem::getSeqNo));


        Integer dataReadMode = DataReadMode.TRANSLATE;
        String readMode = getQueryCondition(pagerInfo, "dataReadMode");
        if (StringUtils.isNotBlank(readMode)) {
            dataReadMode = Integer.valueOf(readMode);
        }
        final Integer mode = dataReadMode;
        VehicleModel v = findVehicle(pagerInfo);
        Map<String, Object> param = Maps.newHashMap();
        param.put("supportProtocol", v.getSupportProtocol());
        boolean isG6 = isG6(param);
        List<String> titleList = new ArrayList<>();
        Map<String, String> writed = Maps.newHashMap();
        dataNos.forEach(data -> {
            if (StringUtils.isNotBlank(data.getNote()) && data.getNote().equals("HIDE")) {
                return;
            }
            if (data.getSeqNo().equals(DataItemKey.G6_ITEM_ServerTime)) {
                return;
            }
            if (writed.containsKey(data.getSeqNo())) {
                return;
            }
            writed.put(data.getSeqNo(), "");
            titleList.add(data.getName());
        });
        new AbstractOfflineExportCsvHandler() {
            @Override
            protected long writeBody(final @NotNull OutputStreamWriter writer) throws IOException {
                long totalCount = 0L;

                VehicleModel vehicleModel = new VehicleModel();
                // 分页查询条件
                final PageRequest pageRequest = buildPageRequest(pagerInfo, false, dataNos, vehicleModel);
                // 查询首页
                pageRequest.setNext(null);
                // 获取总条数
                pageRequest.setGetCount(true);
                // 每页记录数
                pageRequest.setPageSize(5000);
                pageRequest.setReadMode(mode);

                // 获取明细数据项
                GlobalResponse<PageResult<Map<String, String>>> resultGlobalResponse =
                        findPageByUUID(pageRequest, vehicleModel.getSupportProtocol());

                final long amount;
                if (resultGlobalResponse != null
                        && resultGlobalResponse.getData() != null) {
                    amount = resultGlobalResponse.getData().getTotalCount();
                } else {
                    amount = 0L;
                }

                // 不获取总条数
                pageRequest.setGetCount(false);
                String vin = getQueryCondition(pagerInfo, "queryContent");
                do {
                    if (resultGlobalResponse != null
                            && resultGlobalResponse.getData() != null
                            && CollectionUtils.isNotEmpty(resultGlobalResponse.getData().getData())) {

                        // 去掉数据项中的.desc翻译项
                        List<Map<String, String>> slimmedDataItems = Lists.newArrayList();

                        if (!isG6) {

                            slimmedDataItems = dataItemsSlimming(resultGlobalResponse.getData().getData(), map,
                                    true, true, true);
                        } else {

                            slimmedDataItems = G6DataItemsSlimming(resultGlobalResponse.getData().getData(), map,
                                    true, true, true);

                        }
                        slimmedDataItems.forEach(e -> e.put("vin", vin));
                        for (Map<String, String> f : slimmedDataItems) {
                            List<String> valueList = new ArrayList<>();
                            Map<String, String> writed = Maps.newHashMap();
                            dataNos.forEach(seq -> {
                                if (StringUtils.isNotBlank(seq.getNote()) && seq.getNote().equals("HIDE")) {
                                    return;
                                }
                                if (seq.getSeqNo().equals(DataItemKey.G6_ITEM_ServerTime)) {
                                    return;
                                }
                                if (writed.containsKey(seq.getSeqNo())) {
                                    return;
                                }
                                writed.put(seq.getSeqNo(), "");
                                String val = "";
                                if (f != null) {
                                    val = f.getOrDefault(seq.getSeqNo(), "");
                                    valueList.add(CsvUtil.encode(val));
                                }

                            });

                            try {
                                totalCount += 1;
                                super.writeBody(writer, Collections.singletonList(valueList));
                            } catch (IOException ignore) {
                                log.error("离线导出 回调异常[{}]", pageRequest.getVid());
                            }
                        }

                        final long progress = OfflineExportProgress.computeProgress(totalCount, amount);
                        OfflineExportProgress.updateProgress(
                                redis,
                                createBy,
                                exportFileName,
                                progress
                        );
                        OfflineExportProgress.pushProgress(
                                ws,
                                taskId,
                                createBy,
                                "导出中",
                                OfflineExportStateMachine.EXPORTING,
                                progress,
                                ""
                        );
                    } else {
                        log.error("明细离线导出 完成[{}]", pageRequest.getVid());
                        break;
                    }

                    if (OfflineExportCancel.existRequest(
                            redis,
                            createBy,
                            exportFileName
                    )) {
                        throw new CancelRequestException();
                    }

                    // 查询下一页
                    pageRequest.setNext(true);
                    // 设置查询首位 RowKey
                    pageRequest.setPageFirstRowKey(resultGlobalResponse.getData().getPageFirstRowKey());
                    // 设置查询末位 RowKey
                    pageRequest.setPageLastRowKey(resultGlobalResponse.getData().getPageLastRowKey());
                    pageRequest.setReadMode(mode);

                    // 获取明细数据项
                    resultGlobalResponse =
                            hisDataClient.findPageByUUID(pageRequest);

                } while (true);


                return totalCount;
            }
        }.csv(createBy, exportFileName, titleList);

    }

}
