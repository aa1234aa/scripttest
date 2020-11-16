## 车辆型号 移除字段
ALTER TABLE `sys_veh_model`
DROP COLUMN `max_speed`;

## 车辆型号告警阀值 修改字段类型
ALTER TABLE `sys_veh_model_alarm`
MODIFY COLUMN `response_mode`  varchar(20) NULL DEFAULT NULL COMMENT '平台响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知' AFTER `enable`;
