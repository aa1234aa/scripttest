package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/26 16:30
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.SuperCapacitorModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.ISuperCapacitorModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.SuperCapacitorModelUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.SuperCapacitorModelUtil;
import com.bitnei.cloud.sys.web.result.SuperCapacitorModelResult;
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
public class SuperCapacitorModelControllerTest extends BaseControllerTest {


    @Resource
    SuperCapacitorModelUtil superCapacitorModelUtil;

    @Resource
    SuperCapacitorModelResult superCapacitorModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    ISuperCapacitorModelService iSuperCapacitorModelService;


    @Autowired
    private IUnitService iUnitService;

    private SuperCapacitorModelModel superCapacitorModelModel;
    private UnitModel unitModel;
    private SuperCapacitorModelModel f;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private SuperCapacitorModelUrl superCapacitorModelUrl;
    private String unitId;



    @Before
    public void before(){
        //0.先实例url地址对象
        superCapacitorModelUrl=new SuperCapacitorModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        unitId=unitModel.getId();
        //2.实例化并赋值测试数据的VO对象
        superCapacitorModelModel=superCapacitorModelUtil.createModel();
        superCapacitorModelModel.setUnitId(unitId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertSuperCapacitorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions =superCapacitorModelResult.getAddResult(superCapacitorModelUrl.getAdd_url(),superCapacitorModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        superCapacitorModelModel=iSuperCapacitorModelService.findByName(superCapacitorModelModel.getName());
        String id=superCapacitorModelModel.getId();
        iSuperCapacitorModelService.deleteMulti(id);
    }

    @Test
    public void getAllSuperCapacitorModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=superCapacitorModelResult.getAllResult(superCapacitorModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateSuperCapacitorModel() throws Exception {
        //1.先进行新增数据
        iSuperCapacitorModelService.insert(superCapacitorModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new SuperCapacitorModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iSuperCapacitorModelService.findByName(superCapacitorModelModel.getName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setName( "BITneicjdr" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=superCapacitorModelResult.getUpdateResult(superCapacitorModelUrl.getUpdate_url(),id,f);
        //8. 进行删除新增的数据 还原数据
        iSuperCapacitorModelService.deleteMulti(id);
    }

    @Test
    public void getSuperCapacitorModelByName() throws Exception {

        //1.先进行新增数据
        iSuperCapacitorModelService.insert(superCapacitorModelModel);
        //2.取得新增成功的对象
        superCapacitorModelModel=iSuperCapacitorModelService.findByName(superCapacitorModelModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(superCapacitorModelModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=superCapacitorModelResult.getModelByNameResult(superCapacitorModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=superCapacitorModelModel.getId();
        iSuperCapacitorModelService.deleteMulti(id);
    }

    @Test
    public void getSuperCapacitorModelDetailById() throws Exception {

        //1.先进行新增数据
        iSuperCapacitorModelService.insert(superCapacitorModelModel);
        //2.取得新增成功的对象
        superCapacitorModelModel=iSuperCapacitorModelService.findByName(superCapacitorModelModel.getName());
        String id=superCapacitorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=superCapacitorModelResult.getDetailByIdResult(superCapacitorModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iSuperCapacitorModelService.deleteMulti(id);
    }

    @Test
    public void deleteSuperCapacitorModel() throws Exception {

        //1.先进行新增数据
        iSuperCapacitorModelService.insert(superCapacitorModelModel);
        //2.取得新增成功的对象的id进行删除
        superCapacitorModelModel=iSuperCapacitorModelService.findByName(superCapacitorModelModel.getName());
        String id=superCapacitorModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=superCapacitorModelResult.getDeleteByIdResult(superCapacitorModelUrl.getDelete_url(),id);
    }
}
