ALTER TABLE fault_safety_accidents CHANGE `report_state` report_state tinyint(4) DEFAULT NULL COMMENT '事故上报状态',CHANGE `platform` platform tinyint(4) DEFAULT NULL COMMENT '上报平台';