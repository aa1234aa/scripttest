package com.bitnei.cloud.sys.util;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2017/8/7.
 */
@Slf4j
public class FileDownload {

    private static final Logger logger = Logger.getLogger(FileDownload.class);

    /**
     * @param response
     * @param filePath //文件完整路径(包括文件名和扩展名)
     * @param fileName //下载后看到的文件名
     * @return 文件名
     */
    public static boolean fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception {
        File file = new File(filePath + File.separator + fileName);
        if (file.exists()) {
            byte[] data = FileUtilDown.toByteArray(filePath + File.separator + fileName);
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream;charset=UTF-8");
            @Cleanup OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            response.flushBuffer();
            return true;
        } else {
            return false;
        }


    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public static Boolean isCN(MultipartFile file) {
        if (file != null) {
            return CharUtil.isChinese(file.getOriginalFilename());
        }
        return false;
    }

    /**
     * 文件上传
     *
     * @param request
     * @param filePath
     * @return
     */
    public static Map<String, Object> uploadFile(HttpServletRequest request, String filePath, String id) {
        Map<String, Object> result = new HashMap<>();
     /*   logger.debug("=======》    文件上传开始  ");
        logger.debug("=======》   文件上传传入路径：" +filePath);*/

        try {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

            Object name = null;
            Object filepath = null;
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file = multiRequest.getFile(iter.next().toString());

                    if (file != null) {
                        String timePath = DateUtil.formatTime(new Date(), DateUtil.DAY_FORMAT);
                        //  logger.debug("=======》 timePath：" +timePath);
                        String tempPath = File.separator + "temp" + File.separator + "report" + File.separator + timePath + File.separator + id + File.separator;
                        // logger.debug("=======》 timePath：" +tempPath);
                        String path = filePath + tempPath;
                        // logger.debug("=======》 拼接后的文件存储路径：" +path);
                        //String path=tempPath;
                        path += file.getOriginalFilename();
                        //上传
                        File file1 = new File(path);
                        file1.setWritable(true);
                        file1.setReadable(true);


                        if (!file1.exists()) {
                            // logger.debug("=======》 文件路径不存在则创建：" +path);
                            file1.mkdirs();
                            // logger.debug("=======》创建成功");
                        }

                        file.transferTo(file1);
                        name = file.getOriginalFilename();
                        filepath = tempPath + file.getOriginalFilename();
                    }
                }
                result.put("status", "0");
            }
            result.put("fileName", name);
            result.put("filePath", filepath);
        } catch (Exception e) {
            logger.debug("=======》   文件上传错误：" + e.getMessage());
           log.error("error", e);
        }
        return result;
    }

    public static Map<String, Object> uploadFile(MultipartFile file, String filePath, String userName, String id) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if (file != null) {
                String timePath = DateUtil.formatTime(new Date(), DateUtil.DAY_FORMAT);
                String tempPath = File.separator + "temp" + File.separator + "report" + File.separator + timePath + File.separator + id + File.separator;
                String path = filePath + tempPath;
                path += file.getOriginalFilename();
                //path=filePath+"/temp/id/";
                String name = file.getOriginalFilename();
                String filepath = path;
                Map<String, Object> map = new HashMap<>();
                map.put("fileName", name);
                map.put("filePath", filepath);
                list.add(map);
            }
            result.put("status", "0");
            result.put("list", list);
        } catch (Exception e) {
           log.error("error", e);
        }
        return result;
    }
}
