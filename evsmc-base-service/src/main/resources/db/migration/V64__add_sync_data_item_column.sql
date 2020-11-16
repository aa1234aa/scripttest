##平台是否同步通讯协议下的数据项

ALTER TABLE `dc_forward_platform`
ADD COLUMN `sync_data_item`  int(11) NULL DEFAULT 0 COMMENT '是否同步类型下的数据项 0 否, 1 同步';
