package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/15 16:15
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.GroupModel;
import com.bitnei.cloud.sys.service.IGroupService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.GroupUrl;
import com.bitnei.cloud.sys.web.data.GroupUtil;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.result.GroupResult;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupControllerTest extends BaseControllerTest {


    @Resource
    IGroupService igroupService ;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    GroupUtil groupUtil;

    @Resource
    GroupResult groupResult;

    private GroupModel groupModel;
    private GroupModel g;
    private PagerInfo pagerInfo;
    private GroupUrl groupUrl;





    @Before
    public void before(){
        //0.先实例url地址对象
        groupUrl=new GroupUrl();
        //1.实例化并生成测试数据的vo对象
        groupModel=groupUtil.createModel();
        //2.实例化并生成pagerInfo对象
        pagerInfo= pagerInfoUtil.createPagerInfo();
    }

    @Test
    public void insertGroup() throws Exception {

        //1.执行测试并验证结果
        ResultActions actions =groupResult.getAddResult(groupUrl.getAdd_url(),groupModel);

        //2.把新增的数据查询出id 再进行删除 还原数据
        groupModel=igroupService.getByName(groupModel.getName());
        String id=groupModel.getId();
        igroupService.deleteMulti(id);

    }

    @Test
    public void getAllGroup() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=groupResult.getAllResult(groupUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateGroup() throws Exception {
        //1.先进行新增
        igroupService.insert(groupModel);
        //2.再实例化是一个Vo对象进行修改
        g=new GroupModel();
        //3.先查询需要修改的Vo对象数据
        g= igroupService.getByName(groupModel.getName());
        //4.取得所要修改的对象的id
        String id=g.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        g.setName( "BITnei" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        g.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=groupResult.getUpdateResult(groupUrl.getUpdate_url(),id,g);

        //8. 进行删除新增的数据 还原数据
        igroupService.deleteMulti(id);
    }


    @Test
    public void getGroupDetailById() throws Exception {

        //1.先进行新增数据
        igroupService.insert(groupModel);
        //2.取得新增成功的对象
        groupModel=igroupService.getByName(groupModel.getName());
        String id=groupModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=groupResult.getDetailByIdResult(groupUrl.getDetail_url(),id);

        //4.进行删除新增的数据 还原数据
        igroupService.deleteMulti(id);
    }

    @Test
    public void deleteGroup() throws Exception {

        //1.先进行新增数据
        igroupService.insert(groupModel);
        //2.取得新增成功的对象的id进行删除
        groupModel=igroupService.getByName(groupModel.getName());
        String id=groupModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=groupResult.getDeleteByIdResult(groupUrl.getDelete_url(),id);

    }


}
