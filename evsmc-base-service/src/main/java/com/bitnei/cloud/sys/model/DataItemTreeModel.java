package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.DataItemDetail;
import com.bitnei.cloud.sys.domain.DataItemTree;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemDetail新增模型<br>
* 描述： DataItemDetail新增模型<br>
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
* <td>2019-03-16 16:09:17</td>
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
@ApiModel(value = "DataItemTreeModel", description = "明细数据页面中的数据项树Model")
public class DataItemTreeModel extends BaseModel {



    private String id;
    private String name;
    private String parentId;
    private String path;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataItemTreeModel fromEntry(DataItemTree entry){
        DataItemTreeModel m = new DataItemTreeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
