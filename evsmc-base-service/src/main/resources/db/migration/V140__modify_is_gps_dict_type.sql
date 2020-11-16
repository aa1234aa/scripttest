/**
 变更人： 杨江桦
 时间： 2019-08-16
 变更内容： 把在线状态无定位的，移到gps是否定位字典项中
 */

DELETE from sys_dict where type = 'ONLINE_STATUS' and val = 4;

INSERT IGNORE INTO `sys_dict`(`id`, `name`, `val`, `type`, `note`, `group_name`, `order_num`, 
`create_time`, `create_by`, `update_time`, `update_by`) VALUES 
('441d465afcd143ca81893aea11b46484', '无定位', '8888', 'GPS_IS_LOCATE', '', '', 3, '2019-08-16 13:55:24', 'admin', NULL, NULL);