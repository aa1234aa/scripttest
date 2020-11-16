package com.bitnei.cloud.sys.util.translate;


import java.util.HashMap;
import java.util.Map;

import static com.bitnei.cloud.sys.util.translate.DataFormatter.TRANSLATE_POSTFIX;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： data-access-service <br>
 * 功能： 发动机状态 <br>
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
@Translate(dataSeqNos ={"2401"}, ruleEnums = {RuleTypeEnum.GB, RuleTypeEnum.DB})
public class MotorStatusTranslate implements ITranslate {
    /**
     * 解析
     *
     * @param dataSeqNo    数据项序号
     * @param value
     * @param ruleTypeEnum 协议编号
     * @return
     */
    @Override
    public Map<String, String> translate(String dataSeqNo, String value, RuleTypeEnum ruleTypeEnum) {

        String key = dataSeqNo + TRANSLATE_POSTFIX;
        Map<String, String> resultMap = new HashMap<>();
        if("1".equals(value)){
            resultMap.put(key, "启动");
        }
        else if ("2".equals(value)){
            resultMap.put(key, "关闭");
        }
        else if ("254".equals(value)){
            resultMap.put(key, "异常");
        }
        else if ("255".equals(value)){
            resultMap.put(key, "无效");
        }

        return resultMap;
    }
}
