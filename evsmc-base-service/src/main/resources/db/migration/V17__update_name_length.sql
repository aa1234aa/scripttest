## 修改车型表整车控制器型号长度为128
alter table sys_veh_model modify column controller_model varchar(128);
## 修改终端型号表终端型号名称长度为128
alter table sys_term_model modify column term_model_name varchar(128);

## 修改动力蓄电池型号名称长度为100
alter table sys_battery_device_model modify column name varchar(100);
## 修改超级电容型号名称长度为100
alter table sys_super_capacitor_model modify column name varchar(100);

## 修改发动机型号名称长度为100
alter table sys_engine_model modify column name varchar(50);
## 修改驱动电机型号名称长度为100
alter table sys_drive_motor_model modify column name varchar(100);

## 修改燃油发电机型号名称长度为100
alter table sys_fuel_generator_model modify column name varchar(100);
## 修改燃料电池系统型号名称长度为100
alter table sys_fuel_system_model modify column name varchar(100);



-- ----------------------------
-- Table structure for sys_user_fault_response_mode_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_fault_response_mode_setting`;
CREATE TABLE `sys_user_fault_response_mode_setting` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户id',
  `popup` int(11) DEFAULT '0' COMMENT 'web弹窗  1=是  0=否',
  `voice` int(11) DEFAULT '0' COMMENT '声音   1=是  0=否',
  `app_popup` int(11) DEFAULT '0' COMMENT 'app提醒   1=是  0=否',
  `message` int(11) DEFAULT '0' COMMENT '手机短信   1=是  0=否',
  `email` int(11) DEFAULT '0' COMMENT '邮件   1=是  0=否',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户故障响应方式设置|用户故障响应方式设置表|fct';
