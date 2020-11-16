/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容： 修改历史分表rule_name字段长度结构存储过程
 */

ALTER TABLE `fault_alarm_info`
MODIFY COLUMN `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称' AFTER `rule_id`;

ALTER TABLE `fault_alarm_info_history` 
MODIFY COLUMN `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称' AFTER `rule_id`;