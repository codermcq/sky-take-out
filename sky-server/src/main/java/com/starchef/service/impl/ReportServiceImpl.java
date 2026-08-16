package com.starchef.service.impl;

import com.starchef.dto.GoodsSalesDTO;
import com.starchef.entity.Orders;
import com.starchef.mapper.OrderMapper;
import com.starchef.mapper.UserMapper;
import com.starchef.service.ReportService;
import com.starchef.service.WorkSpaceService;
import com.starchef.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 统计营业额
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = getDateList(begin, end);

        ArrayList<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(map);

            turnover =  turnover == null ? 0.0 : turnover;

            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList( StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = getDateList(begin, end);

        // 新增用户数量
        ArrayList<Integer> newUserList = new ArrayList<>();
        // 每天的总的用户数量
        ArrayList<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap();
            map.put("end", endTime);

            Integer totalUser = userMapper.countByMap(map);
            totalUserList.add(totalUser);

            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = getDateList(begin, end);

        ArrayList<Integer> orderCountList = new ArrayList<>();
        ArrayList<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);

            Integer order = orderMapper.getOrderCountList(map);
            orderCountList.add(order);

            map.put("status", Orders.COMPLETED);
            Integer validOrder = orderMapper.getOrderCountList(map);
            validOrderCountList.add(validOrder);
        }

        // 从每日数据汇总得到时间范围内的总订单数和有效订单数
        int totalOrderCount = orderCountList.stream().mapToInt(Integer::intValue).sum();
        int validOrderCount = validOrderCountList.stream().mapToInt(Integer::intValue).sum();
        // 先转 double 避免整数除法截断，返回比率（0~1），前端会 *100 显示百分比
        Double orderCompletionRate = totalOrderCount > 0
                ? (double) validOrderCount / totalOrderCount
                : 0.0;

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> nameList = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 根据开始和结束返回时间段列表
     * @param begin
     * @param end
     * @return
     */
    private ArrayList<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        return dateList;
    }

    /**
     * 导出运营数据报表
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) throws IOException {
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        LocalDateTime beginTime = LocalDateTime.of(dateBegin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(dateEnd, LocalTime.MAX);

        // 查询最近30天的运营数据
        BusinessDataVO businessDataVO = workSpaceService.buildBusinessData(beginTime, endTime);

        // 通过POI将数据写入Excel文件中
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        // 基于模板文件创建一个新的Excel文件
        XSSFWorkbook excel = new XSSFWorkbook(is);

        // 填充数据
        // 时间（Excel 第2行）
        XSSFSheet sheet = excel.getSheet("statistics");

        sheet.getRow(1).getCell(1).setCellValue("时间" + beginTime + " ~ " + endTime);

        // 概览数据（Excel 第5、6行）
        XSSFRow row = sheet.getRow(4);
        row.getCell(2).setCellValue(businessDataVO.getTurnover());
        row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        row.getCell(6).setCellValue(businessDataVO.getNewUsers());

        row = sheet.getRow(5);
        row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

        // 填充明细数据（Excel 第9行起，共30行；模板行数不足时自动补行）
        for (int i = 0; i < 30; i++) {
            LocalDate date = dateBegin.plusDays(i);
            BusinessDataVO businessData = workSpaceService.buildBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            row = sheet.getRow(8 + i);
            if (row == null) {
                row = sheet.createRow(8 + i);
            }
            setCellValue(row, 1, date.toString());
            setCellValue(row, 2, businessData.getTurnover());
            setCellValue(row, 3, businessData.getValidOrderCount());
            setCellValue(row, 4, businessData.getOrderCompletionRate());
            setCellValue(row, 5, businessData.getUnitPrice());
            setCellValue(row, 6, businessData.getNewUsers());
        }

        // 通过输出流将Excel文件下载到客户端浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        excel.write(outputStream);

        // 关闭资源
        outputStream.close();
        excel.close();
    }

    /**
     * 设置单元格值（单元格不存在时自动创建）
     */
    private static void setCellValue(XSSFRow row, int col, Object value) {
        XSSFCell cell = row.getCell(col);
        if (cell == null) {
            cell = row.createCell(col);
        }
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

}

