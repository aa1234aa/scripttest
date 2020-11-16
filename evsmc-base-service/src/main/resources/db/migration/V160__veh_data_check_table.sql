## 数据质量检测记录
DROP TABLE IF EXISTS `veh_data_check_record`;
CREATE TABLE `veh_data_check_record` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  `vin` varchar(45) DEFAULT NULL COMMENT '车架号',
  `check_result` tinyint(4) DEFAULT NULL COMMENT '检测结果 0:未通过,  1:已通过',
  `reason` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `check_time_bg` varchar(20) DEFAULT NULL COMMENT '检测时间范围起',
  `check_time_ed` varchar(20) DEFAULT NULL COMMENT '检测时间范围止',
  `login_packet_num` int(11) DEFAULT NULL COMMENT '车辆登入报文数',
  `logout_packet_num` int(11) DEFAULT NULL COMMENT '车辆登出报文数',
  `real_status_packet_num` int(11) DEFAULT NULL COMMENT '实时上报信息报文数',
  `packet_type_check_result` tinyint(4) DEFAULT NULL COMMENT '报文类型检测结果 0:未通过，1:已通过',
  `need_upload_num` int(11) DEFAULT NULL COMMENT '需上传报文条数',
  `real_upload_num` int(11) DEFAULT NULL COMMENT '实际上传报文条数',
  `packet_loss_rate` double DEFAULT NULL COMMENT '丢包率阈值',
  `packet_real_loss_rate` double DEFAULT NULL COMMENT '实际丢包率',
  `loss_check_result` tinyint(4) DEFAULT NULL COMMENT '丢包率检测结果 0:未通过,  1:已通过',
  `data_packet_num` int(11) DEFAULT NULL COMMENT '数据项检测报文总数',
  `data_exception_num` int(11) DEFAULT NULL COMMENT '数据项检测异常报文总数',
  `data_item_exception` double DEFAULT NULL COMMENT '数据项异常阈值',
  `data_real_exception` double DEFAULT NULL COMMENT '数据项实际异常总比例',
  `data_exception_result` tinyint(4) DEFAULT NULL COMMENT '数据项异常检测总结果 0:未通过,  1:已通过',
  PRIMARY KEY (`id`),
  KEY `idx_vin` (`vin`) USING BTREE,
  KEY `idx_check_result` (`check_result`) USING BTREE,
  KEY `idx_create_by` (`create_by`) USING BTREE,
  KEY `idx_reason` (`reason`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆数据检测记录|车辆数据检测记录|dcre';

## 数据质量检测数据项结果
DROP TABLE IF EXISTS `veh_data_item_check_result`;
CREATE TABLE `veh_data_item_check_result` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `vin` varchar(45) DEFAULT NULL COMMENT '车架号',
  `check_record_id` varchar(36) DEFAULT NULL COMMENT '车辆数据检测记录id',
  `seq_no` varchar(16) DEFAULT NULL COMMENT '数据项编码',
  `data_item_name` varchar(45) DEFAULT NULL COMMENT '数据项名称',
  `item_packet_num` int(11) DEFAULT NULL COMMENT '数据项检测报文数',
  `item_exception_num` int(11) DEFAULT NULL COMMENT '数据项检测异常报文数',
  `data_item_exception` double DEFAULT NULL COMMENT '数据项异常阈值',
  `item_real_exception` double DEFAULT NULL COMMENT '数据项实际异常比例',
  `item_exception_result` tinyint(4) DEFAULT NULL COMMENT '数据项异常检测结果',
  PRIMARY KEY (`id`),
  KEY `idx_vin` (`vin`) USING BTREE,
  KEY `idx_check_record_id` (`check_record_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_seq_no` (`seq_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆数据项检测结果|车辆数据项检测结果|dicr';