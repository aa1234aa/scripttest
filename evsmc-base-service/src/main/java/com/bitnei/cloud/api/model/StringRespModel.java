package com.bitnei.cloud.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StringRespModel {

    private String result;

    public static StringRespModel build(String result) {
        return new StringRespModel(result);
    }
}
