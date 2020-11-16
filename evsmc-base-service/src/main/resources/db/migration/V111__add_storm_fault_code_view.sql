-- 添加启用状态
ALTER TABLE `fault_code_rule`
  ADD COLUMN `enable_time_threshold`  tinyint(1) NULL COMMENT '是否启用时间阀值 0：不启用，1：启用' AFTER `begin_count_threshold`,
  ADD COLUMN `enable_count_threshold`  tinyint(1) NULL COMMENT '是否启用帧数阀值 0：不启用，1：启用' AFTER `end_count_threshold`;

ALTER TABLE `fault_parameter_rule`
  ADD COLUMN `enable_time_threshold`  tinyint(1) NULL COMMENT '是否启用时间阀值 0：不启用，1：启用' AFTER `begin_count_threshold`,
  ADD COLUMN `enable_count_threshold`  tinyint(1) NULL COMMENT '是否启用帧数阀值 0：不启用，1：启用' AFTER `end_count_threshold`;

-- 故障码规则视图
create or replace view storm_fault_code_rule as
SELECT
  r.id AS rule_id,
  r.fault_name,
  r.veh_model_id,
  r.normal_code,
  r.enable_time_threshold,
  case when r.enable_time_threshold = 1 then r.begin_threshold else 0 end AS begin_threshold,
  case when r.enable_time_threshold = 1 then r.end_threshold else 0 end AS end_threshold,
  r.enable_count_threshold,
  case when r.enable_count_threshold = 1 then r.begin_count_threshold else 0 end AS begin_count_threshold,
  case when r.enable_count_threshold = 1 then r.end_count_threshold else 0 end AS end_count_threshold,
  r.start_point,
  r.exception_code_length,
  ct.type_code AS fault_type,
  ri.id AS rule_item_id,
  ri.exception_code,
  ri.alarm_level,
  r.analyze_type
FROM
  fault_code_rule r
    INNER JOIN fault_code_type ct ON r.fault_code_type_id = ct.id
    INNER JOIN fault_code_rule_item ri ON r.id = ri.fault_code_rule_id
WHERE
    r.enabled_status = 1
  AND r.delete_status = 0
  AND ri.delete_status = 0;

-- 参数异常规则视图
create or replace view storm_fault_parameter_rule as
select
  r.id AS rule_identity,
  r.NAME as rule_name,
  r.veh_model_id AS model_identities,
  r.formula AS formula,
  r.enable_time_threshold,
  case when r.enable_time_threshold = 1 then r.begin_threshold else 0 end AS begin_threshold,
  case when r.enable_time_threshold = 1 then r.end_threshold else 0 end AS end_threshold,
  r.enable_count_threshold,
  case when r.enable_count_threshold = 1 then r.begin_count_threshold else 0 end AS begin_count_threshold,
  case when r.enable_count_threshold = 1 then r.end_count_threshold else 0 end AS end_count_threshold,
  r.preset_rule,
  r.type
from
  fault_parameter_rule AS r
where
    r.enabled_status = 1
  and r.delete_status = 0;
