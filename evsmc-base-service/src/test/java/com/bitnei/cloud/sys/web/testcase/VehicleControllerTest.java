package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/28 16:01
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.*;
import com.bitnei.cloud.sys.service.*;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehicleUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.SimManagementUtil;
import com.bitnei.cloud.sys.web.data.TermModelUnitUtil;
import com.bitnei.cloud.sys.web.data.VehicleUtil;
import com.bitnei.cloud.sys.web.result.VehicleResult;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
public class VehicleControllerTest extends BaseControllerTest {

    @Resource
    VehicleUtil vehicleUtil;

    @Resource
    VehicleResult vehicleResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    TermModelUnitUtil termModelUnitUtil;

    @Resource
    IVehicleService iVehicleService;

    @Resource
    SimManagementUtil simManagementUtil;

    @Resource
    private ITermModelUnitService iTermModelUnitService;

    @Resource
    private ITermModelService iTermModelService;

    @Resource
    private IVehModelService iVehModelService;


    @Resource
    IUnitService iUnitService;

    @Resource
    ISimManagementService iSimManagementService;


    private VehicleModel vehicleModel;
    private VehicleModel u;
    private VehModelModel vehModelModel;
    private TermModelUnitModel termModelUnitModel;
    private TermModelModel termModelModel;
    private SimManagementModel simManagementModel;
    private UnitModel unitModel1;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehicleUrl vehicleUrl;



    private String vehModelId;
    /** 终端编号型号id **/
    private String termId;
    /** 生产单位id **/
    private String manuUnitId;
    /** 终端型号ID **/
    private String sysTermModelId;
    /** 运营商   1.联通 2移动  3电信 **/
    private String simId;



    @Before
    public void before(){
        //0.先实例url地址对象
        vehicleUrl=new VehicleUrl();

        //1.先查询关联的车辆型号ID
        vehModelModel=iVehModelService.getByName(MODELNAME);
        vehModelId=vehModelModel.getId();

        //2.查询车载终端id
        termModelModel=iTermModelService.findByTermModelName(TermModelName);
        sysTermModelId=termModelModel.getId();

        //3.新增sim卡
        simManagementModel =simManagementUtil.createModel();
        iSimManagementService.insert(simManagementModel);
        simManagementModel=iSimManagementService.findByIccId(simManagementModel.getIccid());
        simId=simManagementModel.getId();

        //4.新增终端编号信息
        termModelUnitModel = termModelUnitUtil.createModel();
        termModelUnitModel.setSysTermModelId(sysTermModelId);
        termModelUnitModel.setSupportProtocol(SUPPORTPROTOCOL);
        termModelUnitModel.setIccid(simManagementModel.getIccid());
        iTermModelUnitService.insert(termModelUnitModel);
        termModelUnitModel=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        termId=termModelUnitModel.getId();

        //5.查询生产厂家id
        unitModel1=iUnitService.getByName(UNITFACTORYNAME);
        manuUnitId=unitModel1.getId();

        //6.实例化并赋值测试数据的po对象 转成VO对象
        vehicleModel = vehicleUtil.createModel();
        vehicleModel.setTermId(termId);
        vehicleModel.setManuUnitId(manuUnitId);
        vehicleModel.setVehModelId(vehModelId);

        //7.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @After
    public void after(){
        iTermModelUnitService.deleteMulti(termId);
        iSimManagementService.deleteMulti(simId);

    }


    @Test
    public void insertVehicle() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehicleResult.getAddResult(vehicleUrl.getAdd_url(),vehicleModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehicleModel=iVehicleService.getByVin(vehicleModel.getVin());
        String id=vehicleModel.getId();
        iVehicleService.deleteMulti(id);

    }

    @Test
    public void getAllVehicle() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehicleResult.getAllResult(vehicleUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehicle() throws Exception {
        //1.先进行新增
        iVehicleService.insert(vehicleModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehicleModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehicleService.getByVin(vehicleModel.getVin());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setVin(RandomValue.getVin());
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehicleResult.getUpdateResult(vehicleUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehicleService.deleteMulti(id);
    }

    @Test
    public void getVehicle() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iVehicleService.insert(vehicleModel);
        //2.取得新增成功的对象
        vehicleModel=iVehicleService.getByVin(vehicleModel.getVin());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("vin");
        c1.setValue(vehicleModel.getVin());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehicleResult.getModelByNameResult(vehicleUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehicleModel.getId();
        iVehicleService.deleteMulti(id);
    }

    @Test
    public void getVehicleDetailById() throws Exception {

        //1.先进行新增数据
        iVehicleService.insert(vehicleModel);
        //2.取得新增成功的对象
        vehicleModel=iVehicleService.getByVin(vehicleModel.getVin());
        String id=vehicleModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehicleResult.getDetailByIdResult(vehicleUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehicleService.deleteMulti(id);
    }

    @Test
    public void deleteVehicle() throws Exception {

        //1.先进行新增数据
        iVehicleService.insert(vehicleModel);
        //2.取得新增成功的对象的id进行删除
        vehicleModel=iVehicleService.getByVin(vehicleModel.getVin());
        String id=vehicleModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehicleResult.getDeleteByIdResult(vehicleUrl.getDelete_url(),id);

    }
}
