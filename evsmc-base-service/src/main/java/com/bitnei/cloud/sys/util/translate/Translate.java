package com.bitnei.cloud.sys.util.translate;


import java.lang.annotation.*;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： data-access-service <br>
 * 功能： 翻译器 <br>
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
 * <td>2019-03-09</td>
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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Translate {
    /**
     * 数据项列表
     * @return
     */
    String[] dataSeqNos();

    /**
     * 协议类型编码
     * @return
     */
    RuleTypeEnum[] ruleEnums();
}
