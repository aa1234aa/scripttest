ALTER TABLE fault_incident_handling CHANGE `describe` document_describe varchar(100) DEFAULT NULL COMMENT '文档描述',ADD from_id varchar(36) DEFAULT NULL COMMENT '来源';