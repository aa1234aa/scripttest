ALTER TABLE `sms_task`
MODIFY COLUMN `remarks`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
MODIFY COLUMN `biz_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信发送流水号';
