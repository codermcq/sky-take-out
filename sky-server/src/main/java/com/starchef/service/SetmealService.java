package com.starchef.service;

import com.starchef.dto.SetmealDTO;
import com.starchef.dto.SetmealPageQueryDTO;
import com.starchef.entity.Setmeal;
import com.starchef.result.PageResult;
import com.starchef.vo.DishItemVO;
import com.starchef.vo.SetmealVO;

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
     * 根据分类id查询套餐（含菜品）
     * @param categoryId
     * @return
     */
    List<SetmealVO> listByCategoryId(Long categoryId);

    /**
     * 启用禁用
     *
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Long id);

    /**
     * 根据套餐id查询
     * @param id
     * @return
     */
    SetmealVO getBySetmealId(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void updateInfo(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
