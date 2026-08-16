package com.starchef.controller.user;

import com.starchef.dto.ShoppingCartDTO;
import com.starchef.entity.ShoppingCart;
import com.starchef.result.Result;
import com.starchef.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
@Api("C端-购物车相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车, 商品信息为: {}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    /**
     * 查询购物车
     * @return
     */
    @ApiOperation("查询购物车")
    @GetMapping("/list")
    public Result list() {
        log.info("查询购物车");

        List<ShoppingCart> list = shoppingCartService.list();

        return Result.success(list);
    }

    /**
     * 删除购物车
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("删除购物车")
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除的购物车信息为: {}", shoppingCartDTO);

        shoppingCartService.sub(shoppingCartDTO);

        return Result.success();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result clean() {
        log.info("清空购物车");

        shoppingCartService.clean();

        return Result.success();
    }
}
