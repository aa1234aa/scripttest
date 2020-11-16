# 所属零部件
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by)
VALUES ('bdfa4893169e4902b1bb1bfb38f93cbf', '通用报警', '0000', 'SUBORDINATE_PARTS', '通用报警', '', '0', NOW(), 'xuzhijie');

# 报警等级
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by)
VALUES ('0cdd2e62603947649e6d96c276f7aca9', '四级', '4', 'ALARM_LEVEL', '四级', NULL, '4', NOW(), 'xuzhijie');

# 参数异常规则
ALTER TABLE fault_parameter_rule ADD COLUMN `type` tinyint(1) DEFAULT 0 COMMENT '规则类型 0 参数规则， 1车型同步过来的规则';

# 车型报警阈值
ALTER TABLE sys_veh_model_alarm ADD COLUMN enable tinyint(1) DEFAULT 0 COMMENT '是否启用报警规则 0 禁用， 1启用';
ALTER TABLE sys_veh_model_alarm ADD COLUMN response_mode  tinyint(1)  DEFAULT NULL COMMENT '平台响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知';
ALTER TABLE sys_veh_model_alarm ADD COLUMN begin_threshold  bigint(20) NOT NULL DEFAULT 0 COMMENT '开始时间阈值(秒)';
ALTER TABLE sys_veh_model_alarm ADD COLUMN end_threshold  bigint(20) NOT NULL DEFAULT 0 COMMENT '结束时间阈值(秒)';


