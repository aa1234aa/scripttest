/**
 变更人： 杨江桦
 时间： 2019-08-16
 变更内容： 修改阈值字段为非必填
 */
ALTER TABLE `fault_code_rule` 
MODIFY COLUMN `begin_threshold` bigint(20) NULL DEFAULT 0 COMMENT '开始时间阈值(秒)' AFTER `threshold`,
MODIFY COLUMN `begin_count_threshold` int(11) NULL DEFAULT 1 COMMENT '开始计数阈值(次)' AFTER `begin_threshold`,
MODIFY COLUMN `end_threshold` bigint(20) NULL DEFAULT 0 COMMENT '结束时间阈值(秒)' AFTER `enable_time_threshold`,
MODIFY COLUMN `end_count_threshold` int(11) NULL DEFAULT 1 COMMENT '结束计数阈值(次)' AFTER `end_threshold`;