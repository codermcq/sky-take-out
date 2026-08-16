package com.starchef.service.impl;

import com.starchef.constant.StatusConstant;
import com.starchef.entity.Orders;
import com.starchef.mapper.DishMapper;
import com.starchef.mapper.OrderMapper;
import com.starchef.mapper.SetmealMapper;
import com.starchef.mapper.UserMapper;
import com.starchef.service.WorkSpaceService;
import com.starchef.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public WorkSpaceVO getWorkSpaceData() {
        LocalDate today = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(today, LocalTime.MAX);

        return WorkSpaceVO.builder()
                .businessData(buildBusinessData(begin, end))
                .orderOverView(buildOrderOverView(begin, end))
                .dishOverView(buildDishOverView())
                .setmealOverView(buildSetmealOverView())
                .build();
    }

    @Override
    public BusinessDataVO buildBusinessData(LocalDateTime begin, LocalDateTime end) {
        // 营业额 = 指定时间段已完成订单金额合计
        Map<String, Object> turnoverMap = new HashMap<>();
        turnoverMap.put("begin", begin);
        turnoverMap.put("end", end);
        turnoverMap.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(turnoverMap);
        turnover = turnover == null ? 0.0 : turnover;

        // 有效订单 = 指定时间段已完成订单数
        Map<String, Object> validMap = new HashMap<>();
        validMap.put("begin", begin);
        validMap.put("end", end);
        validMap.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.getOrderCountList(validMap);
        validOrderCount = validOrderCount == null ? 0 : validOrderCount;

        // 总订单数
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("begin", begin);
        totalMap.put("end", end);
        Integer totalOrderCount = orderMapper.getOrderCountList(totalMap);
        totalOrderCount = totalOrderCount == null ? 0 : totalOrderCount;

        // 订单完成率
        Double orderCompletionRate = totalOrderCount > 0
                ? (double) validOrderCount / totalOrderCount
                : 0.0;

        // 平均客单价
        Double unitPrice = validOrderCount > 0 ? turnover / validOrderCount : 0.0;

        // 新增用户
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("begin", begin);
        newUserMap.put("end", end);
        Integer newUsers = userMapper.countByMap(newUserMap);
        newUsers = newUsers == null ? 0 : newUsers;

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    @Override
    public OrderOverViewVO buildOrderOverView(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        Integer allOrders = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.PENDING_PAYMENT);
        Integer pendingPayment = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waiting = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.CONFIRMED);
        Integer delivered = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.DELIVERY_IN_PROGRESS);
        Integer inDelivery = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.COMPLETED);
        Integer completed = nvl(orderMapper.getOrderCountList(map));

        map.put("status", Orders.CANCELLED);
        Integer cancelled = nvl(orderMapper.getOrderCountList(map));

        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .pendingPaymentOrders(pendingPayment)
                .waitingOrders(waiting)
                .deliveredOrders(delivered)
                .inDeliveryOrders(inDelivery)
                .completedOrders(completed)
                .cancelledOrders(cancelled)
                .build();
    }

    @Override
    public DishOverViewVO buildDishOverView() {
        Integer sold = nvl(dishMapper.countByStatus(StatusConstant.ENABLE));
        Integer discontinued = nvl(dishMapper.countByStatus(StatusConstant.DISABLE));
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public SetmealOverViewVO buildSetmealOverView() {
        Integer sold = nvl(setmealMapper.countByStatus(StatusConstant.ENABLE));
        Integer discontinued = nvl(setmealMapper.countByStatus(StatusConstant.DISABLE));
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    private static int nvl(Integer val) {
        return val == null ? 0 : val;
    }
}
