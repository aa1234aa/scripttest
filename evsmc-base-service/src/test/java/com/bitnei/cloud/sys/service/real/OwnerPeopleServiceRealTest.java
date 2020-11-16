package com.bitnei.cloud.sys.service.real;

/*
@author 黄永雄
@create 2019/11/11 19:53
*/

import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.util.DbUtil;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.commons.util.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OwnerPeopleServiceRealTest {

    @Autowired
    IOwnerPeopleService iownerPeopleService;

    private OwnerPeopleModel model;
    private PagerInfo pagerInfo=new PagerInfo();
    private List<Condition> conditionList=new ArrayList<>();

    private String uuid= UtilHelper.getUUID();
    private String ownerName= RandomValue.getChineseName();
    private String unitId="5847b30fa1854fd694790d4ed017b61f";
    private String unitName="高尔夫汽车售后服务有限公司";
    private String jobPost="销售员";
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

    private Connection conn;
    private Statement stmt;
    private ResultSet rs ;


    @Test
    public void test01InsertOwnerPeople(){

        model=new OwnerPeopleModel();
        model.setOwnerName(ownerName);
        model.setUnitId(unitId);
        model.setUnitName(unitName);
        model.setJobPost(jobPost);
        model.setJobNumber(jobNumber);
        model.setSex(sex);
        model.setTelPhone(telPhone);
        model.setAddress(address);
        model.setEmail(email);
        model.setCardType(cardType);
        model.setCardNo(cardNo);
        model.setCardAddress(cardAddress);
        model.setFrontCardImgId(frontCardImgId);
        model.setBackCardImgId(backCardImgId);
        model.setFaceCardImgId(faceCardImgId);

        iownerPeopleService.insert(model);
        Assert.assertEquals(model.getOwnerName(),ownerName);
        System.out.println("单位联系人id："+model.getId()+"---》单位联系人名："+model.getOwnerName());
    }

    @Test
    public void test02DeleteOwnerPeople() throws Exception {


        DbUtil dbUtil= new DbUtil();
        String sql=" SELECT id FROM sys_owner_people ORDER BY create_time DESC LIMIT 1";

        System.out.println(sql);
        conn=dbUtil.getCon();
        stmt=conn.createStatement();
        rs = stmt.executeQuery(sql) ;

        while(rs.next()){
            String owner_id=rs.getString("id");
            iownerPeopleService.deleteMulti(owner_id);
            System.out.println(owner_id);
        }
        dbUtil.closeCon(conn);

//        Assert.assertEquals(model.getId()==null,false);
    }

    @Test
    public void test03UpdateOwnerPeople(){

    }

    @Test
    public void test04getAll(){

        pagerInfo.setLimit(10);
        pagerInfo.setStart(0);
        Object obj=iownerPeopleService.list(pagerInfo);
        List<OwnerPeopleModel> models = new ArrayList<>();

        Assert.assertEquals(obj.toString().length()>1000,true);

    }

}
