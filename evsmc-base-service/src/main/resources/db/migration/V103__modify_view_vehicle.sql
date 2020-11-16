
create or replace view storm_vehicle as
select
    v.uuid as vehicleIdentity,
    v.vin as vehicleNumber,
    v.veh_model_id as modelIdentity,
    m.batt_type as batteryType,
    ifnull(t.support_protocol, 'ac298dfcc7774c7eacd5b5d0d3e91d3d') as protocolIdentity
from sys_vehicle as v
    left join sys_veh_model as m on m.id = v.veh_model_id
    left join sys_term_model_unit as t on t.id = v.term_id
where v.is_delete = 0;