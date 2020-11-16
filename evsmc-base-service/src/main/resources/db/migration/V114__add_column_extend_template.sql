ALTER TABLE `sys_core_extend_template`
  ADD COLUMN `validate_message`  varchar(64) NULL COMMENT '验证信息' AFTER `validate_rule`;
