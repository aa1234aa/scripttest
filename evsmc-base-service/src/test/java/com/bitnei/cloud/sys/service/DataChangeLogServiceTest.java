package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.sys.domain.Table;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DataChangeLogServiceTest {

    @Resource
    private IDataChangeLogService iDataChangeLogService;

    @Test
    public void genXml() throws IOException, TemplateException {
        List<Map<String,Object>> list = Lists.newArrayList();

        String[] tableNames = {"sys_user", "sys_unit", "sys_role", "sys_module_data_item", "sys_owner_people", "sys_veh_owner",
                "sys_core_resource", "sys_vehicle", "sys_veh_model", "sys_veh_notice", "sys_veh_type", "sys_veh_brand", "sys_veh_series",
                "sys_term_model_unit", "sys_term_model", "sys_sim_management", "sys_engery_device", "sys_battery_device_model",
                "sys_super_capacitor_model", "sys_drive_device", "sys_drive_motor_model", "sys_engine_model", "sys_power_device",
                "sys_fuel_generator_model", "sys_fuel_system_model", "sys_dict", "sys_area", "sys_module", "sys_unit_type"};
        for (String tableName : tableNames) {
            List<Table> table = iDataChangeLogService.findTable(new String[]{tableName}, null);
            Map<String, Object> map = Maps.newHashMap();
            Table t = table.get(0);
            map.put("name", t.getTableComment());
            map.put("table", t.getTableName());
            List<Map<String,String>> columnList  =  Lists.newArrayList();
            List<Table> columns = iDataChangeLogService.findTable(null, t.getTableName());
            for (Table column : columns) {
                Map<String, String> columnMap = Maps.newHashMap();
                columnMap.put("columnName", column.getColumnName());
                columnMap.put("desc", column.getColumnComment());
                if(!map.containsKey("instField") && "UNI".equals(column.getColumnKey())) {
                    map.put("instField", column.getColumnName());
                }
                columnList.add(columnMap);
            }
            if(!map.containsKey("instField")){
                map.put("instField", "id");
            }
            map.put("module", columnList);
            list.add(map);
        }


        Map<String, Object> m = Maps.newHashMap();
        m.put("list",list);

        String templateFile = "D://opt";
        System.out.println("模板路径=======" + templateFile);


        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        cfg.setDirectoryForTemplateLoading(new File(templateFile));
        Template template = cfg.getTemplate("data-change.ftl");
        StringWriter result = new StringWriter();
        template.process(m,result);
        String filePath = "D:/opt/data-change.xml";
        FileOutputStream fos = new FileOutputStream(filePath);
        Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(result.toString());
        bufferedWriter.close();
        writer.close();
    }
}
