package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.DateUtil;
import com.bitnei.cloud.common.util.ResponseStreamWriter;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.screen.protocol.StringUtil;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.domain.UploadFile;
import com.bitnei.cloud.sys.service.IUploadFileService;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UploadFileService实现<br>
* 描述： UploadFileService实现<br>
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
@Service
@Slf4j
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.UploadFileMapper" )
public class UploadFileService extends BaseService implements IUploadFileService {

    public static final Map<String,String> imageContentType = new HashMap<>();



    static {
        imageContentType.put("jpg","image/jpeg");
        imageContentType.put("jpeg","image/jpeg");
        imageContentType.put("png","image/png");
        imageContentType.put("tif","image/tiff");
        imageContentType.put("tiff","image/tiff");
        imageContentType.put("ico","image/x-icon");
        imageContentType.put("bmp","image/bmp");
        imageContentType.put("gif","image/gif");
        imageContentType.put("pdf","application/pdf");
        imageContentType.put("zip","application/zip");
        imageContentType.put("rar","application/rar");

    }


    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file , 1);
    }

    @Override
    public String uploadFile(MultipartFile file, int saveType) {

        try {
            UploadFile uploadFile = new UploadFile();
            uploadFile.setName(file.getOriginalFilename());
            uploadFile.setType(saveType);
            //设置后缀名
            String fileName = file.getOriginalFilename();
            String[] tmp = fileName.split("\\.");
            if (tmp.length != 1){
                uploadFile.setExtension(tmp[tmp.length-1]);
            }
            //设置文件大小，单位kB
            uploadFile.setFileSize((int)(file.getSize()/1024));
            //设置上传时间
            uploadFile.setCreateTime(DateUtil.getNow());
            //设置上传人
            uploadFile.setCreateBy(ServletUtil.getCurrentUser());

//            Blob content = new SerialBlob(file.getBytes());
            uploadFile.setContent(file.getBytes());
            String id = UtilHelper.getUUID();
            uploadFile.setId(id);

            super.insert(uploadFile);
            return id;

        }

        catch (IOException e) {
           log.error("error", e);
            throw new BusinessException("IO操作失败");
        }


    }

    @Override
    public String uploadFile(byte[] file, String fileName, int saveType) {

            UploadFile uploadFile = new UploadFile();
            uploadFile.setName(fileName);
            uploadFile.setType(saveType);
            String[] tmp = fileName.split("\\.");
            if (tmp.length != 1){
                uploadFile.setExtension(tmp[tmp.length-1]);
            }
            //设置文件大小，单位kB
            uploadFile.setFileSize(file.length/1024);
            //设置上传时间
            uploadFile.setCreateTime(DateUtil.getNow());
            //设置上传人
            uploadFile.setCreateBy(ServletUtil.getCurrentUser());

            uploadFile.setContent(file);
            String id = UtilHelper.getUUID();
            uploadFile.setId(id);

            super.insert(uploadFile);
            return id;

    }

    @Override
    public UploadFile get(String id) {
        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_upload_file", "uf");
        params.put("id",id);
        return unique("findById", params);
    }

    @Override
    public String viewImage(String id) {
        try {
            //获取当权限的map
            Map<String,Object> params = DataAccessKit.getAuthMap("sys_upload_file", "uf");
            params.put("id",id);
            UploadFile entry = unique("findById", params);
            String base64Code = String.format("data:image/%s;base64,", entry.getExtension().toLowerCase());
            base64Code += Base64.encodeToString(entry.getContent());
            return base64Code;
//            response.setContentType(imageContentType.getOrDefault(entry.getExtension().toLowerCase(), "image/jpeg"));
//            response.getOutputStream().write(entry.getContent());
//            response.getOutputStream().flush();
        }
        catch (Exception e) {
            log.error("error",e);
            throw new BusinessException("文件找不到");
        }

    }

    @Override
    public void download(String id, HttpServletResponse response) {

        Map<String,Object> params = DataAccessKit.getAuthMap("sys_upload_file", "uf");
        params.put("id", id);
        UploadFile entry = unique("findById", params);

        try {
            response.setContentLength(entry.getContent().length);
            response.setContentType(imageContentType.getOrDefault(
                    entry.getExtension().toLowerCase(), "image/jpeg"));
            response.addHeader("Content-Disposition",
                    new String(String.format("inline; filename=%s",entry.getName()).getBytes(),"ISO8859-1"));
            OutputStream toClient = response.getOutputStream();
            toClient.write(entry.getContent());
            toClient.flush();
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }

    }

    /**
     * 获取上传导入查询文件，解析并缓存，返回md5文件码
     *
     * @param file
     * @return
     */
    @Override
    public String getImportSearchMd5(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith("xls") && !file.getOriginalFilename().endsWith("xlsx")){
            throw new BusinessException("文件后续名应为xls或xlsx");
        }
        long maxFileSize = (long)1024*1024*5;
        if (file.getSize() > maxFileSize){
            throw new BusinessException("文件大小应限制在5M以内！");
        }
        try {
            String md5 = ServletUtil.getImportSearchMd5(file.getInputStream());
            if (StringUtil.isEmpty(md5)){
                throw new BusinessException("文件内容解析失败");
            }
            return md5;
        }
        catch (Exception e ){
            log.warn("warn", e);
            throw new BusinessException("文件内容解析失败");
        }


    }

    /**
     * 获取错误记录表格
     *
     * @param messageType
     */
    @Override
    public void failRecordFile(String messageType) {

        File file = ExcelBatchHandler.getFailureFile(messageType);
        if (file == null){
            throw new BusinessException("文件未生成或已失效");
        }
        ResponseStreamWriter.flushFile(file, "错误数据表.xls", ResponseStreamWriter.MimeEnum.XLS);
    }


}
