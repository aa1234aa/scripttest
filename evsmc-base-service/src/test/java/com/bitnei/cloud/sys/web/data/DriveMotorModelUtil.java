package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/12 14:12
*/

import com.bitnei.cloud.sys.domain.DriveMotorModel;
import com.bitnei.cloud.sys.model.DriveMotorModelModel;
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
public class DriveMotorModelUtil {


    /** 型号名称 **/
    private String name="bitneiqddj"+ RandomValue.getNum(1000,9999   );
    /** 驱动电机种类 **/
    private Integer driveType=1;
    /** 驱动电机冷却方式 **/
    private Integer driveColdMode=1;
    /** 额定电压(V) **/
    private Double ratedVoltage=Double.valueOf(RandomValue.getNum(180,220));
    /** 最大工作电流(A) **/
    private Double maxElectricity=Double.valueOf(RandomValue.getNum(5,12));
    /** 驱动电机控制器型号 **/
    private String controllerModel="bitkzxh"+RandomValue.getNum(1000,9999);
    /** 额定功率(KW) **/
    private Double ratedPower=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值功率(KW) **/
    private Double peakPower=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 额定转速(r/min) **/
    private Double ratedRotateSpeed=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值转速(r/min) **/
    private Double peakRotateSpeed=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 额定转矩(N.m) **/
    private Double ratedTorque=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 峰值转矩(N.m) **/
    private Double peakTorque=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 驱动电机最大输出转矩(N.m) **/
    private Double maxOutputTorque=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 驱动电机最高转速(r/min) **/
    private Double maxRotateSpeed=Double.valueOf(RandomValue.getNum(1000,1500));
    /** 驱动电机生产企业 **/
    private String prodUnitId;
    /** 驱动电机控制器生产企业 **/
    private String controllerProdUnitId;
    /** 有效比功率(KW/kg) */
    private Double validPower=Double.valueOf(RandomValue.getNum(1000,1500));

    private DriveMotorModel driveMotorModel;
    private DriveMotorModelModel driveMotorModelModel;

    public DriveMotorModelModel createModel(){

        driveMotorModel = new DriveMotorModel();
        driveMotorModel.setName(name);
        driveMotorModel.setDriveType(driveType);
        driveMotorModel.setDriveColdMode(driveColdMode);
        driveMotorModel.setRatedPower(ratedPower);
        driveMotorModel.setRatedVoltage(ratedVoltage);
        driveMotorModel.setMaxElectricity(maxElectricity);
        driveMotorModel.setRatedRotateSpeed(ratedRotateSpeed);
        driveMotorModel.setRatedTorque(ratedTorque);
        driveMotorModel.setMaxRotateSpeed(maxRotateSpeed);
        driveMotorModel.setMaxOutputTorque(maxOutputTorque);
        driveMotorModel.setPeakPower(peakPower);
        driveMotorModel.setPeakRotateSpeed(peakRotateSpeed);
        driveMotorModel.setPeakTorque(peakTorque);
        driveMotorModel.setValidPower(validPower);
        driveMotorModel.setControllerModel(controllerModel);
        driveMotorModel.setProdUnitId(prodUnitId);
        driveMotorModel.setControllerProdUnitId(controllerProdUnitId);

        driveMotorModelModel=new DriveMotorModelModel();
        driveMotorModelModel=DriveMotorModelModel.fromEntry(driveMotorModel);
        return driveMotorModelModel;
    }

}
