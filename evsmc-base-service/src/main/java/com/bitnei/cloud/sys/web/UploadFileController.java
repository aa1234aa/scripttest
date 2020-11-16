package com.bitnei.cloud.sys.web;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.auth.annotation.RequiresAuthentication;
import com.bitnei.cloud.common.constant.Version;
import com.bitnei.cloud.common.util.I18nUtil;
import com.bitnei.cloud.sys.domain.UploadFile;
import com.bitnei.cloud.sys.service.IUploadFileService;
import com.bitnei.cloud.sys.service.impl.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jodd.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： 上传文件管理<br>
* 描述： 上传文件管理控制器<br>
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
* <td>2018-10-31 12:42:12</td>
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
@Api(value = "上传文件管理", description = "上传文件管理" , tags = {"上传文件管理"})
@Controller
@RequestMapping(value = "/"+ Version.VERSION_V1+"/sys")
public class UploadFileController{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private IUploadFileService uploadFileService;


    /**
    * 上传文件
    * @param file
    * @return
    */
    @ApiOperation(value = "上传文件 " ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/uploadFiles/upload")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg upload(@RequestParam("file") MultipartFile file){

        String id = uploadFileService.uploadFile(file);
        return ResultMsg.getResult(id,"文件上传成功", 200);
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传图片返回路径" ,notes = "上传图片返回路径")
    @PostMapping(value = "/uploadFiles/ue/upload")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg uploadReturnPath(@RequestParam("file") MultipartFile file){

        String fileName = file.getOriginalFilename();
        String[] tmp = fileName.split("\\.");
        if (tmp.length != 1){
            String extension = tmp[tmp.length-1];
            if (!UploadFileService.imageContentType.containsKey(extension.trim().toLowerCase())) {
                throw new BusinessException("仅支持以下图片格式：jpg、jpeg、png、tif、tiff、ico、bmp、gif");
            }
        }

        String id = uploadFileService.uploadFile(file);
        return ResultMsg.getResult("/v1/sys/uploadFiles/ue/download/" + id,"文件上传成功", 200);
    }

    /**
     * 下载文件流形式
     * @param id
     * @return
     */
    @ApiOperation(value = "下载文件流形式" ,notes = "下载文件流形式")
    @GetMapping(value = "/uploadFiles/ue/download/{id}")
    @ResponseBody
    public ResultMsg download(@PathVariable String id, HttpServletResponse response){

        if (id.contains(".")) {
            String[] idAndExtent = id.split("\\.");
            uploadFileService.download(idAndExtent[0], response);
            return ResultMsg.getResult("下载成功");
        }
        uploadFileService.download(id, response);
        return ResultMsg.getResult("下载成功");
    }


    /**
     * 上传文件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传导入查询接口" ,notes = "通过excel，批量导入数据，支持xls、xlsx")
    @PostMapping(value = "/uploadFiles/importSearchXls")
    @ResponseBody
    @RequiresAuthentication
    public ResultMsg importSearchXls(@RequestParam("file") MultipartFile file){

        String md5 = uploadFileService.getImportSearchMd5(file);
        return ResultMsg.getObjectResult(md5);
    }


    /**
     * 根据id获取对象
     * @return
     */
    @ApiOperation(value = "获取图片" , notes = "根据ID获取获取图片")
    @ApiImplicitParam(name = "id", value = "图片ID", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/uploadFiles/image/{id}")
    @RequiresAuthentication
    @ResponseBody
    public ResultMsg image(@PathVariable String id){
        String base64Code = uploadFileService.viewImage(id);
        Map<String,String> map = new HashMap<>();
        map.put("src", base64Code);
        return ResultMsg.getObjectResult(map);
    }


    /**
     * 根据id获取对象
     *
     * @return
     */
    @ApiOperation(value = "获取错误记录表格" , notes = "获取错误记录表格")
    @ApiImplicitParam(name = "messageType", value = "消息类型，同Websocket的type", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/uploadFiles/failRecordFile/{messageType}")
    @RequiresAuthentication
    public void failRecordFile(@PathVariable String messageType){
        uploadFileService.failRecordFile(messageType);
        return;
    }

    @ApiOperation(value = "根据ID获取文件信息" , notes = "根据ID获取文件信息, openService服务")
    @ApiImplicitParam(name = "id", value = "文件id", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/uploadFiles/file/{id}")
    @RequiresAuthentication
    @ResponseBody
    public ResultMsg getFile(@PathVariable String id) {
        UploadFile file = uploadFileService.get(id);
        Map<String,String> map = new HashMap<>(16);
        if(file != null) {
            // 文件后缀
            map.put("extension", file.getExtension());
            // 文件名
            map.put("name", file.getName());
            // 文件base64编码
            map.put("base64Code", Base64.encodeToString(file.getContent()));
        }
        return ResultMsg.getObjectResult(map);
    }

    @ApiOperation(value = "根据ID获取文件信息" , notes = "根据ID获取文件信息, openService服务")
    @ApiImplicitParam(name = "id", value = "文件id", required = true, dataType = "String",paramType = "path")
    @GetMapping(value = "/uploadFiles/fileName/{id}")
    @RequiresAuthentication
    @ResponseBody
    public ResultMsg getFileName(@PathVariable String id) {
        UploadFile file = uploadFileService.get(id);
        Map<String,String> map = new HashMap<>(16);
        if(file != null) {
            // 文件后缀
            map.put("extension", file.getExtension());
            // 文件名
            map.put("name", file.getName());

        }
        return ResultMsg.getObjectResult(map);
    }







}
