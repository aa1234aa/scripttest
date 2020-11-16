## 数据质量检测规则
DROP TABLE IF EXISTS veh_data_check_rule;
CREATE TABLE `veh_data_check_rule` (
`id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '规则名称' ,
`veh_model_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车辆型号' ,
`protocol_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯协议' ,
`packet_loss_rate`  double NULL COMMENT '丢包率阈值' ,
`data_item_exception`  double NULL COMMENT '数据项异常阈值' ,
`add_item_ids`  text NULL COMMENT '数据项Ids' ,
`create_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
`update_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间' ,
`update_by`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `UNIQUE_name` (`name`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据质量检测规则|数据质量检测规则|dcr';

## 数据质量检测规则数据项
DROP TABLE IF EXISTS veh_data_check_rule_item;
CREATE TABLE `veh_data_check_rule_item` (
`id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`check_rule_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测规则ID' ,
`data_item_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据项ID' ,
`create_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间' ,
`create_by`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据质量检测规则数据项|数据质量检测规则数据项|dcri';


## 车辆数据质量检测 菜单权限
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('29fe937845534310b2ae1e093b180d1f', '4', '车辆数据质量检测', 'vehDataQualityCheck', NULL, '0', '4/29fe937845534310b2ae1e093b180d1f/', '', '', NULL, '7', '0', '2019-09-18 14:49:17', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('a78376ec6d144778bc7afe3025084591', '29fe937845534310b2ae1e093b180d1f', '数据质量检测规则设置', 'dataCheckRule', NULL, '0', '4/29fe937845534310b2ae1e093b180d1f/a78376ec6d144778bc7afe3025084591/', '', '/dataCheckRule', NULL, '4', '0', '2019-09-18 14:52:10', 'admin', NULL, NULL);

## 数据质量检测规则 按钮权限
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('938b39d1e30842b5a90c07b30669c123', 'a78376ec6d144778bc7afe3025084591', '检测规则编辑', 'dataCheckRuleEdit', NULL, '1', '4/29fe937845534310b2ae1e093b180d1f/a78376ec6d144778bc7afe3025084591/938b39d1e30842b5a90c07b30669c123/', '', '', NULL, '1', '0', '2019-09-23 09:32:50', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('0fb0e9ce773f49b79005436aad35656c', 'a78376ec6d144778bc7afe3025084591', '检测规则删除', 'dataCheckRuleRemove', NULL, '1', '4/29fe937845534310b2ae1e093b180d1f/a78376ec6d144778bc7afe3025084591/0fb0e9ce773f49b79005436aad35656c/', '', '', NULL, '1', '0', '2019-09-23 09:32:11', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('ed83a2d02258416386916109d5e66b41', 'a78376ec6d144778bc7afe3025084591', '检测规则批量删除', 'dataCheckRuleBatchDelete', NULL, '1', '4/29fe937845534310b2ae1e093b180d1f/a78376ec6d144778bc7afe3025084591/ed83a2d02258416386916109d5e66b41/', '', '', NULL, '1', '0', '2019-09-23 09:30:49', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_module (id, parent_id, name, code, is_root, is_fun, path, icon, action, is_fullscreen, order_num, is_hidden, create_time, create_by, update_time, update_by) VALUES ('32a9a89840024d06baa9784adc16ef83', 'a78376ec6d144778bc7afe3025084591', '检测规则新增', 'dataCheckRuleAdd', NULL, '1', '4/29fe937845534310b2ae1e093b180d1f/a78376ec6d144778bc7afe3025084591/32a9a89840024d06baa9784adc16ef83/', '', '', NULL, '1', '0', '2019-09-23 09:26:07', 'admin', NULL, NULL);
