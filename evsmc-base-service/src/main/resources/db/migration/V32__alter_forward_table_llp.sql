ALTER TABLE `dc_forward_platform_vehicle`
ADD COLUMN `add_mode`  int NULL DEFAULT 1 COMMENT '添加方式' AFTER `vehicle_id`;