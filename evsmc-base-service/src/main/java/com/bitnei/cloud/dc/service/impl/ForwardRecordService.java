package com.bitnei.cloud.dc.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultListMsg;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.client.das.ForwardRecordClient;
import com.bitnei.cloud.common.client.das.ProtocolMessageClient;
import com.bitnei.cloud.common.client.model.*;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.dc.model.ForwardRecordModel;
import com.bitnei.cloud.dc.service.IForwardRecordService;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.dao.VehicleMapper;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.service.IVehicleService;
import com.bitnei.cloud.sys.util.MessageUtils;
import com.bitnei.commons.util.MapperUtil;
import jodd.util.ObjectUtil;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lijiezhou on 2019/2/21.
 */
@Slf4j
@Service
public class ForwardRecordService extends BaseService implements IForwardRecordService {

    @Autowired
    private ForwardRecordClient forwardRecordClient;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private IVehicleService vehicleService;

    @Override
    @SneakyThrows
    public ResultMsg list(PagerInfo pagerInfo) {
        //获取当权限的map
        Map<String,Object> params = new HashMap<String,Object>();
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        int size=50;
        if (pagerInfo.getLimit()!=null&&pagerInfo.getLimit()>0){
            size=pagerInfo.getLimit();
        }
        ForwardQueryModel qm = new ForwardQueryModel();
        //起始数
        if (pagerInfo.getStart() == null) {
            qm.setStart(0);
            qm.setPageNo(1);
        } else {
            qm.setStart(pagerInfo.getStart());

            qm.setPageNo(pagerInfo.getStart()/size+1);
        }
        //每页大小
        if (pagerInfo.getLimit() == null) {
            qm.setLimit(-1);
            qm.setPageSize(Integer.MAX_VALUE);
        } else {
            qm.setLimit(pagerInfo.getLimit());
            qm.setPageSize(size);
        }
        //vin
        String vin = params.get("vin") != null && !"".equals(String.valueOf(params.get("vin"))) ? String.valueOf(params.get("vin")) : "";
        //车牌号
        String licensePlate = params.get("licensePlate") != null && !"".equals(String.valueOf(params.get("licensePlate"))) ? String.valueOf(params.get("licensePlate")) : "";
        String uuid = "";
        //开始时间
        qm.setStartTime(params.get("beginTime") != null && !"".equals(String.valueOf(params.get("beginTime"))) ? String.valueOf(params.get("beginTime")) : null);
        //结束时间
        qm.setEndTime(params.get("endTime") != null && !"".equals(String.valueOf(params.get("endTime"))) ? String.valueOf(params.get("endTime")) : null);
        //车辆方式|报文类型
        qm.setType(params.get("type") != null && !"".equals(String.valueOf(params.get("type"))) ? String.valueOf(params.get("type")) : null);
        //转发类型
        qm.setFlag(params.get("flag") != null && !"".equals(String.valueOf(params.get("flag"))) ? String.valueOf(params.get("flag")) : null);
        //转发结果
        qm.setResult(params.get("result") != null && !"".equals(String.valueOf(params.get("result"))) ? String.valueOf(params.get("result")) : null);
        //转发平台id
        qm.setForwardId(params.get("platformId") != null && !"".equals(String.valueOf(params.get("platformId"))) ? String.valueOf(params.get("platformId")) : null);
        //排序
        qm.setOrderBy(params.get("orderBy") != null && !"".equals(String.valueOf(params.get("orderBy"))) ? String.valueOf(params.get("orderBy")) : null);

        qm.setAsc(StringUtils.isNotBlank(qm.getOrderBy())&&qm.getOrderBy().equals("1"));
        if (StringUtil.isEmpty(vin) && StringUtil.isEmpty(licensePlate)) {
            throw new BusinessException("需传入vin或车牌号以查看数据");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("vin", vin);
            map.put("licensePlate", licensePlate);
            uuid = vehicleMapper.findUuidByVinAndLicense(map);
            if (uuid == null || uuid.equals("")) {
                return null;
            }
            qm.setUuid(uuid);
            qm.setVid(uuid);
        }
        if (qm.getOrderBy() == null || qm.getOrderBy().equals("")) {
            qm.setOrderBy("0");
        }
        if ((qm.getStartTime() != null && !qm.getStartTime().equals("")) && (qm.getEndTime() != null && !qm.getEndTime().equals(""))) {
            long end = DateUtil.strToDate_ex_full(qm.getEndTime()).getTime();
            long start = DateUtil.strToDate_ex_full(qm.getStartTime()).getTime();
            if (((end - start)) > (long) 30 * (long) 86400000) {
                throw new BusinessException("时间跨度不能超过30天");
            }
        } else if ((qm.getStartTime() == null || qm.getStartTime().equals("")) && (qm.getEndTime() == null || qm.getEndTime().equals(""))) {
            long times = System.currentTimeMillis();
            long startTimes = times - (long) 29 * (long) 86400000;
            String endTime = DateUtil.getDate(times);
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(startTimes)) + " 00:00:00";
            qm.setStartTime(startTime);
            qm.setEndTime(endTime);

        }
        if (licensePlate.equals("")) {
            VehicleModel vehicle = vehicleService.getByUuid(qm.getUuid());
            licensePlate = vehicle.getLicensePlate();
        }

        GlobalResponse<PageResult<ForwardRecord>> pagerModel = forwardRecordClient.findPageByUUID(qm);
        PagerModel p =new PagerModel();
        p.setTotal(pagerModel.getData().getTotalCount());

        List<ForwardRecordModel> list = new ArrayList<>();
        for (Object data : pagerModel.getData().getData()) {
            Map<String, Object> tt = MapperUtil.Object2Map(data) ;
            ForwardRecordModel model = new ForwardRecordModel();
            model.setLicensePlate(licensePlate);
            model.setVin(tt.get("vin").toString());
            model.setForwardId(tt.get("forwardId").toString());
            model.setTime(tt.get("forwardTime").toString());
            model.setFlag(tt.get("flag").toString());
            model.setType(tt.get("type").toString());
            model.setResult(tt.get("result").toString());
            model.setPacket(tt.get("packet") == null || "".equals(tt.get("packet")) ? null : buildPacketResults(tt.get("packet").toString()));
            list.add(model);
        }
        p.setRows(list);
        return p.toResultMsg(qm);
    }

    /**
     * 处理转发报文数据
     * @param packet 转发原始报文(base64字符串)
     * @return
     */
    private String buildPacketResults(String packet) {
        byte[] packData = Base64.decodeBase64(packet);
        return MessageUtils.byte2HexStr(packData);
    }

    @Override
    public void export(PagerInfo pagerInfo) {
        pagerInfo.setStart(0);
        pagerInfo.setLimit(Integer.MAX_VALUE);
        ResultListMsg resultMsg = (ResultListMsg) this.list(pagerInfo);
        ArrayList objects = new ArrayList();
        for (ForwardRecordModel model : (List<ForwardRecordModel>) resultMsg.getData()) {
            objects.add(model);
        }
        DataLoader.loadNames(objects);
        String var7 = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String var8 = var7 + "dc/res/forwardRecord/export.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("数据转发日志");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(objects);
        String outName = String.format("数据转发日志-导出-%s.xls", DateUtil.getShortDate());
        EasyExcel.renderResponse(var8, outName, ed);
    }
}
