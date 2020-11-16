DROP TABLE dc_forward_platform_vehicle;

ALTER TABLE `dc_forward_vehicle`
ADD COLUMN `add_mode`  int(11) NULL DEFAULT 1 COMMENT '添加方式' AFTER `error_message`;