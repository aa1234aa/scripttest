ALTER TABLE `sys_core_extend_value`
  MODIFY COLUMN `json_value` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '属性值,Json字符串' AFTER `id_val`;

