/**
 变更人： 杨江桦
 时间： 2019-06-27
 变更内容： 增加升级明细索引
 */
ALTER TABLE `sys_uppackage_send_details`
ADD INDEX `idx_vin_uppack_send_status`(`vin`, `uppackage_send_status`) USING BTREE;