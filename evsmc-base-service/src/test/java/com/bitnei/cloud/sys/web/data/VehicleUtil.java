package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 17:28
*/

import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.util.RandomValue;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@Service
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehicleUtil {

    /** 车架号 **/
    private String vin= RandomValue.getVin();
//    /** 系统内部编码 **/
//    private String uuid;
//    /** 车牌类型 **/
//    private Integer licenseType;
//    /** 车牌号 **/
//    private String licensePlate;
    /** 内部编号 **/
    private String interNo=RandomValue.getInterNo();
    /** 车型id **/
    private String vehModelId;
    /** 颜色 **/
    private String color="#FFFFFF";
    /** 颜色个性化名称 **/
    private String colorNickName="飞天白";
    /** 终端编号型号id **/
    private String termId;
//    /** 车辆阶段(1:生产 2:入库 3:销售 4:运营 5:报废) **/
//    private String stage;
//    /** 车辆阶段变更日期 **/
//    private String stageChangeDate;
    /** 生产单位id **/
    private String manuUnitId;
    /** 车辆生产批次 **/
    private String produceBatch=String.valueOf(RandomValue.getNum(1000,9999));
    /** 整车质保期(3年/10万公里) **/
    private String qualityYears=RandomValue.getNowTimeWithOutHMS();
    /** 车辆合格证编号 **/
    private String vehCertificateNumber=String.valueOf(RandomValue.getNum(10000000,99999999));
    /** 车辆生产日期 **/
    private String produceDate=RandomValue.getNowTimeWithOutHMS();
    /** 车辆出厂日期 **/
    private String factoryDate=RandomValue.getNowTimeWithOutHMS();
//    /** 销售日期 **/
//    private String sellDate;
//    /** 销售价格(万元) **/
//    private Double sellPrice;
//    /** 购车领域 **/
//    private Integer sellForField;
//    /** 私人车主ID **/
//    private String sellPriVehOwnerId;
//    /** 购车单位ID **/
//    private String sellPubUnitId;
//    /** 购车城市id **/
//    private String sellCityId;
//    /** 经销商 ID **/
//    private String sell4sUnitId;
//    /** 销售人员id **/
//    private String sellSellerId;
//    /** 保险投保日期 **/
//    private String sellSecureDate;
//    /** 首次年检时间 **/
//    private String sellFirstCheckDate;
//    /** 发票号 **/
//    private String sellInvoiceNo;
//    /** 发票时间 **/
//    private String sellInvoiceDate;
//    /** 发票图片id **/
//    private String sellInvoiceImgId;
//    /** 车辆行驶证号 **/
//    private String sellLicenseNo;
//    /** 行驶证注册日期 **/
//    private String sellLicenseRegDate;
//    /** 行驶证发放日期 **/
//    private String sellLicenseGrantDate;
//    /** 行驶证照片 **/
//    private String sellLicenseImgId;
//    /** 是否已新增销售信息 **/
//    private Integer isSelled;
//    /** 运营内部编号 **/
//    private String operInterNo;
//    /** 车辆用途 **/
//    private Integer operUseFor;
//    /** 车辆运营性质 **/
//    private Integer operUseType;
//    /** 用车个人id **/
//    private String operVehOwnerId;
//    /** 用车单位id **/
//    private String operUnitId;
//    /** 上牌城市id **/
//    private String operLicenseCityId;
//    /** 运营区域 **/
//    private String operAreaId;
//    /** 投运时间(yyyy-MM-dd) **/
//    private String operTime;
//    /** 售后负责人 **/
//    private String operSupportOwnerId;
//    /** 存放城市 **/
//    private String operSaveCityId;
//    /** 存放地址 **/
//    private String operSaveAddress;
//    /** 充电桩城市id **/
//    private String operChgCityId;
//    /** 充电桩地址 **/
//    private String operChgAddress;
//    /** 是否已新增运营信息 **/
//    private Integer isOpered;
//    /** 补贴申报状态 **/
//    private Integer subsidyApplyStatus;
//    /** 申报次数 **/
//    private Integer susidyApplyCount;

    private Vehicle vehicle;
    private VehicleModel vehicleModel;

    public VehicleModel createModel(){

        vehicle=new Vehicle();
        vehicle.setVin(vin);
        vehicle.setInterNo(interNo);
        vehicle.setVehModelId(vehModelId);
        vehicle.setColor(color);
        vehicle.setColorNickName(colorNickName);
        vehicle.setTermId(termId);
        vehicle.setManuUnitId(manuUnitId);
        vehicle.setProduceBatch(produceBatch);
        vehicle.setQualityYears(qualityYears);
        vehicle.setVehCertificateNumber(vehCertificateNumber);
        vehicle.setProduceDate(produceDate);

        vehicleModel = new VehicleModel();
        vehicleModel = VehicleModel.fromEntry(vehicle);
        return vehicleModel;

    }
}
