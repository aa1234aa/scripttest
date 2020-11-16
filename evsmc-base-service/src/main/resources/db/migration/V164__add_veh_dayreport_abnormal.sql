-- 车辆异常数据项报表脚本

-- 1,新增新能源车辆异常数据项报表
DROP TABLE IF EXISTS `veh_dayreport_item_abnormal`;
CREATE TABLE `veh_dayreport_item_abnormal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `vid` varchar(40) DEFAULT NULL COMMENT '车辆uuid',
  `vin` varchar(20) DEFAULT NULL COMMENT '车辆vin',
  `report_date` date NOT NULL COMMENT '报表日期',
  `veh_status_num` int(11) DEFAULT '0' COMMENT '车辆状态异常条数',
  `charge_status_num` int(11) DEFAULT '0' COMMENT '充电状态异常条数',
  `speed_num` int(11) DEFAULT '0' COMMENT '速度异常条数',
  `mileage_num` int(11) DEFAULT '0' COMMENT '累计里程异常条数',
  `total_voltage_num` int(11) DEFAULT '0' COMMENT '总电压异常条数',
  `total_current_num` int(11) DEFAULT '0' COMMENT '总电流异常条数',
  `soc_num` int(11) DEFAULT '0' COMMENT 'soc异常条数',
  `driver_motor_num` int(11) DEFAULT '0' COMMENT '驱动电机异常条数',
  `fuel_cell_num` int(11) DEFAULT '0' COMMENT '燃料电池异常条数',
  `engine_num` int(11) DEFAULT '0' COMMENT '发动机异常条数',
  `location_num` int(11) DEFAULT '0' COMMENT '车辆位置数据异常条数',
  `extreme_num` int(11) DEFAULT '0' COMMENT '极值数据异常条数',
  `alarm_num` int(11) DEFAULT '0' COMMENT '报警数据异常条数',
  `last_commit_time` datetime DEFAULT NULL COMMENT '最后通讯时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆异常数据项报表|车辆异常数据项报表|dia';

-- 2,新增燃油车辆异常数据项报表
DROP TABLE IF EXISTS `veh_dayreport_fuel_item_abnormal`;
CREATE TABLE `veh_dayreport_fuel_item_abnormal` (
  `id` int(11) NOT NULL COMMENT 'id' AUTO_INCREMENT ,
  `vid` VARCHAR(40) DEFAULT NULL COMMENT '车辆UUID',
  `vin` VARCHAR(20) DEFAULT NULL COMMENT '车辆vin',
  `report_date` date DEFAULT NULL COMMENT '报表时间',
  `tank_level_num` int(11) DEFAULT '0' COMMENT '油箱液位异常条数',
  `speed_num` int(11) DEFAULT '0' COMMENT '车速异常条数',
  `mileage_num` int(11) DEFAULT '0' COMMENT '累计里程异常条数',
  `engine_num` int(11) DEFAULT '0' COMMENT '发动机数据异常条数',
  `location_num` int(11) DEFAULT '0' COMMENT '车辆位置数据异常条数',
  `last_commit_time` datetime DEFAULT NULL COMMENT '最后通讯时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='燃油车数据项异常日报|燃油车数据项异常日报|vdfia';

-- 3,车辆异常数据项报表菜单&按钮权限
INSERT IGNORE INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('82b9039d7f5244009c58e87809b7ae56', '4144816210b449ce90bc2be3c243a3cb', '车辆异常数据项报表', 'ABNORMALDATAITEM', NULL, '0', '805e07ec8edb4d7689046c9e48ceb0ac/4144816210b449ce90bc2be3c243a3cb/82b9039d7f5244009c58e87809b7ae56/', '', '/vehAbnormalDataItem/abnormalDataItemEnergy', NULL, '10', '0', '2019-09-17 18:05:59', 'admin', '2019-09-18 15:06:49', 'admin');
INSERT IGNORE INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('190967cb455d48ff88abd8982d12ea6a', '82b9039d7f5244009c58e87809b7ae56', '新能源车离线导出', 'abnormalEnergyOfflineExport', NULL, '1', '805e07ec8edb4d7689046c9e48ceb0ac/4144816210b449ce90bc2be3c243a3cb/82b9039d7f5244009c58e87809b7ae56/190967cb455d48ff88abd8982d12ea6a/', '', '', NULL, '1', '0', '2019-09-24 15:53:12', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4b2896f919594f0b922970f8c247907f', '82b9039d7f5244009c58e87809b7ae56', '新能源车导出', 'abnormalEnergyExport', NULL, '1', '805e07ec8edb4d7689046c9e48ceb0ac/4144816210b449ce90bc2be3c243a3cb/82b9039d7f5244009c58e87809b7ae56/4b2896f919594f0b922970f8c247907f/', '', '', NULL, '1', '0', '2019-09-24 15:52:55', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('a7230886058145eaa8f937b761842e38', '82b9039d7f5244009c58e87809b7ae56', '燃油车离线导出', 'abnormalFuelOfflineExport', NULL, '1', '805e07ec8edb4d7689046c9e48ceb0ac/4144816210b449ce90bc2be3c243a3cb/82b9039d7f5244009c58e87809b7ae56/a7230886058145eaa8f937b761842e38/', '', '', NULL, '1', '0', '2019-09-24 15:52:32', 'admin', NULL, NULL);
INSERT IGNORE INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('e4b4f3a776ea4eabbca404ad7ecc5431', '82b9039d7f5244009c58e87809b7ae56', '燃油车导出', 'abnormalFuelExport', NULL, '1', '805e07ec8edb4d7689046c9e48ceb0ac/4144816210b449ce90bc2be3c243a3cb/82b9039d7f5244009c58e87809b7ae56/e4b4f3a776ea4eabbca404ad7ecc5431/', '', '', NULL, '1', '0', '2019-09-24 15:52:16', 'admin', NULL, NULL);


-- 4, 调整BOOL_TYPE的排序,让'是'排序在前面
UPDATE sys_dict SET order_num = 2 WHERE id = '3628730021377474652';

