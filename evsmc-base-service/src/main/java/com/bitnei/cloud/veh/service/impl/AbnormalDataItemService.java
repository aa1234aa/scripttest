package com.bitnei.cloud.veh.service.impl;

import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.veh.model.AbnormalDataItemEnergyModel;
import com.bitnei.cloud.veh.model.AbnormalDataItemFuelModel;
import com.bitnei.cloud.veh.service.IAbnormalDataItemService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import java.util.List;

/**
 * 车辆异常数据项报表service
 * @author zhouxianzhou
 * @date 2019/9/17 16:14
 **/
@Slf4j
@Service
public class AbnormalDataItemService extends BaseService implements IAbnormalDataItemService {

    private static List<Object> energyList = Lists.newArrayList();
    private static List<Object> fuelList = Lists.newArrayList();

    static {
        // 模拟数据
        for (int i = 1; i <= 10; i++) {
            AbnormalDataItemEnergyModel model = new AbnormalDataItemEnergyModel();
            model.setVin("");
            model.setVehModelName("车辆型号" + i);
            model.setVehUnitName("汽车厂商" + i);
            model.setOperUnitName("运营单位" + i);
            model.setVehStatusCount("10");
            model.setChargeStateCount("10");
            model.setSpeedCount("10");
            model.setDistanceCount("10");
            model.setBatTotalVCount("10");
            model.setBatTotalICount("10");
            model.setSocCount("10");
            model.setDriveMotorCount("10");
            model.setFuelBatteryCount("10");
            model.setEngineDataCount("10");
            model.setPositionDataCount("10");
            model.setLimitDataCount("10");
            model.setAlarmDataCount("10");
            model.setName("ZXZ_ENERGY_" + i);
            model.setSex(""+i);
            energyList.add(model);
            AbnormalDataItemFuelModel fuelModel = new AbnormalDataItemFuelModel();
            fuelModel.setName("ZXZ_FUEL_" + i);
            fuelModel.setAge(10 + i);
            fuelList.add(fuelModel);
        }
    }


    @Override
    public Object energyList(PagerInfo pagerInfo) {
        PagerResult pm = new PagerResult();
        pm.setData(energyList);
        pm.setTotal(10L);
        return pm;
    }

    @Override
    public void energyExport(PagerInfo pagerInfo) {
        String rootPath = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String path = rootPath + "veh/res/abnormalDataItem/energyExport.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("新能源车辆异常数据项报表");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(energyList);
        String outName = String.format("新能源车辆异常数据项报表-导出-%s.xls", DateUtil.getShortDate());
        EasyExcel.renderResponse(path, outName, ed);
    }

    @Override
    public Object fuelList(PagerInfo pagerInfo) {
        PagerResult pm = new PagerResult();
        pm.setData(fuelList);
        pm.setTotal(10L);
        return pm;
    }

    @Override
    public void fuelExport(PagerInfo pagerInfo) {
        String rootPath = RequestContext.class.getResource("/com/bitnei/cloud/").getFile();
        String path = rootPath + "veh/res/abnormalDataItem/fuelExport.xls";
        ExcelData ed = new ExcelData();
        ed.setTitle("燃油车辆异常数据项报表");
        ed.setExportTime(DateUtil.getNow());
        ed.setData(fuelList);
        String outName = String.format("燃油车辆异常数据项报表-导出-%s.xls", DateUtil.getShortDate());
        EasyExcel.renderResponse(path, outName, ed);

    }


}
