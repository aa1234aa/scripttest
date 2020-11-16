ALTER TABLE `sys_group`
ADD COLUMN `resource_type_id`  varchar(36) NULL COMMENT '资源类型ID' AFTER `rule_type`;
