package com.starchef.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工作台数据聚合 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkSpaceVO implements Serializable {

    /** 今日运营数据 */
    private BusinessDataVO businessData;

    /** 订单状态概览（当日各状态数量） */
    private OrderOverViewVO orderOverView;

    /** 菜品总览 */
    private DishOverViewVO dishOverView;

    /** 套餐总览 */
    private SetmealOverViewVO setmealOverView;
}
