alter table fault_parameter_rule add begin_threshold bigint default 0 not null comment '开始时间阈值(秒)' after threshold;

alter table fault_parameter_rule add end_threshold bigint default 0 not null comment '结束时间阈值(秒)' after begin_threshold;

update fault_parameter_rule set begin_threshold=threshold,end_threshold=threshold where threshold is not null;

alter table fault_code_rule add begin_threshold bigint default 0 not null comment '开始时间阈值(秒)' after threshold;

alter table fault_code_rule add end_threshold bigint default 0 not null comment '结束时间阈值(秒)' after begin_threshold;

update fault_code_rule set begin_threshold=threshold,end_threshold=threshold where threshold is not null;