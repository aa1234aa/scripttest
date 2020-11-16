ALTER TABLE `sys_group`
ADD COLUMN `user_id`  varchar(36) NULL COMMENT '用户ID' AFTER `resource_type_id`,
ADD UNIQUE INDEX `userId` (`user_id`) ;

insert into sys_group(id, name, is_valid, description, rule_type, resource_type_id, user_id, create_by, create_time) select uuid(),concat(username,'@group'), 1, '用户默认权限组', 2, '1111', id, 'admin',now() from sys_user;
