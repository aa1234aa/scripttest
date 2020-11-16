package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/29 20:49
*/


import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.model.UnitModel;
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
public class OwnerPeopleUtil {


    private String ownerName= RandomValue.getChineseName();
    private String unitId;
    private String unitName;
    private String jobPost="单元测试岗位";
    private String jobNumber="667788";
    private Integer sex=1;
    private String telPhone=RandomValue.getTel();
    private String address=RandomValue.getRoad();
    private String email=RandomValue.getEmail(3,10);
    private Integer cardType=1;
    private String cardNo= RandomValue.getCardNo();
    private String cardAddress=RandomValue.getRoad();
    private String frontCardImgId="2d2a1fef19304ce2a158420152fa9216";
    private String backCardImgId="2d2a1fef19304ce2a158420152fa9216";
    private String faceCardImgId="2d2a1fef19304ce2a158420152fa9216";

    private OwnerPeople ownerPeople;
    private OwnerPeopleModel ownerPeopleModel;
    
    
    public OwnerPeopleModel createModel(){
        ownerPeople=new OwnerPeople();
        ownerPeople.setOwnerName(ownerName);
        ownerPeople.setJobPost(jobPost);
        ownerPeople.setJobNumber(jobNumber);
        ownerPeople.setSex(sex);
        ownerPeople.setTelPhone(telPhone);
        ownerPeople.setAddress(address);
        ownerPeople.setEmail(email);
        ownerPeople.setCardType(cardType);
        ownerPeople.setCardNo(cardNo);
        ownerPeople.setCardAddress(cardAddress);
        ownerPeople.setFrontCardImgId(frontCardImgId);
        ownerPeople.setBackCardImgId(backCardImgId);
        ownerPeople.setFaceCardImgId(faceCardImgId);
        ownerPeopleModel=OwnerPeopleModel.fromEntry(ownerPeople);
        return ownerPeopleModel;
    }
  
}
