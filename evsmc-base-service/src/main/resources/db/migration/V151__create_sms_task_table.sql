CREATE TABLE `sms_task` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `template_id` varchar(36) DEFAULT NULL COMMENT '模板id',
  `template_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
  `template_code` varchar(50) DEFAULT NULL COMMENT '对应短信提供商的模板code',
  `service_type` int(11) DEFAULT NULL COMMENT '业务类型: 1、短信下发; 2、终端短信唤醒',
  `sms_content` text COMMENT '短信内容',
  `receivers` varchar(500) DEFAULT NULL COMMENT '接收人/车辆id, 以json格式保存',
  `variables` varchar(500) DEFAULT NULL COMMENT '以json格式保存',
  `status` int(11) DEFAULT NULL COMMENT '任务状态: 0发送, 1未发送(草搞)',
  `remarks` varchar(45) DEFAULT NULL COMMENT '备注',
  `biz_id` varchar(200) DEFAULT NULL COMMENT '短信发送流水号',
  `create_by` varchar(36) DEFAULT NULL COMMENT '发送人',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
  `add_all` int(11) DEFAULT '0' COMMENT '是否为 添加全部查询结果:  0不是, 1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信下发任务|短信下发任务表|gst';


CREATE TABLE `sms_task_item` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `task_id` varchar(36) DEFAULT NULL COMMENT '任务id',
  `service_type` int(11) DEFAULT NULL COMMENT '业务类型: 1、短信下发; 2、终端短信唤醒; 3、故障',
  `receiver_type` int(11) DEFAULT NULL COMMENT '接收人类型: 1、单位联系人, 2、个人车主, 3、车辆负责人',
  `receiver_id` varchar(36) DEFAULT NULL COMMENT '接收人id',
  `receiver` varchar(100) DEFAULT NULL COMMENT '接收人',
  `vehicle_id` varchar(36) DEFAULT NULL COMMENT '业务类型为：2 输入车辆id',
  `vin` varchar(45) DEFAULT NULL COMMENT '车架号',
  `msisd` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `biz_id` varchar(200) DEFAULT NULL COMMENT '短信发送流水号',
  `send_status` int(11) DEFAULT NULL COMMENT '发送状态 1：等待回执，2：发送失败，3：发送成功',
  `send_time` varchar(20) DEFAULT NULL COMMENT '发送时间',
  `fail_msg` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
  `fault_id` varchar(36) DEFAULT NULL COMMENT '业务类型为：3 输入 故障id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信下发任务明细|短信下发任务明细表|gsti';
