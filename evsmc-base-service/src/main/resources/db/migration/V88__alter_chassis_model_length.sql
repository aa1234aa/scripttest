ALTER TABLE `sys_veh_model`
MODIFY COLUMN `chassis_model`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '底盘型号' AFTER `controller_unit_id`;