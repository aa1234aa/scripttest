
INSERT IGNORE INTO sys_dict (id, name, val, type, note, group_name, order_num, create_time, create_by, update_time, update_by)
VALUES ('387df79098be11e9b607089e01629908', '中置', '5', 'INSTALL_POSITION', '安装位置', NULL, '2', '2019-06-27 16:00:00', 'admin', NULL, NULL);

update sys_dict set order_num = 3 where id = '3628730021377474655' and val = 2;
update sys_dict set order_num = 4 where id = '3628730021377474656' and val = 3;
update sys_dict set order_num = 5 where id = '3628730021377474657' and val = 4;