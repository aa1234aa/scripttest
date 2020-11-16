# 故障码表新增字段
alter table fault_code_rule add begin_count_threshold int default 1 not null comment '开始计数阈值(次)' after begin_threshold;
alter table fault_code_rule add end_count_threshold int default 1 not null comment '结束计数阈值(次)' after end_threshold;
update fault_code_rule set begin_count_threshold=begin_threshold div 10, end_count_threshold=end_threshold div 10;