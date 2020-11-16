-- region 短信下发任务明细状态表
create table sms_task_item_state(
    `fault_alarm_id` varchar(36) NOT NULL COMMENT 'fault_alarm_info表ID',
    `fault_status` int(11) DEFAULT NULL COMMENT '故障状态  1:未结束, 2已结束',
  `fault_begin_time` varchar(20) DEFAULT NULL COMMENT '报警开始时间',
    `sms_id` varchar(36) DEFAULT NULL COMMENT 'sms_task_item表ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信下发任务明细状态|短信下发任务明细状态表|stis';
-- endregion
