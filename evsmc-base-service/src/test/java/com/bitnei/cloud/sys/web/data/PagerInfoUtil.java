package com.bitnei.cloud.sys.web.data;

/*
@author 黄永雄
@create 2019/11/29 18:46
*/

import com.bitnei.cloud.orm.bean.PagerInfo;
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
public class PagerInfoUtil {

    private PagerInfo pagerInfo;

    public PagerInfo createPagerInfo(){

        pagerInfo=new PagerInfo();
        pagerInfo.setLimit(10);
        pagerInfo.setStart(0);
        return pagerInfo;
    }
}
