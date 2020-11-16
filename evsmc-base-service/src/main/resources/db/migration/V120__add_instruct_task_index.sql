/**
 变更人： 杨江桦
 时间： 2019-08-02
 变更内容： 车辆升级任务和国标指令缓存任务表增加索引
 */

drop procedure IF EXISTS del_task_idx;

DELIMITER //
create procedure del_task_idx(IN p_tablename varchar(200), IN p_idxname VARCHAR(200))
begin
    DECLARE str VARCHAR(250);
        set @str=concat(' drop index ',p_idxname,' on ',p_tablename);
        select count(*) into @cnt from information_schema.statistics
            where table_name=p_tablename and index_name=p_idxname and table_schema = DATABASE();
        if @cnt >0 then
        PREPARE stmt FROM @str;
        EXECUTE stmt;
        end if;
end //
DELIMITER;

call del_task_idx('sys_instruct_task','idx_instruct_id_status');

alter table `sys_instruct_task` ADD INDEX `idx_instruct_id_status`(`instruct_id`, `status`) USING BTREE;