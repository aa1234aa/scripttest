## 车辆型号 添加字段
alter table sys_veh_model add (
	engine_model varchar(128) comment '发动机型号',
	emission_level varchar(128) comment '排放标准',
	urea_tank_capacity varchar(128) comment '尿素箱容积(L)',
	vehicle_firm varchar(128) comment '整车生产企业',
	reduction_ratio varchar(128) comment '驱动桥主减速比',
	transmission_forward_num varchar(128) comment '前进档个数',
	transmission_gear_ratio varchar(128) comment '各档速比',
	use_type varchar(128) comment '用途类型',
	product_area varchar(128) comment '产地',
	max_allow_mass varchar(128) comment '最大允许装载质量',
	bearing_max_allow_mass varchar(128) comment '半挂牵引车鞍座最大允许承载质量',
	min_ground_clearance varchar(128) comment '最小离地间隙',
	veh_sliding_coefficient varchar(128) comment '车辆滑行系数(A、B、C)',
	axles_number varchar(128) comment '轴数/列车轴数',
	wheel_base varchar(128) comment '轴距(mm)',
	wheelbase_detail  varchar(128) comment '轮距(前/后)(mm)',
	body_model  varchar(128) comment '车身（或驾驶室）型式',
	tire_number  varchar(128) comment '轮胎数',
	tire_manufacturer  varchar(128) comment '轮胎生产企业',
	tire_spec  varchar(128) comment '轮胎规格',
	tire_pressure  varchar(128) comment '轮胎气压(kPa)',
	approach_departure_angle  varchar(128) comment '接近角/离去角(°)',
	transmission_name  varchar(128) comment '变速箱',
	transmission_model  varchar(128) comment '变速器型号',
	transmission_firm  varchar(128) comment '变速器生产企业',
	transmission_type  varchar(128) comment '型式/操纵方式',
	fuel_tank_capacity  varchar(128) comment '油箱容积',
	allowed_max_power  varchar(128) comment '由发动机驱动的附件允许吸收的最大功率(kW)',
	abc_speed  varchar(128) comment 'ESC试验ABC转速(r/min)',
	tms_monitor_version  varchar(128) comment '设备监控版本号',
	drive_type_location  varchar(128) comment '驱动型式及位置'
);

## 改字段命名
ALTER TABLE `sys_engine_model`
	CHANGE COLUMN `ecu_sftware_firm` `ecu_software_firm` VARCHAR(128) NULL DEFAULT NULL COMMENT 'ECU软体生产厂' AFTER `ecu_software_model`;
