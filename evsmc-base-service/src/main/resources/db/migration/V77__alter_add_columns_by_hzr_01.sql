
## 去掉默认值
ALTER TABLE `sys_veh_model`
DROP COLUMN `car_body_structure`,
DROP COLUMN `veh_tech_level`;

## 车辆型号 新增字段
ALTER TABLE `sys_veh_model`
ADD COLUMN `car_body_structure`  tinyint(4) NULL COMMENT '车体结构' AFTER `rating_date`,
ADD COLUMN `veh_tech_level`  tinyint(4) NULL COMMENT '车辆技术等级' AFTER `car_body_structure`,
ADD COLUMN `environmental_info_no`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '环保信息公开编号' AFTER `veh_tech_level`,
ADD COLUMN `model_details`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车型详情' AFTER `environmental_info_no`,
ADD COLUMN `vehicle_type`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车辆类型' AFTER `model_details`,
ADD COLUMN `veh_inspection_report`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车辆检测报告文件' AFTER `vehicle_type`,
ADD COLUMN `total_guests_num`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '总客数' AFTER `veh_inspection_report`,
ADD COLUMN `record_activation_mode`  tinyint(4) NULL COMMENT '备案激活模式' AFTER `total_guests_num`;

## 备案激活模式 字典项
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('9175e984a92211e9b607089e01629908', '备案激活模式', 'RECORD_ACTIVATION_MODE', '备案激活模式', '2019-07-18 14:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('9e56bf3ca92211e9b607089e01629908', '无需备案激活', '1', 'RECORD_ACTIVATION_MODE', '备案激活模式', '', '1', '2019-07-18 14:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('a6024626a92211e9b607089e01629908', '需备案激活', '2', 'RECORD_ACTIVATION_MODE', '备案激活模式', '', '2', '2019-07-18 14:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('ad2dcd40a92211e9b607089e01629908', '其他', '3', 'RECORD_ACTIVATION_MODE', '备案激活模式', '', '3', '2019-07-18 14:04:39', 'admin', NULL, NULL);