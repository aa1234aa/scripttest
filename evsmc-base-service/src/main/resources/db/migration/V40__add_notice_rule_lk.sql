DROP TABLE IF EXISTS fault_notifier_rule_lk;
-- 2.4_创建故障通知表与规则关联表
CREATE TABLE `fault_notifier_rule_lk` (
`id`  varchar(36) NOT NULL COMMENT '主键ID' ,
`notifier_id`  varchar(36) NOT NULL COMMENT '推送负责人ID' ,
`rule_id`  varchar(36) NOT NULL COMMENT '规则ID, all表示全部规则' ,
`rule_type`  int(11) NOT NULL COMMENT '规则类型: 1=参数, 2=故障码, 3=围栏' ,
`create_by`  varchar(36) NULL DEFAULT NULL COMMENT '分配人' ,
`create_time`  varchar(20) NULL DEFAULT NULL COMMENT '分配时间' ,
PRIMARY KEY (`id`),
INDEX `idx_notifier_id` (`notifier_id`) USING BTREE ,
INDEX `idx_rule_type_rule_id` (`rule_type`, `rule_id`) USING BTREE
)
ENGINE=InnoDB
COMMENT='故障|故障通知表与规则关联表|fnrl'
;



