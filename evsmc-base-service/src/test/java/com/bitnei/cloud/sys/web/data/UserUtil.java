package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/30 14:28
*/

import com.bitnei.cloud.sys.domain.OwnerPeople;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.sys.model.UserModel;
import com.bitnei.cloud.sys.util.RandomValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserUtil {

    private static final String PASSWORD="MOCZh7SjeudUVDU7VtVDSse+rcdcdU0S1QPXAByLHPTyGxpUlGq6Sq4X8AwzblYtWnO8wpXdZ3WKkBo8KUtMy1sn7pTeiulwzBn5ZaX45W+wL7WUdYN/E9SKQLCUT9IlGP9hwR9p22nc5sFW6BqF15VyJR3zXD7P2jQHssQt6BY=";

    /**账户名 */
    private String username = "bitnei" + RandomValue.getNum(1000, 99999);
    /**密码 */
    @JsonIgnore
    private String password = PASSWORD;
    /**负责人id */
    private String ownerId;
    /**永久有效 1:是 0:否 */
    private Integer isNeverExpire = 1;
    /**是否有效 */
    private Integer isValid = 1;
    /**默认角色 */
    private String defRoleId;
    /**是否开通app权限 */
    private Integer openApp = 1;
    /**是否开通微信权限 */
    private Integer openWx = 1;
    /**是否所有权限组 */
    private Integer isAllPermissions = 1;
    private String roleIds;

    private User user;
    private UserModel userModel;

    public UserModel createModel(){
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIsNeverExpire(isNeverExpire);
        user.setIsValid(isValid);
        user.setOpenWx(openWx);
        user.setOpenApp(openApp);
        user.setIsAllPermissions(isAllPermissions);
        userModel = new UserModel();
        userModel = UserModel.fromEntry(user);
        return userModel;
    }



}
