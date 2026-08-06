package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    void deleteBatch(List<Long> ids);

    /**
     * 菜品起售禁售
     * @param status
     * @param id
     */
    void status(Integer status, Long id);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    DishVO getInfoById(Long id);

    /**
     * 修改菜品信息
     * @param dishVO
     */
    void updateInfo(DishVO dishVO);

    /**
     * 根据分类id查询菜品信息
     * @param categoryId
     * @return
     */
    List<DishVO> list(Long categoryId);
}
