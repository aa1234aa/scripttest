
## 1. 故障报警

## 1.1 处理记录表
delete fa from fault_alarm_process fa, fault_alarm_info ai where  ai.id = fa.fault_alarm_id  and ai.rule_name like '%bitnei%';
delete fa from fault_alarm_process fa,  fault_alarm_info ai ,sys_vehicle v where  fa.fault_alarm_id=ai.id and v.id=ai.vehicle_id and  v.vin like '%bitnei%';
## 1.2 清理测试规则的数据
delete FROM  fault_alarm_info WHERE rule_name LIKE '%bitnei%';
delete fa from fault_alarm_info_history_201906 fa,sys_vehicle v where v.id=fa.vehicle_id and v.vin like '%bitnei%'
## 1.3 清理测试车辆产生的故障数据
delete ai from fault_alarm_info ai, sys_vehicle v where  v.id=ai.vehicle_id and  v.vin like '%bitnei%';
## 1.4 删除参数规则
DELETE FROM fault_parameter_rule where name like '%bitnei%';
## 1.5 删除故障码规则
delete from fault_code_rule where fault_name like '%bitnei%';



#  数据交换

## 平台转入日志
delete pe from dc_platform_event pe, dc_platform_information pi where pi.id=pe.pid and pi.name like '%bitnei%';
## 平台转入配置
delete from dc_platform_information where name like '%bitnei%';
## 静态数据推送日志
delete from dc_platform_vehicle_push_log;
delete from sys_push_batch;
delete from sys_push_batch_detail;
delete from sys_veh_push_status;

## 删除平台和规则关联关系
delete lk from dc_platform_rule_lk lk, dc_forward_rule rule where rule.id=lk.forward_rule_id and rule.name like '%bitnei%';
delete it from dc_forward_rule_item it, dc_forward_rule rule where rule.id=it.forward_rule_id and rule.name like '%bitnei%';
delete from dc_forward_rule where name like '%bitnei%';

## 删除平台关联车辆
delete fv from dc_forward_vehicle fv, dc_forward_platform fp where fp.id=fv.platform_id and fp.name like '%bitnei%';
delete from dc_forward_platform where name like '%bitnei%';

## 删除通讯协议
delete lk from dc_rule_item_lk lk , dc_rule rule  where rule.id=lk.rule_id and  rule.name like  '%bitnei%';
delete from dc_rule where name like  '%bitnei%';

# 异常车辆
delete can from sys_veh_not_can_201906 can,  sys_vehicle v where  v.uuid=can.veh_uuid and  v.vin like '%bitnei%';
delete np from sys_veh_not_position_201906 np,  sys_vehicle v where  v.uuid=np.veh_uuid and  v.vin like '%bitnei%';
delete ir from sys_veh_idle_record ir,  sys_vehicle v where  v.id=ir.vehicle_id and  v.vin like '%bitnei%';
delete np from sys_soc_vehicle_log_201906 np,  sys_vehicle v where  v.uuid=np.vid and  v.vin like '%bitnei%'
delete from geely_iccid_change_log where vin like '%bitnei%';

# 远程升级和远程控制

DELETE from sys_instruct_task where instruct_id in (SELECT id from sys_uppackage_send where task_name LIKE '%bitnei%' or task_name LIKE '%BITNEI%')
                                 or instruct_id in (SELECT id from sys_instruct_send_rule where vin LIKE '%bitnei%' or vin LIKE '%BITNEI%');

DELETE from sys_uppackage_send where task_name LIKE '%bitnei%' or task_name LIKE '%BITNEI%';
DELETE from sys_uppackage_send_details where vin LIKE '%bitnei%' or vin LIKE '%BITNEI%';
DELETE from sys_instruct_send_rule where vin LIKE '%bitnei%' or vin LIKE '%BITNEI%';
DELETE from sys_uppackage_info where file_name LIKE '%bitnei%' or file_name LIKE '%BITNEI%' or nickname LIKE '%bitnei%' or nickname LIKE '%BITNEI%';
DELETE from sys_vehicle_version_info where serial_number LIKE '%bitnei%' or serial_number LIKE '%BITNEI%';
DELETE from sys_upgrade_log where task_name LIKE '%bitnei%' or task_name LIKE '%BITNEI%';
DELETE from sys_term_param_record where vin LIKE '%bitnei%' or vin LIKE '%BITNEI%';
DELETE from sys_instruct_management_model where instruct_management_id in (SELECT id from sys_instruct_management where name LIKE '%bitnei%' or name LIKE '%BITNEI%');
DELETE from sys_instruct_management where name LIKE '%bitnei%' or name LIKE '%BITNEI%';

# 统计分析
delete vdr from veh_dayreport_region vdr, sys_vehicle v where v.uuid = vdr.vid and v.vin like '%bitnei%';
delete vds from veh_dayreport_summary vds, sys_vehicle v where v.uuid = vds.vid and v.vin like '%bitnei%';
delete vde from veh_dayreport_expansion vde, sys_vehicle v where v.uuid = vde.vid and v.vin like '%bitnei%';
delete vdc from veh_dayreport_chargestate vdc, sys_vehicle v where v.uuid = vdc.vid and v.vin like '%bitnei%';
delete vdru from veh_dayreport_runstate vdru, sys_vehicle v where v.uuid = vdru.vid and v.vin like '%bitnei%';
delete vdmc from veh_dayreport_mileage_check vdmc, sys_vehicle v where v.uuid = vdmc.vid and v.vin like '%bitnei%';
delete vdas from veh_dayreport_abnormal_situation vdas, sys_vehicle v where v.uuid = vdas.vid and v.vin like '%bitnei%';

## 删除品牌系列
DELETE FROM sys_veh_brand WHERE `name` LIKE '%bitnei%';
DELETE FROM sys_veh_series WHERE `name` LIKE '%bitnei%';
## 删除车辆型号预警  删除车辆型号
DELETE ma FROM sys_veh_model_alarm ma, sys_veh_model m WHERE ma.id = m.id AND m.veh_model_name LIKE '%bitnei%';
DELETE FROM sys_veh_model WHERE veh_model_name LIKE '%bitnei%';
## 删除车辆公告
DELETE FROM sys_veh_notice WHERE `name` LIKE '%bitnei%';
##  删除车辆信息  删除车辆与 可充能装置 驱动装置 发电装置 关联信息
DELETE lk FROM sys_vehicle_engery_device_lk lk, sys_vehicle v WHERE lk.vehicle_id = v.id AND v.vin LIKE '%bitnei%';
DELETE lk FROM sys_vehicle_drive_device_lk lk, sys_vehicle v WHERE lk.vehicle_id = v.id AND v.vin LIKE '%bitnei%';
DELETE lk FROM sys_vehicle_power_device_lk lk, sys_vehicle v WHERE lk.vehicle_id = v.id AND v.vin LIKE '%bitnei%';
## 删除车辆种类
DELETE FROM sys_veh_type WHERE `name` LIKE '%bitnei%';

# 删除单位
delete from sys_unit where name like  '%bitnei%';
# 删除单位联系人
delete from sys_owner_people where owner_name like '%bitnei%';
# 删除车主
delete from sys_veh_owner where owner_name like  '%bitnei%';
# 删除权限组
delete rr from sys_group_resource_rule rr, sys_group g where g.id=rr.group_id and g.name like '%bitnei%';
delete lk from sys_user_group_lk lk, sys_group g where lk.group_id=g.id and  g.name like '%bitnei%';
delete from sys_group where name like '%bitnei%';

# 删除角色
delete lk from sys_user_role_lk lk, sys_role ro where ro.id=lk.role_id and ro.name like '%bitnei%';
delete from sys_role where name like '%bitnei%';
delete from sys_user where username like  '%bitnei%' and username !='bitnei';


## 可充电储能装置
delete from sys_battery_device_model where name like '%bitnei%';
delete from sys_super_capacitor_model where name like '%bitnei%';
delete from sys_engery_device where name like '%bitnei%';
## 驱动装置
delete from sys_drive_motor_model where name like '%bitnei%';
delete from sys_engine_model where name like '%bitnei%';
delete from sys_drive_device where code like '%bitnei%';
## 发电装置
delete from sys_fuel_generator_model where name like '%bitnei%';
delete from sys_fuel_system_model where name like '%bitnei%';
delete from sys_power_device where code like '%bitnei%';
## SIM
delete from sys_sim_management where iccid like '%bitnei%';
## 终端
delete FROM sys_term_model WHERE term_model_name LIKE '%bitnei%';
delete FROM sys_term_model_unit WHERE serial_number LIKE '%bitnei%';

## 删除零件号-通讯协议关联管理信息
DELETE FROM geely_term_part_number_item WHERE part_number_id IN(SELECT id FROM geely_term_part_number where part_number like '%bitnei%');
DELETE FROM geely_term_part_number WHERE part_number like '%bitnei%';

DELETE from geely_vehicle_activate_log  where vin LIKE '%bitnei%';
delete from geely_server_inspect_log where iccid like '%bitnei%';
delete from geely_server_inspect_log where term_number '%bitnei%';
delete from sys_data_change_log where inst_name like  '%bitnei%';
delete from sys_data_change_log where operator like  '%bitnei%';


# 最后删除车辆
delete from sys_vehicle where vin like '%bitnei%';
