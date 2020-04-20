package com.hmk.javaweb.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 * 
 * @author lq
 */
public class ExcelUtil {

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
    public static final String EMPTY                     = "";
    public static final String POINT                     = ".";
    public static SimpleDateFormat sdf                       = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 获得path的后缀名
     * 
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }

    /**
     * 单元格格式
     * 
     * @param hssfCell
     * @return
     */
    @SuppressWarnings({ "static-access", "deprecation" })
    public static String getHValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(hssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 单元格格式
     * 
     * @param xssfCell
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("#.######");
                cellValue = df.format(xssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("000000")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    /**
     * 自定义xssf日期工具类
     * 
     * @author lp
     */
    static class XSSFDateUtil extends DateUtil {

        protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
            return DateUtil.absoluteDay(cal, use1904windowing);
        }
    }

    /**
     * 导出列表数据
     * @param title 表头集合
     * @param position  表头字段位置集合
     * @param data 需要导出的数据
     * @param sheetName 导出数据后在excel表格中左下角显示的工作簿名称（注意：不是导出后的文件名）
     * @param outputStream 从controller层通过response获取到的输出流
     */
    public static void exportDataToExcel(Map<String, String> title, Map<String, Integer> position, List<Map<String, Object>> data, String sheetName, OutputStream outputStream) throws Exception {
        if (data == null || data.size() < 1) {
            return;
        }
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Row header = sheet.createRow(0);
            //设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 字体样式
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)14);
            headerStyle.setFont(font);

            int col = 0;
            //遍历表头集合
            for (String key: title.keySet()) {
                sheet.setColumnWidth(col, 6000);
                // 设置表格头部
                Cell headerCell = header.createCell(position.get(key));
                headerCell.setCellValue(title.get(key) + "");
                headerCell.setCellStyle(headerStyle);
                col++;
            }
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            /*
             * 遍历要导出列表的数据data 并与title的key相比较， 确认后插入值
             * 创建列时，根据title的key然后将值插入到对应的列中（position，dataMap，title三个集合的key值是一一对应的）
             */
            if (data != null && data.size() > 0) {
                int r = 0;
                for (Map<String, Object> dataMap : data) {
                    Row row = sheet.createRow(r + 1);
                    for (String dkey : dataMap.keySet()) {
                        for (String key : title.keySet()) {
                            if (key.equals(dkey)) {
                                Cell cell = row.createCell(position.get(key));
                                cell.setCellValue(dataMap.get(dkey) + "");
                                cell.setCellStyle(style);
                                break;
                            }
                        }
                    }
                    r++;
                }
            }
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            throw new Exception("导出列表失败。");
        }
    }
}
