package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/19 13:34
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.VehOwner;
import com.bitnei.cloud.sys.model.VehOwnerModel;
import com.bitnei.cloud.sys.service.IVehOwnerService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehOwnerUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehOwnerUtil;
import com.bitnei.cloud.sys.web.result.VehOwnerResult;
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
public class VehOwnerControllerTest extends BaseControllerTest {

    @Resource
    IVehOwnerService iVehOwnerService;
    @Resource
    VehOwnerUtil vehOwnerUtil;
    @Resource
    PagerInfoUtil pagerInfoUtil;
    @Resource
    VehOwnerResult vehOwnerResult;

    private VehOwner vehOwner;
    private VehOwnerModel vehOwnerModel;
    private VehOwner u;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehOwnerUrl vehOwnerUrl;


    @Before
    public void before(){
        //0.先实例url地址对象
        vehOwnerUrl=new VehOwnerUrl();
        //1.实例model并生成测试数据
        vehOwnerModel = vehOwnerUtil.createModel();
        //2.实例pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertVehOwner() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehOwnerResult.getAddResult(vehOwnerUrl.getAdd_url(),vehOwnerModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehOwner=new VehOwner();
        vehOwner=iVehOwnerService.findByOwnerName(vehOwnerModel.getOwnerName());
        String id=vehOwner.getId();
        iVehOwnerService.deleteMulti(id);
    }

    @Test
    public void getAllVehOwner() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehOwnerResult.getAllResult(vehOwnerUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehOwner() throws Exception {
        //1.先进行新增数据
        iVehOwnerService.insert(vehOwnerModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehOwner();
        //3.先查询需要修改的Vo对象数据
        u=iVehOwnerService.findByOwnerName(vehOwnerModel.getOwnerName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setOwnerName(RandomValue.getChineseName()+1);
        //6.取得id并设置值
        u.setId(id);
        vehOwnerModel = vehOwnerModel.fromEntry(u);
        //7.执行测试并验证结果
        ResultActions actions=vehOwnerResult.getUpdateResult(vehOwnerUrl.getUpdate_url(),id,vehOwnerModel);
        //8. 进行删除新增的数据 还原数据
        iVehOwnerService.deleteMulti(id);
    }

    @Test
    public void getVehOwnerByOwnerName() throws Exception {

        //1.先进行新增数据
        iVehOwnerService.insert(vehOwnerModel);
        //2.取得新增成功的对象
        vehOwner=iVehOwnerService.findByOwnerName(vehOwnerModel.getOwnerName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("ownerName");
        c1.setValue(vehOwner.getOwnerName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehOwnerResult.getModelByNameResult(vehOwnerUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehOwner.getId();
        iVehOwnerService.deleteMulti(id);
    }

    @Test
    public void getVehOwnerDetailById() throws Exception {

        //1.先进行新增数据
        iVehOwnerService.insert(vehOwnerModel);
        //2.取得新增成功的对象
        vehOwner=iVehOwnerService.findByOwnerName(vehOwnerModel.getOwnerName());
        String id=vehOwner.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehOwnerResult.getDetailByIdResult(vehOwnerUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehOwnerService.deleteMulti(id);
    }

    @Test
    public void deleteVehOwner() throws Exception {

        //1.先进行新增数据
        iVehOwnerService.insert(vehOwnerModel);
        //2.取得新增成功的对象的id进行删除
        vehOwner=iVehOwnerService.findByOwnerName(vehOwnerModel.getOwnerName());
        String id=vehOwner.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehOwnerResult.getDeleteByIdResult(vehOwnerUrl.getDelete_url(),id);

    }
}
