
INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac1', '事故发生时车辆状态', 'FAULT_VEHICLE_STATUS', '事故发生时车辆状态', '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac2', '预案类型', 'CASE_TYPE', '预案类型', '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac3', '场景类型', 'SCENE_TYPE', '场景类型', '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437082', '运行', '1', 'FAULT_VEHICLE_STATUS', '事故发生时车辆状态', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437083', '熄火', '2', 'FAULT_VEHICLE_STATUS', '事故发生时车辆状态', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437084', '充电', '3', 'FAULT_VEHICLE_STATUS', '事故发生时车辆状态', NULL, 3, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437085', '事故现场处置预案', '1', 'CASE_TYPE', '预案类型', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437086', '高频报警整改方案', '2', 'CASE_TYPE', '预案类型', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437087', '主要部件及位置介绍', '1', 'SCENE_TYPE', '场景类型', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437088', '救援行动', '2', 'SCENE_TYPE', '场景类型', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437089', '打开车辆', '3', 'SCENE_TYPE', '场景类型', NULL, 3, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437090', '禁止行为', '4', 'SCENE_TYPE', '场景类型', NULL, 4, '2019-07-10 18:48:07', 'admin', NULL, NULL);

INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac4', '事故上报状态', 'FAULT_REPORT_ STATUS', '事故上报状态', '2019-07-10 184807', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437091', '未上报', '1', 'FAULT_REPORT_ STATUS', '事故上报状态', NULL, 1, '2019-07-10 184807', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437092', '已上报', '2', 'FAULT_REPORT_ STATUS', '事故上报状态', NULL, 2, '2019-07-10 184807', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437093', '上报失败', '3', 'FAULT_REPORT_ STATUS', '事故上报状态', NULL, 3, '2019-07-10 184807', 'admin', NULL, NULL);