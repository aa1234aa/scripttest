package com.bitnei.cloud.common.smsInterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * by hzr 2019-08-13
 */
@FeignClient("ws-service")
public interface SmsWsServiceClient {

    /**
     * 发送短信
     * @return
     */
    @PostMapping("/api/v1/ws/sendPhoneSms")
    void sendPhoneSms(@RequestParam("phoneNo") String phoneNo, @RequestParam("templateCode") String templateCode, @RequestParam("templateParam") String templateParam);
}
