package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import com.google.common.collect.Sets;
import jodd.util.StringUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.Set;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupResourceRule实体<br>
* 描述： GroupResourceRule实体<br>
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
* <td>2019-01-22 16:30:51</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Data
public class GroupResourceRule extends TailBean {

    /** 主键 **/
    private String id;
    /** 资源项ID **/
    private String resourceItemId;
    /** 组ID **/
    private String groupId;
    /** 操作符(0:等于 1:不等于) **/
    private Integer op;
    /** 值 **/
    private String val;
    /** 序号 **/
    private Integer orderNum;
    /** 与前一条规则逻辑运算符(0:与 1:或) **/
    private Integer preLogicOp;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    public Set<String> getValSet(){
        Set<String> set = Sets.newHashSet();
        if (StringUtil.isNotEmpty(val)){
            String[] tmp = val.split(",");
            for (String t: tmp){
                set.add(t);
            }
        }
        return set;
    }

}
