package com.bitnei.cloud.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.Exception.CancelRequestException;
import com.bitnei.cloud.common.Exception.ExportErrorException;
import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.client.WsServiceClient;
import com.bitnei.cloud.common.client.das.ProtocolMessageClient;
import com.bitnei.cloud.common.client.model.*;
import com.bitnei.cloud.common.handler.AbstractOfflineExportCsvHandler;
import com.bitnei.cloud.common.handler.IOfflineExportCallback;
import com.bitnei.cloud.common.handler.OfflineExportCancel;
import com.bitnei.cloud.common.handler.OfflineExportProgress;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.domain.RuleType;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.domain.OfflineExportStateMachine;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.ProtocolDataDto;
import com.bitnei.cloud.sys.model.ProtocolDto;
import com.bitnei.cloud.sys.service.IOfflineExportService;
import com.bitnei.cloud.sys.service.IProtocolMessageService;
import com.bitnei.cloud.sys.util.MessageBean;
import com.bitnei.cloud.sys.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AfDbcService实现<br>
 * 描述： AfDbcService实现<br>
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
 * <td>2019-03-04 17:10:36</td>
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
@RequiredArgsConstructor
public class ProtocolMessageService extends CommonBaseService implements IProtocolMessageService, IOfflineExportCallback {

    @Resource(name = "webRedisKit")
    private RedisKit redis;

    @Autowired
    private WsServiceClient ws;

    private final ProtocolMessageClient protocolMessageClient;
    private final VehicleMapper vehicleMapper;
    private final com.bitnei.cloud.dc.mapper.RuleTypeMapper ruleTypeMapper;

    public static final String GB = "gb";
    public static final String G6 = "g6";

    @Override
    public ResultMsg findProtocolMessage(PagerInfo pagerInfo) {

        addQueryCondition(pagerInfo, "limitDay", "30");
        try {
            checkDate(pagerInfo);
        } catch (BusinessException e) {

            throw new ExportErrorException(e.getMessage());
        }
        Map<String, Object> params = ServletUtil.pageInfoToMap(pagerInfo);
        ProtocolQueryModel protocolQueryModel = new ProtocolQueryModel();
        if (params.containsKey("beginTime")) {
            protocolQueryModel.setStartTime(params.get("beginTime").toString());
        }
        if (params.containsKey("endTime")) {
            protocolQueryModel.setEndTime(params.get("endTime").toString());
        }

        if (params.containsKey("type")) {
            protocolQueryModel.setType(Integer.valueOf(params.get("type").toString()));
            if (protocolQueryModel.getType() == 0) {
                protocolQueryModel.setType(null);
            }
        }
        if (params.containsKey("orderBy")) {
            protocolQueryModel.setAsc(Integer.valueOf(params.get("orderBy").toString()) == 1);
//            protocolQueryModel.setOrderBy(Integer.valueOf(params.get("orderBy").toString()));
        }
        if (params.containsKey("verify")) {
            protocolQueryModel.setVerify(Integer.valueOf(params.get("verify").toString()));
        }
        int size = 50;
        if (pagerInfo.getLimit() != null && pagerInfo.getLimit() > 0) {
            size = pagerInfo.getLimit();
        }
        protocolQueryModel.setPageNo(pagerInfo.getStart() / size + 1);
        protocolQueryModel.setPageSize(size);
//        protocolQueryModel.setLimit(size);
//        protocolQueryModel.setStart(pagerInfo.getStart());


        String vin = null != params.get("vin") ? params.get("vin").toString() : null;
        String licensePlate = null != params.get("licensePlate") ? params.get("licensePlate").toString() : null;
        String interNo = null != params.get("interNo") ? params.get("interNo").toString() : null;

        if (StringUtils.isEmpty(vin) && StringUtils.isEmpty(licensePlate) && StringUtils.isEmpty(interNo)) {
            throw new ExportErrorException("请填写车牌或VIN或内部编码");
        }

        if (null == protocolQueryModel.getStartTime() || null == protocolQueryModel.getEndTime()) {
            throw new ExportErrorException("查询开始时间与结束时间不可为空");
        }

        if (StringUtils.equals(protocolQueryModel.getStartTime(), protocolQueryModel.getEndTime())) {
            throw new ExportErrorException("起止时间不能相同");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("licensePlate", licensePlate);
        map.put("interNo", interNo);
        Vehicle vehicle = vehicleMapper.findByVinAndLicenseAndInterNo(map);

        if (null == vehicle) {
            throw new ExportErrorException("车辆信息不存在，请检查");
        }

        protocolQueryModel.setVid(vehicle.getUuid());

        GlobalResponse<PageResult<ProtocolMessageData>> globalResponse =
                findPageByUUID(protocolQueryModel, vehicle.getSupportProtocol());

        if (null == globalResponse || globalResponse.getData() == null ||
                CollectionUtils.isEmpty(globalResponse.getData().getData())) {
            // throw new ExportErrorException("车辆该时间段无报文信息");
            PagerModel pagerModel = new PagerModel();
            pagerModel.setTotal(0);
            pagerModel.setRows(new ArrayList());

            return pagerModel.toResultMsg(protocolQueryModel);
        }
        map.put("uuid", vehicle.getUuid());

        List<JSONObject> results = getData(globalResponse, vehicle.getVin(), vehicle.getInterNo(),
                vehicle.getLicensePlate(), vehicle.getRuleTypeName());


        PagerModel pagerModel = new PagerModel();
        pagerModel.setTotal(globalResponse.getData().getTotalCount());
        int index = pagerInfo.getStart() == null ? 1 : pagerInfo.getStart() + 1;
        if (CollectionUtils.isNotEmpty(results)) {
            for (JSONObject f : results) {
                f.put("index", index++);
            }
        }

        pagerModel.setRows(results);

        return pagerModel.toResultMsg(protocolQueryModel);

    }

    private GlobalResponse<PageResult<ProtocolMessageData>> findPageByUUID(
            ProtocolQueryModel protocolQueryModel, String supportProtocol) {
        if (StringUtils.isEmpty(supportProtocol)) {
            return protocolMessageClient.findPageByUUID(protocolQueryModel);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("supportProtocol", supportProtocol);
        List<RuleType> ruleTypes = ruleTypeMapper.findByRuleId(params);
        if (CollectionUtils.isNotEmpty(ruleTypes)) {
            for (RuleType it : ruleTypes) {
                if (it.getName().contains("国六") || it.getCode().contains("17691")) {
                    protocolQueryModel.setType(convertType(protocolQueryModel.getType(), G6, true));
                    protocolQueryModel.setRuleType(RuleTypeEnum.GB_T17691.getCode());
                    GlobalResponse<PageResult<ProtocolMessageData>> globalResponse =
                            protocolMessageClient.findPageByUUID(protocolQueryModel);
                    if (null != globalResponse && null != globalResponse.getData() &&
                            CollectionUtils.isNotEmpty(globalResponse.getData().getData())) {
                        for (Object item : globalResponse.getData().getData()) {
                            ProtocolMessageData data = (ProtocolMessageData) item;
                            for (ProtocolMessage protocolMessage : data.getProtocolMessages()) {
                                protocolMessage.setType(convertType(protocolMessage.getType(), G6, false));
                            }
                        }
                    }
                    return globalResponse;
                }
            }
        }
        return protocolMessageClient.findPageByUUID(protocolQueryModel);
    }

    /**
     * 实际报文类型和字典的报文类型互相转换
     *
     * @param type
     * @return
     */
    private Integer convertType(Integer type, String parseType, Boolean dictToType) {

        if (null == type) {
            return type;
        }

        //国六的报文类型和字典的报文类型互相转换
        if (parseType.equals(G6)) {
            //字典类型转实际类型
            if (dictToType) {
                switch (type) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 3;
                    case 4:
                        return 4;
                    case 5:
                        throw new BusinessException("国六协议不存在该报文类型");
                    case 6:
                        throw new BusinessException("国六协议不存在该报文类型");
                    case 7:
                        throw new BusinessException("国六协议不存在该报文类型");
                    case 8:
                        return 5;
                    case 66:
                        return 6;
                    case 67:
                        return 7;
                    case 68:
                        return 8;
                    default:
                        break;
                }
            }
            //实际类型转字典类型
            else {
                switch (type) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 3;
                    case 4:
                        return 4;
                    case 5:
                        return 8;
                    case 6:
                        return 66;
                    case 7:
                        return 67;
                    case 8:
                        return 68;
                    default:
                        break;
                }
            }
        }
        return type;
    }

    public List<JSONObject> getData(GlobalResponse<PageResult<ProtocolMessageData>> globalResponse,
                                    String vin, String interNo, String lic, String ruleTypeName) {
        List<JSONObject> results = new ArrayList<>();
        for (Object data : globalResponse.getData().getData()) {

            JSONObject jsonObject = JSONObject.fromObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("protocolMessages");
            if (CollectionUtils.isEmpty(jsonArray)) {
                continue;
            }
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                Object value = jsonObject.get(key);
                if (null == value || value.toString().equals("null")) {
                    jsonObject.put(key, "");
                }
            }
            jsonObject.put("vin", vin);
            jsonObject.put("interNo", interNo);
            jsonObject.put("licensePlate", lic);
            jsonObject.put("ruleTypeName", ruleTypeName);


            JSONObject obj = JSONObject.fromObject(jsonArray.get(0));
            Integer type = obj.getInt("type");


            jsonObject.put("serverRecvTime", obj.get("serverRecvTime"));
            jsonObject.put("srvrRecvTime", obj.get("serverRecvTime"));


            Object datas = jsonArray.stream().map(m -> {
                JSONObject o = JSONObject.fromObject(m);
                return o.get("data");
            }).collect(Collectors.joining(","));
            String d = datas.toString();
            if ("BITNEI12345678901".equals(vin)) {
                d = d + "," + d;
            }
            jsonObject.put("data", d);
            int totalDataLen = jsonArray.stream().mapToInt(js -> {
                JSONObject o = JSONObject.fromObject(js);
                return (Integer) o.get("dataLength");
            }).sum();
            jsonObject.put("dataLength", totalDataLen);
            String lens = jsonArray.stream().map(l -> {
                JSONObject o = JSONObject.fromObject(l);
                return String.valueOf(o.get("dataLength"));
            }).collect(Collectors.joining(",")).toString();
            if ("BITNEI12345678901".equals(vin)) {
                lens = lens + "," + lens;
            }
            jsonObject.put("itemDataLength", lens);
            String typeName;
            //是否是国标，根据ruleType来判断（这个逻辑有待确认）
            Boolean isGuoBiao = true;
            switch (type) {
                case 1:
                    typeName = "车辆登入";
                    break;
                case 2:
                    typeName = "实时信息上报";
                    break;
                case 3:
                    typeName = isGuoBiao ? "补发信息上报" : "状态信息上报";
                    break;
                case 4:
                    typeName = isGuoBiao ? "车辆登出" : "心跳";
                    break;
                case 5:
                    typeName = isGuoBiao ? "平台登入" : "失败";
                    break;
                case 6:
                    typeName = isGuoBiao ? "平台登出" : "失败";
                    break;
                case 7:
                    typeName = isGuoBiao ? "心跳" : "失败";
                    break;
                case 8:
                    typeName = isGuoBiao ? "终端校时" : "失败";
                    break;
                case 66:
                    typeName = ruleTypeName != null && (ruleTypeName.contains("国六") || ruleTypeName.contains("17691")) ? "终端防拆" : "";
                    break;
                default:
                    typeName = "";
            }
            jsonObject.put("typeName", typeName);
            jsonObject.put("type", type);

            Integer verify = obj.getInt("verify");
            String verifyName;
            switch (verify) {
                case 0:
                    verifyName = "正确报文";
                    break;
                case 1:
                    verifyName = "报文结构错误";
                    break;
                case 2:
                    verifyName = "应答标志错误";
                    break;
                case 3:
                    verifyName = "平台用户重复登录";
                    break;
                case 4:
                    verifyName = "实时、补发报文应答标志错误";
                    break;
                case 5:
                    verifyName = "实时、补发报文整车数据结构错误";
                    break;
                case 6:
                    verifyName = "实时、补发报文驱动电机数据结构错误";
                    break;
                case 7:
                    verifyName = "实时、补发报文燃料电池数据结构错误";
                    break;
                case 8:
                    verifyName = "实时、补发报文发动机数据结构错误";
                    break;
                case 9:
                    verifyName = "时报、补发文车辆位置数据结构错误";
                    break;
                case 10:
                    verifyName = "实时、补发报文极值数据结构错误";
                    break;
                case 11:
                    verifyName = "实时、补发报文报警数据结构错误";
                    break;
                case 12:
                    verifyName = "实时、补发报文单体电压数据结构错误";
                    break;
                case 13:
                    verifyName = "实时、补发报文单体温度数据结构错误";
                    break;
                case 14:
                    verifyName = "平台报文超时";
                    break;
                case 15:
                    verifyName = "车辆报文超时";
                    break;
                case 16:
                    verifyName = "补发、实时报文时间错误";
                    break;
                case 17:
                    verifyName = "Redis 查询失败错误";
                    break;

                default:
                    verifyName = "失败";
            }

            jsonObject.put("verifyName", verifyName);
            results.add(jsonObject);
        }
        return results;
    }

    @Override
    public void export(PagerInfo pagerInfo) {

        addQueryCondition(pagerInfo, "limitDay", "30");
        try {
            checkDate(pagerInfo);
        } catch (BusinessException e) {

            throw new ExportErrorException("超过3天请使用离线导出模式");
        }

        //如果时间区间过大,该方式可能会超时
        pagerInfo.setStart(0);
        pagerInfo.setLimit(Integer.MAX_VALUE);
        ResultListMsg data = (ResultListMsg) findProtocolMessage(pagerInfo);
        List<JSONObject> rows = (List<JSONObject>) data.getData();

//        List<Object> results = rows.stream().map(it ->
//                JSONObject.toBean(it, ProtocolDto.class)).collect(Collectors.toList());
        List<Object> results1 = rows.stream().map(it -> {
                    ProtocolDto dto = new ProtocolDto();
                    dto.setInterNo(it.getOrDefault("interNo", "").toString());
                    dto.setVin(it.getOrDefault("vin", "").toString());
                    dto.setLicensePlate(it.getOrDefault("licensePlate", "").toString());
                    dto.setSrvrRecvTime(it.getOrDefault("srvrRecvTime", "").toString());
                    dto.setData(it.getOrDefault("data", "").toString());
                    dto.setDataLength((Integer) it.getOrDefault("dataLength", 0));
                    dto.setRuleTypeName(it.getOrDefault("ruleTypeName", "").toString());
                    dto.setType((Integer) it.getOrDefault("type", 0));
                    dto.setTypeName(it.getOrDefault("typeName", "").toString());
                    dto.setVerifyName(it.getOrDefault("verifyName", "").toString());
                    dto.setRecvTime(it.getOrDefault("recvTime", "").toString());
                    return dto;
                }
        ).collect(Collectors.toList());
        DataLoader.loadNames(rows);
        String srcBase = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String srcFile = srcBase + "sys/res/protocolMessage/export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("导出报文记录");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(results1);
        String outName = String.format("%s-导出-%s.xls", "导出报文记录", DateUtil.getShortDate());
        EasyExcel.renderResponse(srcFile, outName, ed);

    }

    @Override
    public List parse(ProtocolDataDto protocolDataDto) {
        //如果是国六的走国六的解析
        Map<String, Object> params = new HashMap<>();
        params.put("vin", protocolDataDto.getVin());
        Vehicle vehicle = vehicleMapper.findByVin(params);

        if (null == vehicle || StringUtils.isEmpty(vehicle.getSupportProtocol())) {
            return doParseStation(protocolDataDto.getData());
        }

        params.put("supportProtocol", vehicle.getSupportProtocol());
        List<RuleType> ruleTypes = ruleTypeMapper.findByRuleId(params);
        if (CollectionUtils.isNotEmpty(ruleTypes)) {
            for (RuleType it : ruleTypes) {
                if (it.getName().contains("国六") || it.getCode().contains("17691")) {
                    return doParse(protocolDataDto.getData(), G6, false);
                }
            }
        }
        //默认是国标的解析
        return doParseStation(protocolDataDto.getData());
    }

    /**
     * 国标解析
     *
     * @param data
     * @return
     */
    @Override
    public List doParseStation(String data) {

        //datas= "232302fe4c5244563650454332444c3031323937380000601009160f091d0101030000000000000000271000010000000000020100280000520828000027100352085208520800063c3c3c3c3c3c04b0082ee00800c80801040000004e2005000000000000000000060000000000000000000028000028000d";
        //平台登录信息
        //datas="232305fe4c5244563650454332444c3031323937380000291009180d32220000626a6c67787900000000000031323334353600000000000000000000000000000098";
        //发送车辆登入
        //datas="232301fe4c5244563650454332444c3031323937380000321009180d32220000696363696400000000000000000000000000000000003132333435363700000000000000000000000000d6";
        //发送实时信息
        //datas="232302fe4c5244563650454332444c30313239373800006b1009180d32230101030100000000000000002710000100000000000201010028000052082800002710032ee02ee0520800063c3c3c3c3c3c04b0082ee00800c80801040000004e20050000000000000000000600000000000000000000280000280700000000000000000008";
        //datas="232302fe4c5244563650454332444c3031323937380001171009180d322d0801011388138800780001780c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c4409010100103f3f3f3f3f3f3f3f3f3f3f3f3f3f3f3fba";
        //datas="232302fe4c5244563650454332444c3031323937380001171009180d32230801011388138800780001780c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c4409010100103f3f3f3f3f3f3f3f3f3f3f3f3f3f3f3fb4";
        // datas="232302fe4c5244563650454332444c30313239373800006b1009180f16290101030104b00001e07800002710000100000000000201010028000052082800002710032ee02ee0520800063c3c3c3c3c3c04b0082ee00800c80801040000004e20050000000000000000000600000000000000000000280000280700000000000000000009";
        //datas="232302fe4c5244563650454332444c30313239373800017c100a181233040178010104b00001e07807d02af8e101000000000002010101280bb852082800002710032ee02ee0520800063c3c3c3c3c3c04b0082ee00800c80801040000004e20050000000000000000000601010d0101020c80000028000028070000000000000000000801011388138800780001780c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c440c4409010100103f3f3f3f3f3f3f3f3f3f3f3f3f3f3f3f80";
        // data=data+","+data;
        if (StringUtils.isEmpty(data)) {
            log.error("报文数据为空");
            return new ArrayList<>();
        }
        int[] packIndex = new int[1];
        packIndex[0] = 1;
        List result = new ArrayList();
        String[] arr = data.split(",");
        Stream.of(arr).forEach(d -> {
            List list = doParse(d, GB, false);
            if (CollectionUtils.isNotEmpty(list)) {
                if (arr.length > 1) {
                    //分包
                    list.forEach(sub -> {
                        MessageBean messageBean = (MessageBean) sub;
                        ((MessageBean) sub).setGroup(String.format("分包%d-%s", packIndex[0], messageBean.getGroup()));
                    });
                    packIndex[0]++;
                }
                result.addAll(list);
            }

        });
        return result;
    }

    private final IOfflineExportService offlineExportService;

    @Override
    public void exportOffline(PagerInfo pagerInfo) {

        addQueryCondition(pagerInfo, "limitDay", "30");
        try {
            checkDate(pagerInfo);
        } catch (BusinessException e) {

            throw new ExportErrorException(e.getMessage());
        }

        String vin = getQueryCondition(pagerInfo, "vin");
        String licensePlate = getQueryCondition(pagerInfo, "licensePlate");
        String interNo = getQueryCondition(pagerInfo, "interNo");

        if (StringUtils.isEmpty(vin) && StringUtils.isEmpty(licensePlate) && StringUtils.isEmpty(interNo)) {
            throw new ExportErrorException("请填写车牌或VIN或内部编码");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("licensePlate", licensePlate);
        map.put("interNo", interNo);
        Vehicle vehicle = vehicleMapper.findByVinAndLicenseAndInterNo(map);

        if (null == vehicle) {
            throw new ExportErrorException("车辆信息不存在，请检查");
        }
        addQueryCondition(pagerInfo, "vin", vehicle.getVin());
        addQueryCondition(pagerInfo, "licensePlate", vehicle.getLicensePlate());
        addQueryCondition(pagerInfo, "interNo", vehicle.getInterNo());
        addQueryCondition(pagerInfo, "ruleTypeName", vehicle.getRuleTypeName());
        addQueryCondition(pagerInfo, "vid", vehicle.getUuid());
        addQueryCondition(pagerInfo, "supportProtocol", vehicle.getSupportProtocol());

        // 回调服务, 使用服务 Bean 类型, 这里使用的就是当前类
        final String exportServiceName = this.getClass().getCanonicalName();

        // 回调服务中的回调方法, 这里使用的是本类中的 exportOfflineProcessor 方法
        final String exportMethodName = "exportOfflineProcessor";
        final String exportFilePrefixName = "导出报文记录";
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


        List<String> titleList = Arrays.asList("VIN", "内部编码", "车牌", "协议类型", "服务器接收时间", "报文时间", "报文类型", "校验", "报文长度", "原始报文");

        new AbstractOfflineExportCsvHandler() {
            @Override
            protected long writeBody(final @NotNull OutputStreamWriter writer) throws IOException {
                long[] totalCount = new long[1];


                ProtocolQueryModel pageRequest = new ProtocolQueryModel();

                pageRequest.setStartTime(getQueryCondition(pagerInfo, "beginTime"));


                pageRequest.setEndTime(getQueryCondition(pagerInfo, "endTime"));
                String type = getQueryCondition(pagerInfo, "type");
                if (StringUtils.isNotBlank(type)) {
                    pageRequest.setType(Integer.valueOf(type));
                }
                String order = getQueryCondition(pagerInfo, "orderBy");
                if (StringUtils.isNotBlank(order)) {
                    pageRequest.setAsc(Integer.valueOf(order) == 1);

                }
                String verify = getQueryCondition(pagerInfo, "verify");
                if (StringUtils.isNotBlank(verify)) {
                    pageRequest.setAsc(Integer.valueOf(order) == 1);
                    pageRequest.setVerify(Integer.valueOf(verify));

                }
                pageRequest.setVid(getQueryCondition(pagerInfo, "vid"));

                int size = 50;
                if (pagerInfo.getLimit() != null && pagerInfo.getLimit() > 0) {
                    size = pagerInfo.getLimit();
                }

                pageRequest.setPageSize(size);
//                pageRequest.setLimit(size);
//                pageRequest.setStart(pagerInfo.getStart());
                pageRequest.setNext(null);
                pageRequest.setSkipPage(false);
                pageRequest.setGetCount(true);
                pageRequest.setPageSize(3000);

                GlobalResponse<PageResult<ProtocolMessageData>> resultGlobalResponse =
                        findPageByUUID(pageRequest, getQueryCondition(pagerInfo, "supportProtocol"));

                final long amount;
                if (resultGlobalResponse != null
                        && resultGlobalResponse.getData() != null) {
                    amount = resultGlobalResponse.getData().getTotalCount();
                } else {
                    amount = 0L;
                }

                pageRequest.setGetCount(false);

                do {
                    try {
                        if (resultGlobalResponse != null
                                && resultGlobalResponse.getData() != null
                                && CollectionUtils.isNotEmpty(resultGlobalResponse.getData().getData())) {

                            List<JSONObject> results = getData(resultGlobalResponse,
                                    getQueryCondition(pagerInfo, "vin"),
                                    getQueryCondition(pagerInfo, "interNo"),
                                    getQueryCondition(pagerInfo, "licensePlate"),
                                    getQueryCondition(pagerInfo, "ruleTypeName")

                            );
                            results.stream().forEach(it -> {
                                List<String> valueList = new ArrayList<>();
                                valueList.add(it.getOrDefault("vin", "").toString());
                                valueList.add(it.getOrDefault("interNo", "").toString());

                                valueList.add(it.getOrDefault("licensePlate", "").toString());
                                valueList.add(it.getOrDefault("ruleTypeName", "").toString());
                                valueList.add(it.getOrDefault("srvrRecvTime", "").toString());
                                valueList.add(it.getOrDefault("recvTime", "").toString());
                                valueList.add(it.getOrDefault("typeName", "").toString());
                                valueList.add(it.getOrDefault("verifyName", "").toString());
                                valueList.add(it.getOrDefault("dataLength", 0).toString());
                                valueList.add(it.getOrDefault("data", "").toString());

                                try {
                                    totalCount[0] += 1;
                                    super.writeBody(writer, Collections.singletonList(valueList));
                                } catch (IOException ignore) {
                                    log.error("报文离线导出 回调异常[{}],中止本次导出", pageRequest.getVid());


                                }
                            });

                            final long progress = OfflineExportProgress.computeProgress(totalCount[0], amount);
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
                            log.error("报文离线导出 完成[{}]", pageRequest.getVid());
                            break;
                        }
                    } catch (Exception e) {
                        log.error("报文离线导出 error[{}],中止本次导出", pageRequest.getVid());

                        log.error("error:", e);
                        break;
                    }

                    if (OfflineExportCancel.existRequest(
                            redis,
                            createBy,
                            exportFileName
                    )) {
                        throw new CancelRequestException();
                    }

                    pageRequest.setPageFirstRowKey(resultGlobalResponse.getData().getPageFirstRowKey());
                    pageRequest.setPageLastRowKey(resultGlobalResponse.getData().getPageLastRowKey());
                    pageRequest.setNext(true);

                    resultGlobalResponse = protocolMessageClient.findPageByUUID(pageRequest);

                } while (true);


                return totalCount[0];
            }
        }.csv(createBy, exportFileName, titleList);

    }

//    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//        String data = "232302fe4c413947334d4244374a535758423038330101e31309111227110101030101220007ef401628275522010e030c0000020101045252274e205d1676271004020000000005000728ea4801cc3b100601430ca801010c9401094e010e48070000000000000000000801011628275500b00001b00c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80ca80c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c940c9409010100204d4b4b4d4d4b4c4d4e4c4c4e4948494a4a49494a4a484849494848494948484880";
//        String g6data = "2323024d5258494b5931383832353131313134340002026d13090b003923015a0113090b00391a0200ffffffff4d5258494b59313838323531313131343464656661756c74000000000000000000000064656661756c74000000000000000000000064656661756c74000000000000000000000000000000000000000000000000000000000001000000020213090b00391a5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b00391b5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b00391c5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b00391d5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b00391e5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b00391f5a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b0039205a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b0039215a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b0039225a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de0213090b0039235a00c87d7d000000000fa00fa000000022202220000028000006aed0c201cbbf4f000026de20103f7c4330c500df66e41c304a98d40bfd69d036b8192a74f17830b84a9d5d5320a47f81096fb2b7e1e0f359f01a3b3a70704d3e07ab356d82b528112d49fd4350d1";
//        List<MessageBean> msg = doParse(data, GB, true);
//        System.out.println(System.currentTimeMillis());
//    }

    public List<MessageBean> doParse(String data, String parseType, boolean onlyData) {
//        String str = data.replaceAll("\"", "");
//       System.out.println(System.currentTimeMillis());
//        StringBuffer sb = new StringBuffer();
//        int length = 2;
//        byte buf[] = new byte[4096];
//        int len = 0;
//        while (length <= str.length()) {
//            String src = str.substring(0, length);
//            if (null == src || StringUtils.isEmpty(src.trim())) {
//                continue;
//            }
//            sb.append(src).append(" ");
//            buf[len++] = (byte) Integer.parseInt(src.trim(), 16);
//            str = str.substring(length, str.length());
//            if (len % 20 == 0) {
//                sb.append("\r\n");
//            }
//        }
        byte buf[] = hexStringToBytes(data);
        if (parseType.toLowerCase().equals(G6)) {
            return MessageUtils.uploadPacketDataG6(buf, buf.length, onlyData);
        }
        return MessageUtils.uploadPacketDataStation(buf, buf.length, onlyData);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
