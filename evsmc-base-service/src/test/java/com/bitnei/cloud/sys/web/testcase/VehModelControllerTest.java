package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/28 11:21
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.model.VehModelModel;
import com.bitnei.cloud.sys.model.VehNoticeModel;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.bitnei.cloud.sys.service.IVehNoticeService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehModelUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehModelUtil;
import com.bitnei.cloud.sys.web.result.VehModelResult;
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
public class VehModelControllerTest extends BaseControllerTest {

    @Resource
    VehModelUtil vehModelUtil;

    @Resource
    VehModelResult vehModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IVehNoticeService iVehNoticeService;

    @Resource
    IVehModelService iVehModelService;

    @Resource
    IUnitService iUnitService;


    private VehModelModel vehModelModel;
    private VehModelModel u;
    private VehNoticeModel vehNoticeModel;
    private UnitModel unitModel1;
    private UnitModel unitModel2;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehModelUrl vehModelUrl;

    private String noticeId;
    private String vehUnitId;
    private String controllerUnitId;
    private String chassisUnitId;

    @Before
    public void before(){

        //0.先实例url地址对象
        vehModelUrl=new VehModelUrl();
        //1.先查询关联的车辆公告 汽车厂商 供应商
        vehNoticeModel=iVehNoticeService.getByName(VEHNOTICENAME);
        noticeId=vehNoticeModel.getId();

        unitModel1=iUnitService.getByName(UNITPRODUCERNAME);
        vehUnitId=unitModel1.getId();


        unitModel2=iUnitService.getByName(UNITSUPPLIERNAME);
        controllerUnitId=unitModel2.getId();
        chassisUnitId=unitModel2.getId();


        //2.实例化并赋值测试数据的po对象 转成VO对象

        vehModelModel=vehModelUtil.createModel();
        vehModelModel.setNoticeId(noticeId);
        vehModelModel.setRuleId(RULEID);
        vehModelModel.setVehUnitId(vehUnitId);
        vehModelModel.setControllerUnitId(controllerUnitId);
        vehModelModel.setChassisUnitId(chassisUnitId);

        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertVehModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehModelResult.getAddResult(vehModelUrl.getAdd_url(),vehModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehModelModel=iVehModelService.getByName(vehModelModel.getVehModelName());
        String id=vehModelModel.getId();
        iVehModelService.deleteMulti(id);

    }

    @Test
    public void getAllVehModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehModelResult.getAllResult(vehModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehModel() throws Exception {
        //1.先进行新增
        iVehModelService.insert(vehModelModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehModelModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehModelService.getByName(vehModelModel.getVehModelName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setVehModelName("BITneivehModel"+RandomValue.getNum(1000,9999));
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehModelResult.getUpdateResult(vehModelUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehModelService.deleteMulti(id);
    }

    @Test
    public void getVehVehModel() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iVehModelService.insert(vehModelModel);
        //2.取得新增成功的对象
        vehModelModel=iVehModelService.getByName(vehModelModel.getVehModelName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("vehModelName");
        c1.setValue(vehModelModel.getVehModelName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehModelResult.getModelByNameResult(vehModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehModelModel.getId();
        iVehModelService.deleteMulti(id);
    }

    @Test
    public void getVehModelDetailById() throws Exception {

        //1.先进行新增数据
        iVehModelService.insert(vehModelModel);
        //2.取得新增成功的对象
        vehModelModel=iVehModelService.getByName(vehModelModel.getVehModelName());
        String id=vehModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehModelResult.getDetailByIdResult(vehModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehModelService.deleteMulti(id);
    }

    @Test
    public void deleteVehModel() throws Exception {

        //1.先进行新增数据
        iVehModelService.insert(vehModelModel);
        //2.取得新增成功的对象的id进行删除
        vehModelModel=iVehModelService.getByName(vehModelModel.getVehModelName());
        String id=vehModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehModelResult.getDeleteByIdResult(vehModelUrl.getDelete_url(),id);

    }
}
