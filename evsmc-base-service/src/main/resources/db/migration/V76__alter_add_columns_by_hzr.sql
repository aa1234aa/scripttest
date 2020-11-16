
## 发动机型号 新增字段
ALTER TABLE `sys_engine_model`
ADD COLUMN `engine_family_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '发动机系族名称' AFTER `paint_coat_firm`,
ADD COLUMN `engine_category`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '发动机类别' AFTER `engine_family_name`,
ADD COLUMN `emission_level`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '排放水平' AFTER `engine_category`,
ADD COLUMN `environmental_info_no`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '环保信息公开编号' AFTER `emission_level`,
ADD COLUMN `contact_number`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '联系电话' AFTER `environmental_info_no`,
ADD COLUMN `contact_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '联系人姓名' AFTER `contact_number`,
ADD COLUMN `interface_photo`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接口照片' AFTER `contact_name`;

## 车载终端 新增字段
ALTER TABLE `sys_term_model_unit`
ADD COLUMN `other_file`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '车载终端其他文件' AFTER `term_description`;
