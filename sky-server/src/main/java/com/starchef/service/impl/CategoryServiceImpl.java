package com.starchef.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.starchef.constant.MessageConstant;
import com.starchef.dto.CategoryDTO;
import com.starchef.dto.CategoryPageQueryDTO;
import com.starchef.entity.Category;
import com.starchef.exception.DeletionNotAllowedException;
import com.starchef.mapper.CategoryMapper;
import com.starchef.mapper.DishMapper;
import com.starchef.mapper.SetmealMapper;
import com.starchef.result.PageResult;
import com.starchef.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(0);
        // createTime/updateTime/createUser/updateUser 由 AutoFillAspect 自动填充
        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用分类状态
     * @param status
     * @param id
     * @return
     */
    @Override
    public void status(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        // updateTime/updateUser 由 AutoFillAspect 自动填充
        categoryMapper.update(category);
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @Override
    public void updateInfo(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        // updateTime/updateUser 由 AutoFillAspect 自动填充
        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有菜品, 不能删除
            throw  new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有菜品, 不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
       return categoryMapper.list(type);
    }

}
