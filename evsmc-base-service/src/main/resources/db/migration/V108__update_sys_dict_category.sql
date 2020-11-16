UPDATE `sys_dict_category` SET `code` = 'FAULT_REPORT_STATUS',`create_time` = '2019-07-10 18:48:07' WHERE `name` = '事故上报状态';
UPDATE `sys_dict` SET `type` = 'FAULT_REPORT_STATUS', `create_time` = '2019-07-10 18:48:07' WHERE `note` = '事故上报状态';
DELETE FROM sys_dict WHERE `type` = 'FAULT_REPORT_STATUS' and `val` = '2';
ALTER TABLE fault_veh_risk_notice DROP COLUMN annotations,DROP COLUMN opinion;