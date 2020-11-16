package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/22 10:58
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.SimManagementModel;
import com.bitnei.cloud.sys.service.ISimManagementService;
import com.bitnei.cloud.sys.web.config.SimManagementUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.SimManagementUtil;
import com.bitnei.cloud.sys.web.result.SimManagementResult;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimManagementTest extends BaseControllerTest {

    @Resource
    SimManagementUtil simManagementUtil;

    @Resource
    SimManagementResult simManagementResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    ISimManagementService isimManagementService;

    private SimManagementModel simManagementModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private SimManagementUrl simManagementUrl;


    @Before
    public void before(){

        //0.先实例url地址对象
        simManagementUrl=new SimManagementUrl();
        //1.实例化并赋值测试数据的po对象 再转成VO对象
        simManagementModel=simManagementUtil.createModel();
        //2.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertSimManagement() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = simManagementResult.getAddResult(simManagementUrl.getAdd_url(),simManagementModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        simManagementModel=isimManagementService.findByIccId(simManagementModel.getIccid());
        String id=simManagementModel.getId();
        isimManagementService.deleteMulti(id);
    }

    @Test
    public void getAllSimManagement() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=simManagementResult.getAllResult(simManagementUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void registerSimManagement() throws Exception {
        //1.先进行新增数据
        isimManagementService.insert(simManagementModel);
        //2.把新增的数据查询出id
        simManagementModel=isimManagementService.findByIccId(simManagementModel.getIccid());
        String id=simManagementModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=simManagementResult.getRegisterResult(simManagementUrl.getRegister_url(),id);
        //4. 进行删除新增的数据 还原数据
        isimManagementService.deleteMulti(id);
    }

    @Test
    public void getSimManagementByIccid() throws Exception {

        //1.先进行新增数据
        isimManagementService.insert(simManagementModel);
        //2.取得新增成功的对象
        simManagementModel=isimManagementService.findByIccId(simManagementModel.getIccid());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("iccid");
        c1.setValue(simManagementModel.getIccid());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=simManagementResult.getModelByNameResult(simManagementUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=simManagementModel.getId();
        isimManagementService.deleteMulti(id);
    }

    @Test
    public void getSimManagementDetailById() throws Exception {

        //1.先进行新增数据
        isimManagementService.insert(simManagementModel);
        //2.取得新增成功的对象
        simManagementModel=isimManagementService.findByIccId(simManagementModel.getIccid());
        String id=simManagementModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=simManagementResult.getDetailByIdResult(simManagementUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        isimManagementService.deleteMulti(id);
    }

    @Test
    public void deleteSimManagement() throws Exception {

        //1.先进行新增数据
        isimManagementService.insert(simManagementModel);
        //2.取得新增成功的对象的id进行删除
        simManagementModel=isimManagementService.findByIccId(simManagementModel.getIccid()); //通过查找用户id作为删除数据操作
        String id=simManagementModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=simManagementResult.getDeleteByIdResult(simManagementUrl.getDelete_url(),id);

    }


}
