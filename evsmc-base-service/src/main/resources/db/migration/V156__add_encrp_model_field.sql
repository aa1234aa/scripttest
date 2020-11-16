-- 芯片型号表添加备案状态(字典项:FILING_STATUS)
ALTER TABLE sys_encryption_chip_model ADD filing_status TINYINT(4) DEFAULT 0 COMMENT '备案状态';
-- 芯片型号备案状态字典项
INSERT IGNORE INTO sys_dict_category (`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('57f2b835febd4bcab1f9718dacf02a40', '芯片备案状态', 'CHIP_FILING_STATUS', '芯片型号备案状态', '2019-09-12 10:21:57', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('3ad4487d78e7478eaf2252e088964b2d', '备案成功', '1', 'CHIP_FILING_STATUS', '备案成功', '', '2', '2019-09-12 10:23:29', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('67b06c5bf1934c169f9f74f4ab921b9d', '备案失败', '2', 'CHIP_FILING_STATUS', '备案失败', '', '3', '2019-09-12 10:23:48', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('b438170b34614cf9bbeea6be1dbc4cd5', '未备案', '0', 'CHIP_FILING_STATUS', '未备案', '', '1', '2019-09-12 10:22:25', 'admin', NULL, NULL);
