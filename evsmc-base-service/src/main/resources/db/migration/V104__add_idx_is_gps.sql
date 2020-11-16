/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容： 实时车辆表新增索引：GPS是否定位状态索引
 */

ALTER TABLE `sys_vehicle_real_status` ADD INDEX `index_is_gps`(`is_gps`) USING BTREE;