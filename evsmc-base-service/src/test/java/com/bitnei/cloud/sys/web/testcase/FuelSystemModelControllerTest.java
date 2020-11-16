package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/14 10:41
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.FuelSystemModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IFuelSystemModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.FuelSystemModelUrl;
import com.bitnei.cloud.sys.web.data.FuelSystemModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.FuelSystemModeResult;
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
public class FuelSystemModelControllerTest extends BaseControllerTest {

    @Resource
    IFuelSystemModelService iFuelSystemModelService;

    @Resource
    IUnitService iUnitService;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    FuelSystemModelUtil fuelSystemModelUtil;

    @Resource
    FuelSystemModeResult fuelSystemModeResult;


    private FuelSystemModelModel fuelSystemModelModel;
    private UnitModel unitModel;
    private FuelSystemModelModel f;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private String prodUnitId;
    private String controllerProdUnitId;
    private FuelSystemModelUrl fuelSystemModelUrl;


    @Before
    public void before(){
        //0.先实例url地址对象
        fuelSystemModelUrl=new FuelSystemModelUrl();
        //1.实例化并查询单位(默认供应商)
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        prodUnitId=unitModel.getId();
        controllerProdUnitId=unitModel.getId();
        //2.实例化并新增测试数据
        fuelSystemModelModel=fuelSystemModelUtil.createModel();
        fuelSystemModelModel.setProdUnitId(prodUnitId);
        fuelSystemModelModel.setProdUnitDisplay(unitModel.getName());
        fuelSystemModelModel.setControllerProdUnitId(controllerProdUnitId);
        fuelSystemModelModel.setControllerProdUnitId(unitModel.getName());
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }



    @Test
    public void insertFuelSystemModel() throws Exception {

        //1.执行测试并验证结果
        ResultActions actions = fuelSystemModeResult.getAddResult(fuelSystemModelUrl.getAdd_url(),fuelSystemModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        fuelSystemModelModel=iFuelSystemModelService.getByName(fuelSystemModelModel.getName());
        String id=fuelSystemModelModel.getId();
        iFuelSystemModelService.deleteMulti(id);
    }

    @Test
    public void getAllFuelSystemModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=fuelSystemModeResult.getAllResult(fuelSystemModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateFuelSystemModel() throws Exception {
        //1.先进行新增数据
        iFuelSystemModelService.insert(fuelSystemModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new FuelSystemModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iFuelSystemModelService.getByName(fuelSystemModelModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneirldc" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=fuelSystemModeResult.getUpdateResult(fuelSystemModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iFuelSystemModelService.deleteMulti(id);
    }

    @Test
    public void getFuelSystemModelByName() throws Exception {

        //1.先进行新增数据
        iFuelSystemModelService.insert(fuelSystemModelModel);
        //2.取得新增成功的对象
        fuelSystemModelModel=iFuelSystemModelService.getByName(fuelSystemModelModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(fuelSystemModelModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=fuelSystemModeResult.getModelByNameResult(fuelSystemModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=fuelSystemModelModel.getId();
        iFuelSystemModelService.deleteMulti(id);
    }

    @Test
    public void getFuelSystemModelDetailById() throws Exception {

        //1.先进行新增数据
        iFuelSystemModelService.insert(fuelSystemModelModel);
        //2.取得新增成功的对象
        fuelSystemModelModel=iFuelSystemModelService.getByName(fuelSystemModelModel.getName());
        String id=fuelSystemModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=fuelSystemModeResult.getDetailByIdResult(fuelSystemModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iFuelSystemModelService.deleteMulti(id);
    }

    @Test
    public void deleteFuelSystemModel() throws Exception {

        //1.先进行新增数据
        iFuelSystemModelService.insert(fuelSystemModelModel);
        //2.取得新增成功的对象的id进行删除
        fuelSystemModelModel=iFuelSystemModelService.getByName(fuelSystemModelModel.getName());
        String id=fuelSystemModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=fuelSystemModeResult.getDeleteByIdResult(fuelSystemModelUrl.getDelete_url(),id);
    }
}
