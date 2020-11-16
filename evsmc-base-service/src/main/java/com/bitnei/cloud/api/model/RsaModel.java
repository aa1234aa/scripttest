package com.bitnei.cloud.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RsaModel {

    private String publicKey;

    private String privateKey;
}
