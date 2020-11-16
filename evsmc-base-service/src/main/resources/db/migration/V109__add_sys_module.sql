/**
 变更人： 李杰洲
 时间： 2019-07-29
 变更内容： 修改车型事故处理预案菜单名，新增两个按钮权限
 */
UPDATE `sys_module` SET `name` = '车型事故处置预案' WHERE `code` = 'INCIDENTHANDLING';
INSERT IGNORE INTO `sys_module`(`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('b64a179774784e44a250747bca47eef6', 'ffa4bd9079cf403d97ec58fac01a7a26', '风险通知导出', 'riskNoticeExport', NULL, 1, '2f60d72f9f3c4b7cba4a4ffcb800cfa1/384103fca9dc4f3486eb8f160192567f/ffa4bd9079cf403d97ec58fac01a7a26/b64a179774784e44a250747bca47eef6/', '', '', NULL, 1, 0, '2019-07-22 10:50:06', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_module`(`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('16d0e24a853d4ef0b5ef67da009b39eb', 'ffa4bd9079cf403d97ec58fac01a7a26', '通知已读处理', 'readNoticeDispose', NULL, 1, '2f60d72f9f3c4b7cba4a4ffcb800cfa1/384103fca9dc4f3486eb8f160192567f/ffa4bd9079cf403d97ec58fac01a7a26/16d0e24a853d4ef0b5ef67da009b39eb/', '', '', NULL, 1, 0, '2019-07-22 10:45:27', 'admin', '2019-07-22 10:46:38', 'admin');