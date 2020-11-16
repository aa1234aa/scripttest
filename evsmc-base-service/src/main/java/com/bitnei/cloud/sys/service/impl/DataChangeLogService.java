package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.DataChangeLog;
import com.bitnei.cloud.sys.domain.Table;
import com.bitnei.cloud.sys.model.DataChangeLogModel;
import com.bitnei.cloud.sys.service.IDataChangeLogService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataChangeLogService实现<br>
* 描述： DataChangeLogService实现<br>
* 授权 : (C) Copyright (c) 2017 <br>
* 公司 : 北京理工新源信息科技有限公司<br>
* ----------------------------------------------------------------------------- <br>
* 修改历史 <br>
* <table width="432" border="1">
* <tr>
* <td>版本</td>
* <td>时间</td>
* <td>作者</td>
* <td>改变</td>
* </tr>
* <tr>
* <td>1.0</td>
* <td>2018-12-04 15:43:28</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.DataChangeLogMapper" )
public class DataChangeLogService extends BaseService implements IDataChangeLogService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_data_change_log", "dlog");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<DataChangeLog> entries = findBySqlId("pagerModel", params);
            List<DataChangeLogModel> models = new ArrayList();
            for(DataChangeLog entry: entries){
                DataChangeLog obj = (DataChangeLog)entry;
                models.add(DataChangeLogModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<DataChangeLogModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                DataChangeLog obj = (DataChangeLog)entry;
                models.add(DataChangeLogModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_data_change_log", "dlog");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<DataChangeLog>(this, "pagerModel", params, "sys/res/dataChangeLog/export.xls", "数据变更日志") {
        }.work();

        return;
    }

    @Override
    public List<Table> findTable(String[] tableNames, String tableName){
        Map<String, Object> params = Maps.newHashMap();
        if(tableNames != null) {
            params.put("tableNames", tableNames);
            return findBySqlId("findTable", params);
        } else {
            params.put("tableName", tableName);
            return findBySqlId("findTableColumns", params);
        }
    }
}
