package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/8 17:05
*/

import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.model.UnitModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.OwnerPeopleUrl;
import com.bitnei.cloud.sys.web.data.OwnerPeopleUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.UnitUtil;
import com.bitnei.cloud.sys.web.result.OwnerPeopleResult;
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
public class OwnerPeopleControllerTest extends BaseControllerTest {

    @Resource
    IUnitService iUnitService;

    @Resource
    IOwnerPeopleService iOwnerPeopleService;
    
    @Resource
    OwnerPeopleUtil ownerPeopleUtil;
    
    @Resource
    UnitUtil unitUtil;
    
    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    OwnerPeopleResult ownerPeopleResult;

    private OwnerPeopleModel ownerPeopleModel;
    private OwnerPeople o;
    private OwnerPeople ownerPeople;
    private UnitModel unitModel;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList=new ArrayList<>();
    private OwnerPeopleUrl ownerPeopleUrl;


    @Before
    public void before() {

        //0.先实例url地址对象
        ownerPeopleUrl=new OwnerPeopleUrl();
        //1.实例化并新增测试数据
        ownerPeopleModel=ownerPeopleUtil.createModel();
        //2.实例化并新增单位信息测试数据 并查询
        unitModel= unitUtil.createFactoryModel();
        iUnitService.insert(unitModel);

        unitModel=iUnitService.getByName(unitModel.getName());
        ownerPeopleModel.setUnitId(unitModel.getId());
        ownerPeopleModel.setUnitName(unitModel.getName());

        //3.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @After
    public void after() {
        iUnitService.deleteMulti(unitModel.getId());
    }

    @Test
    public void insertOwnerPeople() throws Exception {

        ResultActions actions=ownerPeopleResult.getAddResult(ownerPeopleUrl.getAdd_url(),ownerPeopleModel);

        //1.把新增的数据查询出id 再进行删除 还原数据
        ownerPeople=iOwnerPeopleService.findByOwnerName(ownerPeopleModel.getOwnerName());
        String id=ownerPeople.getId();
        iOwnerPeopleService.deleteMulti(id);


    }

    @Test
    public void getAllOwnerPeople() throws Exception {

        ResultActions actions=ownerPeopleResult.getAllResult(ownerPeopleUrl.getAll_url(),pagerInfo);

    }

    @Test
    public void getOwnerPeopleByConditons() throws Exception {

        //1.先进行新增数据
        iOwnerPeopleService.insert(ownerPeopleModel);
        //2.取得新增成功的对象
        ownerPeople=iOwnerPeopleService.findByOwnerName(ownerPeopleModel.getOwnerName());
        //3.设置查询对象

        Condition c1=new Condition();
        c1.setName("ownerName");
        c1.setValue(ownerPeople.getOwnerName());
        Condition c2=new Condition();
        c2.setName("telPhone");
        c2.setValue(ownerPeople.getTelPhone());
        conditionList.add(0,c1);
        conditionList.add(1,c2);
        pagerInfo.setConditions(conditionList);

        ResultActions actions=ownerPeopleResult.getModelByNameResult(ownerPeopleUrl.getAll_url(),pagerInfo);


        //1.把新增的数据查询出id 再进行删除 还原数据
        String id=ownerPeople.getId();
        iOwnerPeopleService.deleteMulti(id);
    }


    @Test
    public void updateOwnerPeople() throws Exception {

        //1.先进行新增数据
        iOwnerPeopleService.insert(ownerPeopleModel);
        //2.再实例化是一个Vo对象进行修改
        o=new OwnerPeople();
        //3.先查询需要修改的Vo对象数据
        o=iOwnerPeopleService.findByOwnerName(ownerPeopleModel.getOwnerName());
        //4.取得所要修改的对象的id
        String owner_id=o.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        o.setOwnerName(RandomValue.getChineseName()+"1");
        o.setUnitId(unitModel.getId());
        o.setUnitName(unitModel.getName());
        //6.取得id并设置值
        o.setId(owner_id);
        ownerPeopleModel=OwnerPeopleModel.fromEntry(o);

        ResultActions actions=ownerPeopleResult.getUpdateResult(ownerPeopleUrl.getUpdate_url(),owner_id,ownerPeopleModel);

        //7.把新增的数据查询出id 再进行删除 还原数据
        iOwnerPeopleService.deleteMulti(owner_id);


    }

    @Test
    public void deleteOwnerPeople() throws Exception {

        //1.先进行新增用户
        iOwnerPeopleService.insert(ownerPeopleModel);
        //2.取得新增成功的对象的id进行删除
        ownerPeople=iOwnerPeopleService.findByOwnerName(ownerPeopleModel.getOwnerName());  //通过查找用户id作为删除数据操作
        String owner_id=ownerPeople.getId();

        ResultActions actions=ownerPeopleResult.getDeleteByIdResult(ownerPeopleUrl.getDelete_url(),owner_id);
    }

////    @SimTest
//    public void owner_06_export() throws Exception {
//
//        String params="{\"conditions\":[],\"sort\":[],\"start\":0,\"limit\":10}";
//        String str=UrlCoder.urlEncoder(params);
//        ResultActions actions=mvc.perform(
//                MockMvcRequestBuilders.get(export_url,str).headers(httpHeader())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//    }
//
////    @SimTest
//    public void owner_07_batchImport() throws Exception {
//
//        File file=new File("D:/ownerBatchImport.xls");
//        ResultActions actions=mvc.perform(
//                MockMvcRequestBuilders.post(batchImport_url).headers(httpHeader())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
//                .andDo(print());
//
//    }
}
