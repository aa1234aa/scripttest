package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/26 14:46
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.DriveDeviceModel;
import com.bitnei.cloud.sys.model.DriveMotorModelModel;
import com.bitnei.cloud.sys.service.IDriveDeviceService;
import com.bitnei.cloud.sys.service.IDriveMotorModelService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.DriveDeviceModelUrl;
import com.bitnei.cloud.sys.web.data.DriveDeviceModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.DriveDeviceModelResult;
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
public class DriveDeviceControllerTest extends BaseControllerTest {

    @Resource
    DriveDeviceModelUtil driveDeviceModelUtil;

    @Resource
    DriveDeviceModelResult driveDeviceModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IDriveMotorModelService iDriveMotorModelService;

    @Resource
    IDriveDeviceService iDriveDeviceService;


    private DriveDeviceModel driveDeviceModel;
    private DriveDeviceModel f;
    private DriveMotorModelModel driveMotorModelModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private DriveDeviceModelUrl driveDeviceModelUrl;
    private String driveModelId;






    @Before
    public void before(){
        //0.先实例url地址对象
        driveDeviceModelUrl=new DriveDeviceModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        driveMotorModelModel=iDriveMotorModelService.findByName(DriveMotorModelName);
        driveModelId=driveMotorModelModel.getId();
        //2.实例化并赋值测试数据的VO对象
        driveDeviceModel=driveDeviceModelUtil.createModel();
        driveDeviceModel.setDriveModelId(driveModelId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertDriveDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = driveDeviceModelResult.getAddResult(driveDeviceModelUrl.getAdd_url(),driveDeviceModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        driveDeviceModel=iDriveDeviceService.findByCode(driveDeviceModel.getCode());
        String id=driveDeviceModel.getId();
        iDriveDeviceService.deleteMulti(id);
    }

    @Test
    public void getAllDriveDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=driveDeviceModelResult.getAllResult(driveDeviceModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateDriveDevice() throws Exception {
        //1.先进行新增数据
        iDriveDeviceService.insert(driveDeviceModel);
        //2.再实例化是一个Vo对象进行修改
        f=new DriveDeviceModel();
        //3.先查询需要修改的Vo对象数据
        f=iDriveDeviceService.findByCode(driveDeviceModel.getCode());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setCode( "BITneiqdzzxx" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=driveDeviceModelResult.getUpdateResult(driveDeviceModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iDriveDeviceService.deleteMulti(id);
    }

    @Test
    public void getDriveDeviceByName() throws Exception {

        //1.先进行新增数据
        iDriveDeviceService.insert(driveDeviceModel);
        //2.取得新增成功的对象
        driveDeviceModel=iDriveDeviceService.findByCode(driveDeviceModel.getCode());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("code");
        c1.setValue(driveDeviceModel.getCode());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=driveDeviceModelResult.getModelByNameResult(driveDeviceModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=driveDeviceModel.getId();
        iDriveDeviceService.deleteMulti(id);
    }

    @Test
    public void getDriveDeviceDetailById() throws Exception {

        //1.先进行新增数据
        iDriveDeviceService.insert(driveDeviceModel);
        //2.取得新增成功的对象
        driveDeviceModel=iDriveDeviceService.findByCode(driveDeviceModel.getCode());
        String id=driveDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=driveDeviceModelResult.getDetailByIdResult(driveDeviceModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iDriveDeviceService.deleteMulti(id);
    }

    @Test
    public void deleteDriveDevice() throws Exception {

        //1.先进行新增数据
        iDriveDeviceService.insert(driveDeviceModel);
        //2.取得新增成功的对象的id进行删除
        driveDeviceModel=iDriveDeviceService.findByCode(driveDeviceModel.getCode());
        String id=driveDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=driveDeviceModelResult.getDeleteByIdResult(driveDeviceModelUrl.getDelete_url(),id);
    }
    
    
}
