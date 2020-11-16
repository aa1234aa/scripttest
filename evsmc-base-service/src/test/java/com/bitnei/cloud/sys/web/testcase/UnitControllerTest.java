package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/19 1:05
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.UnitUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.UnitUtil;
import com.bitnei.cloud.sys.web.result.UnitResult;
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
public class UnitControllerTest extends BaseControllerTest {


    @Resource
    IUnitService iUnitService ;

    @Resource
    UnitUtil unitUtil;


    @Resource
    UnitResult unitResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    private UnitModel unitModel;
    private UnitModel u;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private  UnitUrl unitUrl;




    @Before
    public void before(){
        //0.先实例url地址对象
        unitUrl=new UnitUrl();
        //1.实例化实体并生成测试数据
        unitModel=unitUtil.createFactoryModel();
        //2.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertUnit() throws Exception {

        //1.执行测试并验证结果
        ResultActions actions =unitResult.getAddResult(unitUrl.getAdd_url(),unitModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        unitModel=iUnitService.getByName(unitModel.getName());
        String id=unitModel.getId();
        iUnitService.deleteMulti(id);

    }

    @Test
    public void getAllUnit() throws Exception {

        //1.执行测试并验证结果
        ResultActions actions=unitResult.getAllResult(unitUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateRole() throws Exception {
        //1.先进行新增
        iUnitService.insert(unitModel);
        //2.再实例化是一个Vo对象进行修改
        u=new UnitModel();
        //3.先查询需要修改的Vo对象数据
        u=iUnitService.getByName(unitModel.getName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setName( "BITnei" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        u.setId(id);
        u.setUnitTypeIds(unitModel.getUnitTypeIds());
        u.setUnitTypeNames(unitModel.getUnitTypeNames());

        //7.执行测试并验证结果
        ResultActions actions=unitResult.getUpdateResult(unitUrl.getUpdate_url(),id,u);

        //8. 进行删除新增的数据 还原数据
        iUnitService.deleteMulti(id);
    }

    @Test
    public void getUnitByName() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iUnitService.insert(unitModel);
        //2.取得新增成功的对象
        unitModel=iUnitService.getByName(unitModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(unitModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);

        //4.执行测试并验证结果
        ResultActions actions=unitResult.getModelByNameResult(unitUrl.getAll_url(),pagerInfo);

        //5.进行删除新增的数据 还原数据
        String id=unitModel.getId();
        iUnitService.deleteMulti(id);
    }

    @Test
    public void getUnitDetailById() throws Exception {

        //1.先进行新增数据
        iUnitService.insert(unitModel);
        //2.取得新增成功的对象
        unitModel=iUnitService.getByName(unitModel.getName());
        String id=unitModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=unitResult.getDetailByIdResult(unitUrl.getDetail_url(),id);

        //4.进行删除新增的数据 还原数据
        iUnitService.deleteMulti(id);
    }

    @Test
    public void deleteUnit() throws Exception {

        //1.先进行新增数据
        iUnitService.insert(unitModel);
        //2.取得新增成功的对象的id进行删除
        unitModel=iUnitService.getByName(unitModel.getName());
        String id=unitModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=unitResult.getDeleteByIdResult(unitUrl.getDelete_url(),id);
    }
}
