package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/30 14:15
*/

import com.bitnei.cloud.sys.domain.Role;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.util.RandomValue;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@Service
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleUtil {

    /** 角色名称 **/
    private String name="bitnei"+ RandomValue.getNum(1000,9999);
    /** 状态 1：有效 0：无效 **/
    private Integer isValid=1;
    /** 备注 **/
    private String note="单元测试角色管理";

    private Role role;
    private RoleModel roleModel;

    public RoleModel createModel(){

        role=new Role();
        role.setName(name);
        role.setIsValid(isValid);
        role.setNote(note);
        roleModel = new RoleModel();
        roleModel = RoleModel.fromEntry(role);
        return roleModel;
    }

}
