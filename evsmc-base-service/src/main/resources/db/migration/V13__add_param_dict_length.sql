
/**
增加终端参数表的主键
 */
INSERT INTO `sys_term_param_dic`(`id`, `name`, `code`, `setup_code`, `receive_code`, `data_size`,
`data_type`, `state`, `describes`, `create_by`, `create_time`, `sequence`, `is_setup`, `param_type`)
VALUES ('13', '公共平台域名长度', '13', '52155', '51155', 1, 'BYTE', 1, '公共平台域名长度 n', NULL, NULL, NULL, 0, 1)
ON DUPLICATE KEY UPDATE `setup_code` = '52155', `receive_code` = '51155', `is_setup` = 0;

INSERT INTO `sys_term_param_dic`(`id`, `name`, `code`, `setup_code`, `receive_code`, `data_size`,
`data_type`, `state`, `describes`, `create_by`, `create_time`, `sequence`, `is_setup`, `param_type`)
VALUES ('4', '远程服务与管理平台域长度', '4', '52055', '51055', 1, 'BYTE', 1, '远程服务与管理平台域名长度 m',
NULL, NULL, NULL, 0, 1)
ON DUPLICATE KEY UPDATE `setup_code` = '52055', `receive_code` = '51055', `is_setup` = 0;