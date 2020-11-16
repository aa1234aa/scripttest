DROP TABLE IF EXISTS `sys_user_block_resource`;
CREATE TABLE `sys_user_block_resource` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '主键',
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `resource_item_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源项ID',
  `resource_object_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对象ID',
  `create_time` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单车辆|黑名单车辆|ubr';