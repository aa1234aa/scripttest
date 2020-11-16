/**
 变更人： 杨江桦
 时间： 2019-08-20
 变更内容： 增加kafka查询topic字典项
 */

INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('7f3065ab1baa4634ac00d8d9da019fd8', '报文数据', '6', 'KAFKA_TOPIC', 'us_packet', '', 5, '2019-08-20 09:41:00', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('2793d4da54534dafaa30dc3e671f86d1', '实时数据', '5', 'KAFKA_TOPIC', 'us_general', '', 4, '2019-08-20 09:16:45', 'admin', NULL, NULL);