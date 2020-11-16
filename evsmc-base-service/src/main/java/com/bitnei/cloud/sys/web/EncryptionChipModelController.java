package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.model.EcoRespModel;
import com.bitnei.cloud.sys.model.EncryptionChipModelModel;
import com.bitnei.cloud.sys.service.IEncryptionChipModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 加密芯片型号<br>
* 描述： 加密芯片型号控制器<br>
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
* <td>2019-07-03 20:06:31</td>
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
@Api(value = "加密芯片型号", description = "加密芯片型号",  tags = {"基础数据-零部件信息-加密芯片型号"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class EncryptionChipModelController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="ENCRYPTIONCHIPMODEL";
    /** 查看 **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE +"_DETAIL";
    /** 列表 **/
    public static final String AUTH_LIST = BASE_AUTH_CODE +"_LIST";
    /** 分页 **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE +"_PAGER";
    /** 新增 **/
    public static final String AUTH_ADD = BASE_AUTH_CODE +"_ADD";
    /** 编辑 **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE +"_UPDATE";
    /** 删除 **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE +"_DELETE";
    /** 导出 **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE +"_EXPORT";
    /** 导入 **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE +"_IMPORT";
    /** 批量导入 **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE +"_BATCH_IMPORT";
    /** 批量更新 **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE +"_BATCH_UPDATE";

    @Autowired
    private IEncryptionChipModelService encryptionChipModelService;


     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/encryptionChipModels/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        EncryptionChipModelModel encryptionChipModel = encryptionChipModelService.get(id);
        return ResultMsg.getResult(encryptionChipModel);
    }



    /**
     * 根据芯片型号获取对象
     *
     * @return
     */
    @ApiOperation(value = "根据芯片型号获取详细信息" , notes = "根据芯片型号获取详细信息")
    @ApiImplicitParam(name = "name", value = "芯片型号", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/encryptionChipModels/name/{name}")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg name(@PathVariable String name){

        EncryptionChipModelModel encryptionChipModel = encryptionChipModelService.getByName(name);
        return ResultMsg.getResult(encryptionChipModel);


    }

     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/encryptionChipModels")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo){

        Object result = encryptionChipModelService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param model
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/encryptionChipModel")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) EncryptionChipModelModel model){

        encryptionChipModelService.insert(model);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param model
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/encryptionChipModels/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) EncryptionChipModelModel model){


        encryptionChipModelService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    @ApiOperation(value = "更新芯片型号国家备案编码", notes = "更新芯片型号国家备案编码" )
    @PutMapping(value = "/encryptionChipModels/updateFilingCode")
    @ResponseBody
    public ResultMsg updateCode(@RequestBody EcoRespModel model) {
        // 更新储存芯片型号上报成功后返回的国家备案编码
        if(Constant.SUCCESS.equals(model.getType())) {
            String chipPrefix = "chipPrefix";
            if(model.getData() != null && model.getData().containsKey(chipPrefix)) {
                String filingCode = model.getData().get(chipPrefix);
                EncryptionChipModelModel chipModel = encryptionChipModelService.get(model.getFromId());
                if(chipModel != null) {
                    chipModel.setFilingCode(filingCode);
                    chipModel.setFilingStatus(Constant.CHIP_FILING_STATUS.SUCCESS);
                    encryptionChipModelService.update(chipModel);
                    return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
                }
            }
        }
        return ResultMsg.getSchemaMsg("fail");
    }

    @ApiOperation(value = "芯片型号环保平台备案上报", notes = "芯片型号环保平台备案上报" )
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @PostMapping(value = "/encryptionChipModels/filing/{id}")
    @ResponseBody
    public ResultMsg filing(@PathVariable String id) {
        encryptionChipModelService.filing(id);
        return ResultMsg.getResult("备案成功");
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/encryptionChipModels/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = encryptionChipModelService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/encryptionChipModels/export/{params}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String",paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        encryptionChipModelService.export(info);
        return ;

    }


}
