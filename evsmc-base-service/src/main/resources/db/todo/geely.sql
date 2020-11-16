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
    ifnull(g.dc_rule_id, ifnull(t.support_protocol, 'ac298dfcc7774c7eacd5b5d0d3e91d3d')) as protocolIdentity
from sys_vehicle as v
         left join sys_veh_model as m on m.id = v.veh_model_id
         left join sys_term_model_unit as t on t.id = v.term_id
         left join geely_term_part_number_item as i on i.part_number = t.term_part_firmware_number
         left join geely_term_part_number as g on g.id = i.part_number_id
where v.is_delete = 0;