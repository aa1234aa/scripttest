package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/27 13:45
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.SimManagementModel;
import com.bitnei.cloud.sys.model.TermModelModel;
import com.bitnei.cloud.sys.model.TermModelUnitModel;
import com.bitnei.cloud.sys.service.ISimManagementService;
import com.bitnei.cloud.sys.service.ITermModelService;
import com.bitnei.cloud.sys.service.ITermModelUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.TermModelUnitUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.SimManagementUtil;
import com.bitnei.cloud.sys.web.data.TermModelUnitUtil;
import com.bitnei.cloud.sys.web.result.TermModelUnitResult;
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
public class TermModelUnitControllerTest extends BaseControllerTest {


    @Resource
    TermModelUnitUtil termModelUnitUtil;

    @Resource
    TermModelUnitResult termModelUnitResult;

    @Resource
    SimManagementUtil simManagementUtil;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    ITermModelUnitService iTermModelUnitService;

    @Resource
    ITermModelService iTermModelService;

    @Resource
    ISimManagementService iSimManagementService;


    private TermModelUnitModel termModelUnitModel;
    private TermModelUnitModel f;
    private TermModelModel termModelModel;
    private SimManagementModel simManagementModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private TermModelUnitUrl termModelUnitUrl;
    private String sysTermModelId;
    private String simId;



    @Before
    public void before(){
        //0.先实例url地址对象
        termModelUnitUrl=new TermModelUnitUrl();
        //1.初始化测试数据 先从相关服务查询到相关id引用来作为测试数据
        termModelModel=iTermModelService.findByTermModelName(TermModelName);
        sysTermModelId=termModelModel.getId();
        //2.一个车载 需要一个SIM卡 所以需要新增SIM卡数据
        simManagementModel = simManagementUtil.createModel();
        iSimManagementService.insert(simManagementModel);
        simManagementModel=iSimManagementService.findByIccId(simManagementModel.getIccid());
        simId=simManagementModel.getId();
        //3.实例化并赋值测试数据的VO对象
        termModelUnitModel=termModelUnitUtil.createModel();
        termModelUnitModel.setSysTermModelId(sysTermModelId);
        termModelUnitModel.setSupportProtocol(SUPPORTPROTOCOL);
        termModelUnitModel.setIccid(simManagementModel.getIccid());
        //4.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @After
    public void after(){

        iSimManagementService.deleteMulti(simId);

    }

    @Test
    public void insertTermModelUnit() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = termModelUnitResult.getAddResult(termModelUnitUrl.getAdd_url(),termModelUnitModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        termModelUnitModel=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        String id=termModelUnitModel.getId();
        iTermModelUnitService.deleteMulti(id);
    }

    @Test
    public void getAllTermModelUnit() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=termModelUnitResult.getAllResult(termModelUnitUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateTermModelUnit() throws Exception {
        //1.先进行新增数据
        iTermModelUnitService.insert(termModelUnitModel);
        //2.再实例化是一个Vo对象进行修改
        f=new TermModelUnitModel();
        //3.先查询需要修改的Vo对象数据
        f=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        //4.取得所要修改的对象的id
        String id=f.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        f.setSerialNumber("BITneichezaixinxi"+RandomValue.getNum(1000,9999));
        //6.取得id并设置值
        f.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=termModelUnitResult.getUpdateResult(termModelUnitUrl.getUpdate_url(),id,f);
        //8.进行删除新增的数据 还原数据
        iTermModelUnitService.deleteMulti(id);
    }

    @Test
    public void getTermModelUnitByName() throws Exception {

        //1.先进行新增数据
        iTermModelUnitService.insert(termModelUnitModel);
        //2.取得新增成功的对象
        termModelUnitModel=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("serialNumber");
        c1.setValue(termModelUnitModel.getSerialNumber());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=termModelUnitResult.getModelByNameResult(termModelUnitUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=termModelUnitModel.getId();
        iTermModelUnitService.deleteMulti(id);
    }

    @Test
    public void getTermModelUnitDetailById() throws Exception {

        //1.先进行新增数据
        iTermModelUnitService.insert(termModelUnitModel);
        //2.取得新增成功的对象
        termModelUnitModel=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        String id=termModelUnitModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=termModelUnitResult.getDetailByIdResult(termModelUnitUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iTermModelUnitService.deleteMulti(id);
    }

    @Test
    public void deleteTermModelUnit() throws Exception {

        //1.先进行新增数据
        iTermModelUnitService.insert(termModelUnitModel);
        //2.取得新增成功的对象的id进行删除
        termModelUnitModel=iTermModelUnitService.findBySerialNumber(termModelUnitModel.getSerialNumber());
        String id=termModelUnitModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=termModelUnitResult.getDeleteByIdResult(termModelUnitUrl.getDelete_url(),id);
    }
}
