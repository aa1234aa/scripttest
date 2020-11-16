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

select * from storm_data;

-- 协议数据项关联
create or replace view storm_cross as
select
    c.rule_id as protocolIdentity,
    c.item_id as dataIdentity
from dc_rule_item_lk as c;

select * from storm_cross;

-- 参数异常规则
select
    r.id as ruleIdentity,
    r.name as name,
    r.veh_model_id as modelIdentities,
    r.formula as formula,
    r.begin_threshold as beginTimeThresholdSecond,
    r.end_threshold as endTimeThresholdSecond
from fault_parameter_rule as r
where r.enabled_status = 1
    and r.delete_status = 0;