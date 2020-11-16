package com.bitnei.cloud.common;

import jodd.util.RandomString;
import org.springframework.http.HttpHeaders;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-module <br>
 * 功能： 基础单元测试类  <br>
 * 描述： 基础单元测试类  <br>
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
 * <td>2018-${MOTH}-17</td>
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
public class BaseControllerTest {


    public static String fuelGeneratorModelModeName="bitneiryfdj8685";      //默认燃油发电机型号名
    public static String ROLENAME="超级管理员";                            //默认角色名
    public static String OWNERNAME="黄永雄";                               //默认负责人
    public static String UNITSUPPLIERNAME="默认供应商";                     //默认供应商
    public static String DriveMotorModelName="bitneiqddj3099";             //默认驱动电机型号
    public static String batteryDeviceModelModelName="bitneidlcdc3397";    //默认动力蓄电池型号
    public static String SUPPORTPROTOCOL="ac298dfcc7774c7eacd5b5d0d3e91d3d"; //GBT32960标准版协议
    public static String RULEID="bfd0c8de4878410088fb573e165d4a09";          //GB_T32960协议类型
    public static String TermModelName="默认车载终端型号";                   //默认车载终端型号
    public static String VEHTYPENAME="默认车辆种类";
    public static String VEHBRANDNAME="默认车型品牌";
    public static String VEHSERIESNAME="默认车型系列";
    public static String VEHNOTICENAME="默认车辆公告型号";
    public static String UNITPRODUCERNAME="默认汽车厂商";
    public static String UNITFACTORYNAME="默认车辆制造工厂";
    public static String MODELNAME="默认车辆型号";



    /**
     * 获取header
     * @return
     */
    public HttpHeaders httpHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept","application/json");
        headers.add("cookie","CSRF=YzI5MzBiYzA3MmZhNGQzMDk1MDA0NTUyZGQwOGZkMDU=;R_SESS=sJPPDUjCHahFygSVdZVGc01t4Gi0s/kr0ZgL6zIWc1CmbehKuKDsMi0TXm5Mi0xs+1+lxywR753hDYN8SC67RsAwT708+JFJ8ML6WbxzeTQBNMJKK+bcPK+DRQgVW2mEQOwq6W5JUeukaCtsN51rMA==");
        headers.add("x-api-csrf","YzI5MzBiYzA3MmZhNGQzMDk1MDA0NTUyZGQwOGZkMDU=");
        return headers;
    }

    /**
     * 获取单元测试使用的id
     * @return
     */
    public String getId(){

        String id = String.format("UNIT%s", RandomString.getInstance().randomBase64(28));
        return id;
    }
}
