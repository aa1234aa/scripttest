-- region 清空规则
delete from fault_parameter_rule where id in(
  -- 电池电压极差过大3级报警
                                             '83f2e5702be64261865048cd703b1fa6',
  -- 电池电压极差过大2级报警
                                             '15fc293df91f408590a7a87a70389611',
  -- 电池电压极差过大1级报警
                                             '1a042a78ff4f4ae084d240134dbfb2d5',
  -- 电池过温3级报警
                                             '181496203f8241c0bdf375af18232935',
  -- 电池过温2级报警
                                             '527b07e2bfe84ee9b51219856c096652',
  -- 电池过温1级报警
                                             '95c958062418479abe5e224664dadd31',
  -- 温度差异过大3级报警
                                             '58330cdca9514876a4cf79310e6e215c',
  -- 温度差异过大2级报警
                                             '13afeda54f2b49ee9e2ab74f059cbe41',
  -- 温度差异过大1级报警
                                             '4c7ea3b25ae14eec8a2ad13994a16656',
  -- 电池快速温升3级报警
                                             'e8afff3639124b4e85372669fe556830',
  -- 电池快速温升2级报警
                                             'f36121f9261e4a1ab223e5a8d7766489',
  -- 绝缘故障3级报警
                                             'cdbb981419be4552bbdb715277ee10ce',
  -- 绝缘故障2级报警
                                             '4b91277bef1940a78bf1cc7f1881e143',
  -- 绝缘故障1级报警
                                             'cccaa5a9e0cf41608100b9cbf630e3e2'
  );
--  endregion 清空规则

-- region 电池电压极差过大3级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '83f2e5702be64261865048cd703b1fa6',
           '电池电压极差过大3级报警',
           3,
           0,
           2,
           0,
           2,
           'd2603-d2606>0.2',
           '电池单体电压最高值-电池单体电压最低值>0.2',
           'd2603,-,d2606,>,0.2',
           '电池单体电压最高值,-,电池单体电压最低值,>,0.2',
           '1,2,3,4',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池电压极差过大3级报警

-- region 电池电压极差过大2级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '15fc293df91f408590a7a87a70389611',
           '电池电压极差过大2级报警',
           2,
           0,
           3,
           0,
           3,
           'd2603-d2606>0.1&&d2603-d2606<=0.2',
           '电池单体电压最高值-电池单体电压最低值>0.1且电池单体电压最高值-电池单体电压最低值<=0.2',
           'd2603,-,d2606,>,0.1,&&,d2603,-,d2606,<=,0.2',
           '电池单体电压最高值,-,电池单体电压最低值,>,0.1,且,电池单体电压最高值,-,电池单体电压最低值,<=,0.2',
           '1,2',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池电压极差过大2级报警

-- region 电池电压极差过大1级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '1a042a78ff4f4ae084d240134dbfb2d5',
           '电池电压极差过大1级报警',
           1,
           0,
           5,
           0,
           5,
           'd2603-d2606>0.05&&d2603-d2606<=0.1',
           '电池单体电压最高值-电池单体电压最低值>0.05且电池单体电压最高值-电池单体电压最低值<=0.1',
           'd2603,-,d2606,>,0.05,&&,d2603,-,d2606,<=,0.1',
           '电池单体电压最高值,-,电池单体电压最低值,>,0.05,且,电池单体电压最高值,-,电池单体电压最低值,<=,0.1',
           '0',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池电压极差过大1级报警

-- region 电池过温3级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '181496203f8241c0bdf375af18232935',
           '电池过温3级报警',
           3,
           0,
           2,
           0,
           2,
           'd2609>70',
           '电池最高温度>70',
           'd2609,>,70',
           '电池最高温度,>,70',
           '1,2,3,4',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池过温3级报警

-- region 电池过温2级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '527b07e2bfe84ee9b51219856c096652',
           '电池过温2级报警',
           2,
           0,
           3,
           0,
           3,
           'd2609>50&&d2609<60',
           '电池最高温度>50且电池最高温度<60',
           'd2609,>,50,&&,d2609,<,60',
           '电池最高温度,>,50,且,电池最高温度,<,60',
           '1,2',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池过温2级报警

-- region 电池过温1级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '95c958062418479abe5e224664dadd31',
           '电池过温1级报警',
           1,
           0,
           5,
           0,
           5,
           'd2609>45&&d2609<50',
           '电池最高温度>45且电池最高温度<50',
           'd2609,>,45,&&,d2609,<,50',
           '电池最高温度,>,45,且,电池最高温度,<,50',
           '0',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池过温1级报警

-- region 温度差异过大3级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '58330cdca9514876a4cf79310e6e215c',
           '温度差异过大3级报警',
           3,
           0,
           2,
           0,
           2,
           'd2609-d2612>8&&d2609>45',
           '电池最高温度-电池最低温度>8且电池最高温度>45',
           'd2609,-,d2612,>,8,&&,d2609,>,45',
           '电池最高温度,-,电池最低温度,>,8,且,电池最高温度,>,45',
           '1,2,3,4',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 温度差异过大3级报警

-- region 温度差异过大2级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '13afeda54f2b49ee9e2ab74f059cbe41',
           '温度差异过大2级报警',
           2,
           0,
           3,
           0,
           3,
           'd2609-d2612>8',
           '电池最高温度-电池最低温度>8',
           'd2609,-,d2612,>,8',
           '电池最高温度,-,电池最低温度,>,8',
           '1,2',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 温度差异过大2级报警

-- region 温度差异过大1级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '4c7ea3b25ae14eec8a2ad13994a16656',
           '温度差异过大1级报警',
           1,
           0,
           5,
           0,
           5,
           'd2609-d2612>5',
           '电池最高温度-电池最低温度>5',
           'd2609,-,d2612,>,5',
           '电池最高温度,-,电池最低温度,>,5',
           '0',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 温度差异过大1级报警

-- region 电池快速温升3级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           'e8afff3639124b4e85372669fe556830',
           '电池快速温升3级报警',
           3,
           1,
           0,
           1,
           0,
           'dmax2103in1s>10',
           '1秒内最高温度变化率>10',
           'dmax2103in1s,>,10',
           '1秒内最高温度变化率,>,10',
           '1,2,3,4',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池快速温升3级报警

-- region 电池快速温升2级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           'f36121f9261e4a1ab223e5a8d7766489',
           '电池快速温升2级报警',
           2,
           30,
           0,
           30,
           0,
           'dmax2103in30s>10',
           '30秒内最高温度变化率>10',
           'dmax2103in30s,>,10',
           '30秒内最高温度变化率,>,10',
           '1,2',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 电池快速温升2级报警

-- region 绝缘故障3级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           'cdbb981419be4552bbdb715277ee10ce',
           '绝缘故障3级报警',
           3,
           0,
           2,
           0,
           2,
           'd2617/d2613<0.5',
           '绝缘电阻/总电压<0.5',
           'd2617,/,d2613,<,0.5',
           '绝缘电阻,/,总电压,<,0.5',
           '1,2,3,4',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 绝缘故障3级报警

-- region 绝缘故障2级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           '4b91277bef1940a78bf1cc7f1881e143',
           '绝缘故障2级报警',
           2,
           0,
           3,
           0,
           3,
           'd2617/d2613>=0.5&&d2617/d2613<1',
           '绝缘电阻/总电压>=0.5且绝缘电阻/总电压<1',
           'd2617,/,d2613,>=,0.5,&&,d2617,/,d2613,<,1',
           '绝缘电阻,/,总电压,>=,0.5,且,绝缘电阻,/,总电压,<,1',
           '1,2',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 绝缘故障2级报警

-- region 绝缘故障1级报警
insert into fault_parameter_rule (
  id,
  name,
  alarm_level,
  begin_threshold,
  begin_count_threshold,
  end_threshold,
  end_count_threshold,
  formula,
  formula_display,
  formula_code,
  formula_chinese,
  response_mode,
  veh_model_id,
  subordinate_parts_id,
  produce_alarm,
  enabled_status,
  dc_rule_type_id,
  delete_status,
  preset_rule,
  type
) values (
           'cccaa5a9e0cf41608100b9cbf630e3e2',
           '绝缘故障1级报警',
           1,
           0,
           5,
           0,
           5,
           'd2617/d2613>=1&&d2617/d2613<2',
           '绝缘电阻/总电压>=1且绝缘电阻/总电压<2',
           'd2617,/,d2613,>=,1,&&,d2617,/,d2613,<,2',
           '绝缘电阻,/,总电压,>=,1,且,绝缘电阻,/,总电压,<,2',
           '0',
           'all',
           '2809',
           1,
           1,
           'bfd0c8de4878410088fb573e165d4a09',
           0,
           1,
           0
         );
-- endregion 绝缘故障1级报警








