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
 * 车型事故处理预案model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class IncidentModel {

    /** 车辆型号 */
    private String vehModel;

    /** 回复意见 */
    private String note;

    /** 文件 */
    private String docs;

    /** 文件类型 1:事故现场处置预案 2：高频报警整改方案 */
    private String docsType;

    /** 上报时间 yyyy-MM-dd hh:mm:ss */
    private String createTime;

}
