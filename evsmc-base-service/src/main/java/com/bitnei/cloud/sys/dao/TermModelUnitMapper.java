package com.bitnei.cloud.sys.dao;


import com.bitnei.cloud.sys.domain.TermModelUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermModelUnitMapper接口<br>
* 描述： TermModelUnitMapper接口，在xml中引用<br>
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
* <td>2018-11-05 10:01:48</td>
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
@Mapper
public interface TermModelUnitMapper {

    /**
     * 根据 iccid、终端型号 查询
     * @param params
     * @return
     */
    TermModelUnit findByParams(Map<String,Object> params);

    /**
     * 插入
     * @param obj
     * @return
     */
    int insert(TermModelUnit obj);


    /**
     * 根据终端编号查询
     * @param params 参数
     * @return 实体
     */
    TermModelUnit findBySerialNumber(Map<String,Object> params);


    /**
     * 根据iccid查询一个
     * @param iccid
     * @return
     */
    TermModelUnit findByIccid(String iccid);

    /**
     * 更新IMEI和条形码
     * @param params imei:IMEI barCode:条形码
     * @return 影响行数
     */
    int updateImeiAndBarCode(Map<String,Object> params);


    Integer findByEncryptionChipIdCount(Map<String,Object> params);

    /**
     * 更新加密芯片ID
     * @param params
     * @return
     */
    int updateEncryptionChipId(Map<String, Object> params);
}
