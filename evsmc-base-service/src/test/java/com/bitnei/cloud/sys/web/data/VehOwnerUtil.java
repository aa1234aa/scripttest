package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/29 18:34
*/

import com.bitnei.cloud.sys.domain.VehOwner;
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
public class VehOwnerUtil {

    /**姓名 */
    private String ownerName=RandomValue.getChineseName();
    /**性别 */
    private Integer sex=1;
    /**手机号 */
    private String telPhone=RandomValue.getTel();
    /**联系地址 */
    private String address=RandomValue.getRoad();
    /**电子邮箱 */
    private String email=RandomValue.getEmail(5,10);
    /**证件类型 1:居民身份证 2:士官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证 */
    private Integer cardType=1;
    /**证件号码 */
    private String cardNo= RandomValue.getCardNo();
    /**证件地址 */
    private String cardAddress=RandomValue.getRoad();
    /**证件正面照id */
    private String frontCardImgId="077cc8586ec84d13a3ab9415ac474dd5";
    /**证件反面照id */
    private String backCardImgId="077cc8586ec84d13a3ab9415ac474dd5";
    /**手持证件照id */
    private String faceCardImgId="077cc8586ec84d13a3ab9415ac474dd5";

    private VehOwner vehOwner;
    private VehOwnerModel vehOwnerModel;

    public VehOwnerModel  createModel(){

        vehOwner = new VehOwner();
        vehOwner.setOwnerName(ownerName);
        vehOwner.setSex(sex);
        vehOwner.setTelPhone(telPhone);
        vehOwner.setAddress(address);
        vehOwner.setEmail(email);
        vehOwner.setCardType(cardType);
        vehOwner.setCardNo(cardNo);
        vehOwner.setFrontCardImgId(frontCardImgId);
        vehOwner.setBackCardImgId(backCardImgId);
        vehOwner.setFaceCardImgId(faceCardImgId);
        vehOwner.setCardAddress(cardAddress);
        vehOwnerModel = new VehOwnerModel();
        vehOwnerModel = vehOwnerModel.fromEntry(vehOwner);
        return vehOwnerModel;
    }

}
