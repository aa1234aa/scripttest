package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/12/13 15:38
*/

import com.bitnei.cloud.sys.domain.VehNotice;
import com.bitnei.cloud.sys.model.VehNoticeModel;
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
public class VehNoticeUtil {


    /** 车辆公告号 **/
    private String name="bitneivehnotice"+ RandomValue.getNum(1000,9999);
    /** 车辆种类id **/
    private String vehTypeId;
    /** 车辆品牌id **/
    private String brandId;
    /** 车系id **/
    private String seriesId;
    /** 公告目录年份 **/
    private Integer noticeYear=2019;
    /** 公告目录批次 **/
    private Integer noticeBatch=RandomValue.getNum(1,9999);
    /** 推荐目录年份 **/
    private Integer recommendYear=2019;
    /** 推荐目录批次 **/
    private Integer recommendBatch=RandomValue.getNum(1,9999);
    /** 发布日期，格式yyyy-MM-dd **/
    private String publishDate=RandomValue.getNowTimeWithOutHMS();
    /** 是否免征 **/
    private Integer isExempt=1;
    /** 是否燃油 **/
    private Integer isFuel=1;
    /** 是否环保 **/
    private Integer isProtection=1;

    private VehNotice vehNotice;
    private VehNoticeModel vehNoticeModel;

    public VehNoticeModel createModel(){

        vehNotice=new VehNotice();
        vehNotice.setName(name);
        vehNotice.setBrandId(brandId);
        vehNotice.setSeriesId(seriesId);
        vehNotice.setVehTypeId(vehTypeId);
        vehNotice.setNoticeBatch(noticeBatch);
        vehNotice.setNoticeYear(noticeYear);
        vehNotice.setRecommendBatch(recommendBatch);
        vehNotice.setRecommendYear(recommendYear);
        vehNotice.setPublishDate(publishDate);
        vehNotice.setIsExempt(isExempt);
        vehNotice.setIsFuel(isFuel);
        vehNotice.setIsProtection(isProtection);
        vehNoticeModel = new VehNoticeModel();
        vehNoticeModel = VehNoticeModel.fromEntry(vehNotice);
        return vehNoticeModel;

    }
}
