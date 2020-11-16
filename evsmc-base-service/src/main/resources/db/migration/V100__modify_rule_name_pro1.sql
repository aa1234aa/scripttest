/**
 变更人： 杨江桦
 时间： 2019-07-26
 变更内容： 修改历史分表rule_name字段长度结构存储过程
 */
drop procedure if exists modifyRuleNamePro;

DELIMITER //
CREATE PROCEDURE modifyRuleNamePro()
BEGIN
  DECLARE tableName varchar(50);
	DECLARE done INT DEFAULT 0;
	DECLARE taskCursor CURSOR FOR SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME
        REGEXP 'fault_alarm_info_history_[0-9]{6}' and table_schema = database();
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	OPEN taskCursor;
	REPEAT
	      FETCH taskCursor INTO tableName;
				IF not done THEN
				  SET @t = concat('ALTER TABLE ',tableName, ' MODIFY COLUMN `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT \'规则名称\' AFTER `rule_id`');
				  PREPARE stmt from @t;
				  EXECUTE stmt;
				END IF;
	UNTIL done END REPEAT;
	CLOSE taskCursor;
END //
DELIMITER;

CALL modifyRuleNamePro();