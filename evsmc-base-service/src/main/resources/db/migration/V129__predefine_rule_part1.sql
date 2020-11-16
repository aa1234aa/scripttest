
-- region 清空规则
delete from fault_parameter_rule where (
    -- 单体过压1级报警-钛酸锂
    id='16e2bd8c4adc49feabc016d1eb660ced'
    -- 单体过压2级报警-钛酸锂
    or id='f5ec016fb674479091450bf1207eca77'
    -- 单体过压3级报警-钛酸锂
    or id='243de1ac89424e2383273489d0a34b91'
    -- 单体过压1级报警-锰酸锂
    or id='b3228c3ab38411e9b607089e01629908'
    -- 单体过压2级报警-锰酸锂
    or id='e415a5a7b38411e9b607089e01629908'
    -- 单体过压3级报警-锰酸锂
    or id='e8641e53b38411e9b607089e01629908'
    -- 单体过压1级报警-磷酸铁锂
    or id='ec606500b38411e9b607089e01629908'
    -- 单体过压2级报警-磷酸铁锂
    or id='f19d9b48b38411e9b607089e01629908'
    -- 单体过压3级报警-磷酸铁锂
    or id='f577d726b38411e9b607089e01629908'
    -- 单体过压1级报警-三元锂
    or id='f9aba2e2b38411e9b607089e01629908'
    -- 单体过压2级报警-三元锂
    or id='fcef28d7b38411e9b607089e01629908'
    -- 单体过压3级报警-三元锂
    or id='0115ca71b38511e9b607089e01629908'
    -- 单体欠压1级报警-钛酸锂
    or id='0496ec76b38511e9b607089e01629908'
    -- 单体欠压2级报警-钛酸锂
    or id='08320255b38511e9b607089e01629908'
    -- 单体欠压3级报警-钛酸锂
    or id='0bede5ecb38511e9b607089e01629908'
    -- 单体欠压1级报警-锰酸锂
    or id='0fa5ad9eb38511e9b607089e01629908'
    -- 单体欠压2级报警-锰酸锂
    or id='137f2691b38511e9b607089e01629908'
    -- 单体欠压3级报警-锰酸锂
    or id='17bdd53db38511e9b607089e01629908'
    -- 单体欠压1级报警-磷酸铁锂
    or id='1b7a1460b38511e9b607089e01629908'
    -- 单体欠压2级报警-磷酸铁锂
    or id='20be7664b38511e9b607089e01629908'
    -- 单体欠压3级报警-磷酸铁锂
    or id='2693bc4eb38511e9b607089e01629908'
    -- 单体欠压1级报警-三元锂
    or id='2adc0c5cb38511e9b607089e01629908'
    -- 单体欠压2级报警-三元锂
    or id='2e2f68beb38511e9b607089e01629908'
    -- 单体欠压3级报警-三元锂
    or id='3225b3c9b38511e9b607089e01629908'
);
--  endregion 清空规则

-- -- -- --

-- region 单体过压1级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '16e2bd8c4adc49feabc016d1eb660ced',
    '单体过压1级报警-钛酸锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==5&&d2603>2.8&&d2603<3.0',
    '电池类型=钛酸锂且电池单体电压最高值(V)>2.8且电池单体电压最高值(V)<3.0',
    'dBTYPE,==,5,&&,d2603,>,2.8,&&,d2603,<,3.0',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),>,2.8,且,电池单体电压最高值(V),<,3.0',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压1级报警-钛酸锂

-- region 单体过压2级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'f5ec016fb674479091450bf1207eca77',
    '单体过压2级报警-钛酸锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==5&&d2603>=3.0&&d2603<=3.2',
    '电池类型=钛酸锂且电池单体电压最高值(V)>=3.0且电池单体电压最高值(V)<=3.2',
    'dBTYPE,==,5,&&,d2603,>=,3.0,&&,d2603,<=,3.2',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),>=,3.0,且,电池单体电压最高值(V),<=,3.2',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压2级报警-钛酸锂

-- region 单体过压3级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '243de1ac89424e2383273489d0a34b91',
    '单体过压3级报警-钛酸锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==5&&d2603>3.2',
    '电池类型=钛酸锂且电池单体电压最高值(V)>3.2',
    'dBTYPE,==,5,&&,d2603,>,3.2',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),>,3.2',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压3级报警-钛酸锂

-- --

-- region 单体过压1级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'b3228c3ab38411e9b607089e01629908',
    '单体过压1级报警-锰酸锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==4&&d2603>4.25&&d2603<4.35',
    '电池类型=锰酸锂且电池单体电压最高值(V)>4.25且电池单体电压最高值(V)<4.35',
    'dBTYPE,==,4,&&,d2603,>,4.25,&&,d2603,<,4.35',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),>,4.25,且,电池单体电压最高值(V),<,4.35',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压1级报警-锰酸锂

-- region 单体过压2级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'e415a5a7b38411e9b607089e01629908',
    '单体过压2级报警-锰酸锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==4&&d2603>=4.35&&d2603<=4.45',
    '电池类型=锰酸锂且电池单体电压最高值(V)>=4.35且电池单体电压最高值(V)<=4.45',
    'dBTYPE,==,4,&&,d2603,>=,4.35,&&,d2603,<=,4.45',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),>=,4.35,且,电池单体电压最高值(V),<=,4.45',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压2级报警-锰酸锂

-- region 单体过压3级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'e8641e53b38411e9b607089e01629908',
    '单体过压3级报警-锰酸锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==4&&d2603>4.45',
    '电池类型=锰酸锂且电池单体电压最高值(V)>4.45',
    'dBTYPE,==,4,&&,d2603,>,4.45',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),>,4.45',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压3级报警-锰酸锂

-- --

-- region 单体过压1级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'ec606500b38411e9b607089e01629908',
    '单体过压1级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==2&&d2603>3.65&&d2603<3.75',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)>3.65且电池单体电压最高值(V)<3.75',
    'dBTYPE,==,2,&&,d2603,>,3.65,&&,d2603,<,3.75',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),>,3.65,且,电池单体电压最高值(V),<,3.75',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压1级报警-磷酸铁锂

-- region 单体过压2级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'f19d9b48b38411e9b607089e01629908',
    '单体过压2级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==2&&d2603>=3.75&&d2603<=3.85',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)>=3.75且电池单体电压最高值(V)<=3.85',
    'dBTYPE,==,2,&&,d2603,>=,3.75,&&,d2603,<=,3.85',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),>=,3.75,且,电池单体电压最高值(V),<=,3.85',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压2级报警-磷酸铁锂

-- region 单体过压3级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'f577d726b38411e9b607089e01629908',
    '单体过压3级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==2&&d2603>3.85',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)>3.85',
    'dBTYPE,==,2,&&,d2603,>,3.85',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),>,3.85',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压3级报警-磷酸铁锂

-- --

-- region 单体过压1级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'f9aba2e2b38411e9b607089e01629908',
    '单体过压1级报警-三元锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==1&&d2603>4.25&&d2603<4.4',
    '电池类型=三元锂且电池单体电压最高值(V)>4.25且电池单体电压最高值(V)<4.4',
    'dBTYPE,==,1,&&,d2603,>,4.25,&&,d2603,<,4.4',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),>,4.25,且,电池单体电压最高值(V),<,4.4',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压1级报警-三元锂

-- region 单体过压2级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    'fcef28d7b38411e9b607089e01629908',
    '单体过压2级报警-三元锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==1&&d2603>=4.4&&d2603<=4.7',
    '电池类型=三元锂且电池单体电压最高值(V)>=4.4且电池单体电压最高值(V)<=4.7',
    'dBTYPE,==,1,&&,d2603,>=,4.4,&&,d2603,<=,4.7',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),>=,4.4,且,电池单体电压最高值(V),<=,4.7',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压2级报警-三元锂

-- region 单体过压3级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '0115ca71b38511e9b607089e01629908',
    '单体过压3级报警-三元锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==1&&d2603>4.7',
    '电池类型=三元锂且电池单体电压最高值(V)>4.7',
    'dBTYPE,==,1,&&,d2603,>,4.7',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),>,4.7',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体过压3级报警-三元锂

-- -- -- --

-- region 单体欠压1级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '0496ec76b38511e9b607089e01629908',
    '单体欠压1级报警-钛酸锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==5&&d2606>1.3&&d2606<1.6',
    '电池类型=钛酸锂且电池单体电压最高值(V)>1.3且电池单体电压最高值(V)<1.6',
    'dBTYPE,==,5,&&,d2606,>,1.3,&&,d2606,<,1.6',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),>,1.3,且,电池单体电压最高值(V),<,1.6',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压1级报警-钛酸锂

-- region 单体欠压2级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '08320255b38511e9b607089e01629908',
    '单体欠压2级报警-钛酸锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==5&&d2606>=1&&d2606<=1.3',
    '电池类型=钛酸锂且电池单体电压最高值(V)>=1且电池单体电压最高值(V)<=1.3',
    'dBTYPE,==,5,&&,d2606,>=,1,&&,d2606,<=,1.3',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),>=,1,且,电池单体电压最高值(V),<=,1.3',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压2级报警-钛酸锂

-- region 单体欠压3级报警-钛酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '0bede5ecb38511e9b607089e01629908',
    '单体欠压3级报警-钛酸锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==5&&d2606<1',
    '电池类型=钛酸锂且电池单体电压最高值(V)<1',
    'dBTYPE,==,5,&&,d2606,<,1',
    '电池类型,=,钛酸锂,且,电池单体电压最高值(V),<,1',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压3级报警-钛酸锂

-- --

-- region 单体欠压1级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '0fa5ad9eb38511e9b607089e01629908',
    '单体欠压1级报警-锰酸锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==4&&d2606>2.25&&d2606<2.5',
    '电池类型=锰酸锂且电池单体电压最高值(V)>2.25且电池单体电压最高值(V)<2.5',
    'dBTYPE,==,4,&&,d2606,>,2.25,&&,d2606,<,2.5',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),>,2.25,且,电池单体电压最高值(V),<,2.5',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压1级报警-锰酸锂

-- region 单体欠压2级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '137f2691b38511e9b607089e01629908',
    '单体欠压2级报警-锰酸锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==4&&d2606>=2&&d2606<=2.25',
    '电池类型=锰酸锂且电池单体电压最高值(V)>=2且电池单体电压最高值(V)<=2.25',
    'dBTYPE,==,4,&&,d2606,>=,2,&&,d2606,<=,2.25',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),>=,2,且,电池单体电压最高值(V),<=,2.25',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压2级报警-锰酸锂

-- region 单体欠压3级报警-锰酸锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '17bdd53db38511e9b607089e01629908',
    '单体欠压3级报警-锰酸锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==4&&d2606<=2',
    '电池类型=锰酸锂且电池单体电压最高值(V)<=2',
    'dBTYPE,==,4,&&,d2606,<=,2',
    '电池类型,=,锰酸锂,且,电池单体电压最高值(V),<=,2',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压3级报警-锰酸锂

-- --

-- region 单体欠压1级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '1b7a1460b38511e9b607089e01629908',
    '单体欠压1级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==2&&d2606>1.8&&d2606<2',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)>1.8且电池单体电压最高值(V)<2',
    'dBTYPE,==,2,&&,d2606,>,1.8,&&,d2606,<,2',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),>,1.8,且,电池单体电压最高值(V),<,2',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压1级报警-磷酸铁锂

-- region 单体欠压2级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '20be7664b38511e9b607089e01629908',
    '单体欠压2级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==2&&d2606>=1.6&&d2606<=1.8',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)>=1.6且电池单体电压最高值(V)<=1.8',
    'dBTYPE,==,2,&&,d2606,>=,1.6,&&,d2606,<=,1.8',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),>=,1.6,且,电池单体电压最高值(V),<=,1.8',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压2级报警-磷酸铁锂

-- region 单体欠压3级报警-磷酸铁锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '2693bc4eb38511e9b607089e01629908',
    '单体欠压3级报警-磷酸铁锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==2&&d2606<1.6',
    '电池类型=磷酸铁锂且电池单体电压最高值(V)<1.6',
    'dBTYPE,==,2,&&,d2606,<,1.6',
    '电池类型,=,磷酸铁锂,且,电池单体电压最高值(V),<,1.6',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压3级报警-磷酸铁锂

-- --

-- region 单体欠压1级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '2adc0c5cb38511e9b607089e01629908',
    '单体欠压1级报警-三元锂',
    'all',
    '2809',
    1,
    1,
    '0',
    0,
    5,
    0,
    5,
    1,
    'dBTYPE==1&&d2606>2.5&&d2606<3',
    '电池类型=三元锂且电池单体电压最高值(V)>2.5且电池单体电压最高值(V)<3',
    'dBTYPE,==,1,&&,d2606,>,2.5,&&,d2606,<,3',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),>,2.5,且,电池单体电压最高值(V),<,3',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压1级报警-三元锂

-- region 单体欠压2级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '2e2f68beb38511e9b607089e01629908',
    '单体欠压2级报警-三元锂',
    'all',
    '2809',
    1,
    2,
    '1,2',
    0,
    3,
    0,
    5,
    1,
    'dBTYPE==1&&d2606>=2&&d2606<=2.5',
    '电池类型=三元锂且电池单体电压最高值(V)>=2且电池单体电压最高值(V)<=2.5',
    'dBTYPE,==,1,&&,d2606,>=,2,&&,d2606,<=,2.5',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),>=,2,且,电池单体电压最高值(V),<=,2.5',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压2级报警-三元锂

-- region 单体欠压3级报警-三元锂
insert into fault_parameter_rule (
    id,
    name,
    veh_model_id,
    subordinate_parts_id,
    produce_alarm,
    alarm_level,
    response_mode,
    begin_threshold,
    begin_count_threshold,
    end_threshold,
    end_count_threshold,
    enabled_status,
    formula,
    formula_display,
    formula_code,
    formula_chinese,
    dc_rule_type_id,
    delete_status,
    preset_rule,
    type
 ) values (
    '3225b3c9b38511e9b607089e01629908',
    '单体欠压3级报警-三元锂',
    'all',
    '2809',
    1,
    3,
    '1,2,3,4',
    0,
    2,
    0,
    5,
    1,
    'dBTYPE==1&&d2606<2',
    '电池类型=三元锂且电池单体电压最高值(V)<2',
    'dBTYPE,==,1,&&,d2606,<,2',
    '电池类型,=,三元锂,且,电池单体电压最高值(V),<,2',
    'bfd0c8de4878410088fb573e165d4a09',
    0,
    1,
    0
);
-- endregion 单体欠压3级报警-三元锂
