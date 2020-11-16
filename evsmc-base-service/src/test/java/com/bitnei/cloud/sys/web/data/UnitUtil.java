package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/29 19:47
*/

import com.bitnei.cloud.sys.domain.Unit;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.model.VehOwnerModel;
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
public class UnitUtil {

    /**
     * 单位名称 *
     */
    private String name="bitnei单位"+ RandomValue.getNum(1000,9999);
    /**
     * 单位简称 *
     */
    private String nickName="bit"+RandomValue.getNum(100,999);
    /**
     * 是否根节点 *
     */
    private Integer isRoot=0;
    /**
     * 父节点id *
     */
    private String parentId="0";
    /**
     * 单位地址 *
     */
    private String address="广东省揭阳市榕城区榕东街道西林社区居民委员会";
    /**
     * 座机号 *
     */
    private String telephone=RandomValue.getTel();
    /**
     * 业务种类 *
     */
    private String bussinessLines="单元测试业务";
    /**
     * 产品品牌，多个用逗号分隔 *
     */
    private String brands="单元测试品牌";
    /**
     * 统一社会信用代码 *
     */
    private String organizationCode="RRXX44443383"+RandomValue.getNum(100000,999999);
    /**
     * 营业执照扫描图片id *
     */
    private String licenceImgId="fc561c1d589b4da3b99a970780d32d54";
    /**
     * 授权书扫描件ID *
     */
    private String certImgId="fc561c1d589b4da3b99a970780d32d54";
    /**
     * 联系人姓名 *
     */
    private String contactorName=RandomValue.getChineseName();
    /**
     * 联系人手机号 *
     */
    private String contactorPhone=RandomValue.getTel();
    /**
     * 联系人地址 *
     */
    private String contactorAddress="广东省揭阳市榕城区榕东街道西林社区居民委员会";
//    /**
//     * 单位类型id，多个用逗号分隔
//     */
//    private String unitTypeIds;
//    /**
//     * 单位类型名称，多个用逗号分隔
//     */
//    private String unitTypeNames;
//    /**
//     * 经度
//     */
    private String lng="116.406819";
    /**
     * 纬度
     */
    private String lat="23.535941";

    private Unit unit;
    private UnitModel unitModel;

    public UnitModel createFactoryModel(){
        //车辆制造工厂
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("0182c2421fbf48469cc3d65244c76361");
        unit.setUnitTypeNames("车辆制造工厂");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel createOperationModel(){
        //运营单位
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("3697640328be46eab6fd34afdba5e521");
        unit.setUnitTypeNames("运营单位");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel createProducerModel(){
        //汽车厂商
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("3");
        unit.setUnitTypeNames("汽车厂商");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel createSupplierModel(){
        //供应商
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("ed53a39efb8b4243abeb9fdd080ceb6f");
        unit.setUnitTypeNames("供应商");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel createDealerModel(){
        //经销商
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("b247a5e5fb7043ff849751c009b4a1b6");
        unit.setUnitTypeNames("经销商");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel createPurchaseUnitModel(){
        //购车单位
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("94f78b1625174dfb834f4e58e52d9358");
        unit.setUnitTypeNames("购车单位");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }

    public UnitModel create4sModel(){
        //4s店
        unit=new Unit();
        unit.setName(name);
        unit.setNickName(nickName);
        unit.setIsRoot(isRoot);
        unit.setParentId(parentId);
        unit.setAddress(address);
        unit.setTelephone(telephone);
        unit.setBrands(brands);
        unit.setBussinessLines(bussinessLines);
        unit.setOrganizationCode(organizationCode);
        unit.setLicenceImgId(licenceImgId);
        unit.setCertImgId(certImgId);
        unit.setBussinessLines(bussinessLines);
        unit.setContactorAddress(contactorAddress);
        unit.setContactorPhone(contactorPhone);
        unit.setContactorName(contactorName);
        unit.setUnitTypeIds("9880e1eeb1204d76b5951b88527dd996");
        unit.setUnitTypeNames("售后服务网点4S店");
        unit.setLat(lat);
        unit.setLng(lng);
        unitModel = new UnitModel();
        unitModel = UnitModel.fromEntry(unit);
        return unitModel;
    }
}
