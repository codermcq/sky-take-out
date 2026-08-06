package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Setmeal> list(Long categoryId) {
        Setmeal setmeal = Setmeal.builder().categoryId(categoryId).status(1).build();
        return setmealMapper.list(setmeal);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishById(Long id) {
        return setmealMapper.getDishById(id);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = Setmeal.builder()
                .categoryId(setmealDTO.getCategoryId())
                .name(setmealDTO.getName())
                .price(setmealDTO.getPrice())
                .status(setmealDTO.getStatus())
                .description(setmealDTO.getDescription())
                .image(setmealDTO.getImage())
                .build();

        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        for (SetmealDish sd : setmealDishList) {
            SetmealDish setmealDish = new SetmealDish();
            setmealDish.setSetmealId(setmealId);
            setmealDish.setDishId(sd.getDishId());
            setmealDish.setName(sd.getName());
            setmealDish.setPrice(sd.getPrice());
            setmealDish.setCopies(sd.getCopies());

            setmealDishMapper.insert(setmealDish);
        }

    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Setmeal setmeal = Setmeal.builder()
                .name(setmealPageQueryDTO.getName())
                .categoryId(setmealPageQueryDTO.getCategoryId())
                .status(setmealPageQueryDTO.getStatus())
                .build();

        Page<Setmeal> page = setmealMapper.pageQuery(setmeal);

        List<SetmealVO> voList = new ArrayList<>();
        for (Setmeal s : page) {
            SetmealVO vo = new SetmealVO();
            BeanUtils.copyProperties(s, vo);

            Category category = categoryMapper.getById(s.getCategoryId());
            vo.setCategoryName(category.getName());

            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(s.getId());
            vo.setSetmealDishes(setmealDishes);

            voList.add(vo);
        }

        return new PageResult(page.getTotal(), voList);
    }

    /**
     * 启用禁用
     *
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder().status(status).id(id).build();

        setmealMapper.update(setmeal);
    }

}