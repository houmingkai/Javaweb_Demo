package com.hmk.javaweb.controller;


import com.hmk.javaweb.constant.ReturnMessage;
import com.hmk.javaweb.entity.OrderStatusEnum;
import com.hmk.javaweb.entity.StrategyDetailDto;
import com.hmk.javaweb.entity.SubOrderDto;
import com.hmk.javaweb.utils.excel.ExcelRead;
import com.hmk.javaweb.utils.excel.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 *   Excel  导入导出
 */


@RestController
@RequestMapping("/excel")
public class POIController {

    private static final Logger logger = LoggerFactory.getLogger(POIController.class);


    /**
     * 上传解析excel
     *
     * @param request
     * @return
     */
    @PostMapping("/uploadExcel")
    public ReturnMessage uploadExcel(HttpServletRequest request) {
        ReturnMessage returnMessage = new ReturnMessage();
        try {

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            ExcelRead excelRead = new ExcelRead();
            List<ArrayList<String>> list = excelRead.readExcel(file);
            returnMessage.setData(list);
            returnMessage.setFlag(true);
        } catch (Exception e) {
            returnMessage.setFlag(false);
            e.printStackTrace();
        }

        return returnMessage;
    }


    @GetMapping("/exportDeatil")
    public Object exportDeatil(HttpServletRequest request, HttpServletResponse response) throws Exception {

        StrategyDetailDto strategyDetail = new StrategyDetailDto();
        strategyDetail.setQuery("导出详情test");

        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息
        response.setContentType("application/vnd.ms-excel");
        String codedFileName;
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            codedFileName = java.net.URLEncoder.encode("策略详情", "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            HSSFSheet sheet = workbook.createSheet();
            sheet.setColumnWidth(0, 20 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 20 * 256);
            sheet.setColumnWidth(8, 20 * 256);
            sheet.setColumnWidth(9, 20 * 256);
            sheet.setColumnWidth(10, 20 * 256);
            sheet.setColumnWidth(11, 20 * 256);
            sheet.setColumnWidth(12, 20 * 256);

            HSSFRow tileRow1 = sheet.createRow(0);// 创建一行
            HSSFRow row = sheet.createRow(1);// 创建一行

            HSSFCell tileCell1 = tileRow1.createCell(0);
            tileCell1.setCellValue("策略名称");
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(strategyDetail.getTitle());

            HSSFCell tileCell2 = tileRow1.createCell(1);
            tileCell2.setCellValue("策略问句");
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(strategyDetail.getQuery());

            HSSFCell tileCell4 = tileRow1.createCell(2);
            tileCell4.setCellValue("回测区间");
            HSSFCell cell4 = row.createCell(2);
            cell4.setCellValue(strategyDetail.getStartDate() + "--" + strategyDetail.getEndDate());

            HSSFCell tileCell5 = tileRow1.createCell(3);
            tileCell5.setCellValue("时机");
            HSSFCell cell5 = row.createCell(3);
            cell5.setCellValue(strategyDetail.getOpportunity());

            HSSFCell tileCell6 = tileRow1.createCell(4);
            tileCell6.setCellValue("持股周期");
            HSSFCell cell6 = row.createCell(4);
            cell6.setCellValue(strategyDetail.getDaysForSaleStrategy() + "天");

            HSSFCell tileCell7 = tileRow1.createCell(5);
            tileCell7.setCellValue("持股数");
            HSSFCell cell7 = row.createCell(5);
            cell7.setCellValue(strategyDetail.getStockHoldCount() + "只");

            HSSFCell tileCell8 = tileRow1.createCell(6);
            tileCell8.setCellValue("仓位控制");
            HSSFCell cell8 = row.createCell(6);
            cell8.setCellValue(strategyDetail.getPositionControl());

            HSSFCell tileCell9 = tileRow1.createCell(7);
            tileCell9.setCellValue("止盈");
            HSSFCell cell9 = row.createCell(7);
            cell9.setCellValue("收益率≥" + strategyDetail.getUpperIncome() + " %时坚定持有；直到最高收益回落 " + strategyDetail.getFallIncome() + " %");

            HSSFCell tileCell10 = tileRow1.createCell(8);
            tileCell10.setCellValue("止损");
            HSSFCell cell10 = row.createCell(8);
            cell10.setCellValue("收益率≤ - " + strategyDetail.getLowerIncome() + "%");

            HSSFCell tileCell11 = tileRow1.createCell(9);
            tileCell11.setCellValue("最大预期年化收益率");
            HSSFCell cell11 = row.createCell(9);
            BigDecimal maxAnnualYield = new BigDecimal(strategyDetail.getMaxAnnualYield());
            BigDecimal num1 = maxAnnualYield.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            cell11.setCellValue(num1 + "%持股(" + strategyDetail.getMaxAnnualYieldDays() + "天)");

            HSSFCell tileCell12 = tileRow1.createCell(10);
            tileCell12.setCellValue("最大成功率");
            HSSFCell cell12 = row.createCell(10);
            BigDecimal maxWinRate = new BigDecimal(strategyDetail.getMaxWinRate());
            BigDecimal num2 = maxWinRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            cell12.setCellValue(num2 + "%持股(" + strategyDetail.getMaxWinRateDays() + "天)");
            try (
                    OutputStream fOut = response.getOutputStream()
            ) {
                workbook.write(fOut);
                fOut.flush();
                fOut.close();
            }
        } finally {
            session.setAttribute("state", "open");
        }
        return "123";
    }



    /**
     *   导出列表数据
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/exportList")
    public Object  exportList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SubOrderDto> orderList = new ArrayList<>();
        SubOrderDto dto1 = new SubOrderDto();
        dto1.setOrder_code("1234567895456");
        dto1.setPhone_num(13333333333L);
        dto1.setProduct_name("test1");
        dto1.setOrder_type(1);
        dto1.setProduct_id(11);
        dto1.setInputtime("2019-09-23 10:37:10");
        dto1.setServer_start("2019-06-23 00:00:00");
        dto1.setServer_end("2019-09-23 10:37:10");
        dto1.setOrder_count(100);
        dto1.setZjzh("123456789");
        dto1.setPay_type(1);
        dto1.setFirst_buy(1);
        dto1.setOrder_status(1);
        orderList.add(dto1);

        Map<String, String> title = new HashMap<>();    // 表头
        String[] arr = {"订单编号","手机号","产品名称","产品类型","产品ID","订单创建时间","订单金额(元)","服务生效时间","服务失效时间","资金账号","支付方式","是否首次购买","订单状态"};
        List<Object> params = Arrays.asList(arr);
        title = setMapData(title,params);

        List<Map<String, Object>> data = new ArrayList<>();     // 需要导出的数据
        Map<String, Object> orderMap = new HashMap<>();
        for (SubOrderDto subOrderDto : orderList) {
            String fistBuy = subOrderDto.getFirst_buy() == 0 ? "是" : "否";
            String orderStatus = OrderStatusEnum.getStatusByCode(subOrderDto.getOrder_status());
            Object[] arrDto = {subOrderDto.getOrder_code(),subOrderDto.getPhone_num(),subOrderDto.getProduct_name(),"策略",
                    subOrderDto.getProduct_id(),subOrderDto.getInputtime(),subOrderDto.getOrder_count(),subOrderDto.getServer_start(),subOrderDto.getServer_end(),
                    subOrderDto.getZjzh(),"微信支付",fistBuy,orderStatus};
            List<Object> dtoList = Arrays.asList(arrDto);
            orderMap = setMapData(orderMap,dtoList);
            data.add(orderMap);
        }


        Map<String, Integer> position = new HashMap<>();        // 表头字段对应的位置（自定义位置）
        Integer[] arrNum = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
        List<Object> positionList = Arrays.asList(arrNum);
        position = setMapData(position,positionList);

        String sheetName = "订单列表数据";

        String excelName = "订单列表.xlsx";
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName);
        response.setContentType("application/x-download");

        ExcelUtil.exportDataToExcel(title,position,data,sheetName,response.getOutputStream());
        return "hello 123";
    }

    public static Map setMapData(Map map,List<Object> list){
        map.put("order_code",list.get(0));
        map.put("phone_num",list.get(1));
        map.put("product_name",list.get(2));
        map.put("order_type",list.get(3));
        map.put("product_id",list.get(4));
        map.put("inputtime",list.get(5));
        map.put("order_count",list.get(6));
        map.put("server_start",list.get(7));
        map.put("server_end",list.get(8));
        map.put("zjzh",list.get(9));
        map.put("pay_type",list.get(10));
        map.put("first_buy",list.get(11));
        map.put("order_status",list.get(12));
        return map;
    }

}
