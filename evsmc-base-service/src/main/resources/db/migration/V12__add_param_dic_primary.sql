
/**
增加终端参数表的主键
 */
ALTER TABLE `sys_term_param_dic`
MODIFY COLUMN `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键标识' FIRST,
ADD PRIMARY KEY (`id`);