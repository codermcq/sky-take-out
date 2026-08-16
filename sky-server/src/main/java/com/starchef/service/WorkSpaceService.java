package com.starchef.service;

import com.starchef.vo.*;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    /**
     * 获取今日工作台聚合数据（运营数据 + 订单概览 + 菜品/套餐总览）
     * @return WorkSpaceVO
     */
    WorkSpaceVO getWorkSpaceData();

    /**
     * 计算指定时间段的运营数据（营业额、有效订单、订单完成率、平均客单价、新增用户）
     * @param begin 开始时间
     * @param end   结束时间
     * @return BusinessDataVO
     */
    BusinessDataVO buildBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 计算指定时间段的订单状态概览（各状态订单数）
     * @param begin 开始时间
     * @param end   结束时间
     * @return OrderOverViewVO
     */
    OrderOverViewVO buildOrderOverView(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取菜品总览（启售/停售数量）
     * @return DishOverViewVO
     */
    DishOverViewVO buildDishOverView();

    /**
     * 获取套餐总览（启售/停售数量）
     * @return SetmealOverViewVO
     */
    SetmealOverViewVO buildSetmealOverView();
}
