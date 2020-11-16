/**
 变更人： 杨江桦
 时间： 2019-08-19
 变更内容： 增加kafka查询topic字典项
 */

INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) 
VALUES ('9e37aca6c8cb407190f79401d85485ac', 'kafka主题', 'KAFKA_TOPIC', '', '2019-08-19 18:44:49', 'admin', NULL, NULL);

INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('a7b2867d343849cea81f1e3eed68ba27', '车辆登录转发相关', '4', 'KAFKA_TOPIC', 'SYNC_VEHICLE_REG', '', 3, '2019-08-19 18:57:02', 'admin', '2019-08-19 18:57:19', 'admin');
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('ec91a1138ec145399f63512f6d5fe7c6', '车辆异常', '3', 'KAFKA_TOPIC', 'notice_topic', '', 2, '2019-08-19 18:52:55', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4adc89316432463eba50f3d240ef98aa', '故障报警', '2', 'KAFKA_TOPIC', 'alarm_topic', '', 1, '2019-08-19 18:51:13', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('46cd5b17241d44069883e5f9ef369067', '控制响应', '1', 'KAFKA_TOPIC', 'us_ctrlrsp', '', 0, '2019-08-19 18:50:16', 'admin', '2019-08-19 18:51:23', 'admin');