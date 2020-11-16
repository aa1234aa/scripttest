package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/25 21:49
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.FuelGeneratorModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IFuelGeneratorModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.FuelGeneratorModelUrl;
import com.bitnei.cloud.sys.web.data.FuelGeneratorModelUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.FuelGeneratorModelResult;
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
public class FuelGeneratorModelControllerTest extends BaseControllerTest {


    @Resource
    FuelGeneratorModelUtil fuelGeneratorModelUtil;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    FuelGeneratorModelResult fuelGeneratorModelResult;

    @Resource
    IFuelGeneratorModelService iFuelGeneratorModelService;

    @Autowired
    private IUnitService iUnitService;

    private FuelGeneratorModelModel fuelGeneratorModelModel;
    private FuelGeneratorModelUrl fuelGeneratorModelUrl;
    private UnitModel unitModel;
    private FuelGeneratorModelModel f;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private String prodUnitId;
    private String controllerProdUnitId;



    @Before
    public void before(){
        //0.先实例url地址对象
        fuelGeneratorModelUrl=new FuelGeneratorModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        prodUnitId=unitModel.getId();
        controllerProdUnitId=unitModel.getId();
        //2.实例化并赋值测试数据的VO对象
        fuelGeneratorModelModel=fuelGeneratorModelUtil.createModel();
        fuelGeneratorModelModel.setProdUnitId(prodUnitId);
        fuelGeneratorModelModel.setControllerProdUnitId(controllerProdUnitId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertFuelGeneratorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = fuelGeneratorModelResult.getAddResult(fuelGeneratorModelUrl.getAdd_url(),fuelGeneratorModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        fuelGeneratorModelModel=iFuelGeneratorModelService.getByName(fuelGeneratorModelModel.getName());
        String id=fuelGeneratorModelModel.getId();
        iFuelGeneratorModelService.deleteMulti(id);
    }

    @Test
    public void getAllFuelGeneratorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=fuelGeneratorModelResult.getAllResult(fuelGeneratorModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateFuelGeneratorModel() throws Exception {
        //1.先进行新增数据
        iFuelGeneratorModelService.insert(fuelGeneratorModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new FuelGeneratorModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iFuelGeneratorModelService.getByName(fuelGeneratorModelModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneirydj" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=fuelGeneratorModelResult.getUpdateResult(fuelGeneratorModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iFuelGeneratorModelService.deleteMulti(id);
    }

    @Test
    public void getFuelGeneratorModelByName() throws Exception {

        //1.先进行新增数据
        iFuelGeneratorModelService.insert(fuelGeneratorModelModel);
        //2.取得新增成功的对象
        fuelGeneratorModelModel=iFuelGeneratorModelService.getByName(fuelGeneratorModelModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(fuelGeneratorModelModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=fuelGeneratorModelResult.getModelByNameResult(fuelGeneratorModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=fuelGeneratorModelModel.getId();
        iFuelGeneratorModelService.deleteMulti(id);
    }

    @Test
    public void getFuelGeneratorModelDetailById() throws Exception {

        //1.先进行新增数据
        iFuelGeneratorModelService.insert(fuelGeneratorModelModel);
        //2.取得新增成功的对象
        fuelGeneratorModelModel=iFuelGeneratorModelService.getByName(fuelGeneratorModelModel.getName());
        String id=fuelGeneratorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=fuelGeneratorModelResult.getDetailByIdResult(fuelGeneratorModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iFuelGeneratorModelService.deleteMulti(id);
    }

    @Test
    public void deleteFuelGeneratorModel() throws Exception {

        //1.先进行新增数据
        iFuelGeneratorModelService.insert(fuelGeneratorModelModel);
        //2.取得新增成功的对象
        fuelGeneratorModelModel=iFuelGeneratorModelService.getByName(fuelGeneratorModelModel.getName());
        String id=fuelGeneratorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=fuelGeneratorModelResult.getDeleteByIdResult(fuelGeneratorModelUrl.getDetail_url(),id);
    }
}
