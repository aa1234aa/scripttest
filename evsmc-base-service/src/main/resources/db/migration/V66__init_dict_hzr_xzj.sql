INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac5', '通知状态', 'NOTICE_STATE', '通知状态', '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437094', '未读', '1', 'NOTICE_STATE', '通知状态', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437095', '未读超时', '2', 'NOTICE_STATE', '通知状态', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437096', '已读', '3', 'NOTICE_STATE', '通知状态', NULL, 3, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437097', '未回复', '4', 'NOTICE_STATE', '通知状态', NULL, 4, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437098', '已回复', '5', 'NOTICE_STATE', '通知状态', NULL, 5, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437099', '审核中', '6', 'NOTICE_STATE', '通知状态', NULL, 6, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6ac6', '风险等级', 'RISK_LEVEL', '风险等级', '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437100', '1级', '1', 'RISK_LEVEL', '风险等级', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437101', '2级', '2', 'RISK_LEVEL', '风险等级', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437102', '3级', '3', 'RISK_LEVEL', '风险等级', NULL, 3, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437103', '4级', '4', 'RISK_LEVEL', '风险等级', NULL, 4, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437104', '5级', '5', 'RISK_LEVEL', '风险等级', NULL, 5, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('477d1ebca77611e98081005056a63d3a', '已灌装', '2', 'PRIVATE_KEY_FILING_STATUS', '私钥灌装状态', '', '2', '2019-07-04 14:15:06', 'admin', NULL, NULL);
# 添加车辆防篡改备案状态字典项

INSERT IGNORE INTO `sys_dict_category` (`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('5668f1c75a8d49fe97449b3281795fe9', '备案状态', 'FILING_STATUS', '车辆防篡改备案状态', '2019-07-03 16:14:39', 'admin', NULL, NULL);

INSERT IGNORE INTO `sys_dict` (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('3b2f7cec5efc4cefb0c8bd98f85251aa', '备案成功', '1', 'FILING_STATUS', '备案成功', '', '2', '2019-07-03 16:15:06', 'admin', NULL, NULL);

INSERT IGNORE INTO `sys_dict` (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('e11dbf827b8e4b47a99b6d52148eb416', '备案失败', '0', 'FILING_STATUS', '备案失败', '', '1', '2019-07-03 16:15:22', 'admin', NULL, NULL);

INSERT IGNORE  INTO `sys_dict` (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('e11dbf827b8e4b47a99b6d52148eb417', '未备案', '2', 'FILING_STATUS', '未备案', '', '3', '2019-07-03 16:15:22', 'admin', NULL, NULL);


# 修改参数报警规则表字段长度

ALTER TABLE `fault_parameter_rule`
MODIFY COLUMN `level_describe`  varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '级别描述' AFTER `response_mode`;


ALTER TABLE `fault_parameter_rule`
MODIFY COLUMN `formula`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公式, strom计算使用' AFTER `enabled_status`;