-- 3、升级平台默认规则

drop table IF EXISTS  dc_forward_rule_platform;
create temporary table dc_forward_rule_platform
select uuid() as id, concat(dp.name, '-默认规则') as name, 1 as status, '升级2.4时sql生成' as note, now() as create_time, 'admin' as create_by, 1 as rule_type,  1 as default_rule, dp.id as platform_id from dc_forward_platform dp where id not in (
select dp.id from dc_forward_rule df, dc_forward_platform dp, dc_platform_rule_lk lk
where df.id = lk.forward_rule_id and dp.id = lk.platform_id and df.default_rule = 1
);


-- #创建默认规则
insert IGNORE into dc_forward_rule(id, name, status, note, create_time, create_by, rule_type, default_rule)
select dfrp.id, dfrp.name, dfrp.status, dfrp.note, dfrp.create_time, dfrp.create_by, dfrp.rule_type, dfrp.default_rule from dc_forward_rule_platform dfrp;


-- #创建规则项
insert IGNORE into dc_forward_rule_item(forward_rule_id, create_time, create_by)
select dfrp.id, now(), 'admin' from dc_forward_rule_platform dfrp;


-- #创建平台规则关系
insert IGNORE into dc_platform_rule_lk (id, forward_rule_id, platform_id, create_time, create_by)
select uuid(), dfrp.id, dfrp.platform_id, now(), 'admin' from dc_forward_rule_platform dfrp;
