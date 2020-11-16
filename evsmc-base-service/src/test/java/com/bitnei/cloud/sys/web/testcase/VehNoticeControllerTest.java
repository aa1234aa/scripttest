package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/28 10:20
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehBrandModel;
import com.bitnei.cloud.sys.model.VehNoticeModel;
import com.bitnei.cloud.sys.model.VehSeriesModel;
import com.bitnei.cloud.sys.model.VehTypeModel;
import com.bitnei.cloud.sys.service.IVehBrandService;
import com.bitnei.cloud.sys.service.IVehNoticeService;
import com.bitnei.cloud.sys.service.IVehSeriesService;
import com.bitnei.cloud.sys.service.IVehTypeService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehNoticeUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehNoticeUtil;
import com.bitnei.cloud.sys.web.result.VehNoticeResult;
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
public class VehNoticeControllerTest extends BaseControllerTest {


    @Resource
    VehNoticeUtil vehNoticeUtil;

    @Resource
    VehNoticeResult vehNoticeResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IVehNoticeService iVehNoticeService;

    @Resource
    IVehTypeService iVehTypeService;

    @Resource
    IVehBrandService iVehBrandService;

    @Resource
    IVehSeriesService iVehSeriesService;

    private VehNoticeModel vehNoticeModel;
    private VehNoticeModel u;
    private VehTypeModel vehTypeModel;
    private VehBrandModel vehBrandModel;
    private VehSeriesModel vehSeriesModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehNoticeUrl vehNoticeUrl;
    private String vehTypeId;
    private String brandId;
    private String seriesId;




    @Before
    public void before(){
        //0.先实例url地址对象
        vehNoticeUrl=new VehNoticeUrl();
        //1.先查询关联的车辆种类，车辆品牌，车辆系列的id
        vehTypeModel=iVehTypeService.getByName(VEHTYPENAME);
        vehTypeId=vehTypeModel.getId();

        vehBrandModel=iVehBrandService.getByName(VEHBRANDNAME);
        brandId=vehBrandModel.getId();

        vehSeriesModel=iVehSeriesService.getByName(VEHSERIESNAME);
        seriesId=vehSeriesModel.getId();
        //2.实例化并赋值测试数据的po对象 转成VO对象
        vehNoticeModel=vehNoticeUtil.createModel();
        vehNoticeModel.setBrandId(brandId);
        vehNoticeModel.setSeriesId(seriesId);
        vehNoticeModel.setVehTypeId(vehTypeId);
        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertVehNotice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehNoticeResult.getAddResult(vehNoticeUrl.getAdd_url(),vehNoticeModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehNoticeModel=iVehNoticeService.getByName(vehNoticeModel.getName());
        String id=vehNoticeModel.getId();
        iVehNoticeService.deleteMulti(id);

    }

    @Test
    public void getAllVehNotice() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehNoticeResult.getAllResult(vehNoticeUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehNotice() throws Exception {
        //1.先进行新增
        iVehNoticeService.insert(vehNoticeModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehNoticeModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehNoticeService.getByName(vehNoticeModel.getName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setName("BITNEIvehnotice"+RandomValue.getNum(1000,9999));
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehNoticeResult.getUpdateResult(vehNoticeUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehNoticeService.deleteMulti(id);
    }

    @Test
    public void getVehNoticeByName() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iVehNoticeService.insert(vehNoticeModel);
        //2.取得新增成功的对象
        vehNoticeModel=iVehNoticeService.getByName(vehNoticeModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(vehNoticeModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehNoticeResult.getModelByNameResult(vehNoticeUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehNoticeModel.getId();
        iVehNoticeService.deleteMulti(id);
    }

    @Test
    public void getVehNoticeDetailById() throws Exception {

        //1.先进行新增数据
        iVehNoticeService.insert(vehNoticeModel);
        //2.取得新增成功的对象
        vehNoticeModel=iVehNoticeService.getByName(vehNoticeModel.getName());
        String id=vehNoticeModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehNoticeResult.getDetailByIdResult(vehNoticeUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehNoticeService.deleteMulti(id);
    }

    @Test
    public void deleteVehNotice() throws Exception {

        //1.先进行新增数据
        iVehNoticeService.insert(vehNoticeModel);
        //2.取得新增成功的对象的id进行删除
        vehNoticeModel=iVehNoticeService.getByName(vehNoticeModel.getName());
        String id=vehNoticeModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehNoticeResult.getDeleteByIdResult(vehNoticeUrl.getDelete_url(),id);

    }
}
