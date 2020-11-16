
## 添加字典 私钥灌装状态
INSERT IGNORE INTO sys_dict_category (id, name, code, note, create_time, create_by, update_time, update_by) VALUES ('763a8aaf9e2111e9b607089e01629908', '私钥灌装状态', 'PRIVATE_KEY_FILING_STATUS', '私钥灌装状态', '2019-07-04 14:14:39', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('a6ec0f0f9e2111e9b607089e01629908', '未灌装', '1', 'PRIVATE_KEY_FILING_STATUS', '私钥灌装状态', '', '1', '2019-07-04 14:15:06', 'admin', NULL, NULL);
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by) VALUES ('c93521b29e2111e9b607089e01629908', '未灌装', '2', 'PRIVATE_KEY_FILING_STATUS', '私钥灌装状态', '', '2', '2019-07-04 14:15:06', 'admin', NULL, NULL);

## 加密芯片表默认值
ALTER TABLE `sys_encryption_chip`
MODIFY COLUMN `filing_status`  tinyint(4) NULL DEFAULT 2 COMMENT '防篡改信息备案状态:0-备案失败，1-备案成功，2-未备案' AFTER `chip_model_id`,
MODIFY COLUMN `private_key_status`  tinyint(4) NULL DEFAULT 1 COMMENT '私钥灌装状态:1-未灌装，2-已灌装' AFTER `filing_status`;

## 支持加密芯片 默认值
ALTER TABLE `sys_term_model`
MODIFY COLUMN `support_encryption_chip`  tinyint(4) NULL DEFAULT 0 COMMENT '支持加密芯片:0-否，1-是' AFTER `update_by`;