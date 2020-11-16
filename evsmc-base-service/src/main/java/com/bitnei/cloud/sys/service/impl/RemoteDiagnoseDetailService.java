package com.bitnei.cloud.sys.service.impl;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.common.bean.WsMessage;
import com.bitnei.cloud.common.bean.WsMessageConst;
import com.bitnei.cloud.common.constant.Constants;
import com.bitnei.cloud.common.handler.ExcelBatchHandler;
import com.bitnei.cloud.common.handler.ExcelExportHandler;
import com.bitnei.cloud.common.util.*;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupExcelUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.RemoteDiagnoseDetail;
import com.bitnei.cloud.sys.model.RemoteDiagnoseDetailModel;
import com.bitnei.cloud.sys.service.IRemoteDiagnoseDetailService;
import com.bitnei.cloud.common.util.ServletUtil;
import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.common.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.support.RequestContext;
import com.bitnei.cloud.orm.annation.Mybatis;
import com.bitnei.cloud.service.impl.BaseService;
import com.bitnei.commons.datatables.PagerModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.commons.datatables.DataGridOptions;
import com.bitnei.commons.util.UtilHelper;
import org.springframework.stereotype.Service;
import com.bitnei.cloud.common.util.EasyExcel;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import com.bitnei.commons.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RemoteDiagnoseDetailService实现<br>
* 描述： RemoteDiagnoseDetailService实现<br>
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
* <td>2019-03-25 16:15:07</td>
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
@Slf4j
@Service
@Mybatis(namespace = "com.bitnei.cloud.sys.dao.RemoteDiagnoseDetailMapper" )
public class RemoteDiagnoseDetailService extends BaseService implements IRemoteDiagnoseDetailService {

   @Override
    public Object list(PagerInfo pagerInfo) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose_detail", "rdd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));
        //非分页查询
        if (pagerInfo.getLimit() == null ||  pagerInfo.getLimit()<0){

            List<RemoteDiagnoseDetail> entries = findBySqlId("pagerModel", params);
            List<RemoteDiagnoseDetailModel> models = new ArrayList();
            for(RemoteDiagnoseDetail entry: entries){
                RemoteDiagnoseDetail obj = (RemoteDiagnoseDetail)entry;
                models.add(RemoteDiagnoseDetailModel.fromEntry(obj));
            }
            return models;

        }
        //分页查询
        else {
            PagerResult pr = findPagerModel("pagerModel", params, pagerInfo.getStart(), pagerInfo.getLimit());

            List<RemoteDiagnoseDetailModel> models = new ArrayList();
            for(Object entry: pr.getData()){
                RemoteDiagnoseDetail obj = (RemoteDiagnoseDetail)entry;
                models.add(RemoteDiagnoseDetailModel.fromEntry(obj));
            }
            pr.setData(Collections.singletonList(models));
            return pr;
        }
    }



    @Override
    public RemoteDiagnoseDetailModel get(String id) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose_detail", "rdd");
        params.put("id",id);
        RemoteDiagnoseDetail entry = unique("findById", params);
        if (entry == null){
            throw new BusinessException("对象已不存在");
        }
        return RemoteDiagnoseDetailModel.fromEntry(entry);
    }




    @Override
    public void insert(RemoteDiagnoseDetailModel model) {

        RemoteDiagnoseDetail obj = new RemoteDiagnoseDetail();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public String insertReturnId(RemoteDiagnoseDetailModel model) {

        RemoteDiagnoseDetail obj = new RemoteDiagnoseDetail();
        BeanUtils.copyProperties(model, obj);
        //单元测试指定id，如果是单元测试，那么就不使用uuid
        String id = UtilHelper.getUUID();
        if (StringUtil.isNotEmpty(model.getId()) && model.getId().startsWith("UNIT") ){
            id = model.getId();
        }
        obj.setId(id);
        int res = super.insert(obj);
        if (res == 0 ){
            throw new BusinessException("新增失败");
        }
        return id;
    }

    @Override
    public void update(RemoteDiagnoseDetailModel model) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose_detail", "rdd");

        RemoteDiagnoseDetail obj = new RemoteDiagnoseDetail();
        BeanUtils.copyProperties(model, obj);
        params.putAll(MapperUtil.Object2Map(obj));
        int res = super.updateByMap(params);
        if (res == 0 ){
            throw new BusinessException("更新失败");
        }
    }

    /**
    * 删除多个
    * @param ids
    * @return
    */
    @Override
    public int deleteMulti(String ids) {

        //获取当权限的map
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose_detail", "rdd");

        String[] arr = ids.split(",");
        int count = 0;
        for (String id:arr){
            params.put("id",id);
            int r = super.deleteByMap(params);
            count+=r;
        }
        return count;
    }


    @Override
    public void export(PagerInfo pagerInfo) {

       //获取权限sql
        Map<String,Object> params = DataAccessKit.getAuthMap("sys_remote_diagnose_detail", "rdd");
        params.putAll(ServletUtil.pageInfoToMap(pagerInfo));

        new ExcelExportHandler<RemoteDiagnoseDetail>(this, "pagerModel", params, "sys/res/remoteDiagnoseDetail/export.xls", "远程诊断明细") {
        }.work();

        return;


    }


    @Override
    public void batchImport(MultipartFile file) {

        String messageType = "REMOTEDIAGNOSEDETAIL"+ WsMessageConst.BATCH_IMPORT_SUFFIX;

        new ExcelBatchHandler<RemoteDiagnoseDetailModel>(file, messageType, GroupExcelImport.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RemoteDiagnoseDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RemoteDiagnoseDetailModel model) {
            	insert(model);
            }
        }.work();

    }

    @Override
    public void batchUpdate(MultipartFile file) {


        String messageType = "REMOTEDIAGNOSEDETAIL"+ WsMessageConst.BATCH_UPDATE_SUFFIX;

        new ExcelBatchHandler<RemoteDiagnoseDetailModel>(file, messageType, GroupExcelUpdate.class) {

            /**
             * 复杂的校验，一般hibernate不能实现校验的，可以在这里进行处理
             *
             * @param model
             * @return
             */
            @Override
            public List<String> extendValidate(RemoteDiagnoseDetailModel model) {
                return null;
            }

            /**
             *  保存实体
             *
             * @param model
             * @return
             */
            @Override
            public void saveObject(RemoteDiagnoseDetailModel model) {
                update(model);
            }
        }.work();

    }

    public void expFaultDetailInfo(String title, Map<String, String> headMap,
                                   List<RemoteDiagnoseDetailModel> data, OutputStream out) {
        int errorNum = 0;//用于记录故障数
        int rightNum = 0;//用于记录正确数
        int exceptionNum = 0;//用于记录异常数
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        workbook.createInformationProperties();
        workbook.getDocumentSummaryInformation().setCompany("*****公司");
        SummaryInformation si = workbook.getSummaryInformation();
        si.setAuthor("*****");  //填加xls文件作者信息
        si.setApplicationName("*****"); //填加xls文件创建程序信息
        si.setComments("*****"); //填加xls文件作者信息
        si.setTitle(title); //填加xls文件标题信息
        si.setSubject(title);//填加文件主题信息
        si.setCreateDateTime(new Date());

        //表头样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        // 列头样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont cellFont = workbook.createFont();
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        HSSFSheet sheet = workbook.createSheet();
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString(title));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("*****");

        //设置各列的列宽
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 6000);
        sheet.setColumnWidth(8, 4000);
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size() + 1];
        String[] headers = new String[headMap.size() + 1];
        int ii = 1;
        headers[0] = "序号";
        for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext(); ) {
            String fieldName = iter.next();
            String fieldValue = headMap.get(fieldName);
            properties[ii] = fieldName;
            headers[ii] = fieldValue;
            ii++;
        }

        //制作表头
        HSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
        titleRow.createCell(0).setCellValue(title);
        titleRow.getCell(0).setCellStyle(titleStyle);
        //设置标题列合并单元格数
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size()));

        HSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);

        }

        // 遍历集合数据，产生数据行
        int rowIndex = 2;
        int numberCount = 1;
        for (int i = 0; i < data.size(); i++) {
            //获取故障详情信息
            String diagnoseDetailString = data.get(i).getResult();
            JSONArray diagnoseDetail = JSONArray.fromObject(diagnoseDetailString.replaceAll("=", ":"));
            int diagnoseDetailSize = diagnoseDetail.size();
            if (diagnoseDetailSize > 0) {
                diagnoseDetailSize--;
            }
            for (int s = 0, t = diagnoseDetail.size(); s < t; s++) {
                JSONObject jsonObject = JSONObject.fromObject(diagnoseDetail.get(s));
                if (jsonObject.getString("result").equals("0")) {
                    rightNum++;
                } else if (jsonObject.getString("result").equals("1")) {
                    errorNum++;
                } else {
                    exceptionNum++;
                }
            }
            //构造正常码信息的合并单元格
            CellRangeAddress numCell = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 0, 0);
            CellRangeAddress vin = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 1, 1);
            CellRangeAddress licensePlate = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 2, 2);
            CellRangeAddress diagnoseResult = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 3, 3);
            CellRangeAddress uploaTime = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 4, 4);
            CellRangeAddress errorNumCell = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 5, 5);
            CellRangeAddress rightNumCell = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 6, 6);
            CellRangeAddress exceptionNumCell = new CellRangeAddress(rowIndex, rowIndex + diagnoseDetailSize, 7, 7);
            //在sheet里增加合并单元格
            if (rowIndex != rowIndex + diagnoseDetailSize) {
                sheet.addMergedRegion(numCell);
                sheet.addMergedRegion(vin);
                sheet.addMergedRegion(licensePlate);
                sheet.addMergedRegion(diagnoseResult);
                sheet.addMergedRegion(uploaTime);
                sheet.addMergedRegion(errorNumCell);
                sheet.addMergedRegion(rightNumCell);
                sheet.addMergedRegion(exceptionNumCell);
            }
            HSSFRow dataRow = sheet.createRow(rowIndex);
//第1列id填入值
            HSSFCell numValueCell = dataRow.createCell(0);
            numValueCell.setCellValue(numberCount);
            numValueCell.setCellStyle(cellStyle);
//          第2列填入值
            HSSFCell vinValueCell = dataRow.createCell(1);
            vinValueCell.setCellValue(data.get(i).getVin() == null ? "" : data.get(i).getVin().toString());
            vinValueCell.setCellStyle(cellStyle);
            //第3列填入值
            HSSFCell licensePlateValueCell = dataRow.createCell(2);
            licensePlateValueCell.setCellValue(data.get(i).getLicensePlate() == null ? "" : data.get(i).getLicensePlate().toString());
            licensePlateValueCell.setCellStyle(cellStyle);
            //第4列填入值
            HSSFCell diagnoseResultValueCell = dataRow.createCell(3);
            String diagnoseResultstr = "";
            if (data.get(i).getFinalResultCode().equals("0")) {
                diagnoseResultstr = "正常";
            } else if (data.get(i).getFinalResultCode().equals("1")) {
                diagnoseResultstr = "故障";
            } else {
                diagnoseResultstr = "异常";
            }
            diagnoseResultValueCell.setCellValue(diagnoseResultstr);
            diagnoseResultValueCell.setCellStyle(cellStyle);
//第5列填入值
            HSSFCell uploaTimeValueCell = dataRow.createCell(4);
            uploaTimeValueCell.setCellValue(data.get(i).getFinalResultCode() == null ? "" : data.get(i).getUploadTime());
            uploaTimeValueCell.setCellStyle(cellStyle);
//第六列填入值
            HSSFCell errorNumValueCell = dataRow.createCell(5);
            errorNumValueCell.setCellValue(String.valueOf(errorNum));
            errorNumValueCell.setCellStyle(cellStyle);
//第七列填入值
            HSSFCell rightNumValueCell = dataRow.createCell(6);
            rightNumValueCell.setCellValue(String.valueOf(rightNum));
            rightNumValueCell.setCellStyle(cellStyle);

            HSSFCell exceptionNumValueCell = dataRow.createCell(7);
            exceptionNumValueCell.setCellValue(String.valueOf(exceptionNum));
            exceptionNumValueCell.setCellStyle(cellStyle);

            for (int m = 0, j = diagnoseDetail.size(); m < j; m++) {
                JSONObject diagnoseDetailObject = JSONObject.fromObject(diagnoseDetail.get(m));
                HSSFRow exceptionRow = null;
                if (m == 0) {
                    exceptionRow = dataRow;
                } else {
                    exceptionRow = sheet.createRow(rowIndex);
                }

                for (int k = 8; k < properties.length; k++) {
                    HSSFCell newCell = exceptionRow.createCell(k);
                    String cellValue = "";
                    Object o = null;
                    o = diagnoseDetailObject.getString(properties[k]);
                    if (o == null) {
                        cellValue = "无";
                    } else if (o.toString().equals("null")) {
                        cellValue = "无";
                    } else {
                        cellValue = o.toString();
                        switch (properties[k]) {
                            case "result":
                                if (cellValue.equals("0")) {
                                    cellValue = "正常";
                                } else if (cellValue.equals("1")) {
                                    cellValue = "故障";
                                } else {
                                    cellValue = "异常";
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    newCell.setCellValue(cellValue);
                    newCell.setCellStyle(cellStyle);
                }
                rowIndex++;
            }
            if (diagnoseDetail.size() == 0) {
                rowIndex++;
            }
            numberCount++;
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
           log.error("error", e);
        } finally {
            try {
                workbook.close();
            } catch (Throwable e) {
               log.error("error", e);
            }
        }
    }

}
