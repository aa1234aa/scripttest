package com.bitnei.cloud.sys.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 鹏 on 2015/10/19.
 */
@Data
public class MessageBean implements Serializable {

    private int id;
    private String name;
    private String src;
    private String value;
    private double offset;
    private double factor;
    private int len;
    private long longValue;
    private long index;
    private Object down = "-";
    private Object up = "-";
    private String group;
    private String unit;

    //辅助字段，如果是数据项，附上数据项编码
    private String seqNo;


    /**
     * 该项数据如果有异常，给出异常信息
     */
    @ApiModelProperty(value = "异常信息，存在信息则说明有异常")
    private String tips;
    @ApiModelProperty(value = "状态，为1说明超过范围")
    public int state = 0;
}


