-- 2019-08-20
## 车辆运营使用信息 增加字段 上牌时间
ALTER TABLE `sys_vehicle`
ADD COLUMN `oper_license_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上牌时间';
