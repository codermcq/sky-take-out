package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id删除菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id删除菜品")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        dishService.deleteBatch(Collections.singletonList(id));
        return Result.success();
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @ApiOperation("菜品批量删除")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 菜品起售禁售
     * @return
     */
    @ApiOperation("菜品起售禁售")
    @PutMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id) {
        log.info("菜品状态和id: {} {}", status, id);
        dishService.status(status, id);
        return Result.success();
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品信息")
    @GetMapping("/{id}")
    public Result getInfoById(@PathVariable Long id) {
        log.info("查询的菜品id为: {}", id);
        DishVO dishVO = dishService.getInfoById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     * @return
     */
    @ApiOperation("修改菜品信息")
    @PutMapping
    public Result updateInfo(@RequestBody DishVO dishVO) {
        log.info("修改的菜品信息为: {}", dishVO);
        dishService.updateInfo(dishVO);
        return Result.success();
    }
}
