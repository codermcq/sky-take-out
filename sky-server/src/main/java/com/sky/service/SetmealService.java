package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealService {
    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    List<Setmeal> list(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishById(Long id);

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 启用禁用
     *
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Long id);
}
