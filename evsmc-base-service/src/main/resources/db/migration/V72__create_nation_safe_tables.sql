CREATE TABLE `fault_disposal_opinions` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `code` varchar(36) DEFAULT NULL COMMENT '唯一标识码',
  `opinions` varchar(500) DEFAULT NULL COMMENT '国家平台管理员意见',
  `time` varchar(20) DEFAULT NULL COMMENT '时间',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国家平台管理员意见表|国家平台管理员意见表|fdo';
CREATE TABLE `veh_risk_history_adjudicate` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `code` varchar(36) DEFAULT NULL COMMENT '唯一标识码',
  `annotations` varchar(200) DEFAULT NULL COMMENT '批注',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆风险历史处理记录表|车辆风险历史处理记录表|rha';
ALTER TABLE fault_veh_risk_notice ADD file_id varchar(36) DEFAULT NULL COMMENT '附件id',ADD code varchar(36) DEFAULT NULL COMMENT '消息编码';
delete lk from sys_user_group_lk lk, sys_group g where g.id=lk.group_id and g.user_id is not null;