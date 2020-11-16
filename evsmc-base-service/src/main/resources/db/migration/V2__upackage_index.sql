/**
 变更人： 杨江桦
 时间： 2019-06-04
 变更内容： 增加文件唯一索引
 */
ALTER TABLE `sys_uppackage_info`
ADD UNIQUE INDEX `uq_file_name`(`file_name`) USING BTREE COMMENT '文件名唯一索引',
ADD UNIQUE INDEX `uq_nickname`(`nickname`) USING BTREE COMMENT '文件别名唯一索引';