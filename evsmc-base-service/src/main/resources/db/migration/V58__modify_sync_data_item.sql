ALTER TABLE `dc_rule`
ADD COLUMN `sync_data_item`  int(11) NULL DEFAULT 0 COMMENT '是否同步类型下的数据项 0 否, 1 同步' AFTER `update_by`;
