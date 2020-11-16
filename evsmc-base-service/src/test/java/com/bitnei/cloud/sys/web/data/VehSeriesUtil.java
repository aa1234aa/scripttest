package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 14:32
*/

import com.bitnei.cloud.sys.domain.VehSeries;
import com.bitnei.cloud.sys.model.VehSeriesModel;
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
public class VehSeriesUtil {

    /** 名称 **/
    private String name="bitnei"+ RandomValue.getNum(1,999);
    /** 品牌ID **/
    private String brandId;
    /** 编码 **/
    private String code=String.valueOf(RandomValue.getNum(10,9999));
    /** 内部编号 **/
    private String interNo=String.valueOf(RandomValue.getNum(1000,9999));
    /** 备注 **/
    private String note="系列备注";

    private VehSeries vehSeries;
    private VehSeriesModel vehSeriesModel;

    public VehSeriesModel createModel(){
        vehSeries=new VehSeries();
        vehSeries.setName(name);
        vehSeries.setBrandId(brandId);
        vehSeries.setCode(code);
        vehSeries.setNote(note);
        vehSeries.setInterNo(interNo);
        vehSeriesModel = new VehSeriesModel();
        vehSeriesModel = VehSeriesModel.fromEntry(vehSeries);
        return vehSeriesModel;
    }
}
