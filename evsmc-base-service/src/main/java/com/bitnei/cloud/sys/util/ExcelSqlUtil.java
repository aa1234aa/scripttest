package com.bitnei.cloud.sys.util;


public class ExcelSqlUtil {


    public static void main(String [] args) throws Exception {

//        String sql = "INSERT INTO `sys_vehicle` (`id`, `vin`, `uuid`, `license_type`, `license_plate`, `inter_no`, `veh_model_id`, `color`, `color_nick_name`, `term_id`, `stage`, `stage_change_date`, `manu_unit_id`, `produce_batch`, `quality_years`, `veh_certificate_number`, `produce_date`, `factory_date`, `sell_date`, `sell_price`, `sell_for_field`, `sell_pri_veh_owner_id`, `sell_pub_unit_id`, `sell_city_id`, `sell_4s_unit_id`, `sell_seller_id`, `sell_secure_date`, `sell_first_check_date`, `sell_invoice_no`, `sell_invoice_date`, `sell_invoice_img_id`, `sell_license_no`, `sell_license_reg_date`, `sell_license_grant_date`, `sell_license_img_id`, `is_selled`, `oper_inter_no`, `oper_use_for`, `oper_use_type`, `oper_veh_owner_id`, `oper_unit_id`, `oper_license_city_id`, `oper_area_id`, `oper_time`, `oper_support_owner_id`, `oper_save_city_id`, `oper_save_address`, `oper_chg_city_id`, `oper_chg_address`, `is_opered`, `subsidy_apply_status`, `susidy_apply_count`, `create_time`, `create_by`, `update_time`, `update_by`, `is_delete`) VALUES " +
//                "('%s', '%s', '%s', '0', '%s', '%s', '86397149af7a4ab286f7074e36743a43', '#000000', '李白', '%s', '4', '2019-03-18 15:55:37', 'c5e6b5f83d4b47b8a8b1309484c58f71', '201801', '1001', '232445', '2017-08-08', '2017-09-09', '2017-09-25', '15', '2', NULL, 'b4cea7b2f18d4dbfbb847c79e850bc0c', '402882d25c0eeeed015c0f6603c308d2', 'c5e6b5f83d4b47b8a8b1309484c58f71', '0cd128cb3e7d47c19b4bbdf22fcb9983', '2019-03-18', '2019-03-18', '%s', '2019-03-18', '79e9cacec082482fb3de5cc9623e6a33', '%s', '2019-03-18', '2019-03-18', '5d842f9409a7461e88d7e13c0650fd94', '1', '%s', '1', '0', '', 'b4cea7b2f18d4dbfbb847c79e850bc0c', '402882d25c0eeeed015c0f6603c308d2', '402882d25c0eeeed015c0f6603c308d2', '2019-03-18', '0cd128cb3e7d47c19b4bbdf22fcb9983', '402882d25c0eeeed015c0f6603c308d2', '杭州市主城区', '402882d25c0eeeed015c0f6603c308d2', '杭州市主城区', '1', NULL, NULL, '%s', 'admin', '2019-03-18 16:29:24', 'admin', '0');";
//        String driveDeviceLkSql  = "INSERT INTO `sys_vehicle_drive_device_lk` (`id`, `create_time`, `create_by`, `vehicle_id`, `drvie_device_id`) VALUES ('%s', '%s', 'admin', '%s', '9b1eb18bbaa9493fb6a367709169714d');";
//        String engeryDeviceLkSql = "INSERT INTO `sys_vehicle_engery_device_lk` (`id`, `create_time`, `create_by`, `vehicle_id`, `engery_device_id`) VALUES ('%s', '%s', 'admin', '%s', '4d6e0af99e0c49e9ab86ea6f036615d5');";
//        String powerDeviceLkSql = "INSERT INTO `sys_vehicle_power_device_lk` (`id`, `create_time`, `create_by`, `vehicle_id`, `power_device_id`) VALUES ('%s', '%s', 'admin', '%s', '194d66a319c546e78b9e5023c90de06c');";
//        String termModelUnitSql = "INSERT INTO `sys_term_model_unit` (`id`, `serial_number`, `sys_term_model_id`, `support_protocol`, `protocol_version`, `iccid`, `imei`, `term_part_firmware_number`, `fireware_version`, `produce_batch`, `factory_date`, `create_time`, `create_by`) VALUES ('%s', '%s', 'b0e3ba509fe64a8588890fe419f979aa', '828751f66e824d6398416582d1a0ac98', 'V3', '89860403101890200000', '100000000000000', NULL, 'V2', '201801', '2019/03/18', '2019-03-18 15:26:17', 'admin');";
//
//        File file = new File("D:\\lgxy\\vehiclex.xls");
//        InputStream inputStream = new FileInputStream(file);
//        List<List<String>> lists = EasyExcel.readExcel(inputStream, 0, 0, 2, 3);
//        for (List<String> list : lists) {
//            String id = UtilHelper.getUUID();
//            String termId = UtilHelper.getUUID();
//            //内部编号
//            String interNo = RandomUtil.generateRandom("临", 7);
//            // 车牌号
//            String licensePlate = list.get(1);
//            // VIN
//            String vin = list.get(2);
//            // uuid
//            String uuid = list.get(3);
//
//            String s1 = RandomUtil.generateRandom("ZXZ", 5);
//            String s2 = RandomUtil.generateRandom("ZXZ", 6);
//            String s3 = RandomUtil.generateRandom("ZXZ", 7);
//
//            String date = DateUtil.getNow();
//
//            String serialNumber = RandomUtil.generateRandom("TERM", 5);
//            System.out.println(String.format(termModelUnitSql,termId, serialNumber));
//            System.out.println(String.format(sql, id, vin, uuid, licensePlate, interNo, termId, s1, s2, s3, date));
//            System.out.println(String.format(driveDeviceLkSql, UtilHelper.getUUID(), date, id));
//            System.out.println(String.format(engeryDeviceLkSql, UtilHelper.getUUID(), date, id));
//            System.out.println(String.format(powerDeviceLkSql, UtilHelper.getUUID(), date, id));
//            System.out.println();
//        }

    }

}
