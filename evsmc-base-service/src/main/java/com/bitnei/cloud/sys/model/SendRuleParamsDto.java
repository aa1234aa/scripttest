package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class SendRuleParamsDto {

    @ApiModelProperty(value = "车id集合，','号分割")
    private String vehIds;

    @NotEmpty(message = "国标code;控制命令参数0x83（当type=2即为动力锁车时）不能为空",
            groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "国标code;控制命令参数0x83（当type=2即为动力锁车时）")
    private String standardCode;

    @ApiModelProperty(value = "指令缓存时间")
    @NotNull(message = "指令缓存时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private Integer instructCacheTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "车辆列表查询条件（选择全部车辆数据时使用，使用查询条件时vehIds要保持为空）")
    private PagerInfo pagerInfo;

    @ApiModelProperty(value = "报警等级（字典项：RULE_ALARM_LEVEL）")
    private Integer alarmLevel;
}
