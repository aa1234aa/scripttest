/**
 变更人： 杨江桦
 时间： 2019-08-27
 变更内容： 增加离线导出的系统识别字段
 */

DROP PROCEDURE IF EXISTS offline_export_addcolumn;

DELIMITER //
CREATE PROCEDURE offline_export_addcolumn() BEGIN
	IF (SELECT NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE()
		AND table_name = 'sys_offline_export' AND COLUMN_NAME = 'system_name'))
		THEN ALTER TABLE `sys_offline_export` ADD COLUMN `system_name` varchar(100) NULL
		DEFAULT 'evsmc-base-service' COMMENT '执行任务系统名称' AFTER `update_by`;
	END IF;
END //
DELIMITER;
CALL offline_export_addcolumn();