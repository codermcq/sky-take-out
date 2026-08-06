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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);

        // 清除缓存数据
        String key = "dish_" + dishDTO.getId();
        cleanCache(key);

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
     * 根据分类id查询菜品列表
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品列表")
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        List<DishVO> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 菜品删除（支持单个和批量）
     * @param ids
     * @return
     */
    @ApiOperation("菜品删除")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品删除: {}", ids);
        dishService.deleteBatch(ids);

        // 清理缓存数据
        cleanCache("dish_*");

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
        // 清理缓存数据
        cleanCache("dish_*");

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
        // 清理缓存数据
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
