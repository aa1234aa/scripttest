
# 处理用户和默认权限组关系
insert IGNORE into sys_user_group_lk(id, user_id, group_id) select uuid(),g.user_id, g.id from sys_group g left join sys_user_group_lk lk on lk.group_id=g.id where g.user_id is not null  and lk.id is null;
# 插入默认用户组的规则
insert into sys_group_resource_rule(id, resource_item_id, group_id, op, val, pre_logic_op, order_num, create_by, create_time) select uuid(),'2ac0576787ee4742adb61cb0e6e0e98d', g.id, 0,'',1,0, 'admin',now()  from sys_group g left join sys_group_resource_rule rr on rr.group_id = g.id where   g.user_id is not null and rr.id is null;