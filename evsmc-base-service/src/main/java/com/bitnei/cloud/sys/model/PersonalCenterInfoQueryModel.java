package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.PersonalCenterInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author cc
 */
@Data
public class PersonalCenterInfoQueryModel extends BaseModel {
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
    @DictName(code = "SEX", joinField = "sex")
    private String sexDisplay;
    @DictName(code = "CERT_TYPE", joinField = "cardType")
    private String cardTypeDisplay;
    private String AccountStateDisplay;
    private Integer isValid;
    private Integer isNeverExpire;


    public static PersonalCenterInfoQueryModel fromEntry(PersonalCenterInfo entry){
        PersonalCenterInfoQueryModel p = new PersonalCenterInfoQueryModel();
        BeanUtils.copyProperties(entry,p);
        return p;
    }

}
