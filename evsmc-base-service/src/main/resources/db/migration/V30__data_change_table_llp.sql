CREATE TABLE IF NOT EXISTS `dc_forward_platform_vehicle` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `platform_id` VARCHAR(36) NULL COMMENT '平台ID',
  `vehicle_id` VARCHAR(36) NULL COMMENT '车辆id',
  `create_time` VARCHAR(20) NULL COMMENT '创建时间',
  `create_by` VARCHAR(36) NULL COMMENT '创建人',
  `update_by` VARCHAR(36) NULL COMMENT '修改人',
  `update_time` VARCHAR(20) NULL COMMENT '修改时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '转发平台车辆|转发平台车辆配置|dfpv';


CREATE TABLE IF NOT EXISTS `dc_forward_platform_vehicle_black_list` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `platform_id` VARCHAR(36) NULL COMMENT '平台id',
  `vehicle_id` VARCHAR(36) NULL COMMENT '车辆id',
  `create_time` VARCHAR(20) NULL COMMENT '创建时间',
  `create_by` VARCHAR(36) NULL COMMENT '创建人',
  `update_by` VARCHAR(36) NULL COMMENT '修改人',
  `update_time` VARCHAR(20) NULL COMMENT '修改时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '转发平台车辆黑名单|转发平台车辆黑名单配置|dfpvbl';
