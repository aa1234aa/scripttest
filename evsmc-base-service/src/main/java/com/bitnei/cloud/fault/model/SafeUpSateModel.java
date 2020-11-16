package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;


/**
 * *  ┏┓　　　┏┓
 *     ┏┛┻━━━┛┻┓
 *     ┃　　　　　　　┃ 　
 *     ┃　　　━　　　┃
 *     ┃　┳┛　┗┳　┃
 *     ┃　　　　　　　┃
 *     ┃　　　┻　　　┃
 *     ┃　　　　　　　┃
 *     ┗━┓　　　┏━┛
 *     　　┃　　　┃神兽保佑
 *     　　┃　　　┃代码无BUG！
 *     　　┃　　　┗━━━┓
 *     　　┃　　　　　　　┣┓
 *     　　┃　　　　　　　┏┛
 *     　　┗┓┓┏━┳┓┏┛
 *     　　　┃┫┫　┃┫┫
 *     　　　┗┻┛　┗┻┛
 *
 *
 * 批量阅读model
 * @author lijiezhou
 */
@Setter
@Getter
public class SafeUpSateModel {

    /** 消息编号 */
    private String code;

    /** vin */
    private String vin;

    /** 消息状态 */
    private String state;

    /** 处理意见 */
    private String dealNote;

    /** 风险等级 */
    private String dangerLevel;

    /** 更新时间 */
    private String updateTime;

}
