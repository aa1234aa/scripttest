package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.config.FtpConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Apache封装的ftp工具，本系统使用这个
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AFTPUtils {
    private static final Logger logger = Logger.getLogger(AFTPUtils.class);
    /**
     * 本地字符编码
     */
    private static String LOCAL_CHARSET = "GBK";

    // FTP协议里面，规定文件名编码为iso-8859-1
    private static String SERVER_CHARSET = "ISO-8859-1";

    private final FtpConfig ftpConfig;

    @Value("${ftp.activity:ftp}")
    private String activity;

    public FTPClient connect() {
        FTPClient ftp;
        if ("ftps".equals(activity)){
            ftp = new FTPSClient(false);
        }else {
            ftp = new FTPClient();
        }
        String addr = ftpConfig.getHost();
        Integer port = ftpConfig.getPort();
        String username = ftpConfig.getUsername();
        String password = ftpConfig.getPassword();
        String path = ftpConfig.getPath();
        Integer ftpMod = ftpConfig.getMod();
//        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        try {
            ftp.connect(addr, port);

            if ("ftps".equals(activity)){
                // Set data channel protection to private
                ((FTPSClient) ftp).execPROT("P");
            }
            ftp.login(username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftp.changeWorkingDirectory(path);
            if (ftpMod == 1) {
                // 设置PassiveMode传输为被动模式
                ftp.enterLocalPassiveMode();
            }
        } catch (Exception e) {
            return null;
        }
        return ftp;
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param ftpClient
     * @param directory      ftp服务器相对路径目录
     * @param remoteFileName 文件名称
     * @return
     */
    public Boolean upload(FTPClient ftpClient, InputStream is, String directory, String remoteFileName) {
        try {
            // 设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //
            // 对远程目录的处理
            String filePath = directory + remoteFileName;
            if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(directory)) {
                // 如果远程目录不存在，则递归创建远程服务器目录
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                while (true) {
                    String subDirectory = filePath.substring(start, end);
                    if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                        if (ftpClient.makeDirectory(subDirectory)) {
                            ftpClient.changeWorkingDirectory(subDirectory);
                        } else {
                            return false;
                        }
                    }
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }

            if (ftpClient.storeFile(new String(remoteFileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET), is)) {
                is.close();
                return true;
            } else {
                is.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param ftpClient
     * @param local          本地文件路径
     * @param directory      ftp服务器相对路径目录
     * @param remoteFileName 文件名称
     * @return
     */
    public Boolean upload(FTPClient ftpClient, String local, String directory, String remoteFileName) {
        try {
            InputStream is = new FileInputStream(local);
            return upload(ftpClient, is, directory, remoteFileName);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从FTP服务器上下载文件
     *
     * @param ftpClient
     * @param fileName  文件名
     * @param remote    文件路径
     * @param response
     * @return
     * @throws IOException
     */

    public Boolean download(FTPClient ftpClient, String fileName, String remote, HttpServletResponse response) throws IOException {
        // 开启输出流弹出文件保存路径选择窗口
        response.setContentType("application/octet-stream");

        response.setContentType("application/OCTET-STREAM;charset=UTF-8");

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        OutputStream out = response.getOutputStream();

        boolean status = ftpClient.retrieveFile(remote, out);

        out.close();

        return status;
    }


    /**
     * 删除远程FTP文件
     *
     * @param remote 远程文件路径
     * @return
     */
    public Boolean delete(FTPClient ftpClient, String remote) {
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filePath = ftpConfig.getWebDownlodefilePath() + remote;
            filePath = filePath.replaceAll("//", "/");
            logger.info("准备删除文件路径filePath=" + filePath);
            FTPFile[] files = ftpClient.listFiles(filePath);
            logger.info("该文件的存在状态files.length=" + files.length);
            if (files.length == 1) {
                Boolean flag = ftpClient.deleteFile(filePath);
                logger.info("文件的删除状态 flag = " + flag);
            }
            return true;
        } catch (Exception e) {
           log.error("error", e);
            return false;
        }
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     */
    private Boolean isDirExist(FTPClient ftpClient, String dir) {
        try {
            // 检查远程是否存在文件
            FTPFile[] files = ftpClient.listFiles(dir);
            if (files.length == 1) {
                long remoteSize = files[0].getSize();
                File f = new File(dir);
                long localSize = f.length();
                if (remoteSize == localSize) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 断开与远程服务器的连接
     *
     * @throws IOException
     */
    public Boolean closeConnect(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传文件到ftp服务器
     *
     * @param is
     * @param fileName
     * @param userName 当前用户
     * @return
     */
    public String upload(InputStream is, String fileName, String userName) {
        try {
            FTPClient ftpClient = connect();
            log.info("FTP客户端初始化状态ftpClient=" + ftpClient);
            if (ftpClient == null) {
                log.info("ftpClient连接失败");
                return null;
            }
            //拼接路径
            String date = DateUtil.getKafkaDataSyncTime();
            String path = this.getPath(userName, date.substring(0, 8), date.substring(8, 10), date.substring(10, 12), date.substring(12));

            Boolean flag = upload(ftpClient, is, path, fileName);
            log.info("文件上传状态flag=" + flag);
            Boolean flag1 = closeConnect(ftpClient);
            log.info("FTP关闭状态flag1=" + flag1);

            log.info("文件上传成功");
            return path + fileName;
        } catch (Exception e) {
           log.error("error", e);
            log.info("上传失败");
            return null;
        }
    }

    private String getPath(String userName, String date, String hour, String minute, String second) {
        return SysDefine.FTP_RELATIVE_PATH.replace("{userName}", userName).replace("{date}", date).replace("{hour}", hour)
                .replace("{minute}", minute).replace("{second}", second);
    }

/*    @Test {
    public static void main(String agrs[]) {
        AFTPUtils t = new AFTPUtils();
        FTPClient ftpClient = t.connect();
        String remoteName = "/admin/20180118/17/14/39/梳理原型问题.xlsx";
        t.delete(ftpClient, remoteName);
    }*/
}