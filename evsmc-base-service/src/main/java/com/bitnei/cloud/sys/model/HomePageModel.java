package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.model.AlarmInfoModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.HomePage;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HomePage新增模型<br>
* 描述： HomePage新增模型<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2019-03-19 14:43:56</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "HomePageModel", description = "首页统计Model（前端进行除法计算x率）")
public class HomePageModel extends BaseModel {

    @ApiModelProperty(value = "车辆总数")
    private Integer total;

    @ApiModelProperty(value = "接入总数")
    private Integer onlined;

    @ApiModelProperty(value = "在线数")
    private Integer vehOnline;

    @ApiModelProperty(value = "活跃数")
    private Integer active;

    @ApiModelProperty(value = "报警列表")
    private Object alarmInfos;

    @ApiModelProperty(value = "区域车辆统计")
    private List<Map<String,Object>> areaStat;

    @ApiModelProperty(value = "gps为0的车辆数")
    private Integer zeroGPS;

    private Integer faultVehCount;
    private Integer notOnlined;
   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static HomePageModel fromEntry(HomePage entry){
        HomePageModel m = new HomePageModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
