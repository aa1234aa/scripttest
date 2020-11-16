## 安全芯片标识ID 修改长度
ALTER TABLE `sys_encryption_chip`
MODIFY COLUMN `identification_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全芯片标识ID' AFTER `id`;