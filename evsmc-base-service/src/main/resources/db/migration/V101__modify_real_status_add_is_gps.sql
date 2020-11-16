/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容： 实时车辆表新增字段：GPS是否定位说明
 */
 
ALTER TABLE `sys_vehicle_real_status` 
ADD COLUMN `is_gps` varchar(20) NULL COMMENT 'GPS是否定位说明' AFTER `current_gps_area_id`;