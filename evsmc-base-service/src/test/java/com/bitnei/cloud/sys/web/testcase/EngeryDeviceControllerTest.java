package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/26 20:42
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.BatteryDeviceModelModel;
import com.bitnei.cloud.sys.model.EngeryDeviceModel;
import com.bitnei.cloud.sys.service.IBatteryDeviceModelService;
import com.bitnei.cloud.sys.service.IEngeryDeviceService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.EngeryDeviceModelUrl;
import com.bitnei.cloud.sys.web.data.EngeryDeviceModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.EngeryDeviceModelResult;
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
public class EngeryDeviceControllerTest extends BaseControllerTest {

    @Resource
    EngeryDeviceModelUtil engeryDeviceModelUtil;

    @Resource
    EngeryDeviceModelResult engeryDeviceModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IEngeryDeviceService iEngeryDeviceService;

    @Resource
    IBatteryDeviceModelService iBatteryDeviceModelService;


    private EngeryDeviceModel engeryDeviceModel;
    private EngeryDeviceModel f;
    private BatteryDeviceModelModel batteryDeviceModelModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private String batteryModelId;
    private EngeryDeviceModelUrl engeryDeviceModelUrl;



    @Before
    public void before(){

        //0.先实例url地址对象
        engeryDeviceModelUrl=new EngeryDeviceModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        batteryDeviceModelModel=iBatteryDeviceModelService.findByName(batteryDeviceModelModelName);
        batteryModelId=batteryDeviceModelModel.getId();
        //2.实例化并赋值测试数据的VO对象
        engeryDeviceModel=engeryDeviceModelUtil.createModel();
        engeryDeviceModel.setBatteryModelId(batteryModelId);

        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertEngeryDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = engeryDeviceModelResult.getAddResult(engeryDeviceModelUrl.getAdd_url(),engeryDeviceModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        engeryDeviceModel=iEngeryDeviceService.findByName(engeryDeviceModel.getName());
        String id=engeryDeviceModel.getId();
        iEngeryDeviceService.deleteMulti(id);
    }

    @Test
    public void getAllEngeryDevice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=engeryDeviceModelResult.getAllResult(engeryDeviceModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateEngeryDevice() throws Exception {
        //1.先进行新增数据
        iEngeryDeviceService.insert(engeryDeviceModel);
        //2.再实例化是一个Vo对象进行修改
        f=new EngeryDeviceModel();
        //3.先查询需要修改的Vo对象数据
        f=iEngeryDeviceService.findByName(engeryDeviceModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneikcdcdc" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=engeryDeviceModelResult.getUpdateResult(engeryDeviceModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iEngeryDeviceService.deleteMulti(id);
    }

    @Test
    public void getEngeryDeviceByName() throws Exception {

        //1.先进行新增数据
        iEngeryDeviceService.insert(engeryDeviceModel);
        //2.取得新增成功的对象
        engeryDeviceModel=iEngeryDeviceService.findByName(engeryDeviceModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(engeryDeviceModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=engeryDeviceModelResult.getModelByNameResult(engeryDeviceModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=engeryDeviceModel.getId();
        iEngeryDeviceService.deleteMulti(id);
    }

    @Test
    public void getEngeryDeviceDetailById() throws Exception {

        //1.先进行新增数据
        iEngeryDeviceService.insert(engeryDeviceModel);
        //2.取得新增成功的对象
        engeryDeviceModel=iEngeryDeviceService.findByName(engeryDeviceModel.getName());
        String id=engeryDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=engeryDeviceModelResult.getDetailByIdResult(engeryDeviceModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iEngeryDeviceService.deleteMulti(id);
    }

    @Test
    public void deleteEngeryDevice() throws Exception {

        //1.先进行新增数据
        iEngeryDeviceService.insert(engeryDeviceModel);
        //2.取得新增成功的对象的id进行删除
        engeryDeviceModel=iEngeryDeviceService.findByName(engeryDeviceModel.getName());
        String id=engeryDeviceModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=engeryDeviceModelResult.getDeleteByIdResult(engeryDeviceModelUrl.getDelete_url(),id);
    }
}
