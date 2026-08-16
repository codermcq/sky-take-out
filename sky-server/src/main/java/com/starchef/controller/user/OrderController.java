package com.starchef.controller.user;

import com.starchef.dto.OrdersDTO;
import com.starchef.dto.OrdersPageQueryDTO;
import com.starchef.dto.OrdersPaymentDTO;
import com.starchef.result.PageResult;
import com.starchef.result.Result;
import com.starchef.service.OrderService;
import com.starchef.vo.OrderPaymentVO;
import com.starchef.vo.OrderSubmitVO;
import com.starchef.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@Api(tags = "C端订单相关接口")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单提交
     * @param ordersDTO
     * @return
     */
    @ApiOperation("订单提交")
    @PostMapping("/submit")
    public Result submit(@RequestBody OrdersDTO ordersDTO) {
        log.info("用户下单: {}", ordersDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单分页查询")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("用户查询历史订单: page={}, status={}, payStatus={}", ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getStatus(), ordersPageQueryDTO.getPayStatus());
        PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> detail(@PathVariable Long id) {
        log.info("用户查询订单详情: id={}", id);
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable Long id) {
        log.info("用户催单: id={}", id);
        orderService.reminder(id);
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @GetMapping("/repeat/{id}")
    @ApiOperation("再来一单")
    public Result repeat(@PathVariable Long id) {
        log.info("用户再来一单: id={}", id);
        orderService.repeatOrder(id);
        return Result.success();
    }

    /**
     * 用户取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("用户取消订单")
    public Result cancel(@PathVariable Long id) {
        log.info("用户取消订单: id={}", id);
        orderService.userCancel(id);
        return Result.success();
    }

    /**
     * 申请退款
     * @param id
     * @return
     */
    @PostMapping("/refund/{id}")
    @ApiOperation("申请退款")
    public Result refund(@PathVariable Long id) {
        log.info("用户申请退款: id={}", id);
        orderService.refund(id);
        return Result.success();
    }
}
