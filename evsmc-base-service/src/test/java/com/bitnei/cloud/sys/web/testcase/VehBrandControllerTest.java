package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/27 20:43
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehBrandModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehBrandUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehBrandUtil;
import com.bitnei.cloud.sys.web.result.VehBrandResult;
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
public class VehBrandControllerTest extends BaseControllerTest {

    @Resource
    VehBrandUtil vehBrandUtil;

    @Resource
    VehBrandResult vehBrandResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IVehBrandService iVehBrandService;

    private VehBrandModel vehBrandModel;
    private VehBrandModel u;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehBrandUrl vehBrandUrl;





    @Before
    public void before(){

        //0.先实例url地址对象
        vehBrandUrl=new VehBrandUrl();
        //1.实例化并赋值测试数据的po对象 转成VO对象
        vehBrandModel=vehBrandUtil.createModel();
        //2.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertVehBrand() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehBrandResult.getAddResult(vehBrandUrl.getAdd_url(),vehBrandModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehBrandModel=iVehBrandService.getByName(vehBrandModel.getName());
        String id=vehBrandModel.getId();
        iVehBrandService.deleteMulti(id);

    }

    @Test
    public void getAllVehBrand() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehBrandResult.getAllResult(vehBrandUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehBrand() throws Exception {
        //1.先进行新增
        iVehBrandService.insert(vehBrandModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehBrandModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehBrandService.getByName(vehBrandModel.getName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setName( "BITNE"+RandomValue.getNum(1,9999));
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehBrandResult.getUpdateResult(vehBrandUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehBrandService.deleteMulti(id);
    }

    @Test
    public void getVehBrandByName() throws Exception {

        //1.先进行新增
        iVehBrandService.insert(vehBrandModel);
        //2.取得新增成功的对象
        vehBrandModel=iVehBrandService.getByName(vehBrandModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(vehBrandModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehBrandResult.getModelByNameResult(vehBrandUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehBrandModel.getId();
        iVehBrandService.deleteMulti(id);
    }

    @Test
    public void getVehBrandDetailById() throws Exception {

        //1.先进行新增数据
        iVehBrandService.insert(vehBrandModel);
        //2.取得新增成功的对象
        vehBrandModel=iVehBrandService.getByName(vehBrandModel.getName());
        String id=vehBrandModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehBrandResult.getDetailByIdResult(vehBrandUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehBrandService.deleteMulti(id);
    }

    @Test
    public void deleteVehBrand() throws Exception {

        //1.先进行新增数据
        iVehBrandService.insert(vehBrandModel);
        //2.取得新增成功的对象的id进行删除
        vehBrandModel=iVehBrandService.getByName(vehBrandModel.getName());
        String id=vehBrandModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehBrandResult.getDeleteByIdResult(vehBrandUrl.getDelete_url(),id);

    }
}
