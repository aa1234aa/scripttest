package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.api.ResultMsg;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.DataAccessKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.domain.UppackageInfo;
import com.bitnei.cloud.sys.enums.UpgradeLogAction;
import com.bitnei.cloud.sys.model.CheckPasswordDto;
import com.bitnei.cloud.sys.model.UpgradeLogModel;
import com.bitnei.cloud.sys.model.UppackageInfoDto;
import com.bitnei.cloud.sys.model.UppackageInfoModel;
import com.bitnei.cloud.sys.service.IUpgradeLogService;
import com.bitnei.cloud.sys.service.IUppackageInfoService;
import com.bitnei.cloud.sys.util.AFTPUtils;
import com.bitnei.cloud.sys.util.DateUtil;
import com.bitnei.cloud.sys.util.SecurityUtil;
import com.bitnei.commons.util.MapperUtil;
import com.bitnei.commons.util.UtilHelper;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UppackageInfoService实现<br>
 * 描述： UppackageInfoService实现<br>
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
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.mapper.UppackageInfoMapper")
@RequiredArgsConstructor
public class UppackageInfoService extends BaseService implements IUppackageInfoService {

    private final AFTPUtils ftpUtils;

    private final IUpgradeLogService upgradeLogService;

    @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null || pagerInfo.getLimit() < 0) {

            List<UppackageInfo> entries = findBySqlId("pagerModel", params);
            List<UppackageInfoDto> models = new ArrayList();
            for (UppackageInfo entry : entries) {
                UppackageInfo obj = (UppackageInfo) entry;
                models.add(UppackageInfoModel.dtoFromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<UppackageInfoDto> models = new ArrayList();
            for (Object entry : pr.getData()) {
                UppackageInfo obj = (UppackageInfo) entry;
                models.add(UppackageInfoModel.dtoFromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }


    @Override
    public UppackageInfoModel get(String id) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");
        params.put("id", id);
        UppackageInfo entry = unique("findById", params);
        if (entry == null) {
            throw new BusinessException("升级包已不存在");
        }
        return UppackageInfoModel.fromEntry(entry);
    }


    @Override
    public void insert(UppackageInfoModel model) {

        UppackageInfo obj = new UppackageInfo();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT")) {
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0) {
            throw new BusinessException("新增失败");
        }

        upgradeLogService.save(UpgradeLogAction.ADD_PACK.getValue(),
                model.getFileName(), "", model.getDescribes(), "");
    }

    @Override
    public void update(UppackageInfoModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");

        UppackageInfo obj = new UppackageInfo();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        upgradeLogService.save(UpgradeLogAction.EDIT_PACK.getValue(),
                model.getFileName(), "", model.getDescribes(), "");
    }

    @Override
    public void editPassword(UppackageInfoModel model) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");

        UppackageInfo obj = new UppackageInfo();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0) {
            throw new BusinessException("更新失败");
        }

        upgradeLogService.save(UpgradeLogAction.EDIT_PASSWORD.getValue(),
                model.getFileName(), "", model.getDescribes(), "");
    }

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id : arr) {

            UppackageInfoModel model = get(id);

            params.put("id", id);
            int r = super.deleteByMap(params);
            count += r;

            upgradeLogService.save(UpgradeLogAction.DEL_PACK.getValue(),
                    model.getFileName(), "", "升级包管理删除升级包", "");

            try {
                FTPClient ftpClient = ftpUtils.connect();
                ftpUtils.delete(ftpClient, model.getPath());
            } catch (Exception e) {
                //删除失败也不影响主流程
                log.error("删除升级包失败，升级包名称:{}, 路径:{}", model.getFileName(), model.getPath());
            }
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

        //获取权限sql
        Map<String, Object> params = DataAccessKit.getAuthMap("sys_uppackage_info", "upi");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<UppackageInfo>(this, "pagerModel", params, "sys/res/uppackageInfo/export.xls", "升级包信息") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "UPPACKAGEINFO" + WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<UppackageInfoModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageInfoModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageInfoModel model) {
                insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "UPPACKAGEINFO" + WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<UppackageInfoModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(UppackageInfoModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(UppackageInfoModel model) {
                update(model);
            }
        }.work();

    }

    /**
     * 上传文件到ftp服务器
     *
     * @param result
     * @param file     本地文件
     * @param userName 当前用户
     * @return
     */
    public Map upload(Map<String, Object> result, MultipartFile file, String userName) {
        try {
            FTPClient ftpClient = ftpUtils.connect();
            log.info("FTP客户端初始化状态ftpClient=" + ftpClient);
            if (ftpClient == null) {
                log.info("ftpClient连接失败");
                result.put("status", 2);
                return result;
            }
            //拼接路径
            String date = DateUtil.getKafkaDataSyncTime();
            String path = this.getPath(userName, date.substring(0, 8), date.substring(8, 10), date.substring(10, 12), date.substring(12));

            String fileName = file.getOriginalFilename();
            Boolean flag = ftpUtils.upload(ftpClient, file.getInputStream(), path, fileName);
            log.info("文件上传状态flag=" + flag);
            Boolean flag1 = ftpUtils.closeConnect(ftpClient);
            log.info("FTP关闭状态flag1=" + flag1);
            if (!(flag && flag1)) {
                result.put("status", 1);
                //上传到FTP服务器之后删除本地文件
                return result;
            }
            result.put("path", path + fileName);
            log.info("文件上传成功，result=" + result);
            return result;
        } catch (Exception e) {
           log.error("error", e);
            log.info("上传失败");
            result.put("status", 2);
            return result;
        }
    }

    public Map upload(Map<String, Object> result, File file, String userName) {
        FTPClient ftpClient = ftpUtils.connect();
        log.info("FTP客户端初始化状态ftpClient=" + ftpClient);
        if (ftpClient == null) {
            log.info("ftpClient连接失败");
            result.put("status", 2);
            //上传到FTP服务器之后删除本地文件
            file.delete();
            return result;
        }
        //拼接路径
        String date = DateUtil.getKafkaDataSyncTime();
        String path = this.getPath(userName, date.substring(0, 8), date.substring(8, 10), date.substring(10, 12), date.substring(12));

        String fileName = file.getName();

        log.info("各个文件路径localFilePath=" + file.getPath() + "   path=" + path + "   fileName=" + fileName);
        Boolean flag = ftpUtils.upload(ftpClient, file.getPath(), path, fileName);
        log.info("文件上传状态flag=" + flag);
        Boolean flag1 = ftpUtils.closeConnect(ftpClient);
        log.info("FTP关闭状态flag1=" + flag1);
        if (!(flag && flag1)) {
            result.put("status", 1);
            //上传到FTP服务器之后删除本地文件
            file.delete();
            return result;
        }
        log.info("文件上传成功，删除本地文件");
        //上传到FTP服务器之后删除本地文件
        file.delete();
//        result.put("path",path+fileName);
        //针对上海万象问题专用，去掉和文件名称相同的最后一级目录 add by renshuo
        result.put("path", path + fileName);
        log.info("文件上传成功，result=" + result);
        return result;
    }

    /**
     * 拼接ftp服务器父路径
     *
     * @param userName 用户名
     * @param date     年月日
     * @param hour     时
     * @param minute   分
     * @param second   秒
     * @return
     */
    private String getPath(String userName, String date, String hour, String minute, String second) {
        return SysDefine.FTP_RELATIVE_PATH.replace("{userName}", userName).replace("{date}", date).replace("{hour}", hour)
                .replace("{minute}", minute).replace("{second}", second);
    }

    public Boolean checkPassword(CheckPasswordDto checkPasswordDto) {

        UppackageInfoModel model;
        try {
            model = get(checkPasswordDto.getId());
        } catch (BusinessException e) {
            //如果对象不存在，说明升级包被删除了，不再校验密码
            return true;
        }

        if (!model.getPassword().equals(SecurityUtil.getMd5(checkPasswordDto.getPassword()))) {
            throw new BusinessException("密码错误");
        }

        return true;
    }
}
