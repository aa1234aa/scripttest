package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.UploadFile;
import com.bitnei.cloud.sys.model.UploadFileModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UploadFileService接口<br>
* 描述： UploadFileService接口，在xml中引用<br>
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

public interface IUploadFileService extends IBaseService {




    /**
     * 上传文件
     * @param  file 上传文件
     * @return
     */
    String uploadFile(MultipartFile file);

    /**
     * 上传文件
     * @param  file 上传文件
     * @param saveType 存储类型 1:数据库 2：本地 3：远程url
     * @return
     */
    String uploadFile(MultipartFile file, int saveType);

    /**
     * 上传文件
     * @param  file 上传文件
     * @param saveType 存储类型 1:数据库 2：本地 3：远程url
     * @return
     */
    String uploadFile(byte[] file, String fileName, int saveType);

    /**
     * 查看图片
     * @param id
     */
    String viewImage(String id);

    void download(String id, HttpServletResponse response);


    /**
     * 获取上传导入查询文件，解析并缓存，返回md5文件码
     * @param file
     * @return
     */
    String getImportSearchMd5(MultipartFile file);


    /**
     * 获取错误记录表格
     * @param messageType
     */
    void failRecordFile(String messageType);

    UploadFile get(String id);
}
