package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.AppBean;
import com.bitnei.cloud.common.util.RSAUtil;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.InstructManagement;
import com.bitnei.cloud.sys.model.CheckPasswordDto;
import com.bitnei.cloud.sys.model.EditPasswordDto;
import com.bitnei.cloud.sys.model.InstructManagementModel;
import com.bitnei.cloud.sys.service.IInstructManagementService;
import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.bitnei.commons.datatables.PagerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.URLDecoder;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 控制命令管理<br>
 * 描述： 控制命令管理控制器<br>
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
 * <td>2019-03-11 15:53:11</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Api(value = "远程控制-控制命令管理", description = "远程控制-控制命令管理", tags = {"远程控制-控制命令管理"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
public class InstructManagementController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "INSTRUCTMANAGEMENT";
    /**
     * 查看
     **/
    public static final String AUTH_DETAIL = BASE_AUTH_CODE + "_DETAIL";
    /**
     * 列表
     **/
    public static final String AUTH_LIST = BASE_AUTH_CODE + "_LIST";
    /**
     * 分页
     **/
    public static final String AUTH_PAGER = BASE_AUTH_CODE + "_PAGER";
    /**
     * 新增
     **/
    public static final String AUTH_ADD = BASE_AUTH_CODE + "_ADD";
    /**
     * 编辑
     **/
    public static final String AUTH_UPDATE = BASE_AUTH_CODE + "_UPDATE";
    /**
     * 删除
     **/
    public static final String AUTH_DELETE = BASE_AUTH_CODE + "_DELETE";
    /**
     * 导出
     **/
    public static final String AUTH_EXPORT = BASE_AUTH_CODE + "_EXPORT";
    /**
     * 导入
     **/
    public static final String AUTH_IMPORT = BASE_AUTH_CODE + "_IMPORT";
    /**
     * 批量导入
     **/
    public static final String AUTH_BATCH_IMPORT = BASE_AUTH_CODE + "_BATCH_IMPORT";
    /**
     * 批量更新
     **/
    public static final String AUTH_BATCH_UPDATE = BASE_AUTH_CODE + "_BATCH_UPDATE";

    @Value("${app.security.rsa.privateKey}")
    private String rsaPriKey;

    @Autowired
    private IInstructManagementService instructManagementService;


    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/instructManagements/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        InstructManagementModel instructManagement = instructManagementService.get(id);
        return ResultMsg.getResult(instructManagement);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询：查询条件：name，status，vehModelId，instructCategoryId")
    @PostMapping(value = "/instructManagements")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = instructManagementService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 保存
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增信息")
    @PostMapping(value = "/instructManagement")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) InstructManagementModel demo1,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        //处理指令的细节
        dealParamData(demo1);
        if (StringUtils.isNotEmpty(demo1.getPasswd())) {
            demo1.setPasswd(SecurityUtil.getMd5(demo1.getPasswd()));
        }

        instructManagementService.insert(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }


    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息（修改的时候密码不用做任何处理，按列表数据回传）")
    @PutMapping(value = "/instructManagements/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) InstructManagementModel demo1,
                            @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        InstructManagementModel model = instructManagementService.get(demo1.getId());

        if (!model.getPasswd().equals(demo1.getPasswd())) {
            throw new BusinessException("密码错误");
        }

        demo1.setPasswd(model.getPasswd());

        //处理指令的细节
        dealParamData(demo1);

        instructManagementService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 重置密码
     *
     * @param editPasswordDto
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PutMapping(value = "/instructManagements/editPassword")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg editPassword(@RequestBody @Validated({GroupUpdate.class}) EditPasswordDto editPasswordDto) {

        InstructManagementModel model = instructManagementService.get(editPasswordDto.getId());

//        if (!model.getPasswd().equals(SecurityUtil.getMd5(editPasswordDto.getOriginPassword()))) {
//            throw new BusinessException("原密码错误");
//        }
        model.setPasswd(SecurityUtil.getMd5(editPasswordDto.getNewPassword()));

        instructManagementService.update(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 处理指令的细节
     *
     * @param demo1
     */
    private void dealParamData(InstructManagementModel demo1) {
        //处理指令的细节
        if (demo1 != null && demo1.getParamData() != null && demo1.getParamData().length() > 2) {
            String s = demo1.getParamData().substring(0, 2);
            if (s.equals("0X") || s.equals("0x")) {
                demo1.setParamData(demo1.getParamData().substring(2, demo1.getParamData().length()));
            }
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象（密码参数单独rsa加密，然后urlencode）")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/instructManagements/{id}/{password}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    @SneakyThrows
    public ResultMsg delete(@PathVariable String id, @PathVariable String password) {

        String passwordUrl = URLDecoder.decode(password);

        String decryptPassword = RSAUtil.decryptByPrivateKey(passwordUrl, rsaPriKey);

        checkPassword(new CheckPasswordDto(id, decryptPassword));

        int count = instructManagementService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));

    }

    /**
     * 校验密码
     *
     * @param
     * @return
     */
    @ApiOperation(value = "校验密码", notes = "校验密码")
    @PostMapping(value = "/instructManagements/checkPassword")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg checkPassword(@RequestBody CheckPasswordDto checkPasswordDto) {

        return ResultMsg.getResult(instructManagementService.checkPassword(checkPasswordDto));
    }

    /**
     * 校验下发的指令是否需要密码
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "校验下发的指令是否需要密码", notes = "校验下发的指令是否需要密码，id为指令的id")
    @GetMapping(value = "/instructManagements/needPassword/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg needPassword(@PathVariable String id) {

        InstructManagementModel managementModel = instructManagementService.get(id);
        return ResultMsg.getResult(null != managementModel && StringUtils.isNotEmpty(managementModel.getPasswd()));

    }

    /**
     * 导出
     *
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出数据，传入参数应和分页查询保持一致")
    @GetMapping(value = "/instructManagements/export/{param}")
    @RequiresPermissions(AUTH_EXPORT)
    @ApiImplicitParam(name = "params", value = "查询参数json字符串，用urlEncoder编码", required = true, dataType = "String", paramType = "path")
    public void export(@PathVariable String params) throws IOException {

        String d = URLDecoder.decode(params);
        PagerInfo info = new ObjectMapper().readValue(d, PagerInfo.class);

        instructManagementService.export(info);
        return;

    }


    /**
     * 批量导入
     *
     * @return
     */
    @ApiOperation(value = "批量导入 ", notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/instructManagements/batchImport")
    @RequiresPermissions(AUTH_BATCH_IMPORT)
    @ResponseBody
    public ResultMsg batchImport(@RequestParam("file") MultipartFile file) {

        instructManagementService.batchImport(file);
        return ResultMsg.getResult(I18nUtil.t("common.importTip"));

    }

    /**
     * 批量更新
     *
     * @return
     */
    @ApiOperation(value = "批量更新 ", notes = "通过excel，批量更新数据，支持xls、xlsx")
    @PutMapping(value = "/instructManagements/batchUpdate")
    @RequiresPermissions(AUTH_BATCH_UPDATE)
    @ResponseBody
    public ResultMsg batchUpdate(@RequestParam("file") MultipartFile file) {

        instructManagementService.batchUpdate(file);
        return ResultMsg.getResult(I18nUtil.t("common.batchUpdateTip"));

    }


}
