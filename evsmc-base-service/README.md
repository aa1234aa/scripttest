# 企业车联网基础服务 v2.3

## 大屏接口-充电热力图&数据统计&轨迹分布数据接口

1. 【充电热力图】获取车辆充电位置

   > 涉及表：VEH_DAY_SINGLE_CHARGE，SYS_VEHICLE，SYS_AREA

2. 【充电热力图】获取充电统计信息

   > 涉及表：VEH_DAYREPORT_CHARGESTATE，SYS_VEHICLE，SYS_AREA

3. 【数据统计】车辆充电时段分布

   > 涉及表：VEH_DAY_SINGLE_CHARGE_RUN

4. 【数据统计】车型平均单车行驶里程

   > 涉及表：VEH_DAYREPORT_RUNSTATE，SYS_VEHICLE，SYS_VEH_MODEL

5. 【数据统计】单车日行驶里程

   > 涉及表：VEH_DAYREPORT_RUNSTATE，SYS_VEHICLE，SYS_AREA，SYS_UNIT

6. 轨迹分布热力图

   > 涉及表：VEH_RUN_HEAT

7. 【数据统计】客户销量情况分布

   > 1. 查询 top 10运营单位
   >    
   >    涉及表：SYS_VEHICLE，SYS_UNIT
   > 
   > 2. 查询 top 10运营单位的在线车辆数
   >    
   >    涉及表：VEH_DAYREPORT_RUNSTATE，SYS_VEHICLE，SYS_UNIT

8. 【数据统计】SOC充电起始状态

   > 涉及表：VEH_DAYREPORT_CATEGORY

9. 获取所有运营单位

   > 涉及表：SYS_VEHICLE，SYS_UNIT



## 大屏接口-首页数据

1. 获取全国监控和在线车辆数

   > > 从段公子的接口取

2. 获取所有省份总车辆数和在线车辆数

   > 1. 获取所有省份【PARENT_ID = ‘0’】
   >    
   >    涉及表：SYS_AREA
   > 
   > 2. 统计所有省份的总车辆数/在线车辆数
   >    
   >    省份的总车辆数涉及表：SYS_VEHICLE，SYS_AREA
   >    
   >    在线车辆数涉及表【online_status = 1】：SYS_VEHICLE，SYS_AREA，SYS_VEHICLE_REAL_STATUS

3. 获取某个区域碳减排信息

   > 1. 获取车辆车型数据
   >    
   >    涉及表：SYS_VEH_MODEL
   > 
   > 2. 根据每个车型查找REDIS中的总里程计算碳排
   >    
   >    REDIS KEY【CARINFO.DISTRICTS.**areaId**.**车型ID**】取【mileage_total】

4. 获取可监控车辆的经纬度

   > 从段公子的接口取

5. 筛选获取车辆信息

   > 涉及表：SYS_VEHICLE

6. 单车监控

   > 数据来源于CTFO中的车辆实时数据
   > 
   > 获取车辆动力方式涉及表：SYS_VEHICLE，SYS_VEH_MODEL，SYS_DICT【type = "POWER_MODE"】
