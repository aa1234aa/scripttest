package com.bitnei.cloud.sys.domain;


import lombok.Data;

@Data
public class PersonalCenterInfo {

   private String ownerName;
   private Integer sex;
   private String telPhone;
   private String email;
    private String jobNumber;
    private String address;
    private Integer cardType;
    private String cardNo;
    private String cardAddress;
    private String frontCardImgId;
    private String backCardImgId;
    private String faceCardImgId;
    private String unitName;
    private String jobPost;
    private String userName;
    private String roleName;
    private String accountState;
    private String lastValidDate;
    private String AccountStateDisplay;
    private Integer isValid;
    private Integer isNeverExpire;

}
