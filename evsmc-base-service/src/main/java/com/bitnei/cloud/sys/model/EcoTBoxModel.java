package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.TermModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 车载终端型号Model, openService使用
 * @author zhouxianzhou
 */
@Setter
@Getter
public class EcoTBoxModel extends BaseModel {

    private String id;

    /** 终端型号 **/
    private String tboxModel;

    /** 终端生产企业机构代码 **/
    private String tboxFactoryId;

    /** 终端厂商名称 **/
    private String tboxFactoryName;

    /** 终端厂商联系人姓名 **/
    private String contactorName;

    /** 终端厂商联系人电话 **/
    private String contactorPhone;

    /** 厂商执照图片 **/
    private String licenceImg;

    /** 厂商说明 **/
    private String tboxFactoryNote;

    /** 安全芯片型号 **/
    private String chipModel;

    /** 车载终端检测报告编号 **/
    private String reportId;

    /** 车载终端检测报告扫描件 **/
    private String reportImg;

    /** 车载终端其它文件 **/
    private String tboxFile;

    /** 车载终端说明 **/
    private String tboxExplain;

    /** 备注 **/
    private String remark;

    /**
     * 将实体转为前台model
     * @param entry TermModel
     * @return EcoTBoxModel
     */
    public static EcoTBoxModel fromEntry(TermModel entry) {
        EcoTBoxModel m = new EcoTBoxModel();
        m.setId(entry.getId());
        m.setTboxModel(entry.getTermModelName());
        m.setTboxFactoryId(ts(entry.get("organizationCode")));
        m.setTboxFactoryName(ts(entry.get("unitName")));
        m.setContactorName(ts(entry.get("contactorName")));
        m.setContactorPhone(ts(entry.get("contactorPhone")));
        m.setLicenceImg(ts(entry.get("licenceImgId")));
        m.setChipModel(ts(entry.get("chipModel")));
        m.setReportId(entry.getTermDetectionNo());
        m.setReportImg(entry.getDetectionReportImgId());
        m.setTboxFile(ts(entry.get("otherFile")));
        m.setTboxExplain(ts(entry.get("termDescription")));
        return m;
    }

    private static String ts(Object obj){
        return obj == null ? null : obj.toString();
    }

}
