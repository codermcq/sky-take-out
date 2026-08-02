package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 根据套餐id查询菜品数量
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询包含的菜品列表
     * @param id
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    List<DishItemVO> getDishItemById(Long id);
}
