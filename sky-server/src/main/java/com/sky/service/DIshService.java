package com.sky.service;

import com.sky.dto.DishDTO;

public interface DIshService {
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    void saveWithFlavor(DishDTO dishDTO);
}
