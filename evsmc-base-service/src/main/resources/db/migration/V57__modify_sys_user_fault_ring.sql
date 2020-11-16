
# sys_user_fault_ring新增弹窗.短信两种报警通知方式
ALTER TABLE sys_user_fault_ring ADD is_allow_dialog int(1) COMMENT '是否弹窗通知报警(1是0否)' AFTER is_allow_ring;
ALTER TABLE sys_user_fault_ring ADD is_allow_sms int(1) COMMENT '是否短信通知报警(1是0否)' AFTER is_allow_dialog;