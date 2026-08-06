package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应套餐id
     * @param dishIds
     * @return
     */

    List<Long> getSetmealIdsDishIds(List<Long> dishIds);

    /**
     * 新增
     * @param setmealDish
     */
    void insert(SetmealDish setmealDish);
}
