package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/27 18:51
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.VehTypeModel;
import com.bitnei.cloud.sys.service.IVehTypeService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.VehTypeUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.VehTypeUtil;
import com.bitnei.cloud.sys.web.result.VehTypeResult;
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

public class VehTypeControllerTest extends BaseControllerTest {

    @Resource
    VehTypeUtil vehTypeUtil;

    @Resource
    VehTypeResult vehTypeResult;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    IVehTypeService iVehTypeService;

    private VehTypeModel vehTypeModel;
    private VehTypeModel u;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private VehTypeUrl vehTypeUrl;

    @Before
    public void before(){
        //0.先实例url地址对象
        vehTypeUrl=new VehTypeUrl();
        //1.实例化并赋值测试数据的po对象 转成VO对象
        vehTypeModel=vehTypeUtil.createModel();
        //2.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertVehType() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = vehTypeResult.getAddResult(vehTypeUrl.getAdd_url(),vehTypeModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        vehTypeModel=iVehTypeService.getByName(vehTypeModel.getName());
        String id=vehTypeModel.getId();
        iVehTypeService.deleteMulti(id);

    }

    @Test
    public void getAllVehType() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=vehTypeResult.getAllResult(vehTypeUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateVehType() throws Exception {
        //1.先进行新增
        iVehTypeService.insert(vehTypeModel);
        //2.再实例化是一个Vo对象进行修改
        u=new VehTypeModel();
        //3.先查询需要修改的Vo对象数据
        u=iVehTypeService.getByName(vehTypeModel.getName());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setName( "BITnei车辆种类" + RandomValue.getNum(10000, 99999));
        //6.取得id并设置值
        u.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=vehTypeResult.getUpdateResult(vehTypeUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iVehTypeService.deleteMulti(id);
    }

    @Test
    public void getVehTypeByName() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iVehTypeService.insert(vehTypeModel);
        //2.取得新增成功的对象
        vehTypeModel=iVehTypeService.getByName(vehTypeModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(vehTypeModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=vehTypeResult.getModelByNameResult(vehTypeUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=vehTypeModel.getId();
        iVehTypeService.deleteMulti(id);
    }

    @Test
    public void getVehTypeDetailById() throws Exception {

        //1.先进行新增数据
        iVehTypeService.insert(vehTypeModel);
        //2.取得新增成功的对象
        vehTypeModel=iVehTypeService.getByName(vehTypeModel.getName());
        String id=vehTypeModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehTypeResult.getDetailByIdResult(vehTypeUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
        iVehTypeService.deleteMulti(id);
    }

    @Test
    public void deleteVehType() throws Exception {

        //1.先进行新增数据
        iVehTypeService.insert(vehTypeModel);
        //2.取得新增成功的对象的id进行删除
        vehTypeModel=iVehTypeService.getByName(vehTypeModel.getName());
        String id=vehTypeModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=vehTypeResult.getDeleteByIdResult(vehTypeUrl.getDelete_url(),id);

    }
}
