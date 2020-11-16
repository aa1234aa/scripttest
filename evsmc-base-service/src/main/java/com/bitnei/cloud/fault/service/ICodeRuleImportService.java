package com.bitnei.cloud.fault.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： CodeRuleImportService接口<br>
 * 描述： CodeRuleImportService接口，在xml中引用<br>
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
 * <td>2019-05-07 09:57:07</td>
 * <td>huangweimin</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @version 1.0
 *
 * @author huangweimin
 * @since JDK1.8
 */
public interface ICodeRuleImportService {

    /**
     * 导入模版下载
     * @param analyzeType 解析方式:1=按字节解析 , 2=按bit位解析
     */
    void getImportTemplateFile(int analyzeType);

    /**
     * 批量导入
     *
     * @param analyzeType 解析方式:1=按字节解析 , 2=按bit位解析
     * @param file 文件
     */
    void batchImport(int analyzeType, MultipartFile file);

    /**
     * 检查故障码长度， 起始位/起始字节
     *
     * @param analyzeType         解析方式
     * @param exceptionCodeLength 故障码长度
     * @param startPoint          起始位/起始字节
     * @param errorCallback       回调
     */
    void checkStartPointAndExceptionCodeLength(int analyzeType, Integer exceptionCodeLength, Integer startPoint, Consumer<String> errorCallback);

}
