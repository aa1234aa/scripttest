## 加密芯片型号 新增字段
ALTER TABLE `sys_encryption_chip_model`
ADD COLUMN `authorization_file_id`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '厂商授权证书';
