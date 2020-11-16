package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 14:14
*/

import com.bitnei.cloud.sys.domain.VehBrand;
import com.bitnei.cloud.sys.model.VehBrandModel;
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
public class VehBrandUtil {

    /** 名称 **/
    private String name="bitnei"+ RandomValue.getNum(1,9999);
    /** 英文品牌名称 **/
    private String englistName="BMW"+RandomValue.getNum(1000,9999);
    /** 编码 **/
    private String code=String.valueOf(RandomValue.getNum(1000,9999));
    /** 商标图片ID **/
    private String brandImgId;
    /** 备注 **/
    private String note="Test备注";

    private VehBrand vehBrand;
    private VehBrandModel vehBrandModel;

    public VehBrandModel createModel(){

        vehBrand=new VehBrand();
        vehBrand.setName(name);
        vehBrand.setEnglistName(englistName);
        vehBrand.setCode(code);
        vehBrand.setNote(note);
        vehBrandModel = new VehBrandModel();
        vehBrandModel = VehBrandModel.fromEntry(vehBrand);
        return vehBrandModel;

    }
}
