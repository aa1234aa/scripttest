package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/27 21:30
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehBrandModel;
import com.bitnei.cloud.sys.model.VehSeriesModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.service.IVehSeriesService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehSeriesUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehBrandUtil;
import com.bitnei.cloud.sys.web.data.VehSeriesUtil;
import com.bitnei.cloud.sys.web.result.VehSeriesResult;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehSeriesControllerTest extends BaseControllerTest {

    @Resource
    VehSeriesUtil vehSeriesUtil;

    @Resource
    VehSeriesResult vehSeriesResult;

    @Resource
    VehBrandUtil vehBrandUtil;

    @Resource
    PagerInfoUtil pagerInfoUtil;


    @Resource
    IVehSeriesService iVehSeriesService;

    @Resource
    IVehBrandService iVehBrandService;

    private VehSeriesModel vehSeriesModel;
    private VehSeriesModel u;
    private VehBrandModel vehBrandModel;
    private PagerInfo pagerInfo;
    private String brandId;
    private List<Condition> conditionList;
    private VehSeriesUrl vehSeriesUrl;



    @Before
    public void before(){
        //0.先实例url地址对象
        vehSeriesUrl=new VehSeriesUrl();
        //1.先生成品牌数据 并获取到品牌id
        vehBrandModel=vehBrandUtil.createModel();
        iVehBrandService.insert(vehBrandModel);
        vehBrandModel=iVehBrandService.getByName(vehBrandModel.getName());
        brandId=vehBrandModel.getId();
        //2.实例化并赋值测试数据的po对象 转成VO对象
        vehSeriesModel=vehSeriesUtil.createModel();
        vehSeriesModel.setBrandId(brandId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @After
    public void after(){

        iVehBrandService.deleteMulti(brandId);

    }

    @Test
    public void insertVehSeries() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehSeriesResult.getAddResult(vehSeriesUrl.getAdd_url(),vehSeriesModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehSeriesModel=iVehSeriesService.getByName(vehSeriesModel.getName());
        String id=vehSeriesModel.getId();
        iVehSeriesService.deleteMulti(id);

    }

    @Test
    public void getAllVehSeries() throws Exception {

        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("brandId");
        c1.setValue(brandId);
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //1.执行测试并验证结果
        ResultActions actions=vehSeriesResult.getAllResult(vehSeriesUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehSeries() throws Exception {
        //1.先进行新增
        iVehSeriesService.insert(vehSeriesModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehSeriesModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehSeriesService.getByName(vehSeriesModel.getName());
        System.out.println(u.getId());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setName( "BITnei"+RandomValue.getNum(1,999));
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehSeriesResult.getUpdateResult(vehSeriesUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehSeriesService.deleteMulti(id);
    }

    @Test
    public void getVehSeriesByName() throws Exception {

        //1.先进行新增
        iVehSeriesService.insert(vehSeriesModel);
        //2.取得新增成功的对象
        vehSeriesModel=iVehSeriesService.getByName(vehSeriesModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("brandId");
        c1.setValue(brandId);
        Condition c2=new Condition();
        c2.setName("name");
        c2.setValue(vehSeriesModel.getName());
        conditionList.add(0,c1);
        conditionList.add(1,c2);
        pagerInfo.setConditions(conditionList);
        //1.执行测试并验证结果
        ResultActions actions=vehSeriesResult.getModelByNameResult(vehSeriesUrl.getAll_url(),pagerInfo);
        //2.进行删除新增的数据 还原数据
        String id=vehSeriesModel.getId();
        iVehSeriesService.deleteMulti(id);
    }

    @Test
    public void getVehSeriesDetailById() throws Exception {

        //1.先进行新增数据
        iVehSeriesService.insert(vehSeriesModel);
        //2.取得新增成功的对象
        vehSeriesModel=iVehSeriesService.getByName(vehSeriesModel.getName());
        String id=vehSeriesModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehSeriesResult.getDetailByIdResult(vehSeriesUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehSeriesService.deleteMulti(id);
    }

    @Test
    public void deleteVehSeries() throws Exception {

        //1.先进行新增数据
        iVehSeriesService.insert(vehSeriesModel);
        //2.取得新增成功的对象的id进行删除
        vehSeriesModel=iVehSeriesService.getByName(vehSeriesModel.getName());
        String id=vehSeriesModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehSeriesResult.getDeleteByIdResult(vehSeriesUrl.getDelete_url(),id);

    }
}
