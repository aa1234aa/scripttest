/**
 变更人： 杨江桦
 时间： 2019-07-01
 变更内容： 控制命令下发记录表流水号字段长度调整
 */
ALTER TABLE `sys_instruct_send_rule` 
MODIFY COLUMN `session_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号';