package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.sys.domain.Log;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * 操作日志MODEL
 * @author zxz
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "LogModel", description = "变更日志Model")
public class LogModel {

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "操作人")
    private String oper;

    @ApiModelProperty(value = "模块")
    private String moduleName;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "内容")
    private String content2;

    @ApiModelProperty(value = "动作")
    private String action;

    @ApiModelProperty(value = "耗时")
    private Integer times;

    @ApiModelProperty(value = "响应结果")
    private String result;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "变更信息")
    private String changeInfo;

    @ApiModelProperty(value = "路径")
    private String url;

    /**
     * 将实体转为前台model
     * @param entry Log
     * @return LogModel
     */
    public static LogModel fromEntry(Log entry){
        LogModel m = new LogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
