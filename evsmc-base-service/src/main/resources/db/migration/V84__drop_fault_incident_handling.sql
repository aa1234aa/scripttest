DROP TABLE fault_incident_handling;
DELETE FROM sys_dict WHERE `type` = 'CASE_TYPE' OR `type` = 'SCENE_TYPE';
DELETE FROM sys_dict_category WHERE `code` = 'CASE_TYPE' OR `code` = 'SCENE_TYPE';
INSERT IGNORE INTO `sys_dict_category`(`id`, `name`, `code`, `note`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fe3885b0525f4eaf86cf7d52fd5e6b01', '文档类型', 'DOCUMENT_TYPE', '文档类型', '2019-02-20 14:01:59', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437202', '事故现场处置预案', '1', 'DOCUMENT_TYPE', '文档类型', NULL, 1, '2019-07-10 18:48:07', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('fd76f645fb314fa6818556b7b4437203', '高频报警整改方案', '2', 'DOCUMENT_TYPE', '文档类型', NULL, 2, '2019-07-10 18:48:07', 'admin', NULL, NULL);
CREATE TABLE `fault_incident_handling` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `veh_model_id` varchar(36) DEFAULT NULL COMMENT '车型id',
  `document_name` varchar(128) DEFAULT NULL COMMENT '文档名称',
  `document_type` tinyint(4) DEFAULT NULL COMMENT '文档类型',
  `report_state` tinyint(4) DEFAULT NULL COMMENT '上报状态',
  `reasons_for_failure` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `report_time` varchar(20) DEFAULT NULL COMMENT '上报时间',
  `platform` tinyint(4) DEFAULT NULL COMMENT '上报平台',
  `component_information` varchar(1000) DEFAULT NULL COMMENT '主要部件信息',
  `rescue` varchar(1000) DEFAULT NULL COMMENT '救援行动',
  `open_vehicle` varchar(1000) DEFAULT NULL COMMENT '打开车辆',
  `prohibitory_acts` varchar(1000) DEFAULT NULL COMMENT '禁止行为',
  `file_id` varchar(36) DEFAULT NULL COMMENT '附件id',
  `document_describe` varchar(17) DEFAULT NULL COMMENT '文档描述',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车型事故处置预案|车型事故处置预案|fih';