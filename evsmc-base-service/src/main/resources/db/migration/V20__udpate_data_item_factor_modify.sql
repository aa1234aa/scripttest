

#燃料电池电压
update dc_data_item set factor= '0.1', unit='V' where seq_no = '2110';
#燃料电池电流
update dc_data_item set  factor= '0.1', unit='A' where seq_no = '2111';
#氢系统中最高温度
update dc_data_item set  factor= '0.1', unit='℃' where seq_no = '2115';
#氢气最高压力
update dc_data_item set  factor= '0.1', unit='MPa' where seq_no = '2119';
#车速
update dc_data_item set  factor= '0.1', unit='km/h' where seq_no = '2201';
#里程
update dc_data_item set  factor= ' 0.1', unit='km' where seq_no = '2202';
#电机控制器输入电压
update dc_data_item set  factor= '0.1', unit='V' where seq_no = '2305';
#电机控制器直流母线电流
update dc_data_item set  factor= '0.1', unit='A' where seq_no = '2306';
#驱动电机转矩
update dc_data_item set  factor= '0.1', unit='N·m', `offset`='20000' where seq_no = '2311';
#燃料消耗率
update dc_data_item set  factor= '0.01', unit='L/100km' where seq_no = '2413';
#经度
update dc_data_item set  factor= '0.000001', unit='°' where seq_no = '2502';
#纬度
update dc_data_item set  factor= '0.000001', unit='°' where seq_no = '2503';
#电池单体电压最高值
update dc_data_item set  factor= '0.001', unit='V' where seq_no = '2603';
#电池单体电压最低值
update dc_data_item set  factor= '0.001', unit='V' where seq_no = '2606';
#总电压
update dc_data_item set  factor= '0.1', unit='V' where seq_no = '2613';
#总电流
update dc_data_item set  factor= '0.1', unit='A' where seq_no = '2614';







