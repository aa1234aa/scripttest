package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.orm.bean.TailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： bqcz-report <br>
 * 功能： 模块 <br>
 * 描述： 模块 <br>
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
 * <td>2018-03-02</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.7
 */
public class ModuleInstall extends TailBean {


    private String id;
    private String name;
    private Integer isFun;
    private String path;
    private String icon;
    private String action;
    private Integer orderNum;
    private String subActions;
    private String parentId;
    private Integer isRoot;

    private List<ModuleInstall> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getSubActions() {
        return subActions;
    }

    public void setSubActions(String subActions) {
        this.subActions = subActions;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }

    public Integer getIsFun() {
        return isFun;
    }

    public void setIsFun(Integer isFun) {
        this.isFun = isFun;
    }

    public List<ModuleInstall> getChildren() {
        return children;
    }

    public void setChildren(List<ModuleInstall> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"isFun\":")
                .append(isFun);
        sb.append(",\"path\":\"")
                .append(path).append('\"');
        sb.append(",\"icon\":\"")
                .append(icon).append('\"');
        sb.append(",\"action\":\"")
                .append(action).append('\"');
        sb.append(",\"orderNum\":")
                .append(orderNum);
        sb.append(",\"subActions\":\"")
                .append(subActions).append('\"');
        sb.append(",\"parentId\":\"")
                .append(parentId).append('\"');
        sb.append(",\"isRoot\":")
                .append(isRoot);
        sb.append('}');
        return sb.toString();
    }
}
