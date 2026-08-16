package com.starchef.service;

import com.starchef.dto.*;
import com.starchef.entity.Orders;
import com.starchef.result.PageResult;
import com.starchef.vo.OrderPaymentVO;
import com.starchef.vo.OrderSubmitVO;
import com.starchef.vo.OrderVO;

public interface OrderService {
    /**
     * 订单提交
     * @param ordersDTO
     * @return
     */
    OrderSubmitVO submit(OrdersDTO ordersDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态并返回订单数据（用于 WebSocket 推送）
     * @param outTradeNo
     * @return 更新后的订单
     */
    Orders paySuccess(String outTradeNo);

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 接单
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 用户端历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户端催单
     * @param id
     */
    void reminder(Long id);

    /**
     * 用户端再来一单
     * @param id
     */
    void repeatOrder(Long id);

    /**
     * 用户端取消订单
     * @param id
     */
    void userCancel(Long id);

    /**
     * 用户端申请退款
     * @param id
     */
    void refund(Long id);
}
