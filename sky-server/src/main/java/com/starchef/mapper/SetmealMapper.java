package com.starchef.mapper;

import com.github.pagehelper.Page;
import com.starchef.annotation.AutoFill;
import com.starchef.entity.Setmeal;
import com.starchef.enumeration.OperationType;
import com.starchef.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
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
     * 根据状态统计套餐数量
     * @param status
     * @return
     */
    @Select("select count(*) from setmeal where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    List<DishItemVO> getDishById(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询套餐
     *
     * @param setmeal
     * @return
     */
    Page<Setmeal> pageQuery(Setmeal setmeal);

    /**
     * 修改套餐信息
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     * @param id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);
}
