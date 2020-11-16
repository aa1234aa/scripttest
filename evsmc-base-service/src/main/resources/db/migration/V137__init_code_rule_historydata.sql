-- 更新旧数据，如果时间阀值&帧阀值都为null,则启用帧数阀值
update fault_parameter_rule set enable_time_threshold = 1, enable_count_threshold = 0 where enable_time_threshold is null and enable_count_threshold is null;
update fault_code_rule set enable_time_threshold = 1, enable_count_threshold = 0 where enable_time_threshold is null and enable_count_threshold is null;
