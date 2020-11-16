package com.bitnei.cloud.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class AbstractOfflineExportCsvHandlerTest {

    @Disabled
    @Test
    void format() {
        final double number = 123.456;
        Assertions.assertEquals("123.46", String.format("%.2f", number));
        Assertions.assertEquals("123", String.format("%.0f", number));
    }
}