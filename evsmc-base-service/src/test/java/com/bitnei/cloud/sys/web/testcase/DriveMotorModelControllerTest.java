package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/26 13:33
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.DriveMotorModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IDriveMotorModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.DriveMotorModelUrl;
import com.bitnei.cloud.sys.web.data.DriveMotorModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.DriveMotorModelResult;
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
public class DriveMotorModelControllerTest extends BaseControllerTest {

    @Resource
    DriveMotorModelUtil driveMotorModelUtil;

    @Resource
    DriveMotorModelResult driveMotorModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IDriveMotorModelService iDriveMotorModelService;


    @Autowired
    private IUnitService iUnitService;

    private DriveMotorModelModel driveMotorModelModel;
    private UnitModel unitModel;
    private DriveMotorModelModel f;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private DriveMotorModelUrl driveMotorModelUrl;
    private String prodUnitId;
    private String controllerProdUnitId;



    @Before
    public void before(){
        //0.先实例url地址对象
        driveMotorModelUrl=new DriveMotorModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        prodUnitId=unitModel.getId();
        controllerProdUnitId=unitModel.getId();
        //2.实例化并赋值测试数据的VO对象
        driveMotorModelModel=driveMotorModelUtil.createModel();
        driveMotorModelModel.setProdUnitId(prodUnitId);
        driveMotorModelModel.setControllerProdUnitId(controllerProdUnitId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertDriveMotorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = driveMotorModelResult.getAddResult(driveMotorModelUrl.getAdd_url(),driveMotorModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        driveMotorModelModel=iDriveMotorModelService.findByName(driveMotorModelModel.getName());
        String id=driveMotorModelModel.getId();
        iDriveMotorModelService.deleteMulti(id);
    }

    @Test
    public void getAllDriveMotorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=driveMotorModelResult.getAllResult(driveMotorModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateDriveMotorModel() throws Exception {
        //1.先进行新增数据
        iDriveMotorModelService.insert(driveMotorModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new DriveMotorModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iDriveMotorModelService.findByName(driveMotorModelModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneiqddjxh" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=driveMotorModelResult.getUpdateResult(driveMotorModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iDriveMotorModelService.deleteMulti(id);
    }

    @Test
    public void getDriveMotorModelByName() throws Exception {

        //1.先进行新增数据
        iDriveMotorModelService.insert(driveMotorModelModel);
        //2.取得新增成功的对象
        driveMotorModelModel=iDriveMotorModelService.findByName(driveMotorModelModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(driveMotorModelModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=driveMotorModelResult.getModelByNameResult(driveMotorModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=driveMotorModelModel.getId();
        iDriveMotorModelService.deleteMulti(id);
    }

    @Test
    public void getDriveMotorModelDetailById() throws Exception {

        //1.先进行新增数据
        iDriveMotorModelService.insert(driveMotorModelModel);
        //2.取得新增成功的对象
        driveMotorModelModel=iDriveMotorModelService.findByName(driveMotorModelModel.getName());
        String id=driveMotorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=driveMotorModelResult.getDetailByIdResult(driveMotorModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iDriveMotorModelService.deleteMulti(id);
    }

    @Test
    public void deleteDriveMotorModel() throws Exception {

        //1.先进行新增数据
        iDriveMotorModelService.insert(driveMotorModelModel);
        //2.取得新增成功的对象的id进行删除
        driveMotorModelModel=iDriveMotorModelService.findByName(driveMotorModelModel.getName());
        String id=driveMotorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=driveMotorModelResult.getDeleteByIdResult(driveMotorModelUrl.getDelete_url(),id);
    }
}
