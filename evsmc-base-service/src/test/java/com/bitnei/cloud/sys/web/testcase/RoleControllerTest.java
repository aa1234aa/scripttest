package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/15 14:05
*/

import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.service.IRoleService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.RoleUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.RoleUtil;
import com.bitnei.cloud.sys.web.result.RoleResult;
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
public class RoleControllerTest extends BaseControllerTest {

    @Resource
    RoleUtil roleUtil;

    @Resource
    RoleResult roleResult;

    @Resource
    IRoleService iRoleService ;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    private RoleModel roleModel;
    private RoleModel r;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private RoleUrl roleUrl;




    @Before
    public void before(){
        //0.先实例url地址对象
        roleUrl=new RoleUrl();
        //1.实例化并赋值测试数据VO对象
        roleModel=roleUtil.createModel();
        //2.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();

    }

    @Test
    public void insertRole() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions = roleResult.getAddResult(roleUrl.getAdd_url(),roleModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        roleModel=iRoleService.getByName(roleModel.getName());
        String id=roleModel.getId();
        iRoleService.deleteMulti(id);

    }

    @Test
    public void getAllRole() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=roleResult.getAllResult(roleUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateRole() throws Exception {
        //1.先进行新增
        iRoleService.insert(roleModel);
        //2.再实例化是一个Vo对象进行修改
        r=new RoleModel();
        //3.先查询需要修改的Vo对象数据
        r=iRoleService.getByName(roleModel.getName());
        //4.取得所要修改的对象的id
        String id=r.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        r.setName( "BITnei" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        r.setId(id);
        //7.执行测试并验证结果
        ResultActions actions=roleResult.getUpdateResult(roleUrl.getUpdate_url(),id,r);

        //8. 进行删除新增的数据 还原数据
        iRoleService.deleteMulti(id);
    }

    @Test
    public void getRoleByName() throws Exception {

        //1.将测试数据的po对象 转成VO对象
        iRoleService.insert(roleModel);
        //2.取得新增成功的对象
        roleModel=iRoleService.getByName(roleModel.getName());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("name");
        c1.setValue(roleModel.getName());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=roleResult.getModelByNameResult(roleUrl.getAll_url(),pagerInfo);

        //5.进行删除新增的数据 还原数据
        String id=roleModel.getId();
        iRoleService.deleteMulti(id);
    }

    @Test
    public void getRoleDetailById() throws Exception {

        //1.先进行新增数据
        iRoleService.insert(roleModel);
        //2.取得新增成功的对象
        roleModel=iRoleService.getByName(roleModel.getName());
        String id=roleModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=roleResult.getDetailByIdResult(roleUrl.getDetail_url(),id);

        //4.进行删除新增的数据 还原数据
        iRoleService.deleteMulti(id);
    }

    @Test
    public void deleteRole() throws Exception {

        //1.先进行新增数据
        iRoleService.insert(roleModel);
        //2.取得新增成功的对象的id进行删除
        roleModel=iRoleService.getByName(roleModel.getName());
        String id=roleModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=roleResult.getDeleteByIdResult(roleUrl.getDelete_url(),id);

    }

}
