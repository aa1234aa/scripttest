package com.bitnei.cloud.sys.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehAuditModel {

    /*    车辆审核状态
    0-基础数据未审核
    1-基础数据程序审核通过
    2-基础数据程序审核未通过
    3-基础数据人工审核通过
    4-基础数据人工审核未通过
    5-实时数据程序审核通过
    6-实时数据程序审核未通过
    7-实时数据人工审核通过
    8-实时数据人工审核未通过
*/

    /** id */
    private String id;
    /** 车辆审核状态 */
    private String code;
    /** 审核信息 */
    private String message;

}
