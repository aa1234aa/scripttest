package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/30 14:04
*/

import com.bitnei.cloud.sys.domain.Group;
import com.bitnei.cloud.sys.model.GroupModel;
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
public class GroupUtil {

    /** 组名 **/
    private String name="bitnei"+ RandomValue.getNum(1111,9999);
    /** 是否有效 **/
    private Integer isValid=1;
    /** 组描述 **/
    private String description="数据权限组单元测试";
    /** 规则类型 **/
    private Integer ruleType=2;
    /** 属性类型 **/
    private String resourceTypeId="1111";
    /** 用户id **/
    private String userId;

    private Group group;
    private GroupModel groupModel;

    public GroupModel createModel(){

        group=new Group();
        group.setName(name);
        group.setIsValid(isValid);
        group.setRuleType(ruleType);
        group.setDescription(description);
        group.setResourceTypeId(resourceTypeId);
        groupModel = new GroupModel();
        groupModel  = GroupModel.fromEntry(group);
        return groupModel;

    }
}
