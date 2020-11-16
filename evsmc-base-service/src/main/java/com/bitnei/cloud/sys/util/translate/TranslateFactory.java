package com.bitnei.cloud.sys.util.translate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： data-access-service <br>
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
 * <td>2019-03-11</td>
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
public class TranslateFactory
{
    private static TranslateFactory ourInstance = new TranslateFactory();

    public static TranslateFactory getInstance() {
        return ourInstance;
    }

    /**
     * 解析器map
     * key: 协议编码-数据项编码
     * value: 翻译器
     */
    private static final Map<String, ITranslate> translateMap = new HashMap<>();

    /**
     * 默认构造函数
     */
    private TranslateFactory() {
    }

    /**
     * 增加翻译器
     * @param t
     * @param iTranslate
     */
    public void putTranslate(Translate t, ITranslate iTranslate){

        for (RuleTypeEnum typeEnum: t.ruleEnums()){
            for (String no: t.dataSeqNos()){
                String key = String.format("%s-%s", typeEnum.getCode(), no);
                translateMap.put(key, iTranslate);

            }
        }
    }

    /**
     * 获取数据项翻译器
     * @param ruleCode
     * @param code
     * @return
     */
    public ITranslate getTranslate(String ruleCode, String code){

        String key = String.format("%s-%s", ruleCode, code);
        return translateMap.get(key);
    }
}
