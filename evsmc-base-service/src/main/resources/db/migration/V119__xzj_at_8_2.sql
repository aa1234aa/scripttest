-- 添加默认值
ALTER TABLE `fault_code_rule`
MODIFY COLUMN `enable_time_threshold`  tinyint(1) NULL DEFAULT 1 COMMENT '是否启用时间阀值 0：不启用，1：启用' AFTER `begin_count_threshold`,
MODIFY COLUMN `enable_count_threshold`  tinyint(1) NULL DEFAULT 1 COMMENT '是否启用帧数阀值 0：不启用，1：启用' AFTER `end_count_threshold`;

ALTER TABLE `fault_parameter_rule`
MODIFY COLUMN `enable_time_threshold`  tinyint(1) NULL DEFAULT 1 COMMENT '是否启用时间阀值 0：不启用，1：启用' AFTER `begin_count_threshold`,
MODIFY COLUMN `enable_count_threshold`  tinyint(1) NULL DEFAULT 1 COMMENT '是否启用帧数阀值 0：不启用，1：启用' AFTER `end_count_threshold`;

-- 删除view
drop view if EXISTS storm_parameter_exception_rule;
drop view if EXISTS storm_fault_parameter_rule;

-- 创建参数异常view
create view storm_parameter_exception_rule as
SELECT
	r.id AS ruleIdentity,
	r.name AS name,
	r.veh_model_id AS modelIdentities,
	r.formula AS formula,
	r.alarm_level AS alarmLevel,
	r.enable_time_threshold as enableTimeThreshold,
	case when r.enable_time_threshold = 1 then r.begin_threshold else 0 end AS beginTimeThresholdSecond,
	case when r.enable_time_threshold = 1 then r.end_threshold else 0 end AS endTimeThresholdSecond,
	r.enable_count_threshold as enableCountThreshold,
	case when r.enable_count_threshold = 1 then r.begin_count_threshold else 0 end AS beginCountThreshold,
	case when r.enable_count_threshold = 1 then r.end_count_threshold else 0 end AS endCountThreshold,
	r.preset_rule AS predefined,
	r.type AS type,
	r.group_name
FROM
	fault_parameter_rule r
WHERE r.enabled_status = 1
  AND r.delete_status = 0