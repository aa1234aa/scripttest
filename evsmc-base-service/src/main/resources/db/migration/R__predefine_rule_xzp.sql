-- 车辆
create or replace view storm_vehicle as
select
    v.uuid as vehicleIdentity,
    v.vin as vehicleNumber,
    v.veh_model_id as modelIdentity,
    (case m.batt_type
        when 4 then 4   -- 锰酸锂
        when 2 then 2   -- 磷酸铁锂
        when 1 then 1   -- 三元锂
        when 5 then 5   -- 钛酸锂
        else 5 end      -- 钛酸锂(默认)
    ) as batteryType,
    ifnull(t.support_protocol, 'ac298dfcc7774c7eacd5b5d0d3e91d3d') as protocolIdentity
from sys_vehicle as v
    left join sys_veh_model as m on m.id = v.veh_model_id
    left join sys_term_model_unit as t on t.id = v.term_id
where v.is_delete = 0;

-- 数据项
create or replace view storm_data as
select
    d.id as dataIdentity,
    d.seq_no as sequencerNumber,
    ifnull(d.offset, 0) as offset,
    ifnull(d.factor, 1) as coefficient,
    ifnull(d.dot, 16) as "precision"
from dc_data_item as d
where d.is_valid = 1;

-- 协议数据项关联
create or replace view storm_cross as
select
    c.rule_id as protocolIdentity,
    c.item_id as dataIdentity
from dc_rule_item_lk as c;

-- 参数异常规则
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
