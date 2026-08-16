package com.starchef.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.starchef.constant.MessageConstant;
import com.starchef.constant.StatusConstant;
import com.starchef.dto.DishDTO;
import com.starchef.dto.DishPageQueryDTO;
import com.starchef.entity.Dish;
import com.starchef.entity.DishFlavor;
import com.starchef.exception.DeletionNotAllowedException;
import com.starchef.mapper.DishFlavorMapper;
import com.starchef.mapper.DishMapper;
import com.starchef.mapper.SetmealDishMapper;
import com.starchef.result.PageResult;
import com.starchef.service.DishService;
import com.starchef.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        // 1. 插入菜品，@Options 会自动回填生成的 ID 到 dishDTO.id
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);
        // 获取insert语句生成主键值
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insert(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> dishes = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(dishes.getTotal(), dishes.getResult());
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 是否存在起售的菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealIds = setmealDishMapper.getSetmealIdsDishIds(ids);

        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

//        for (Long id : ids) {
//            dishMapper.deleteId(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }

        dishMapper.deleteIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 菜品起售禁售
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();

        dishMapper.updateInfo(dish);
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @Override
    public DishVO getInfoById(Long id) {
        return dishMapper.getInfoById(id);
    }

    /**
     * 修改菜品信息
     * @param dishVO
     */
    @Override
    @Transactional
    public void updateInfo(DishVO dishVO) {
        // DishVO → Dish 实体，AutoFill 才能注入 updateTime/updateUser
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishVO, dish);
        dishMapper.updateInfo(dish);

        // 更新口味：先删后插
        dishFlavorMapper.deleteByDishId(dishVO.getId());
        List<DishFlavor> flavors = dishVO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(f -> f.setDishId(dishVO.getId()));
            dishFlavorMapper.insert(flavors);
        }
    }

    /**
     * 根据分类id查询相关菜品和相关口味
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setStatus(StatusConstant.ENABLE);
        dish.setCategoryId(categoryId);

        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();

        // 查询每个菜品相关的口味
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();

            BeanUtils.copyProperties(d,dishVO);


            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }

}
