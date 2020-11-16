package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/25 22:57
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.PowerDevice;
import com.bitnei.cloud.sys.model.FuelGeneratorModelModel;
import com.bitnei.cloud.sys.model.PowerDeviceModel;
import com.bitnei.cloud.sys.service.IFuelGeneratorModelService;
import com.bitnei.cloud.sys.service.IPowerDeviceService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.PowerDeviceUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.PowerDeviceModelUtil;
import com.bitnei.cloud.sys.web.result.PowerDeviceResult;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PowerDeviceControllerTest extends BaseControllerTest {


    @Autowired
    private MockMvc mvc;

    @Resource
    PowerDeviceModelUtil powerDeviceModelUtil;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    PowerDeviceResult powerDeviceResult;

    @Resource
    IPowerDeviceService iPowerDeviceService;

    @Resource
    IFuelGeneratorModelService iFuelGeneratorModelService;


    private PowerDevice powerDevice;
    private PowerDeviceModel powerDeviceModel;
    private PowerDeviceModel p;
    private FuelGeneratorModelModel fuelGeneratorModelModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private String fuelGeneratorModelId;
    private PowerDeviceUrl powerDeviceUrl;



    @Before
    public void before(){

        //0.先实例url地址对象
        powerDeviceUrl=new PowerDeviceUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        fuelGeneratorModelModel=iFuelGeneratorModelService.getByName(fuelGeneratorModelModeName);
        fuelGeneratorModelId=fuelGeneratorModelModel.getId();
        //2.实例化并赋值测试数据的VO对象
        powerDeviceModel=powerDeviceModelUtil.createModel();
        powerDeviceModel.setFuelGeneratorModelId(fuelGeneratorModelId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertPowerDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = powerDeviceResult.getAddResult(powerDeviceUrl.getAdd_url(),powerDeviceModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        powerDeviceModel=iPowerDeviceService.getByCode(powerDeviceModel.getCode());
        String id=powerDeviceModel.getId();
        iPowerDeviceService.deleteMulti(id);
    }

    @Test
    public void getAllPowerDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=powerDeviceResult.getAllResult(powerDeviceUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updatePowerDevice() throws Exception {
        //1.先进行新增数据
        iPowerDeviceService.insert(powerDeviceModel);
        //2.再实例化是一个Vo对象进行修改
        p=new PowerDeviceModel();
        //3.先查询需要修改的Vo对象数据
        p=iPowerDeviceService.getByCode(powerDeviceModel.getCode());
        //4.取得所要修改的对象的id
        String id=p.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        p.setCode( "BITneifdzz" + RandomValue.getNum(1000, 9999));
        //6.取得id并设置值
        p.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=powerDeviceResult.getUpdateResult(powerDeviceUrl.getUpdate_url(),id,p);
        //8. 进行删除新增的数据 还原数据
        iPowerDeviceService.deleteMulti(id);
    }

    @Test
    public void getPowerDeviceByName() throws Exception {

        //1.先进行新增数据
        iPowerDeviceService.insert(powerDeviceModel);
        //2.取得新增成功的对象
        powerDeviceModel=iPowerDeviceService.getByCode(powerDeviceModel.getCode());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("code");
        c1.setValue(powerDeviceModel.getCode());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=powerDeviceResult.getModelByNameResult(powerDeviceUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=powerDeviceModel.getId();
        iPowerDeviceService.deleteMulti(id);
    }

    @Test
    public void getPowerDeviceDetailById() throws Exception {

        //1.先进行新增数据
        iPowerDeviceService.insert(powerDeviceModel);
        //2.取得新增成功的对象
        powerDeviceModel=iPowerDeviceService.getByCode(powerDeviceModel.getCode());
        String id=powerDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=powerDeviceResult.getDetailByIdResult(powerDeviceUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iPowerDeviceService.deleteMulti(id);
    }

    @Test
    public void deletePowerDevice() throws Exception {

        //1.先进行新增数据
        iPowerDeviceService.insert(powerDeviceModel);
        //2.取得新增成功的对象的id进行删除
        powerDeviceModel=iPowerDeviceService.getByCode(powerDeviceModel.getCode());
        String id=powerDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=powerDeviceResult.getDeleteByIdResult(powerDeviceUrl.getDelete_url(),id);
    }
    
    
}
