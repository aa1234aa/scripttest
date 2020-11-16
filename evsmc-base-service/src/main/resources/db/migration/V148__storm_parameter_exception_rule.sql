create or replace view storm_parameter_exception_rule as
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
 r.group_name as groupName
FROM
 fault_parameter_rule r
WHERE r.enabled_status = 1
  AND r.delete_status = 0
