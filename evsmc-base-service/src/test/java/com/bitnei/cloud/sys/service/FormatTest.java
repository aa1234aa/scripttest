package com.bitnei.cloud.sys.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class FormatTest {

    @Disabled
    @Test
    void dateFormat() {
        System.out.println("历史故障信息-admin-" +
            DateFormatUtils.format(
                System.currentTimeMillis(),
                "yyyy-MM-dd_HH-mm-ss_SSS"
            )
            + ".csv");
        System.out.println("历史故障信息-admin-" +
            DateFormatUtils.format(
                System.currentTimeMillis(),
                "yyyyMMddHHmmssSSS"
            )
            + ".csv");
        System.out.println("历史故障信息-admin-" +
            DateFormatUtils.format(
                System.currentTimeMillis(),
                "yyyyMMdd-HHmmss-SSS"
            )
            + ".csv");
    }
}
