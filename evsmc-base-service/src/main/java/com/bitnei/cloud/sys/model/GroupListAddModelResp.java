package com.bitnei.cloud.sys.model;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "GroupListAddModelResp", description = "列表模式新增响应Model")
public class GroupListAddModelResp {

    /** 追加数量 **/
    private int appendCount = 0;
    /** 黑名单数量 **/
    private int blockCount = 0;
    /** 黑名单id列表 **/
    private List<String> blockIds = Lists.newArrayList();
    /** 追加数量id **/
    private List<String> appendIds = Lists.newArrayList();

    /**
     * 增加黑名单id
     * @param id
     */
    public void addBlockId(final String id){
        blockIds.add(id);
        blockCount ++ ;
    }

    public void addAppendId(final String id){
        appendIds.add(id);
        appendCount ++ ;
    }

}
