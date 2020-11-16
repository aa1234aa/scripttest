package com.bitnei.cloud.sys.service.impl;

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
import com.bitnei.cloud.sys.dao.DictMapper;
import com.bitnei.cloud.sys.dao.TermModelUnitMapper;
import com.bitnei.cloud.sys.domain.Dict;
import com.bitnei.cloud.sys.domain.EncryptionChip;
import com.bitnei.cloud.sys.model.EncryptionChipModel;
import com.bitnei.cloud.sys.model.EncryptionChipModelModel;
import com.bitnei.cloud.sys.service.IEncryptionChipModelService;
import com.bitnei.cloud.sys.service.IEncryptionChipService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EncryptionChipService实现<br>
* 描述： EncryptionChipService实现<br>
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
* <td>2019-07-03 20:07:23</td>
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
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.EncryptionChipMapper" )
public class EncryptionChipService extends BaseService implements IEncryptionChipService {

    @Resource
    private DictMapper dictMapper;
    @Autowired
    private IEncryptionChipModelService encryptionChipModelService;
    @Resource
    private TermModelUnitMapper termModelUnitMapper;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<EncryptionChip> entries = findBySqlId("pagerModel", params);
            List<EncryptionChipModel> models = new ArrayList();
            for(EncryptionChip entry: entries){
                EncryptionChip obj = (EncryptionChip)entry;
                models.add(EncryptionChipModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<EncryptionChipModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                EncryptionChip obj = (EncryptionChip)entry;
                models.add(EncryptionChipModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public EncryptionChipModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");
        params.put("id",id);
        EncryptionChip entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return EncryptionChipModel.fromEntry(entry);
    }


    @Override
    public EncryptionChipModel getByIdentificationId(String identificationId){

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");
        params.put("identificationId",identificationId);
        EncryptionChip entry = unique("findByIdentificationId", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return EncryptionChipModel.fromEntry(entry);
    }


    @Override
    public void insert(EncryptionChipModel model) {

        EncryptionChip obj = new EncryptionChip();
        BeanUtils.copyProperties(model, obj);
        String id = UtilHelper.getUUID();
        obj.setId(id);
        obj.setFilingStatus(2);
        obj.setCreateTime(DateUtil.getNow());
        obj.setCreateBy(ServletUtil.getCurrentUser());
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void update(EncryptionChipModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");

        EncryptionChip obj = new EncryptionChip();
        BeanUtils.copyProperties(model, obj);
        obj.setUpdateTime(DateUtil.getNow());
        obj.setUpdateBy(ServletUtil.getCurrentUser());
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
    @Transactional(rollbackFor = Exception.class)
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            Map<String, Object> valiParams = new HashMap<>();
            valiParams.put("encryptionChipId", id);
            Integer tmuCount = termModelUnitMapper.findByEncryptionChipIdCount(valiParams);
            if (tmuCount > 0) {
                throw new BusinessException("存在加密芯片已关联车载终端，不可删除", 300);
            }
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_encryption_chip", "ec");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<EncryptionChip>(this, "pagerModel", params, "sys/res/encryptionChip/export.xls", "加密芯片") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "ENCRYPTIONCHIP"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<EncryptionChipModel>(file, messageType, GroupExcelImport.class, 0, 1) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EncryptionChipModel model) {
                List<String> errors = Lists.newArrayList();
                // 芯片型号
                if(StringUtils.isNotBlank(model.getChipModelDisplay())){
                    try {
                        EncryptionChipModelModel entry = encryptionChipModelService.getByName(model.getChipModelDisplay().trim());
                        model.setChipModelId(entry.getId());
                    } catch (BusinessException e) {
                        errors.add("芯片型号不存在，请确认");
                    }
                }
                // 私钥灌装状态
                if (StringUtils.isNotBlank(model.getPrivateKeyStatusDisplay())) {
                    Map<String, Object> statusParams = ImmutableMap.of("type", "PRIVATE_KEY_FILING_STATUS", "name", model.getPrivateKeyStatusDisplay());
                    Dict dict = dictMapper.getByTypeAndName(statusParams);
                    if (dict != null) {
                        model.setPrivateKeyStatus(Integer.parseInt(dict.getValue()));
                    } else {
                        errors.add("私钥灌装状态填写不正确");
                    }
                }
                return errors;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(EncryptionChipModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "ENCRYPTIONCHIP"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<EncryptionChipModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(EncryptionChipModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(EncryptionChipModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 生成导入模板文件
     */
    @Override
    public void getImportTemplateFile() {
        EasyExcel.renderImportDemoFile("加密芯片导入模板.xls", EncryptionChipModel.class);
    }



}
