-- 新增车辆数据质量日报
DROP TABLE IF EXISTS `veh_dayreport_data_quality`;
CREATE TABLE `veh_dayreport_data_quality` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vid` varchar(40) DEFAULT NULL,
  `vin` varchar(20) DEFAULT NULL,
  `report_date` date DEFAULT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '车辆当日首次上线时间',
  `end_time` datetime DEFAULT NULL COMMENT '车辆当日最后通讯时间',
  `should_upload_num` int(11) DEFAULT NULL COMMENT '应该上传的报文',
  `actual_upload_num` int(11) DEFAULT NULL COMMENT '实际上传的报文总条数',
  `abnormal_num` int(11) DEFAULT NULL COMMENT '异常报文条数',
  `anbormal_rate` double DEFAULT NULL COMMENT '异常报文比例',
  `miss_rate` double DEFAULT NULL COMMENT '实时数据丢包率',
  `exist_forward` double DEFAULT NULL COMMENT '是否存在转发报文,0不存在，1存在',
  `forward_num` int(11) DEFAULT NULL COMMENT '转发报文总条数',
  `miss_forward_rate` double DEFAULT NULL COMMENT '数据转发丢包率',
  `frequency` int(11) DEFAULT NULL COMMENT '数据上传的频率',
  PRIMARY KEY (`id`),
  KEY `veh_dayreport_data_quality_vid_report_date_index` (`vid`,`report_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9070 DEFAULT CHARSET=utf8mb4 COMMENT='数据质量日报|数据质量日报|vddq';
