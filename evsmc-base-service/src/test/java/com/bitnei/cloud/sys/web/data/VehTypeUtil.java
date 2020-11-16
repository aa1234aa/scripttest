package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 13:18
*/

import com.bitnei.cloud.sys.domain.VehType;
import com.bitnei.cloud.sys.model.VehTypeModel;
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
public class VehTypeUtil {

    /** 父种类id **/
    private String parentId;
    /** 名称 **/
    private String name="bitnei车辆种类"+ RandomValue.getNum(1000,9999);
    /** 种类性质 1:商用车 2:乘用车 **/
    private Integer attrCls=RandomValue.getNum(1,2);
    /** 编码 **/
    private String code=String.valueOf(RandomValue.getNum(100000,999999));
    /** 排序 **/
    private Integer orderNum=RandomValue.getNum(1,9999);
    /** 备注 **/
    private String note="testNote";
    /** 树路径 **/
    private String path;

    private VehType vehType;
    private VehTypeModel vehTypeModel;

    public VehTypeModel  createModel(){

        vehType=new VehType();
        vehType.setName(name);
        vehType.setAttrCls(attrCls);
        vehType.setCode(code);
        vehType.setOrderNum(orderNum);
        vehType.setNote(note);
        vehTypeModel = new VehTypeModel();
        vehTypeModel = VehTypeModel.fromEntry(vehType);
        return vehTypeModel;

    }
}
