## 修改sys_push_batch_detail与dc_forward_vehicle静态数据推送时推送错误信息字段长度
ALTER TABLE sys_push_batch_detail modify column error_info varchar(256);
ALTER TABLE dc_forward_vehicle modify column error_message varchar(256);