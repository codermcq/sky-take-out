package com.starchef.controller.user;

import com.starchef.constant.StatusConstant;
import com.starchef.entity.Dish;
import com.starchef.result.Result;
import com.starchef.service.DishService;
import com.starchef.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端-菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("C端根据分类id查询菜品: categoryId={}", categoryId);
        // 构造redis中的key
        String key = "dish_" + categoryId;
        // 查询redis中是否存在菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // 存在直接返回
            return Result.success(list);
        }

        // 不存在则查询数据库
        list = dishService.list(categoryId);
        // 存到redis中
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }


}
