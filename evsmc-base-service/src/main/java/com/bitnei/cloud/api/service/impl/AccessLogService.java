package com.bitnei.cloud.api.service.impl;

import com.bitnei.cloud.api.dao.ApiDetailMapper;
import com.bitnei.cloud.api.utils.AesUtils;
import com.bitnei.cloud.api.utils.RsaUtils;
import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.api.domain.AccessLog;
import com.bitnei.cloud.api.model.AccessLogModel;
import com.bitnei.cloud.api.service.IAccessLogService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import jodd.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AccessLogService实现<br>
* 描述： AccessLogService实现<br>
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
* <td>2019-01-16 15:24:09</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.api.dao.AccessLogMapper" )
public class AccessLogService extends BaseService implements IAccessLogService {

    @Resource
    private ApiDetailMapper apiDetailMapper;

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("api_access_log", "al");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<AccessLogModel> entries = findBySqlId("pagerAccessLogModel", params);
            return convertEntityToModel(entries);
        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerAccessLogModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<AccessLogModel> entries = pr.getData().stream().map(it -> (AccessLogModel) it).collect(Collectors.toList());
            List<AccessLogModel> models = convertEntityToModel(entries);
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }

    private List<AccessLogModel> convertEntityToModel(List<AccessLogModel> entities) {
        return entities.stream().peek(model -> {
            //加密方案 0:AES 1:RSA
            if (null != model.getEncryptType()) {
                switch (model.getEncryptType()) {
                    case 0: {
                        try {
                            model.setRequestMsgDecoded(AesUtils.decode(model.getRequestMsg(), model.getAppKey()));
                            model.setRespMessageDecoded(AesUtils.decode(model.getRespMessage(), model.getAppKey()));
                        } catch (Exception e) {
                            log.error("AES 解密失败，解密数据：pushLog，id:{}", model.getId());
                           log.error("error", e);
                        }
                        break;
                    }
                    case 1: {
                        try {
                            model.setRequestMsgDecoded(RsaUtils.decryptByPrivateKey(model.getRequestMsg(),
                                    model.getRsaPriKey()));
                            model.setRespMessageDecoded(RsaUtils.decryptByPrivateKey(model.getRespMessage(),
                                    model.getRsaPriKey()));
                        } catch (Exception e) {
                            log.error("RSA 解密失败，解密数据：pushLog，id:{}", model.getId());
                           log.error("error", e);
                        }
                    }
                    default:
                        break;
                }
            }
        }).collect(Collectors.toList());
    }



    @Override
    public AccessLogModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("api_access_log", "al");
        params.put("id",id);
        AccessLog entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return AccessLogModel.fromEntry(entry);
    }




    @Override
    public void insert(AccessLogModel model) {

        AccessLog obj = new AccessLog();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        //另一个project 通过feign调用进行日志保存 无法获取并传递被请求接口的id过来，这个字段用接口url代替，这里处理从库中查出接口id
        List<String> apiList = apiDetailMapper.getApiIdByUrl(obj.getApiDetailId());
        if(CollectionUtils.isEmpty(apiList)){
            throw new BusinessException("url对应的接口不存在");
        }else{
            obj.setApiDetailId(apiList.get(0));
        }

        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(AccessLogModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("api_access_log", "al");

        AccessLog obj = new AccessLog();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
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
        Map<String,Object> params = DataAccessKit.getAuthMap("api_access_log", "al");

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
        Map<String,Object> params = DataAccessKit.getAuthMap("api_access_log", "al");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<AccessLog>(this, "pagerModel", params, "api/res/accessLog/export.xls", "接口访问日志") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ACCESSLOG"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<AccessLogModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AccessLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AccessLogModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ACCESSLOG"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<AccessLogModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(AccessLogModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(AccessLogModel model) {
                update(model);
            }
        }.work();

    }



}
