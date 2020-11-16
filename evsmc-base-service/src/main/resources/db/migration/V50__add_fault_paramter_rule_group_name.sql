# 参数异常规则
ALTER TABLE `fault_parameter_rule`
ADD COLUMN `group_name`  varchar(50) NULL DEFAULT NULL COMMENT '用于车辆型号通用规则分组' AFTER `type`;