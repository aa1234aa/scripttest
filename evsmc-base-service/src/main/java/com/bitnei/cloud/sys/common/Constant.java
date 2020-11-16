package com.bitnei.cloud.sys.common;

/**
 * Created by Administrator on 2017/8/12.
 */
public interface Constant {
    //api
    public static String NAMESPACE_URI = "http://api.jasperwireless.com/ws/schema";
    public static String PREFIX = "jws";

    public static String URL = "https://api.10646.cn/ws/service/terminal";//物联网地址
    public static String redisKeyPrefix = "evsmc2.3_baseService_login_module_user_";
    public static final String redisKeyPrefix_user_lock = "evsmc2.3_baseService_login_module_user_lock_";

    //TODO 吉利隐藏  沃特玛使用 1
    public static String LICENSE_KEY = "dbefdb26-c6a1-4001-b9f0-8d512d32d538";//验证
    public static String USER_NAME = "zhencong";//用户名
    public static String PASS_WORD = "zc199505";//密码

    //吉利联通配置
    /*public static String LICENSE_KEY = "7050668d-3eda-417f-acf5-24c7b2c3132f";//验证
    public static String USER_NAME = "nbjlqc";//用户名
    public static String PASS_WORD = "1Q2W3E4R";//密码*/

    public static String SECURITY_POLICY = "securityPolicy.xml";//安全策略
    public static String ACTIVATE_TYPE = "ACTIVATED_NAME";//状态三 停用  DEACTIVATED_NAME  激活  ACTIVATED_NAME
    public static String DEACTIVATION_TYPE = "DEACTIVATED_NAME";//状态三 停用  DEACTIVATED_NAME  激活  ACTIVATED_NAME

    public static String STATUS_CODE = "3"; //状态码

    public static String SECURITY_MESSAGE_POJO = "SECURITY_MESSAGE_POJO"; //微信消息定KEY


    // 升级任务指令类型:自义定
    public static String PROTOCOL_TYPE_CUSTOM = "99";

    /**
     * 车辆在线，数据库状态
     */
    public static Integer VEHICLE_ONLINE = 1;
    /**
     * 车辆离线，数据库状态
     */
    public static Integer VEHICLE_OFFLINE = 2;

    //删除状
    public static Integer STATUS = 1;


    /**
     * yes and no or True false
     */
    interface TrueAndFalse {
        Integer TRUE = 1;
        Integer FALSE = 0;
    }

    /**
     * 推送类型
     */
    interface SendInfoType {
        String SHORT_MESSAGE = "1"; //短信
        String WECHAT = "2";   //微信
    }


    /**
     * 删除状态
     */
    interface DeleteType {
        String NO_DELETE = "1";  //可用状态
        String OFF_DELETE = "0";  //不可用

    }

    /**
     * 工单类别
     */
    interface WorkOrderType {
        Integer CARYEARINSPECTION = 10; //车辆年检
        Integer CARMAINTAIN = 20; //车辆保养
        Integer CARREPAIR = 30; //车辆维修
        Integer CARACCIDENT = 40; //车辆事故
        Integer CALLTHEPOLICE = 50; //报警处理工单
    }


    /**
     * 工单状态
     */
    interface WorkOrderStatus {
        Integer TOBECONFIRMED = 100; //待确认
        Integer TOBECHECK = 200; //待审核
        Integer TOBEDISPATCH = 300; //审核通过，待派单
        Integer DISPATH = 301; // 审核通过，已派单
        Integer WORKING = 400; //执行中
        Integer TOBEFINISH = 500; //待完结
        Integer TOBEEVALUATE = 600; //待评价
        Integer FINISH = 700; //完结
        Integer NOTPASS = 101; //确认不通过
        Integer NOTPASSCHECK = 201; //审核不通过
        Integer CANCEL = 800; //取消
        Integer STOP = 801; //中止
        Integer HANG = 900; //搁置
    }

    /**
     * 执行状态
     */
    interface WorkOrderExecuteStatus {
        Integer UNCOMMIT = 411; //未提交方案
        Integer COMMIT = 412; //提交方案
        Integer CONFIRM = 413; //已确认   ==>工单待完结
        Integer UNCONFIRM = 414; //不通过  -==工单取消
    }

    /**
     * 工单来源
     */
    interface WorkOrderSource {
        Integer PC = 1; //PC端
        Integer WECHAT = 2; //微信端
        Integer FOUREZERO = 3; //400来电
        Integer SECURITY_ALARM = 4; //安全告警
        Integer ANNUAL_SOURCE = 5; //年检工单
        Integer UPKEEP_SOURCE = 6; //保养工单
        Integer ALARM_SOURCE = 7;//数据告警工单
        Integer FAULT_SOURCE = 8;//故障码告警工单
    }

    /**
     * 工单来源描述
     */
    interface OrderSource {
        String FOUREZERO = "400来电"; //400来电
        String SECURITY_ALARM = "故障报警"; //安全告警
    }

    /**
     * 工单服务方式
     */
    interface WorkOrderServerType {
        Integer TOTHESTORE = 1; //到店
        Integer SCENE = 2; //现场
    }

    /**
     * 工单优先级
     */
    interface ORDER_PRIORITY {
        String GENERAL = "10"; //一般
        String URGENT = "20"; //紧急
    }

    /**
     * 生成编码前缀
     */
    interface DATA_SOURE_CODE {
        String PC = "PC";
        String PHONEPC = "PC400";
    }

    /**
     * 是否完成处理
     */
    interface Complete {
        String YES = "1";//已完成
        String NO = "2";//未完成
    }

    /**
     * 400处理状态
     */
    interface DeelState {
        String NEW = "1";
        String FELLOW = "2";
        String COMPLETE = "3";
    }

    /**
     * SIM卡状态
     */
    interface SIM_STATUS {
        String NORMAL = "1";//正常
        String OWE = "-1";///欠费
        String INACTIVE = "3";//未激活
        String STOP = "2";//停用
        String UNREGISTER = "4";//未注册
    }

    /**
     * 车辆状态
     */
    interface CAR_STATUS {
        String BE_SALE = "10";//待销售
        String HAVE_SALE = "20";//已销售
        String HAVE_SCRAP = "30";//已报废
    }

    /**
     * Left to right
     */
    interface DealType {
        Short Left = 1;//左
        Short Right = 2;//右
    }

    /**
     * 处理记录 信息来源
     */
    interface InfoSource {
        Short PC = 1;
        Short WX = 2;
    }

    /**
     * 运营商
     */
    interface InfoCdmaType {
        String MOVE = "200"; //移动
        String UNICOM = "100"; //联通
        String TELECOM = "300"; //电信
    }


    ///////////////////////// 通知kafa 常量////////////////////////////

    /**
     * 1、模块，及 kafa 传送的Key
     */
    interface ModuleStatusAndKafaKey {
        String FAULT_WARNING = "FAULT_WARNING "; //安全预警
        String FAULT_ALARM = "FAULT_ALARM "; //故障告警
        String DISTRIBUTE_WORKORDER = "DISTRIBUTE_WORKORDER";  //工单
    }


    /**
     * 通知操作类型
     */
    interface NoticeType {
        String ORDER_CONFIRM = "101";  //工单确认 、批量确认
        String ORDER_AUDIT = "102";  //工单审核 、批量审核
        String ORDER_SEND = "103";  //工单派发
        String ORDER_STOP = "104";  //工单中止
        //....

        String FAULT_REVERT = "201";  //故障回复
        String FAULT_DEAL_WITH = "202"; // 故障处理
        String EARLY_WARNING = "301"; // 预警处理


    }

    /**
     * 派单人类型  1 为派发人标识，
     * 2 为创建工单人标识
     */
    interface DistributeOrderType {
        String ORDER_DISTRIBUTE = "1";  //派发人
        String ORDER_CREATE = "2";  //创建工单人
    }

    // 预警提醒类型
    interface AlarmInfoType {
        String FAULT_WARNING = "100"; //安全预警
        String FAULT_ALARM = "200"; //故障告警
        String BUSINESS_REMINDER = "300"; //业务提醒
    }

    //  用户类型: 0：个人 1：单位
    interface UserType {
        Integer personal = 0;
        Integer unit = 1;
    }

    //  短息接口Key
    interface MsKey {
        String CID = "1ZuHgz4ZSurS";
    }

    //  导出文件名
    interface EXCEL_FILE_NAME {
        //工时配置
        String WORK_TIME_NAME = "工时配置.xls";
    }

    /**
     * 故障消息 故障开始 -0  故障结束 -1
     */
    interface INFOALERTPOJOSTATUS {
        Integer START_STATUS = 0;
        Integer END_STATUS = 1;
    }

    /**
     * 平台响应方式
     */
    interface SYS_INFO_ALERT {
        String NOTHING = "0";  //无
        String PROMPTDIALOG = "1"; //弹屏
        String PROMPTDIALOGANDVOICE = "2"; //弹屏+声音

    }

    /**
     * 可充电装置类型 1:动力蓄电池 2:超级电容
     */
    interface EnergyDeviceType {
        /**
         * 动力蓄电池
         **/
        Integer BATTERY = 1;
        /**
         * 超级电容
         **/
        Integer CAPACITY = 2;
    }

    /**
     * 证件类型 1:居民身份证 2:军官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证
     */
    interface CARDTYPE {
        /**
         * 居民身份证
         **/
        Integer IDENTITY = 1;
        /**
         * 军官证
         **/
        Integer SERGEANT = 2;
        /**
         * 学生证
         **/
        Integer STUDENT = 3;
        /**
         * 驾驶证
         **/
        Integer DRIVINGLICENCE = 4;
        /**
         * 护照
         **/
        Integer PROTECTION = 5;
        /**
         * 港澳通行证
         **/
        Integer HKANDMP = 6;
    }

    /**
     * 申报状态，0未申报，1待审核，2待拨款，3已拨款，4待整改
     */
    interface SUBSIDY_APPLY_STATUS {
        /**
         * 未申报
         **/
        Integer UNDECLARED = 0;
    }

    /**
     * 芯片型号备案状态，0未备案 1备案成功 2备案失败
     */
    interface CHIP_FILING_STATUS {
        /** 0未备案 **/
        Integer UN = 0;
        /** 1备案成功 **/
        Integer SUCCESS = 1;
        /** 2备案失败 **/
        Integer FAIL = 2;
    }

    /**
     * 单位类型编码
     */
    interface UNIT_TYPE_CODE {
        /**
         * 供应商
         **/
        String PROVIDER = "1007";
        /**
         * 汽车厂商
         **/
        String VEHICLE = "0001";
        /**
         * 运营单位
         **/
        String OPER = "1003";
        /**
         * 购车单位
         **/
        String SELL = "1002";
    }

    /**
     * 燃料形式 = 柴油
     **/
    Integer FUEL_FORM_DIESEL = 2;
    /**
     * 动力方式 = 传统燃油车
     **/
    Integer POWER_MODE_FUEL = 6;


    String SUCCESS = "success";
    String FAIL = "fail";

    int RESP_TYPE_CHIP = 2;
    int RESP_TYPE_VEHICLE = 5;
}
