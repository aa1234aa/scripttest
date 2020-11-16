ALTER TABLE `sys_user_block_resource`
  ADD UNIQUE INDEX `USER_OBJ_ID_UNQ` (`user_id`, `resource_object_id`) ;

