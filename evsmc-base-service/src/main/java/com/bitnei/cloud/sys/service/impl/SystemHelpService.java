package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.util.EasyExcel;
import com.bitnei.cloud.common.util.ImportDemo;
import com.bitnei.cloud.common.util.RedisKit;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.screen.protocol.StringUtil;
import com.bitnei.cloud.sys.dao.UserMapper;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.SoftwareVersion;
import com.bitnei.cloud.sys.service.ISystermHelpService;
import com.bitnei.cloud.sys.util.Ip2CityUtil;
import com.google.common.collect.Maps;
import com.youbenzi.mdtool.tool.MDTool;
import jodd.util.ClassLoaderUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-04-12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Service
@Slf4j
public class SystemHelpService implements ISystermHelpService {

    @Resource(name = "webRedisKit")
    private RedisKit redisKit;
    @Resource
    private UserMapper userMapper;
    /**
     * 获取系统版本信息
     *
     * @return
     */
    @Override
    public SoftwareVersion getVersion() {


        String fileName = "classpath:version.xls";
        if (ResourceUtils.isUrl(fileName)){
            try {
                @Cleanup InputStream stream = null;
                try {
                    stream = new FileInputStream("version.xls");
                }
                catch (Exception e){
                    //什么也不做
                }

                if (stream == null){
                    String srcFile = SystemHelpService.class.getResource("/version.xls").getPath();
                    log.info("版本文件路径:{}", srcFile);

                    if (srcFile.contains("!")) {
                        stream = ClassLoaderUtil.getResourceAsStream(fileName);
                    } else {
                        stream = new FileInputStream(srcFile);
                    }

                }

                List<SoftwareVersion> modes = EasyExcel.readExcel2Models(stream, 0, SoftwareVersion.class,0, 1);
                if (stream != null){
                    stream.close();
                }
                if (modes.size() > 0){
                    return modes.get(modes.size()-1);
                }
                else {
                    throw new BusinessException("版本信息为空");
                }

            }
            catch (Exception e){
                log.error("error", e);
                throw new BusinessException("版本信息文件解析失败");
            }
        }
        else {
            throw new BusinessException("版本信息文件不存在");
        }
    }

    /**
     * 获取详细的错误信息
     *
     * @param code
     * @return
     */
    @Override
    public void getErrorMessage(String code) {

        String message = redisKit.get(11, "exception."+code, "");
        StringBuffer sb = new StringBuffer();
        sb.append("```\n");
        sb.append(StringUtil.isEmpty(message)? "异常不存在": message);

        sb.append("```\n");

        //渲染成html
        String html = MDTool.markdown2Html(sb.toString());
        //输出到前端
        ServletUtil.getResponse().setContentType("text/html;charset=utf-8");
        try {
            PrintWriter p = ServletUtil.getResponse().getWriter();
            p.write(html);
            p.flush();
            p.close();
        } catch (IOException e) {
            log.error("error", e);
        }

    }

    /**
     * 检查状态
     */
    @Override
    public void health() {

        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put("username", "admin");
            userMapper.findByUsername(params);
        }
        catch (Exception e){
            throw new BusinessException("系统异常");
        }
    }
}
