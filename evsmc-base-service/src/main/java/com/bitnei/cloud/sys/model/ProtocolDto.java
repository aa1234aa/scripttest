package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.client.model.ProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolDto {

    private String vid;

    private String recvTime;

    private String srvrRecvTime;

    private Integer type;

    private String data;

    private Integer verify;

    private Integer dataLength;

    private String vin;

    private String interNo;

    private String licensePlate;

    private String ruleTypeName;

    private String typeName;

    private String verifyName;


    @JsonIgnore
    private List<ProtocolMessage> protocolMessages;
}
