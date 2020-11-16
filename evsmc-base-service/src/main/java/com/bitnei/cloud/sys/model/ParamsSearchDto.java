package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ParamsSearchDto {

    @ApiModelProperty(value = "多个车辆id，逗号拼接")
    private String vehIds;

    @ApiModelProperty(value = "参数项code，逗号拼接")
    private String dictCodes;

    @ApiModelProperty(value = "车辆列表查询条件（选择全部车辆数据时使用，使用查询条件时vehIds要保持为空）")
    private PagerInfo pagerInfo;
}
