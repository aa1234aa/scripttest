package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.common.bean.ExcelData;
import com.bitnei.cloud.sys.common.SysDefine;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jodd.io.FileUtil;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * wtm excel 工具类
 * 提供 统一下载方法，及 读取组装表头及数据
 *
 * @author my
 * @date 2017‎年‎12‎月‎12‎日
 */
@Component
@Slf4j
public class ExcDownImpTemplatesUtils {

    private static final Logger logger = Logger.getLogger(ExcDownImpTemplatesUtils.class);


    /**
     * 提供 下载模版
     *
     * @param request
     * @param response
     */
    public static void downExcTemplates(HttpServletRequest request, HttpServletResponse response,
                                        String userName) throws Exception {
        request.setCharacterEncoding("UTF8");
        response.setCharacterEncoding("UTF8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/x-excel");

        String fileType = request.getParameter("fileType");
        String fileName = new String(request.getParameter("fileName")
                .getBytes("ISO-8859-1"), "utf8");

        logger.debug("===>fileName :" + fileName + "  ===>fileType: " + fileType);
        String dir = "";
        if (fileType.equals("1")) {  //  设定文件保存的目录 fileType : 1 外部路径文件 待定?  2、工程内路径
            dir = File.separator + "temp" + File.separator + "report" + File.separator + userName;
            fileName = fileName + ".xlsx";
        } else if (fileType.equals("2")) {
//            dir = request.getSession().getServletContext().getRealPath(File.separator+"temp");
            dir = request.getSession().getServletContext().getRealPath("/temp");
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            String fileNameAndPath = dir + File.separator + SysDefine.impFileTemplatePath + File.separator + fileName;
            System.out.println(fileNameAndPath);
            File f = new File(fileNameAndPath);
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    new String(fileName.getBytes("utf8"), "ISO-8859-1"));
            response.setHeader("Content-Length", String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            logger.debug("下载导入模版出错：===》" + e.getMessage());
           log.error("error", e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (!fileType.equals("2")) {
                File f = new File(dir + fileName);
                f.delete();
            }
        }
    }

    /**
     * 上传文件 存储并检验 文件是否合法
     *
     * @param request
     * @return
     */
    public static Map<String, Object> importDataExcel(HttpServletRequest request) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer message = new StringBuffer();
        Boolean flag = false;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (null == fileMap || fileMap.size() == 0) {
            message.append("请选择文件\n");
            map.put("message", message.toString());
            return map;
        }
        MultipartFile file = fileMap.get("file");
        if (null == file) {
            message.append("请选择文件\n");
            map.put("message", message.toString());
            return map;
        }
        String fileName = file.getOriginalFilename();
        String[] str = fileName.split("\\.");
        if (str.length > 0 && !str[str.length - 1].equals("xls")) {
            message.append("仅支持xls文件上传，请选择正确格式的excel文件\n");
            map.put("message", message.toString());
            return map;
        }
        fileName = DateUtil.getKafkaDataSyncTime() + fileName;
//        String dir = request.getSession().getServletContext().getRealPath(File.separator+"temp"+
//         File.separator+"vehDate"+File.separator);    //设定文件保存的目录
        String dir = "";
        FileUtils.writeByteArrayToFile(new File(dir, fileName), file.getBytes());
        flag = true;
        message.append("上传成功\n");
        message.append("开始数据库导入-----------------------------------\n");
        map.put("message", message.toString());
        map.put("dir", dir);
        map.put("fileName", fileName);
        map.put("flag", flag);
        return map;

    }


    /**
     * @param filePathAndName 文件名称 及 所在路径
     * @param sheetTotal      根据sheet 个数 返回 多值
     * @return
     */
    public static Map<String, Object> initHeaderAndDataToArrayBySheetNumber(String filePathAndName,
                                                                            int sheetTotal) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        InputStream inputStream = new FileInputStream(filePathAndName);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);

        DecimalFormat df = new DecimalFormat("0");//使用DecimalFormat类对科学计数法格式的数字进行格式化
        for (int i = 0; i < sheetTotal; i++) {
            HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
            int rowNum = sheet.getLastRowNum();//行数
            List<String> headerList = null;
            List<Map<String, String>> dataMapList = new ArrayList<Map<String, String>>();

            for (int j = 0; j < rowNum; j++) {
                HSSFRow row = sheet.getRow(i);//单元格数
                if (row != null) {
                    int rowLength = row.getLastCellNum();
                    if (i == 0) {
                        headerList = new ArrayList<String>();
                        for (int l = 0; l < rowLength; l++) {
                            headerList.add(row.getCell(l).getStringCellValue());
                        }
                    } else {
                        Map<String, String> dataMap = new HashMap<String, String>();
                        if (headerList == null){
                            continue;
                        }
                        rowLength = Math.min(rowLength, headerList.size());
                        for (int k = 0; k < rowLength; k++) {
                            if (row.getCell(k) != null) {
                                String value = "";
                                if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                    value = row.getCell(k).getStringCellValue();
                                } else if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                    value = df.format(row.getCell(k).getNumericCellValue());
                                }
                                dataMap.put(headerList.get(k), value);
                            } else {
                                dataMap.put(headerList.get(k), null);
                            }
                        }
                        dataMapList.add(dataMap);
                    }
                }
            }
            map.put("dataMapList" + (i + 1), dataMapList);
        }

        return map;
    }

    /**
     * 根据文件名称 组装表头及数据
     *
     * @param filePathAndName 文件名称 及 所在路径
     */
    public static Map<String, Object> initHeaderAndDataToArray(String filePathAndName) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        InputStream inputStream = new FileInputStream(filePathAndName);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);

        int rowNum = sheet.getLastRowNum();//行数
        List<String> headerList = new ArrayList<String>();
        List<Map<String, String>> dataMapList = new ArrayList<Map<String, String>>();
        DecimalFormat df = new DecimalFormat("0");//使用DecimalFormat类对科学计数法格式的数字进行格式化
        for (int i = 0; i <= rowNum; i++) {

            HSSFRow row = sheet.getRow(i);//单元格数
            if (row != null) {
                int rowLength = row.getLastCellNum();
                if (i == 0) {
                    for (int j = 0; j < rowLength; j++) {
                        headerList.add(row.getCell(j).getStringCellValue());
                    }
                    map.put("headerList", headerList);
                } else {
                    Map<String, String> dataMap = new HashMap<String, String>();
                    rowLength = Math.min(rowLength, headerList.size());
                    for (int k = 0; k < rowLength; k++) {
                        if (row.getCell(k) != null) {
                            String value = "";
                            if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                value = row.getCell(k).getStringCellValue();
                            } else if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                value = df.format(row.getCell(k).getNumericCellValue());
                            }
                            dataMap.put(headerList.get(k), value);
                        } else {
                            dataMap.put(headerList.get(k), null);
                        }
                    }
                    dataMapList.add(dataMap);
                }
            }
        }
        map.put("dataMapList", dataMapList);

        return map;
    }

    /**
     * 根据文件名称 组装表头及数据
     *
     * @param filePathAndName 文件名称 及 所在路径
     * @param headStartRow    自定义 读取 标题头 起始行      ( n-1   eg:  第三行为标题行,传入值 则为 2)
     * @param dataStartRow    自定义 读取  数据 起始行   ( n-1   eg:  第四行为数据行,传入值 则为 3)
     */
    public static Map<String, Object> initHeaderAndDataToArray(String filePathAndName, int headStartRow,
                                                               int dataStartRow) throws IOException {

        InputStream inputStream = new FileInputStream(filePathAndName);

        return initHeaderAndDataToArray(inputStream, headStartRow, dataStartRow);
    }

    public static Map<String, Object> initHeaderAndDataToArray(InputStream inputStream, int headStartRow,
                                                               int dataStartRow) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);

        int rowNum = sheet.getLastRowNum();//行数
        List<String> headerList = new ArrayList<String>();
        List<Map<String, String>> dataMapList = new ArrayList<Map<String, String>>();
        DecimalFormat df = new DecimalFormat("0");//使用DecimalFormat类对科学计数法格式的数字进行格式化

        for (int i = headStartRow; i <= rowNum; i++) {

            HSSFRow row = sheet.getRow(i);//单元格数
            if (row != null) {
                int rowLength = row.getLastCellNum();
                if (i == headStartRow) {
                    for (int j = 0; j < rowLength; j++) {
                        headerList.add(row.getCell(j).getStringCellValue());
                    }
                    map.put("headerList", headerList);
                    i = dataStartRow - 1;
                } else {
                    Map<String, String> dataMap = new HashMap<String, String>();
                    rowLength = Math.min(rowLength, headerList.size());
                    for (int k = 0; k < rowLength; k++) {
                        if (row.getCell(k) != null) {
                            String value = "";
                            if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                value = row.getCell(k).getStringCellValue();
                            } else if (row.getCell(k).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                value = df.format(row.getCell(k).getNumericCellValue());
                            }
                            dataMap.put(headerList.get(k), value);
                        } else {
                            dataMap.put(headerList.get(k), null);
                        }
                    }
                    dataMapList.add(dataMap);
                }
            }
        }
        map.put("dataMapList", dataMapList);

        return map;
    }


    public static File createExcelResultFile(ExcelData ed, int pagSize, int startIndex, Template template,
                                             String srcFile) throws IOException, TemplateException {
        InputStream stream = getInputStream(srcFile);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(stream);
        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int fieldRowIndex = 1;
        HSSFRow fieldRow = sheet.getRow(fieldRowIndex);
        int columnNum = fieldRow.getPhysicalNumberOfCells();
        if (columnNum > 1) {
            --columnNum;
        }

        sheet.removeRow(fieldRow);
        Map<String, Object> root = new HashMap(10);
        int endIndex = startIndex + pagSize;
        if (endIndex > ed.getData().size()) {
            endIndex = ed.getData().size();
        }

        root.put("list", ed.getData().subList(startIndex, endIndex));
        StringWriter writer = new StringWriter();
        template.process(root, writer);
        String str = writer.toString();
        String[] rows = str.split("#R");
        sheet.shiftRows(0, 1, 1);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(ed.getTitle());
        setTitleStyle(workbook, titleCell);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, columnNum);
        sheet.addMergedRegion(region);
        sheet.createFreezePane(0, 2);
        int dataRowIndex = 2;

        int configSheetIndex;
        for(configSheetIndex = 0; configSheetIndex < rows.length; ++configSheetIndex) {
            String row = rows[configSheetIndex];
            if (StringUtil.isNotEmpty(row)) {
                HSSFRow hssfRow = sheet.createRow(configSheetIndex + dataRowIndex);
                String[] cells = row.split("#C");

                for(int j = 0; j < cells.length; ++j) {
                    hssfRow.createCell(j).setCellValue(cells[j]);
                }
            }
        }

        configSheetIndex = workbook.getSheetIndex("参数配置");
        if (configSheetIndex != -1) {
            workbook.removeSheetAt(configSheetIndex);
        }

        workbook.setSheetName(0, "导出数据");
        File tempFile = FileUtil.createTempFile();
        workbook.write(tempFile);
        workbook.close();
        return tempFile;
    }

    private static InputStream getInputStream(String srcFile) {
        try {
            InputStream stream = null;
            if (srcFile.contains("!")) {
                if (!srcFile.startsWith("/com")) {
                    int index = srcFile.indexOf("/com");
                    srcFile = srcFile.substring(index);
                }

                stream = ClassLoaderUtil.getResourceAsStream(srcFile);
            } else {
                stream = new FileInputStream(srcFile);
            }

            return (InputStream)stream;
        } catch (Exception var3) {
            logger.error("error", var3);
            return null;
        }
    }

    private static void setTitleStyle(Workbook workbook, HSSFCell cell) {
        Font fontHead = workbook.createFont();
        fontHead.setFontHeightInPoints((short)14);
        fontHead.setColor((short)32767);
        fontHead.setBold(true);
        CellStyle cellStyleHead = workbook.createCellStyle();
        cellStyleHead.setFont(fontHead);
        cellStyleHead.setAlignment((short)2);
        cellStyleHead.setFillForegroundColor(IndexedColors.LIME.getIndex());
        cellStyleHead.setWrapText(true);
        cellStyleHead.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleHead.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleHead.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleHead.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cell.setCellStyle(cellStyleHead);
    }
}
