CREATE TABLE `fault_incident_handling` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `veh_model_id` varchar(36) DEFAULT NULL COMMENT '车型id',
  `name` varchar(128) NOT NULL COMMENT '事故场景名称',
  `case_type` tinyint(4) NOT NULL COMMENT '预案类型',
  `scene_type` tinyint(4) NOT NULL COMMENT '场景类型',
  `scenario_describe` varchar(500) DEFAULT NULL COMMENT '事故场景描述',
  `processing_scheme` varchar(1000) NOT NULL COMMENT '处理方案',
  `describe` varchar(100) DEFAULT NULL COMMENT '文档描述',
  `platform` varchar(500) DEFAULT NULL COMMENT '上报平台',
  `report_state` varchar(500) DEFAULT NULL COMMENT '上报状态',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车型事故处置预案|车型事故处置预案|fih';