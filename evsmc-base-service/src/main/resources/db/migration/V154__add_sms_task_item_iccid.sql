ALTER TABLE `sms_task_item`
ADD COLUMN `iccid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '吉利联通发送ICCID';
