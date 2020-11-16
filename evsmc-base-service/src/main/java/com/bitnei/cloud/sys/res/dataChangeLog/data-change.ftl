<?xml version="1.0" encoding="UTF-8"?>
<modules>
    <#list list as map>
        <module name="${map["name"]}" table="${map["table"]}" instField="username">
        <#assign _list = map["model"]>
        <#list _list as modelMap>
            <field columnName="modelMap["columnName"]" desc="modelMap["columnName"]"/>
        </#list>
        </module>
    </#list>

    <module name="车辆" table="sys_vehicle" instField="vin">
        <field columnName="license_plate" desc="车牌"/>
        <field columnName="inter_no" desc="生产内部编号"/>
    </module>
</modules>