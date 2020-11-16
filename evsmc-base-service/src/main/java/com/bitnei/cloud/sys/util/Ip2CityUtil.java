package com.bitnei.cloud.sys.util;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrTokenizer;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author
 */
@Slf4j
public class Ip2CityUtil {

    public static final int singleIpMaxLength = 15;

    public static String getCityInfo(String ip){

        String dbPath = Ip2CityUtil.class.getResource("/ip2region.db").getPath();
        File file = new File(dbPath);
        if ( file.exists() == false ) {
            log.info("找不到ip2region静态库");
        }

        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);
            if (Util.isIpAddress(ip) == false ) {
                log.info("无效的ip地址");
            }
            DataBlock dataBlock  =  searcher.btreeSearch(ip);
            int cityId = dataBlock.getCityId();
            return String.valueOf(cityId);

        } catch (Exception e) {
           log.error("error", e);
        }
        return null;
    }



    /**
     * 获取访问者IP
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        //String ipFromNginx = request.getHeader("X-Real-IP");
        //return StringUtils.isEmpty(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if(StringUtil.isNotEmpty(ip))
        {
            if(ip.length()>singleIpMaxLength)
            {
                ip = ip.split(",")[0];
            }
        }
        return ip;

    }





}
