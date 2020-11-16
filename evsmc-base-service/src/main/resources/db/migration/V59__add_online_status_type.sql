/**
 变更人： 杨江桦
 时间： 2019-07-10
 变更内容： 增加在线状态字典类型'上过线'
 */
INSERT IGNORE INTO `sys_dict` ( `id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by` )
VALUES
	( 'c1508cbc333f4dab9687233514b70acc', '上过线', '3', 'ONLINE_STATUS', '', '', NULL, '2019-07-10 08:44:55', 'admin', NULL, NULL );