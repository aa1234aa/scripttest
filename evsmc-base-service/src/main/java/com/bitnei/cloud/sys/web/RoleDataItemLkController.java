package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.RoleDataItemLk;
import com.bitnei.cloud.sys.model.RoleDataItemLkModel;
import com.bitnei.cloud.sys.service.IRoleDataItemLkService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 数据项角色数据项中间表<br>
* 描述： 数据项角色数据项中间表控制器<br>
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
* <td>2018-11-22 10:28:44</td>
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
@Api(value = "角色数据项", description = "角色数据项",  tags = {"角色数据项"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class RoleDataItemLkController{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 模块基础请求前缀 **/
    public static final String BASE_AUTH_CODE ="ROLEDATAITEMLK";
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
    private IRoleDataItemLkService roleDataItemLkService;

     /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息" , notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/roleDataItemLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id){

        RoleDataItemLkModel roleDataItemLk = roleDataItemLkService.get(id);
        return ResultMsg.getResult(roleDataItemLk);
    }




     /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询" , notes = "多条件查询")
    @PostMapping(value = "/roleDataItemLks")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = roleDataItemLkService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }




    /**
    * 保存
    * @param demo1
    * @return
    */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/roleDataItemLk")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) RoleDataItemLkModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        roleDataItemLkService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }



    /**
    * 更新
    * @param demo1
    * @return
    */
    @ApiOperation(value = "更新", notes = "编辑更新信息" )
    @PutMapping(value = "/roleDataItemLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) RoleDataItemLkModel demo1, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        roleDataItemLkService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }




    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",paramType = "path")
    @DeleteMapping(value = "/roleDataItemLks/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    public ResultMsg delete(@PathVariable String id){

        int count = roleDataItemLkService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));


    }


    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出" ,notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/roleDataItemLks/export")
    @RequiresPermissions(AUTH_EXPORT)
    public void export(@RequestBody @Validated PagerInfo info){

        roleDataItemLkService.export(info);
        return ;

    }


    /**
     * 批量导入
     * @return
     */
    @ApiOperation(value = "批量导入 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/roleDataItemLks/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file){

        roleDataItemLkService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     * @return
     */
    @ApiOperation(value = "批量更新 " ,notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/roleDataItemLks/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file){

        roleDataItemLkService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }


    /**
     * 查询角色列数据项
     * @return
     */
    @Getter
    @Setter
    @ApiModel(value = "FieldModel", description = "列数据项model")
    static class FieldModel{
        @ApiModelProperty(value = "是否隐藏")
        private String[] isHidden;
        @ApiModelProperty(value = "是否脱敏")
        private String[] isSensitive;
        @ApiModelProperty(value = "是否父节点")
        private boolean isParent ;
    }
    @ApiOperation(value = "查询角色列数据项 " ,notes = "查询角色列数据项")
    @GetMapping(value = "/roles/queryRoleModuleDataItem/{roleId}")
    @ResponseBody
    public ResultMsg queryModuleDataItem(@PathVariable String roleId){
        Map<String,FieldModel> result = new HashMap<>();
        List<Map<String, Object>> maps = roleDataItemLkService.queryModuleByRoleId(roleId);
        List<RoleDataItemLk> allItemLks = roleDataItemLkService.queryRoleModuleDateItemPermission(roleId);
        for(Map<String,Object> map :  maps) {

            String moduleId = map.get("module_id").toString();
            int subModuleCount = NumberUtils.toInt(map.get("sub_module_count").toString(), 0);
            List<RoleDataItemLk> models = new ArrayList<>();
            for (RoleDataItemLk lk: allItemLks){
                if (moduleId.equals(lk.get("moduleId").toString())){
                    models.add(lk);
                }
            }
            FieldModel fieldModel = new FieldModel();
            List<String> isHidden=new ArrayList<>();
            List<String> isSensitive=new ArrayList<>();
            for(RoleDataItemLk rdil : models){
                if(rdil.getIsHidden() != null && rdil.getIsHidden() == 1) {
                    isHidden.add(rdil.getModuleDataItemId());
                }
                if(rdil.getIsSensitive()!=null && rdil.getIsSensitive() == 1) {
                    isSensitive.add(rdil.getModuleDataItemId());
                }
            }
            fieldModel.isParent=subModuleCount >0;
            fieldModel.isHidden=isHidden.toArray(new String[0]);
            fieldModel.isSensitive=isSensitive.toArray(new String[0]);
            result.put(moduleId,fieldModel);
        }
        return ResultMsg.getResult(result);
    }

    /**
     * 设置角色数据项权限，是否脱敏，是否隐藏
     *
     * @param
     *
     */
    @Getter
    @Setter
    @ApiModel(value = "RoleFieldModel", description = "角色列数据项model")
    static class RoleFieldModel{
        @ApiModelProperty(value = "角色")
        private String roleId;
        @ApiModelProperty(value = "数据项权限")
        private Map<String,FieldModel> data;
    }
    @ApiOperation(value = "设置角色列权限" , notes = "设置角色数据项权限，是否脱敏，是否隐藏")
    @PostMapping(value = "/roles/updateRoleModuleDataItem")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg updateRoleModuleDateItemPermission(@RequestBody RoleFieldModel param){
        Map<String,FieldModel> data = param.data;

        //删除已有记录
        roleDataItemLkService.deleteRoleDataItem(param.roleId);
        roleDataItemLkService.deleteRoleModuleLk(param.roleId);

        for (Map.Entry<String,FieldModel> entry : data.entrySet()) {
            roleDataItemLkService.updateRoleDateItemPermission(param.roleId,entry.getKey(), Arrays.asList(entry.getValue().isHidden),Arrays.asList(entry.getValue().isSensitive));
        }

        return ResultMsg.getResult("设置角色列权限完成");
    }
}
