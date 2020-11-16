 DELETE from fault_parameter_manage where dc_rule_type_id='a18e836c5c7a462da3cac54ce78c9d70';
  insert  into fault_parameter_manage(id,dc_rule_type_id,dc_data_item_id,code)
 (SELECT uuid(),rule_type_id,id,seq_no from dc_data_item i where i.rule_type_id='a18e836c5c7a462da3cac54ce78c9d70' and i.seq_no not in (SELECT code from fault_parameter_manage));
