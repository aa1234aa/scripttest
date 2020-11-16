-- 2019-08-16
## 新建短信模板维护数据表 sys_msg_template
DROP TABLE IF EXISTS `sys_msg_template`;
CREATE TABLE `sys_msg_template` (
`id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`template_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板名称' ,
`inter_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内部编码' ,
`template_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信模板' ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '短信内容' ,
`status`  tinyint(4) NULL DEFAULT 1 COMMENT '启用状态: 0：禁用 1：启用' ,
`msg_param`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '短信参数' ,
`create_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
`update_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间' ,
`update_by`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信模板|短信模板|mt';
ALTER TABLE `sys_msg_template`
ADD UNIQUE INDEX `unique_name` (`template_name`) ,
ADD UNIQUE INDEX `unique_code` (`inter_code`) ;

## 短信模板 菜单权限
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('49ee4263099c4c9997549f7e6644327f', '44fc1551a174459b8b7439fb629b2345', '短信模板', 'msgTemplate', NULL, '0', '44fc1551a174459b8b7439fb629b2345/49ee4263099c4c9997549f7e6644327f/', '', '/msgTemplate', NULL, '6', '0', '2019-08-12 16:43:55', 'admin', NULL, NULL);

## 短信模板 按钮权限
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('3629b9953d95400c9cad1d0656b9d45d', '49ee4263099c4c9997549f7e6644327f', '短信模板删除', 'msgTemplateRemove', NULL, '1', '44fc1551a174459b8b7439fb629b2345/49ee4263099c4c9997549f7e6644327f/3629b9953d95400c9cad1d0656b9d45d/', '', '', NULL, '1', '0', '2019-08-15 17:31:22', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('b439fbbcb6084138b83e1d14ce15957a', '49ee4263099c4c9997549f7e6644327f', '短信模板编辑', 'msgTemplateEdit', NULL, '1', '44fc1551a174459b8b7439fb629b2345/49ee4263099c4c9997549f7e6644327f/b439fbbcb6084138b83e1d14ce15957a/', '', '', NULL, '1', '0', '2019-08-15 17:31:06', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('67eb75adb5fb4464aee2853441e1a8ee', '49ee4263099c4c9997549f7e6644327f', '短信模板批量删除', 'msgTemplateBatchDelete', NULL, '1', '44fc1551a174459b8b7439fb629b2345/49ee4263099c4c9997549f7e6644327f/67eb75adb5fb4464aee2853441e1a8ee/', '', '', NULL, '1', '0', '2019-08-15 17:30:48', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('5a4bfbfd3409498b85dbe35c542fc1db', '49ee4263099c4c9997549f7e6644327f', '短信模板新增', 'msgTemplateAdd', NULL, '1', '44fc1551a174459b8b7439fb629b2345/49ee4263099c4c9997549f7e6644327f/5a4bfbfd3409498b85dbe35c542fc1db/', '', '', NULL, '1', '0', '2019-08-15 17:30:27', 'admin', NULL, NULL);

## 添加短信模板
INSERT IGNORE INTO sys_msg_template (id, template_name, inter_code, template_id, content, status, msg_param, create_time, create_by, update_time, update_by) VALUES ('6d130e5a94b34eefa3b2e97d1ccae195', '故障报警', '1000', '107968', '【北京现代】尊敬的客户您好！车牌号为${P1}的车辆发生了${P2}。为保证行车安全，请您确认车辆状态或向附近的4S店求助。感谢您选择北京现代。', '1', 'P1 = 车牌号;2 = 故障名称', '2019-08-16 13:57:50', 'admin', '2019-08-16 13:58:55', 'admin');
