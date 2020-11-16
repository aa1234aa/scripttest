package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.annotation.SLog;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresPermissions;
import com.bitnei.cloud.common.bean.CsrfInfo;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.RSAUtil;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.config.FtpConfig;
import com.bitnei.cloud.sys.dao.UserMapper;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.CheckPasswordDto;
import com.bitnei.cloud.sys.model.EditPasswordDto;
import com.bitnei.cloud.sys.model.UppackageInfoDto;
import com.bitnei.cloud.sys.model.UppackageInfoModel;
import com.bitnei.cloud.sys.service.IAfDbcService;
import com.bitnei.cloud.sys.service.IUppackageInfoService;
import com.bitnei.cloud.sys.util.AFTPUtils;
import com.bitnei.cloud.sys.util.FileDownload;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： 升级包信息<br>
 * 描述： 升级包信息控制器<br>
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
 * <td>2019-03-04 14:05:24</td>
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
@Api(value = "远程升级-升级包信息", description = "远程升级-升级包信息", tags = {"远程升级-升级包信息"})
@Controller
@RequestMapping(value = "/" + Version.VERSION_V1 + "/sys")
@RequiredArgsConstructor
public class UppackageInfoController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IUppackageInfoService uppackageInfoService;

    private final FtpConfig ftpConfig;

    private final IAfDbcService dbcService;

    private final UserMapper userMapper;

    private final AFTPUtils ftpUtils;

    @Value("${app.security.rsa.privateKey}")
    private String rsaPriKey;

    /**
     * 模块基础请求前缀
     **/
    public static final String BASE_AUTH_CODE = "UPPACKAGEINFO";
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


    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/uppackageInfos/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_DETAIL)
    public ResultMsg get(@PathVariable String id) {

        UppackageInfoModel uppackageInfo = uppackageInfoService.get(id);
        return ResultMsg.getResult(uppackageInfo);
    }


    /**
     * 多条件查询
     *
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "多条件查询，参数支持：fileName, type, beginTime, endTime, nickname")
    @PostMapping(value = "/uppackageInfos")
    @ResponseBody
    @RequiresPermissions(AUTH_LIST)
    public ResultMsg list(@RequestBody @Validated PagerInfo pagerInfo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }
        Object result = uppackageInfoService.list(pagerInfo);
        return ResultMsg.getResult(result);
    }


    /**
     * 新增升级包信息
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "新增升级包信息", notes = "新增升级包信息")
    @PostMapping(value = "/uppackageInfo")
    @ResponseBody
    @RequiresPermissions(AUTH_ADD)
    public ResultMsg add(@RequestBody @Validated({GroupInsert.class}) UppackageInfoModel demo1,
                         BindingResult bindingResult
//            , @RequestParam String dbcId
    ) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        ResultMsg resultMsg = validateParams(demo1);
        if (null != resultMsg) {
            return resultMsg;
        }

        String username = ServletUtil.getCurrentUser();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("username", username);
        User user = userMapper.findByUsername(queryMap);
        if (null == user) {
            throw new BusinessException("操作用户不存在");
        }

        //md5 password
        demo1.setPassword(SecurityUtil.getMd5(demo1.getPassword()));

        /*
        判断是否需要生成dbc文件
            如果dbcId不为空，则生成dbc文件，上传到服务器，为path赋值
         */
//        if (StringUtils.isNotEmpty(dbcId)) {
//            try {
//                String filePath = dbcService.createDBCFile(user.getId(), dbcId,
//                        demo1.getFileName(), demo1.getPath());
//                File file = new File(filePath);
//                Map<String, Object> result = new HashMap<String, Object>();
//                result = uppackageInfoService.upload(result, file, username);
//                String ftpPath = result.get("path") == null ? null : result.get("path").toString();
//                if (StringUtils.isEmpty(ftpPath)) {
//                    return ResultMsg.getResult(null, "获取dbc文件错误！", 300);
//                }
//                demo1.setPath(ftpPath);
//                demo1.setFileName(demo1.getFileName() + ".dbc");
//            } catch (IOException e) {
//               log.error("error", e);
//            }
//        }
        demo1.setCreateById(user.getId());
        demo1.setCreateBy(ServletUtil.getCurrentUser());
        demo1.setCreateTime(DateUtil.getNow());

        uppackageInfoService.insert(demo1);

        return ResultMsg.getResult(I18nUtil.t("common.addSuccess"));
    }

    private ResultMsg validateParams(UppackageInfoModel model) {

        if (StringUtils.isNotEmpty(model.getFirmwareVersion())) {
            if (model.getFirmwareVersion().length() > 32) {
                return ResultMsg.getResult(null, "固件版本号字符长度为0~32", -1);
            }
        }

        if (StringUtils.isNotEmpty(model.getExtVersion())) {
            if (model.getExtVersion().length() > 32) {
                return ResultMsg.getResult(null, "扩展版本号字符长度为0~32", -1);
            }
        }

        if (StringUtils.isNotEmpty(model.getHardwareVersion())) {
            if (model.getHardwareVersion().length() > 32) {
                return ResultMsg.getResult(null, "硬件版本号字符长度为0~32", -1);
            }
        }

        if (StringUtils.isNotEmpty(model.getDescribes())) {
            if (model.getDescribes().length() > 32 || model.getDescribes().length() < 2) {
                return ResultMsg.getResult(null, "备注字符长度为2~32", -1);
            }
        }
        return null;
    }

    /**
     * 更新
     *
     * @param demo1
     * @return
     */
    @ApiOperation(value = "更新", notes = "编辑更新信息（密码字段需要在前面校验输入框带过来）")
    @PutMapping(value = "/uppackageInfos/{id}")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg update(@RequestBody @Validated({GroupUpdate.class}) UppackageInfoModel demo1,
                            @PathVariable String id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResultMsg.getSchemaMsg(bindingResult);
        }

        ResultMsg resultMsg = validateParams(demo1);
        if (null != resultMsg) {
            return resultMsg;
        }

        UppackageInfoModel model = uppackageInfoService.get(demo1.getId());

        if (!model.getPassword().equals(demo1.getPassword())) {
            throw new BusinessException("密码错误");
        }

        demo1.setPassword(model.getPassword());

        uppackageInfoService.update(demo1);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 重置密码
     *
     * @param editPasswordDto
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PutMapping(value = "/uppackageInfos/editPassword")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg editPassword(@RequestBody @Validated({GroupUpdate.class}) EditPasswordDto editPasswordDto) {

        UppackageInfoModel model = uppackageInfoService.get(editPasswordDto.getId());

//        if (!model.getPassword().equals(SecurityUtil.getMd5(editPasswordDto.getOriginPassword()))) {
//            throw new BusinessException("原密码错误");
//        }
        model.setPassword(SecurityUtil.getMd5(editPasswordDto.getNewPassword()));

        uppackageInfoService.editPassword(model);
        return ResultMsg.getResult(I18nUtil.t("common.editSuccess"));
    }

    /**
     * 校验密码
     *
     * @param
     * @return
     */
    @ApiOperation(value = "校验密码", notes = "校验密码")
    @PostMapping(value = "/uppackageInfos/checkPassword")
    @ResponseBody
    @RequiresPermissions(AUTH_UPDATE)
    public ResultMsg checkPassword(@RequestBody CheckPasswordDto checkPasswordDto) {

        return ResultMsg.getResult(uppackageInfoService.checkPassword(checkPasswordDto));
    }

    /**
     * 删除
     *
     * @param id
     * @param password
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除一个或多个对象")
    @ApiImplicitParam(name = "id", value = "ID列表，多个用逗号分隔", required = true, dataType = "String",
            paramType = "path")
    @DeleteMapping(value = "/uppackageInfos/{id}/{password}")
    @ResponseBody
    @RequiresPermissions(AUTH_DELETE)
    @SLog(action = "删除")
    @SneakyThrows
    public ResultMsg delete(@PathVariable String id, @PathVariable String password) {

        String passwordUrl = URLDecoder.decode(password);

        String decryptPassword = RSAUtil.decryptByPrivateKey(passwordUrl, rsaPriKey);

        checkPassword(new CheckPasswordDto(id, decryptPassword));
        int count = uppackageInfoService.deleteMulti(id);
        return ResultMsg.getResult(I18nUtil.t("common.deleteSuccess", count));

    }

    /**
     * 文件上传，上传文件到ftp服务器
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "升级包上传到ftp服务器", notes = "升级包上传到ftp服务器" +
            "（0：文件上传成功、1：文件上传失败、2：ftp服务器未连接、3：文件名中含有中文字符）")
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("file") MultipartFile file) {
        logger.info("###########################文件上传，上传文件到ftp服务器START###########################");
        //先将文件保存到本地服务器
        logger.info("先将文件保存到本地服务器");
        Map<String, Object> result = new HashMap<>();
        //（0：文件上传成功、1：文件上传失败、2：ftp服务器未连接、3：文件名中含有中文字符）

        Boolean isCN = FileDownload.isCN(file);
        if (isCN) {
            logger.info("文件名中含有中文字符");
            result.put("status", 3);
        } else {
            String userName = ServletUtil.getCurrentUser();
            result = FileDownload.uploadFile(file, ftpConfig.getWebDownlodefilePath(), userName, "temp");

            if (StringUtils.isNotEmpty(result.get("list").toString())) {
                ServletUtil.getCurrentUser();
                //上传文件到ftp服务器
                logger.info("上传文件到ftp服务器");
                result = uppackageInfoService.upload(result, file, userName);
            } else {
                logger.info("文件保存到本地服务器失败");
                result.put("status", 1);
            }
        }
        logger.info("###########################文件上传，上传文件到ftp服务器END###########################");
        return result;
    }

    /**
     * 下载升级包
     *
     * @param params
     * @param response
     */
    @ApiOperation(value = "下载升级包", notes = "下载升级包，参数:id,password")
    @GetMapping("/download/{params}")
    @ResponseBody
    @SneakyThrows
    public void download(@PathVariable String params, HttpServletResponse response) {

        String d = URLDecoder.decode(params);
        CheckPasswordDto info = new ObjectMapper().readValue(d, CheckPasswordDto.class);

        UppackageInfoModel model = uppackageInfoService.get(info.getId());

        String decryptPassword = RSAUtil.decryptByPrivateKey(info.getPassword(), rsaPriKey);

        if (!model.getPassword().equals(SecurityUtil.getMd5(decryptPassword))) {
            throw new BusinessException("密码错误");
        }

        try {

            FTPClient ftpClient = ftpUtils.connect();
            if (ftpClient != null) {
                //获取ftp服务器相对路径
                String filePath = model.getPath();
                filePath = filePath.replaceAll("//", "/");
                log.info("filePath=" + filePath);
                // 设置response参数，可以打开下载页面
                response.reset();
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" +
                        new String((model.getFileName()).getBytes(), "iso-8859-1"));
                ServletOutputStream outputStream = response.getOutputStream();
                boolean b = ftpClient.retrieveFile(filePath, outputStream);
                log.info("升级包下是否成功： =》" + b);
                outputStream.close();
            }
        } catch (Exception e) {
           log.error("error", e);
            log.error(e.getMessage());
        }

    }

}
