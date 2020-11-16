package com.bitnei.cloud.common.feginInterface;

import com.bitnei.cloud.common.client.model.DataRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("data-access-service")
public interface DataAccessInterface {

    @PostMapping({"/bigData/realData/findByAllVehicle"})
    Map<String, Map<String, String>> findRealDataByAllVehicle(@RequestBody DataRequest dataRequest);
}
