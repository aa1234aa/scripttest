package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.common.DataItemKey;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.HisdataQuery;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： HisdataQuery新增模型<br>
* 描述： HisdataQuery新增模型<br>
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
* <td>2019-03-22 16:01:36</td>
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
@ApiModel(value = "HisdataQueryModel", description = "历史数据(轨迹)查询Model")
public class HisdataQueryModel extends BaseModel {

    @ApiModelProperty(value = "轨迹开始时间")
    private String startTime;

    @ApiModelProperty(value = "轨迹结束时间")
    private String endTime;

    @ApiModelProperty(value = "轨迹持续时间")
    private String durationTime;

    private VehicleModel veh;
    private VehicleRealStatusModel vehicleRealStatusModel;

    @ApiModelProperty(value = "轨迹(历史)数据")

    private List<Map<String,String>> subdata;

    public static HisdataQueryModel create(List<Map<String,String>> dataList,VehicleModel vehicleModel){
       HisdataQueryModel hisdataQueryModel= create(dataList);
       hisdataQueryModel.setVeh(vehicleModel);
       return hisdataQueryModel;

    }
    public static HisdataQueryModel create(List<Map<String,String>> dataList){
        if (dataList==null||dataList.size()==0){
            throw new BusinessException("无数据");
        }
        HisdataQueryModel model=new HisdataQueryModel();
        String key=DataItemKey.RtDateTime;

        if (dataList.size() == 1) {
            Map<String, String> map = dataList.get(0);


            model.setStartTime(map.get(key));
            model.setEndTime(map.get(key));
            model.setDurationTime("0");
        }
        else {


            model.setEndTime(dataList.get(dataList.size() - 1).get(key));
            model.setStartTime(dataList.get(0).get(key));
            long t = com.bitnei.cloud.sys.util.DateUtil.getDateDifferenceSec(model.getEndTime(), model.getStartTime());
            model.setDurationTime(com.bitnei.cloud.sys.util.DateUtil.formatSeconds(t));
        }
        model.setSubdata(dataList);

        return model;
    }

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static HisdataQueryModel fromEntry(HisdataQuery entry){
        HisdataQueryModel m = new HisdataQueryModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
