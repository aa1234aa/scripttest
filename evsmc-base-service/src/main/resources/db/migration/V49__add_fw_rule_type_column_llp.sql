
##是否支持加密及通讯协议
ALTER TABLE `dc_rule_type`
ADD COLUMN `support_encrypt`  int NULL DEFAULT 1 COMMENT '1、支持加密, 2、不支持。' AFTER `port`,
ADD COLUMN `protocol`  int NULL DEFAULT 1 COMMENT '通讯方式' AFTER `support_encrypt`;




##是否为默认规则
ALTER TABLE `dc_forward_rule`
ADD COLUMN `default_rule`  int NULL DEFAULT 0 COMMENT '0 不是, 1 是';



# 参数异常规则
ALTER TABLE `fault_parameter_rule`
MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称' AFTER `id`;

# 故障码规则
ALTER TABLE `fault_code_rule`
MODIFY COLUMN `fault_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '故障码规则名称' AFTER `fault_code_type_id`;
