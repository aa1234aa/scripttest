/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容：调整GPS是否定位状态字典项
 */

UPDATE sys_dict set name = '从未定位' where id = 'a691dac352a94a0c9a1a14fd49793a34';
UPDATE sys_dict set name = '有效定位' where id = 'ed1a893a180147b5877be7d9dc467380';
UPDATE sys_dict set name = '无效定位' where id = '67e08522cb244d20ad57bb434b6d1d50';