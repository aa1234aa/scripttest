package com.bitnei.cloud.sys.client;

import com.bitnei.cloud.fault.model.ResultMsgModel;
import com.bitnei.cloud.sys.model.EncryptionChipModelModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 开放服务接口调用
 * @author zhouxianzhou
 * @date 2019/9/11 14:05
 **/
@FeignClient("evsmc-openservice")
public interface OpenServiceClient {

    /**
     * 芯片型号备案上报
     * @param list 芯片信号列表
     * @return
     */
    @PostMapping({"api/v1/evsmc/chipModelUp"})
    ResultMsgModel chipModelUp(@RequestBody List<EncryptionChipModelModel> list);

}
