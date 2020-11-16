## 车辆运营使用信息 新增字段
ALTER TABLE `sys_vehicle`
DROP COLUMN `annual_inspection_status`;

ALTER TABLE `sys_vehicle`
ADD COLUMN `annual_inspection_status`  tinyint(4) NULL COMMENT '年检状态:1-未年检；2-已年检；3-年检过期' AFTER `maintenance_period`;
