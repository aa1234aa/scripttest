/**
  段飞洋
 */

drop table if exists monitor_electronic_fence;
/*==============================================================*/
/* Table: monitor_electronic_fence                              */
/*==============================================================*/
create table monitor_electronic_fence
(
   id                   varchar(36) not null comment '唯一标识',
   group_code           varchar(32) comment '组编码，时间戳',
   name                 varchar(32) comment '围栏名称',
   create_by            varchar(36) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(36) comment '修改人',
   update_time          datetime comment '修改时间',
   rule_status          tinyint(4) comment '规则状态：1、启用0、禁用',
   status_flag          tinyint(4) comment '是否有效：1、启用0、禁用',
   rule_type            tinyint(4) comment '规则类型：8、驶离4、驶入',
   rule_use             tinyint(4) comment '规则用途：1、行驶围栏2、停车围栏',
   period_type          tinyint(4) comment '周期类型：1、单次执行2、每周循环3、每天循环',
   response_mode        tinyint(4) comment '平台响应方式：0、无1、系统弹窗2、声音提醒3、APP弹窗提醒4、短信通知',
   start_date           date comment '开始日期',
   end_date             date comment '结束日期',
   rule_week            varchar(64) comment '星期，多个之间用的逗号分隔，周一为1到周日为7',
   start_time           time comment '开始启用时间',
   end_time             time comment '结束启用时间',
   chart_type           tinyint(4) comment 'chart类型1、圆形 2、多边形',
   lonlat_range         varchar(500) comment '经纬度范围，当地图类型为1圆形时，两个值为半径;圆点为2多边形时，每一个;的值为经纬度点',
   primary key (id)
);
alter table monitor_electronic_fence comment '电子围栏|电子围栏信息表|efe';

drop table if exists monitor_fence_veh_lk;
/*==============================================================*/
/* Table: monitor_fence_veh_lk                                  */
/*==============================================================*/
create table monitor_fence_veh_lk
(
   id                   varchar(36) comment '唯一标识',
   electronic_fence_id  varchar(36) comment '围栏id',
   vid                  varchar(36) comment '车辆uuid',
   state                tinyint(4) comment '是否有效1有效0无效',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (id)
);
alter table monitor_fence_veh_lk comment '电子围栏|围栏关联车辆表|fvlk';

INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('0541f949ebb64737a6465c96b834b9d1', '7b84044be37d453c8a96bbcd2b0f2114', '电子围栏报警规则设置', 'electronicFence', NULL, '0', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/', '', '/electronicFence', NULL, '10', '0', '2019-05-27 10:30:53', 'admin', '2019-05-27 10:33:10', 'admin');
INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('1755a560a15b4a8682c131e9ceeffccc', '0541f949ebb64737a6465c96b834b9d1', '删除', 'fenceRemove', NULL, '1', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/1755a560a15b4a8682c131e9ceeffccc/', '', '', NULL, '1', '0', '2019-06-06 13:52:13', 'admin', NULL, NULL);
INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('1927195a28c649e29edac97652f3b9c8', '0541f949ebb64737a6465c96b834b9d1', '编辑', 'fenceEdit', NULL, '1', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/1927195a28c649e29edac97652f3b9c8/', '', '', NULL, '1', '0', '2019-06-06 13:51:50', 'admin', NULL, NULL);
INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('2022f5470c254a19b4328aafb9bb74ae', '0541f949ebb64737a6465c96b834b9d1', '新增车辆', 'fenceVehAdd', NULL, '1', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/2022f5470c254a19b4328aafb9bb74ae/', '', '', NULL, '1', '0', '2019-06-06 13:52:29', 'admin', NULL, NULL);
INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('6bc60f41e99a4ce59605165e4a47f20e', '0541f949ebb64737a6465c96b834b9d1', '新增', 'fenceAdd', NULL, '1', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/6bc60f41e99a4ce59605165e4a47f20e/', '', '', NULL, '1', '0', '2019-06-06 13:51:40', 'admin', NULL, NULL);
INSERT INTO `sys_module` (`id`, `parent_id`, `name`, `code`, `is_root`, `is_fun`, `path`, `icon`, `action`, `is_fullscreen`, `order_num`, `is_hidden`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('93d15eee3a43448d8846264650c07d86', '0541f949ebb64737a6465c96b834b9d1', '删除车辆', 'fenceVehRemove', NULL, '1', '2f60d72f9f3c4b7cba4a4ffcb800cfa1/7b84044be37d453c8a96bbcd2b0f2114/0541f949ebb64737a6465c96b834b9d1/93d15eee3a43448d8846264650c07d86/', '', '', NULL, '1', '0', '2019-06-06 13:52:39', 'admin', NULL, NULL);

