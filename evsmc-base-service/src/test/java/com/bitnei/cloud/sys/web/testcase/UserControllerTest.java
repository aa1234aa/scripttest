package com.bitnei.cloud.sys.web.testcase;

/*
@author 黄永雄
@create 2019/11/14 10:41
*/


import com.bitnei.cloud.common.BaseControllerTest;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.model.OwnerPeopleModel;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.service.IOwnerPeopleService;
import com.bitnei.cloud.sys.service.IRoleService;
import com.bitnei.cloud.sys.service.IUnitService;
import com.bitnei.cloud.sys.service.IUserService;
import com.bitnei.cloud.sys.util.RandomValue;
import com.bitnei.cloud.sys.web.config.UserUrl;
import com.bitnei.cloud.sys.web.data.PagerInfoUtil;
import com.bitnei.cloud.sys.web.data.UserUtil;
import com.bitnei.cloud.sys.web.result.UserResult;
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
public class UserControllerTest extends BaseControllerTest {


    @Resource
    IUserService iUserService;

    @Resource
    IOwnerPeopleService iOwnerPeopleService;

    @Resource
    IRoleService iroleService;

    @Resource
    IUnitService iUnitService;

    @Resource
    PagerInfoUtil pagerInfoUtil;

    @Resource
    UserUtil userUtil;

    @Resource
    UserResult userResult;


    private UserModel userModel;
    private UserModel u;
    private PagerInfo pagerInfo;
    private List<Condition> conditionList;
    private RoleModel roleModel;
    private OwnerPeopleModel ownerPeopleModel;
    private OwnerPeople ownerPeople;
    private String roleId;
    private String owenerId;
    private UserUrl userUrl;



    @Before
    public void before(){
        //0.先实例url地址对象
        userUrl=new UserUrl();

        //1.初始化测试数据 查询角色作为测试数据
        roleModel=iroleService.getByName(ROLENAME);
        roleId=roleModel.getId();

        //2.初始化测试数据 查询负责人作为测试数据
        ownerPeople=iOwnerPeopleService.findByOwnerName(OWNERNAME);
        owenerId=ownerPeople.getId();

        //3.实例化并赋值测试数据VO对象
        userModel=userUtil.createModel();
        userModel.setRoleIds(roleId);
        userModel.setDefRoleId(roleId);
        userModel.setOwnerId(owenerId);
        //4.实例化pagerInfo对象并赋值
        pagerInfo=pagerInfoUtil.createPagerInfo();
    }


    @Test
    public void insertUser() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions =userResult.getAddResult(userUrl.getAdd_url(),userModel);
        //2.把新增的数据查询出id 再进行删除 还原数据
        userModel=iUserService.findByUsername(userModel.getUsername());
        String id=userModel.getId();
        iUserService.deleteMulti(id);
    }

    @Test
    public void getAllUser() throws Exception {
        //1.执行测试并验证结果
        ResultActions actions=userResult.getAllResult(userUrl.getAll_url(),pagerInfo);
    }

    @Test
    public void updateUser() throws Exception {
        //1.先进行新增数据
        iUserService.insert(userModel);
        //2.再实例化是一个Vo对象进行修改
        u=new UserModel();
        //3.先查询需要修改的Vo对象数据
        u=iUserService.findByUsername(userModel.getUsername());
        //4.取得所要修改的对象的id
        String id=u.getId();
        //5.将所要修改的Vo对象 重新编辑用户名
        u.setUsername( "bitnei" + RandomValue.getNum(100, 999));
        //6.取得id并设置值
        u.setId(id);
        u.setRoleIds(roleId);
        //7.执行测试并验证结果
        ResultActions actions=userResult.getUpdateResult(userUrl.getUpdate_url(),id,u);
        //8. 进行删除新增的数据 还原数据
        iUserService.deleteMulti(id);
    }

    @Test
    public void getUserByUserName() throws Exception {

        //1.先进行新增数据
        iUserService.insert(userModel);
        //2.取得新增成功的对象
        userModel=iUserService.findByUsername(userModel.getUsername());
        //3.设置查询对象
        conditionList=new ArrayList<>();
        Condition c1=new Condition();
        c1.setName("username");
        c1.setValue(userModel.getUsername());
        conditionList.add(0,c1);
        pagerInfo.setConditions(conditionList);
        //4.执行测试并验证结果
        ResultActions actions=userResult.getModelByNameResult(userUrl.getAll_url(),pagerInfo);
        //5.进行删除新增的数据 还原数据
        String id=userModel.getId();
        iUserService.deleteMulti(id);
    }

    @Test
    public void getUserDetailById() throws Exception {

        //1.先进行新增数据
        iUserService.insert(userModel);
        //2.取得新增成功的对象
        userModel=iUserService.findByUsername(userModel.getUsername());
        String id=userModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=userResult.getDetailByIdResult(userUrl.getDetail_url(),id);
        //4.进行删除新增的数据 还原数据
         iUserService.deleteMulti(id);
    }

    @Test
    public void deleteUser() throws Exception {

        //1.先进行新增数据
        iUserService.insert(userModel);
        //2.取得新增成功的对象的id进行删除
        userModel=iUserService.findByUsername(userModel.getUsername());  //通过查找用户id作为删除数据操作
        String id=userModel.getId();
        //3.执行测试并验证结果
        ResultActions actions=userResult.getDeleteByIdResult(userUrl.getDelete_url(),id);

    }
}
