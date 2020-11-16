package com.bitnei.cloud.compent.kafka.domain;

import com.bitnei.cloud.sys.util.DateUtil;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenp on 2015-09-14.
 */
@ToString
public class PlatformPojo implements Serializable {

    //命令前缀
    private String prefix;
    //序列号
    private int seqNo;
    //VIN
    private String vin;
    //命令
    private String cmd;
    //返回时间
    private String recvTime;
    //源
    private String message;

    //数据
    private Map<String,String> data = new HashMap<String,String>();

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public static PlatformPojo parse(String message){
        PlatformPojo pojo = new PlatformPojo();
        String[] spArr = message.split(" ");
        pojo.setPrefix(spArr[0].trim());
        if (null != spArr[1] && !org.springframework.util.StringUtils.isEmpty(spArr[1].toString())) {
            pojo.setSeqNo(Integer.valueOf(spArr[1]).intValue());
        }
        pojo.setVin(spArr[2].trim());
        pojo.setCmd(spArr[3].trim());
        pojo.setRecvTime(DateUtil.formatTime(new Date(), DateUtil.FULL_ST_FORMAT));

        //开始解析data
        String dstr = spArr[4];
        String result = dstr.replace("{","").replace("}","");

        String[] dataItems = result.split(",");
        for (int i=0;i<dataItems.length;i++){
            String item = dataItems[i];
            if(!StringUtils.isEmpty(item)){
                String[] mp = item.split(":");
                pojo.getData().put(mp[0].trim(),mp[1].trim());
            }
        }

        return pojo;


    }

    public static PlatformPojo translate(String message){
        PlatformPojo pojo = new PlatformPojo();

        String[] dataItems = message.split(",");
       Arrays.stream(dataItems).filter(StringUtils::isNotEmpty).forEach(t->{
         String[] mp=  t.split(":");
         if (mp[0].equals("TYPE")){
             pojo.setCmd(mp[1]);
         }
         if (mp[0].equals("VID")){
             pojo.setVin(mp[1]);
         }
           if (mp[0].equals("TIME")){
               pojo.setRecvTime( mp[1]);
           }
            if (mp.length>1&& org.apache.commons.lang3.StringUtils.isNotBlank(mp[1])){
                pojo.getData().put(mp[0].trim(),mp[1].trim());
            }

       });


        return pojo;


    }

}
