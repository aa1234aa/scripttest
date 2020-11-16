package com.bitnei.cloud.sys.web.suite;

/*
@author 黄永雄
@create 2019/11/26 23:21
*/

import com.bitnei.cloud.sys.web.testcase.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
    @Suite.SuiteClasses({VehOwnerControllerTest.class,             //个人车主信息
                         UnitControllerTest.class,                 //单位信息管理
                         OwnerPeopleControllerTest.class,          //单位联系人
                         GroupControllerTest.class,                //数据权限组管理
                         RoleControllerTest.class,                 //角色管理
                         UserControllerTest.class,                 //账号管理
                         FuelSystemModelControllerTest.class,      //燃料电池系统型号
                         FuelGeneratorModelControllerTest.class,   //燃油发电机型号
                         PowerDeviceControllerTest.class,          //发电装置信息
                         EngineModelControllerTest.class,          //发动机型号
                         DriveMotorModelControllerTest.class,      //驱动装置型号
                         DriveDeviceControllerTest.class,          //驱动装置信息
                         SuperCapacitorModelControllerTest.class,  //超级电容型号
                         BatteryDeviceModelControllerTest.class,   //动力蓄电池型号
                         EngeryDeviceControllerTest.class,         //可充电储能装置信息
                         SimManagementTest.class,                  //SIM卡管理
                         TermModelControllerTest.class,            //车载终端型号
                         TermModelUnitControllerTest.class,        //车载终端信息
                         VehTypeControllerTest.class,              //车辆种类信息
                         VehBrandControllerTest.class,             //车辆品牌
                         VehSeriesControllerTest.class,             //车辆系列
                         VehNoticeControllerTest.class,             //车辆公告
                         VehModelControllerTest.class,               //车辆型号
                         VehicleControllerTest.class                 //车辆列表
                    })
public class TestSysController {
}
