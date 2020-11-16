package com.bitnei.cloud.fault.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.fault.client.VehRiskClient;
import com.bitnei.cloud.fault.dao.IncidentHandlingMapper;
import com.bitnei.cloud.fault.model.IncidentModel;
import com.bitnei.cloud.fault.model.ResultMsgModel;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.fault.domain.IncidentHandling;
import com.bitnei.cloud.fault.model.IncidentHandlingModel;
import com.bitnei.cloud.fault.service.IIncidentHandlingService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.sys.service.IVehModelService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.commons.util.MapperUtil;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： IncidentHandlingService实现<br>
* 描述： IncidentHandlingService实现<br>
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
* <td>2019-07-03 16:32:41</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.fault.dao.IncidentHandlingMapper" )
public class IncidentHandlingService extends BaseService implements IIncidentHandlingService {

    @Resource
    private IncidentHandlingMapper incidentHandlingMapper;

    @Resource
    private IVehModelService vehModelService;
    @Resource
    private VehRiskClient vehRiskClient;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<IncidentHandling> entries = findBySqlId("pagerModel", params);
            List<IncidentHandlingModel> models = new ArrayList();
            for(IncidentHandling entry: entries){
                IncidentHandling obj = (IncidentHandling)entry;
                models.add(IncidentHandlingModel.fromEntry(obj));
            }
            return models;
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<IncidentHandlingModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                IncidentHandling obj = (IncidentHandling)entry;
                models.add(IncidentHandlingModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    @Override
    public IncidentHandlingModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");
        params.put("id",id);
        IncidentHandling entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return IncidentHandlingModel.fromEntry(entry);
    }




    @Override
    public void insert(IncidentHandlingModel model) {

        IncidentHandling obj = new IncidentHandling();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setReportState(1);
        obj.setCreateTime(DateUtil.getNow());
        // 第一次新增时，创建时间等同于更新时间，用于预案上报统一使用updateTime字段
        obj.setUpdateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(IncidentHandlingModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");

        IncidentHandling obj = new IncidentHandling();
        BeanUtils.copyProperties(model, obj);
        obj.setReportState(1);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
        params.putAll(MapperUtil.Object2Map(obj));
        if (null!=params.get("reportState")&&((Integer)params.get("reportState"))==3){
            params.put("reportState",1);
        }
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<IncidentHandling>(this, "pagerModel", params, "fault/res/incidentHandling/export.xls", "车型事故处置预案") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "INCIDENTHANDLING"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<IncidentHandlingModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(IncidentHandlingModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(IncidentHandlingModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "INCIDENTHANDLING"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<IncidentHandlingModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(IncidentHandlingModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(IncidentHandlingModel model) {
                update(model);
            }
        }.work();

    }

    @Override
    public String copyReport(String id, String targetVehModelId,Integer copy) {

        if (copy != 1){
            Map<String, Object> params = new HashMap<>();
            params.put("vehModelId",targetVehModelId);
            List<IncidentHandling> target = findBySqlId("pagerModel", params);
            List<Integer> targetType = Lists.newArrayList();
            target.forEach(m -> {
                if (m.getDocumentType() != null){
                    targetType.add(m.getDocumentType());
                }
            });
            // 用于判断是否已经拥有相同类型的文档类型 1：是  0：否
            final int[] trueOrFalse = {0};
            IncidentHandlingModel source = this.get(id);
            DataLoader.loadNames(source);
            if (source.getDocumentType() != null){
                targetType.forEach(i -> {
                    if (i.equals(source.getDocumentType())){
                        trueOrFalse[0] = 1;
                    }
                });
            }
            if (trueOrFalse[0] == 1){

                return "将覆盖车型原处置预案/整改方案，是否确定?";
            }
            else {
                return "将新增"+source.getVehModelName()+"的处置预案/整改方案，是否确认?";
            }
        }
        else {
            //获取当权限的map
            Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");

            IncidentHandlingModel source = this.get(id);
            params.put("vehModelId",targetVehModelId);
            params.put("documentType",source.getDocumentType());
            // 复制时的覆盖需删除原先文档类型下的车型预案
            super.deleteBySqlId("deleteOld",params);
            source.setVehModelId(targetVehModelId);
            // 新增复制
            this.insert(source);
            return "复制成功";
        }
    }

    @Override
    public void truUpAlarm(String ids,String platform){

//        Map<String,Object> params = DataAccessKit.getAuthMap("fault_incident_handling", "fih");
//        String authSQL = params.get("authSQL") == null ? null : params.get("authSQL").toString();
        String []arr = ids.split(",");
        String updateTime = DateUtil.getNow();
        String updateBy = ServletUtil.getCurrentUser();
        String[] platforms = platform.split(",");
        for (String s : platforms){
            if ("1".equals(s)){
                for (String id : arr){
                    IncidentHandlingModel model = this.get(id);
                    if (model.getReportState() != null && model.getReportState() != 4){
                        DataLoader.loadNames(model);
                        model.setUpdateTime(updateTime);
                        model.setUpdateBy(updateBy);
                        IncidentModel data = new IncidentModel();
                        data.setVehModel(model.getVehModelName());
                        data.setNote(model.getDocumentDescribe());
                        data.setDocs(model.getFileId());
                        data.setDocsType(String.valueOf(model.getDocumentType()));
                        data.setCreateTime(updateTime);
                        ResultMsgModel resultMsgModel = vehRiskClient.truUpAlarm(data);
                        model.setReportTime(updateTime);
                        model.setPlatform(1);
                        if (resultMsgModel.getCode() == 0){
                            // 成功
                            model.setReportState(4);
                            model.setReasonsForFailure(null);
                        }
                        else {
                            model.setReportState(3);
                            model.setReasonsForFailure(resultMsgModel.getMsg());
                        }

                        // 权限码问题
                        incidentHandlingMapper.updateReportState(model);
                    }
                }
            }
            else {
                throw new BusinessException("目前请预案上报至国家平台");
            }
        }


    }

}
