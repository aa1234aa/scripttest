DROP TABLE IF EXISTS sys_vehicle_filing;
CREATE TABLE sys_vehicle_filing(
	id varchar(36) NOT NULL COMMENT '主键标识',
	vehicle_id varchar(36) NOT NULL COMMENT '车辆id',
	`status` TINYINT(4) COMMENT '备案状态',
	status_info VARCHAR(256) COMMENT '备案信息',
	public_key VARCHAR(128) COMMENT '国家平台公钥',
	signr VARCHAR(128) COMMENT '签名R值',
	signs VARCHAR(128) COMMENT '签名S值',
	create_time varchar(20) DEFAULT NULL COMMENT '创建时间',
  create_by varchar(36) DEFAULT NULL COMMENT '创建人',
  update_time varchar(20) DEFAULT NULL COMMENT '更新时间',
  update_by varchar(36) DEFAULT NULL COMMENT '更新人',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆防篡改备案|车辆防篡改备案表|svf';