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
 * 批量回复model
 * @author lijiezhou
 */
@Setter
@Getter
public class SafeUpReplySateModel {

    /** 消息编号 */
    private String code;

    /** vin */
    private String vin;

    /** 消息状态 */
    private String state;

    /** 回复意见 */
    private String note;

    /** 文件id */
    private String docs;

    /** 更新时间 */
    private String updateTime;

}
