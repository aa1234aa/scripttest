/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容： 实时车辆表新增字段：GPS是否定位状态字典项
 */

INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('158ed90b1d914a8592a9eba693c839d9', 'GPS是否定位', 'GPS_IS_LOCATE', '监控中心-gps是否定位状态值', '2019-07-26 16:17:44', 'admin', NULL, NULL);

INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('a691dac352a94a0c9a1a14fd49793a34', '从未定位', '9999', 'GPS_IS_LOCATE', '', '', 2, '2019-07-26 16:19:52', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('ed1a893a180147b5877be7d9dc467380', '有定位', '1', 'GPS_IS_LOCATE', '', '', 0, '2019-07-26 16:18:45', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('67e08522cb244d20ad57bb434b6d1d50', '无定位', '0', 'GPS_IS_LOCATE', '', '', 1, '2019-07-26 16:18:32', 'admin', NULL, NULL);