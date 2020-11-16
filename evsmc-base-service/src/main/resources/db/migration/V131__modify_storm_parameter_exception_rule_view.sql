create or replace view storm_parameter_exception_rule as
select
    r.id as ruleIdentity,
    r.name as name,
    r.veh_model_id as modelIdentities,
    r.formula as formula,
    r.alarm_level as alarmLevel,
    r.begin_threshold as beginTimeThresholdSecond,
    r.begin_count_threshold as beginCountThreshold,
    r.end_threshold as endTimeThresholdSecond,
    r.end_count_threshold as endCountThreshold,
    r.preset_rule as predefined,
    r.group_name as groupName
from fault_parameter_rule as r
where r.enabled_status = 1
    and r.delete_status = 0;
