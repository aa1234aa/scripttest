/**
开放接口初始化脚本
 */

delete from api_application where id='ffd6929b619744309728245d12bd99c9';
INSERT INTO api_application (`id`, `name`, `code`, `version`, `note`, `heart_time`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('ffd6929b619744309728245d12bd99c9', '车联网对外开放接口', 'EVSMC-BIGDATA-API', '1.0', '车联网对外开放接口', NULL, '2019-06-11 19:51:29', 'admin', NULL, NULL);


delete from api_api_detail where id in ('5', '6');
INSERT INTO api_api_detail (`id`, `name`, `application_code`, `url`, `note`, `create_time`, `create_by`, `update_time`, `update_by`, `version`) VALUES ('5', '实时数据访问接口', 'EVSMC-BIGDATA-API', '/services/realData', '查询车辆数据实时数据', NULL, 'chf', NULL, NULL, '1.0.1');
INSERT INTO api_api_detail (`id`, `name`, `application_code`, `url`, `note`, `create_time`, `create_by`, `update_time`, `update_by`, `version`) VALUES ('6', '历史数据访问接口', 'EVSMC-BIGDATA-API', '/services/hisData', '查询车辆数据历史数据', NULL, 'chf', NULL, NULL, '1.0.1');
