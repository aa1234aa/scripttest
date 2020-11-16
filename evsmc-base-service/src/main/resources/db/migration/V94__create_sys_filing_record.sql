# 国六防篡改备案上报记录表
DROP TABLE IF EXISTS sys_filing_record;
CREATE TABLE sys_filing_record(
	id varchar(36) NOT NULL COMMENT '主键标识',
	from_type TINYINT(4) COMMENT '记录类型(1:芯片 2:终端 3:发动机 4:车型 5:车辆)',
	from_id varchar(36) NOT NULL COMMENT '类型对应id',
	from_status TINYINT(4) COMMENT '状态(0:失败 1:成功)',
	create_time varchar(20) DEFAULT NULL COMMENT '创建时间',
  create_by varchar(36) DEFAULT NULL COMMENT '创建人',
  update_time varchar(20) DEFAULT NULL COMMENT '更新时间',
  update_by varchar(36) DEFAULT NULL COMMENT '更新人',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国六防篡改备案上报记录|国六防篡改备案上报记录表|sfr';