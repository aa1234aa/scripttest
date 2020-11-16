## 加密芯片型号 新增字段
ALTER TABLE `sys_encryption_chip_model`
ADD COLUMN `iso_secret_img_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '芯片资质图片国密' AFTER `update_by`,
ADD COLUMN `chip_attachment_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '芯片附件' AFTER `iso_secret_img_id`,
ADD COLUMN `chip_declare`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '芯片说明' AFTER `chip_attachment_id`;
ALTER TABLE `sys_encryption_chip_model`
ADD COLUMN `iso_9001_img_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '芯片资质图片9001' AFTER `chip_declare`;

## 终端型号 新增字段
ALTER TABLE `sys_term_model`
ADD COLUMN `term_detection_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车载终端检测编号' AFTER `support_encryption_chip`,
ADD COLUMN `detection_report_img_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '终端检测报告扫描件' AFTER `term_detection_no`;

## 车载终端 新增字段
ALTER TABLE `sys_term_model_unit`
ADD COLUMN `network_access_number`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '终端入网证号' AFTER `encryption_chip_id`,
ADD COLUMN `term_description`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车载终端说明' AFTER `network_access_number`;

## 车牌类型 字典类型
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('1fdfde03a15f11e9b607089e01629908', '车牌类型', 'LICENSE_TYPE', '车牌类型', '2019-07-08 16:14:39', 'admin', NULL, NULL);

## 车辆型号 新增字段
ALTER TABLE `sys_veh_model`
ADD COLUMN `car_body_structure`  tinyint(4) NULL DEFAULT 1 COMMENT '车体结构' AFTER `drive_type_location`,
ADD COLUMN `approved_load`  double NULL COMMENT '核定载重(t)' AFTER `car_body_structure`,
ADD COLUMN `max_total_mass`  double NULL COMMENT '车辆最大总质量(t)' AFTER `approved_load`,
ADD COLUMN `epa_veh_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '环保局车辆类型' AFTER `max_total_mass`,
ADD COLUMN `transport_bureau_veh_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运输局车辆类型' AFTER `epa_veh_type`,
ADD COLUMN `veh_tech_level`  tinyint(4) NULL DEFAULT 1 COMMENT '车辆技术等级' AFTER `transport_bureau_veh_type`,
ADD COLUMN `rating_date`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '等级评定日期' AFTER `veh_tech_level`;

## 新增字典 车体结构
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('f40547d3a16711e9b607089e01629908', '车体结构', 'CAR_BODY_STRUCTURE', '车体结构', '2019-07-08 18:14:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('c93521b29e2111e9b607089e01629908', '承载式车身', '1', 'CAR_BODY_STRUCTURE', '车体结构', '', '1', '2019-07-08 18:15:06', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('812b7016a16811e9b607089e01629908', '半承载式车身', '2', 'CAR_BODY_STRUCTURE', '车体结构', '', '2', '2019-07-08 18:15:06', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('868759eca16811e9b607089e01629908', '非承载式车身', '3', 'CAR_BODY_STRUCTURE', '车体结构', '', '3', '2019-07-08 18:15:06', 'admin', NULL, NULL);
## 车辆技术等级
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('9ab18919a16811e9b607089e01629908', '车辆技术等级', 'VEH_TECH_LEVEL', '车辆技术等级', '2019-07-08 18:14:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('e709628fa16811e9b607089e01629908', '一级', '1', 'VEH_TECH_LEVEL', '车辆技术等级', '', '1', '2019-07-08 18:15:06', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('ec02ac66a16811e9b607089e01629908', '二级', '2', 'VEH_TECH_LEVEL', '车辆技术等级', '', '2', '2019-07-08 18:15:06', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('f43078e4a16811e9b607089e01629908', '三级', '3', 'VEH_TECH_LEVEL', '车辆技术等级', '', '3', '2019-07-08 18:15:06', 'admin', NULL, NULL);

## 车牌颜色
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('add507d2a20e11e9b607089e01629908', '车牌颜色', 'LICENSE_COLOR', '车牌颜色', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('c761379ea20e11e9b607089e01629908', '蓝色', '1', 'LICENSE_COLOR', '车牌颜色', '', '1', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('cf2d1e67a20e11e9b607089e01629908', '绿色', '2', 'LICENSE_COLOR', '车牌颜色', '', '2', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('d52cb6f6a20e11e9b607089e01629908', '黄绿', '3', 'LICENSE_COLOR', '车牌颜色', '', '3', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('3985352da20f11e9b607089e01629908', '白色', '4', 'LICENSE_COLOR', '车牌颜色', '', '4', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('3f3d8524a20f11e9b607089e01629908', '黑色', '5', 'LICENSE_COLOR', '车牌颜色', '', '5', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('447d2f40a20f11e9b607089e01629908', '黄色', '6', 'LICENSE_COLOR', '车牌颜色', '', '6', '2019-07-09 16:04:39', 'admin', NULL, NULL);
## 二级维护状态
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('b8ee95efa20f11e9b607089e01629908', '二级维护状态', 'SECONDARY_MTC_STATUS', '二级维护状态', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('c1070d13a20f11e9b607089e01629908', '未维护', '1', 'SECONDARY_MTC_STATUS', '二级维护状态', '', '1', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('c6844ae6a20f11e9b607089e01629908', '已维护', '2', 'SECONDARY_MTC_STATUS', '二级维护状态', '', '2', '2019-07-09 16:04:39', 'admin', NULL, NULL);
## 年检状态
INSERT INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('0f06e776a21011e9b607089e01629908', '年检状态', 'ANNUAL_INSPECTION_STATUS', '年检状态', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('14a8e125a21011e9b607089e01629908', '未年检', '1', 'ANNUAL_INSPECTION_STATUS', '年检状态', '', '1', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('1ccc409fa21011e9b607089e01629908', '已年检', '2', 'ANNUAL_INSPECTION_STATUS', '年检状态', '', '2', '2019-07-09 16:04:39', 'admin', NULL, NULL);
INSERT INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('23e314c5a21011e9b607089e01629908', '年检过期', '3', 'ANNUAL_INSPECTION_STATUS', '年检状态', '', '3', '2019-07-09 16:04:39', 'admin', NULL, NULL);

## 车辆运营使用信息 新增字段
ALTER TABLE `sys_vehicle`
ADD COLUMN `secondary_mtc_status`  tinyint(4) NULL DEFAULT 1 COMMENT '二级维护状态:1-未维护；2-已维护' AFTER `license_plate_color`,
ADD COLUMN `secondary_mtc_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级维护时间' AFTER `secondary_mtc_status`,
ADD COLUMN `forced_scrap_date`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '强制报废日期' AFTER `secondary_mtc_time`,
ADD COLUMN `initial_registration_date`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '初次登记日期' AFTER `forced_scrap_date`,
ADD COLUMN `annual_inspection_status`  tinyint(4) NULL DEFAULT 1 COMMENT '年检状态:1-未年检；2-已年检；3-年检过期' AFTER `initial_registration_date`,
ADD COLUMN `annual_inspection_date`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年检日期' AFTER `annual_inspection_status`,
ADD COLUMN `annual_inspection_period`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年检有效期' AFTER `annual_inspection_date`,
ADD COLUMN `maintenance_period`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保养有效期' AFTER `annual_inspection_period`;





