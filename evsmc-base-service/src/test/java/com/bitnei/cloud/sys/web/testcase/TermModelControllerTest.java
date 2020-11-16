package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/27 11:15
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.TermModelModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.ITermModelService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.TermModelUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.TermModelResult;
import com.bitnei.cloud.sys.web.data.TermModelUtil;
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
public class TermModelControllerTest extends BaseControllerTest {

    @Resource
    TermModelUtil termModelUtil;

    @Resource
    TermModelResult termModelResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;


    @Resource
    ITermModelService iTermModelService;

    @Autowired
    private IUnitService iUnitService;


    private TermModelModel termModelModel;
    private TermModelModel f;
    private UnitModel unitModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private String unitId;
    private TermModelUrl termModelUrl;




    @Before
    public void before(){

        //0.先实例url地址对象
        termModelUrl=new TermModelUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        unitModel=iUnitService.getByName(UNITSUPPLIERNAME);
        unitId=unitModel.getId();
        //2.实例化并赋值测试数据的VO对象
        termModelModel = termModelUtil.createModel();
        termModelModel.setUnitId(unitId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertTermModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions =termModelResult.getAddResult(termModelUrl.getAdd_url(),termModelModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        termModelModel=iTermModelService.findByTermModelName(termModelModel.getTermModelName());
        String id=termModelModel.getId();
        iTermModelService.deleteMulti(id);
    }

    @Test
    public void getAllTermModel() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=termModelResult.getAllResult(termModelUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateTermModel() throws Exception {
        //1.先进行新增数据
        iTermModelService.insert(termModelModel);
        //2.再实例化是一个Vo对象进行修改
        f=new TermModelModel();
        //3.先查询需要修改的Vo对象数据
        f=iTermModelService.findByTermModelName(termModelModel.getTermModelName());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setTermModelName( "BITneitermmodel" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=termModelResult.getUpdateResult(termModelUrl.getUpdate_url(),id,f);
        //8.进行删除新增的数据 还原数据
        iTermModelService.deleteMulti(id);
    }

    @Test
    public void getTermModelByName() throws Exception {

        //1.先进行新增数据
        iTermModelService.insert(termModelModel);
        //2.取得新增成功的对象
        termModelModel=iTermModelService.findByTermModelName(termModelModel.getTermModelName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("termModelName");
        c1.setValue(termModelModel.getTermModelName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=termModelResult.getModelByNameResult(termModelUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=termModelModel.getId();
        iTermModelService.deleteMulti(id);
    }

    @Test
    public void getTermModelDetailById() throws Exception {

        //1.先进行新增数据
        iTermModelService.insert(termModelModel);
        //2.取得新增成功的对象
        termModelModel=iTermModelService.findByTermModelName(termModelModel.getTermModelName());
        String id=termModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=termModelResult.getDetailByIdResult(termModelUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iTermModelService.deleteMulti(id);
    }

    @Test
    public void deleteTermModel() throws Exception {

        //1.先进行新增数据
        iTermModelService.insert(termModelModel);
        //2.取得新增成功的对象的id进行删除
        termModelModel=iTermModelService.findByTermModelName(termModelModel.getTermModelName());
        String id=termModelModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=termModelResult.getDeleteByIdResult(termModelUrl.getDelete_url(),id);
    }

}
