package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.cloud.screen.protocol.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-03-20</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Data
public class GroupRuleInfo extends TailBean {
    /** 表名 **/
    private String tableName;
    /** 组id **/
    private String groupId;
    /** 操作符 **/
    private String preCode;
    /** 值 **/
    private String val;
    /** 操作符 **/
    private Integer op;
    /** 与前一逻辑运算符 **/
    private Integer preLogicOp;
    /** 排序 **/
    private Integer orderNum;
    /** 我的标签 **/
    private String myTags;

    /**
     * 增加标签
     * @param tags
     */
    public void addTags(String preCode, String tags, boolean isTree){
        if (StringUtil.isEmpty(myTags)){
           myTags = "";
        }

        String tagStr = preCode+tags;
        if (isTree){
            tagStr += "*";
        }
        if (op == null){
            op = 0;
        }
         if (op == 1){
             if (StringUtil.isEmpty(myTags)){
                myTags += " +CHENPENG ";
             }
             myTags += String.format(" -%s ",  tagStr);
         }
         else {
             myTags += (" "+tagStr);
         }



    }


}
