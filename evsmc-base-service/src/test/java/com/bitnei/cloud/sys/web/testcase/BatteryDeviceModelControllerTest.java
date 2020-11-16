package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/26 17:07
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.BatteryDeviceModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IBatteryDeviceModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.BatteryDeviceModelUrl;
import com.bitnei.cloud.sys.web.data.BatteryDeviceModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.BatteryDeviceModelResult;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatteryDeviceModelControllerTest extends BaseControllerTest {

    @Resource
    BatteryDeviceModelUtil batteryDeviceModelUtil;

    @Resource
    BatteryDeviceModelResult batteryDeviceModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IBatteryDeviceModelService iBatteryDeviceModelService;


    @Autowired
    private IUnitService iUnitService;

    private BatteryDeviceModelModel batteryDeviceModelModel;
    private UnitModel unitModel;
    private BatteryDeviceModelModel f;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private BatteryDeviceModelUrl batteryDeviceModelUrl;
    private String unitId;
    private String batteryCellUnitId;
    private String bmsUnitId;



    @Before
    public void before(){
        //0.先实例url地址对象
        batteryDeviceModelUrl=new BatteryDeviceModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        unitId=unitModel.getId();
        batteryCellUnitId=unitModel.getId();
        bmsUnitId=unitModel.getId();

        //2.实例化并赋值测试数据的VO对象
        batteryDeviceModelModel=batteryDeviceModelUtil.createModel();
        batteryDeviceModelModel.setUnitId(unitId);
        batteryDeviceModelModel.setBatteryCellUnitId(batteryCellUnitId);
        batteryDeviceModelModel.setBmsUnitId(bmsUnitId);

        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertBatteryDeviceModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = batteryDeviceModelResult.getAddResult(batteryDeviceModelUrl.getAdd_url(),batteryDeviceModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        batteryDeviceModelModel=iBatteryDeviceModelService.findByName(batteryDeviceModelModel.getName());
        String id=batteryDeviceModelModel.getId();
        iBatteryDeviceModelService.deleteMulti(id);
    }

    @Test
    public void getAllBatteryDeviceModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=batteryDeviceModelResult.getAllResult(batteryDeviceModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateBatteryDeviceModel() throws Exception {
        //1.先进行新增数据
        iBatteryDeviceModelService.insert(batteryDeviceModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new BatteryDeviceModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iBatteryDeviceModelService.findByName(batteryDeviceModelModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneidlcdc" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=batteryDeviceModelResult.getUpdateResult(batteryDeviceModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iBatteryDeviceModelService.deleteMulti(id);
    }

    @Test
    public void getBatteryDeviceModelByName() throws Exception {

        //1.先进行新增数据
        iBatteryDeviceModelService.insert(batteryDeviceModelModel);
        //2.取得新增成功的对象
        batteryDeviceModelModel=iBatteryDeviceModelService.findByName(batteryDeviceModelModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(batteryDeviceModelModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=batteryDeviceModelResult.getModelByNameResult(batteryDeviceModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=batteryDeviceModelModel.getId();
        iBatteryDeviceModelService.deleteMulti(id);
    }

    @Test
    public void getBatteryDeviceModelDetailById() throws Exception {

        //1.先进行新增数据
        iBatteryDeviceModelService.insert(batteryDeviceModelModel);
        //2.取得新增成功的对象
        batteryDeviceModelModel=iBatteryDeviceModelService.findByName(batteryDeviceModelModel.getName());
        String id=batteryDeviceModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=batteryDeviceModelResult.getDetailByIdResult(batteryDeviceModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iBatteryDeviceModelService.deleteMulti(id);
    }

    @Test
    public void deleteBatteryDeviceModel() throws Exception {

        //1.先进行新增数据
        iBatteryDeviceModelService.insert(batteryDeviceModelModel);
        //2.取得新增成功的对象的id进行删除
        batteryDeviceModelModel=iBatteryDeviceModelService.findByName(batteryDeviceModelModel.getName());
        String id=batteryDeviceModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=batteryDeviceModelResult.getDeleteByIdResult(batteryDeviceModelUrl.getDelete_url(),id);
    }
}
