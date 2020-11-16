package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleStageLogModel;
import com.bitnei.cloud.sys.util.RandomUtil;
import com.bitnei.commons.util.UtilHelper;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class VehicleServiceTest {

    @Autowired
    private IVehicleService vehicleService;

    @Test
    public void get() {
        VehicleModel vehicleModel = this.vehicleService.get("1c9ee4fa8f37405b855186a7e63b3d88");
        System.out.println("vehicleModel:" + vehicleModel.getVin());
        List<VehicleStageLogModel> stageLogs = vehicleModel.getStageLogs();
        for (VehicleStageLogModel stageLog : stageLogs) {
            System.out.println("VehicleStageLogModel :" + stageLog);
        }

    }



    @Test
    public void vehicleList() {
        PagerInfo pagerInfo = new PagerInfo();
        Object o = vehicleService.vehicleList(pagerInfo);

        String termModelUnitSql = "INSERT INTO `sys_term_model_unit` (`id`, `serial_number`, `sys_term_model_id`, `support_protocol`, `protocol_version`, `iccid`, `imei`, `term_part_firmware_number`, `fireware_version`, `produce_batch`, `factory_date`, `create_time`, `create_by`) VALUES ('%s', '%s', 'b0e3ba509fe64a8588890fe419f979aa', '828751f66e824d6398416582d1a0ac98', 'V3', '89860403101890200000', '100000000000000', NULL, 'V2', '201801', '2019/03/18', '2019-03-18 15:26:17', 'admin');";
        PagerResult pagerResult = (PagerResult) o;
        System.out.println("pagerResult.getTotal() : " + pagerResult.getTotal());
        List<Object> data = pagerResult.getData();
        for (Object datum : data) {
//            VehicleModel datum1 = (VehicleModel) datum;
            System.out.println("datum.toString() : " + datum.toString());
        }
    }


    @Test
    public void updateVehicleTerm(){
        PagerInfo pagerInfo = new PagerInfo();
        List<VehicleModel> list = (List<VehicleModel>) vehicleService.vehicleList(pagerInfo);
        String uodateSql = "UPDATE sys_vehicle SET term_id = '%s' WHERE id = '%s' ;";
        String termModelUnitSql = "INSERT INTO `sys_term_model_unit` (`id`, `serial_number`, `sys_term_model_id`, `support_protocol`, `protocol_version`, `iccid`, `imei`, `fireware_version`, `produce_batch`, `factory_date`, `create_time`, `create_by`) VALUES ('%s', '%s', 'b0e3ba509fe64a8588890fe419f979aa', '828751f66e824d6398416582d1a0ac98', 'V3', '%s', '%s', 'V2', '201801', '2019/04/12', '2019-04-12 15:26:17', 'admin');";
        String simSql = "INSERT INTO `sys_sim_management` (`id`, `global_sim_type`, `iccid`, `msisd`, `create_time`, `create_by`) VALUES ('%s', '2', '%s', '%s','2019-04-12 16:02:37', 'admin');";
        for (VehicleModel v : list) {

            String simId = UtilHelper.getUUID();
            String iccid = RandomUtil.generateRandom("ICCID", 15);
            String msisd = RandomUtil.generateRandomNum("13",9);
            String termId = UtilHelper.getUUID();
            String serialNumber = RandomUtil.generateRandom("ZXZTERM", 4);
            String imei = RandomUtil.generateRandom("IMEI", 11);
            System.out.println(String.format(simSql,simId, iccid, msisd));
            System.out.println(String.format(termModelUnitSql,termId, serialNumber, iccid, imei));
            System.out.println(String.format(uodateSql,termId, v.getId()));

            System.out.println();
        }

    }

    @Test
    @Rollback(false)
    public void insert() {
        VehicleModel model = new VehicleModel();
        model.setVin("vin-20181221");
        model.setInterNo("inter_no_00001");
        model.setVehModelId("veh_model_id");
        model.setColor("color");
        model.setColorNickName("colorNickName");
        model.setTermId("termId");
        model.setManuUnitId("manuUnitId");
        model.setProduceBatch("produceBatch");
        model.setQualityYears("qualityYears");
        model.setVehCertificateNumber("vehCertificateNumber");
        model.setProduceDate("2017-12-21");
        model.setFactoryDate("2018-12-21");
        model.setDriveDeviceIds("driveDeviceIds");
        model.setEngeryDeviceIds("engeryDeviceIds");
        model.setPowerDeviceIds("powerDeviceIds");
        vehicleService.insert(model);
    }

    @Test
    public void generateInterNo() {
        for (int i = 0; i < 20; i++) {
            String interNo = vehicleService.generateInterNo();
            System.out.println(interNo);
        }

    }

    @Test
    public void test() {
        String srcFile = "D:\\lgxy\\evsmc-base-service\\src\\main\\java\\com\\bitnei\\cloud\\sys\\res\\vehicle\\nationExport.xlsx";

        List<ExcelData> excelDataList = new ArrayList<>();
        ExcelData ed = new ExcelData();
        UserModel um = new UserModel();
        um.setUsername("陈鹏");
        um.setDeptNames("部门名称");
        ed.getData().add(um);
        ed.setListRowIndex(1);
        excelDataList.add(ed);

        ExcelData ed1 = new ExcelData();
        UserModel um1 = new UserModel();
        um1.setUsername("陈鹏1");
        um1.setDeptNames("部门名称1");
        um1.setCreateBy("admin");
        ed1.getData().add(um1);
        ed1.setListRowIndex(1);
        excelDataList.add(ed1);
        try {
            File file = EasyExcel.createSheetFile(excelDataList, srcFile, "测试.xlsx");

            System.out.println(file.getAbsoluteFile());
        } catch (IOException | TemplateException e) {
           log.error("error", e);
        }
    }


}
