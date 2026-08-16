package com.starchef.mapper;

import com.github.pagehelper.Page;
import com.starchef.annotation.AutoFill;
import com.starchef.dto.DishPageQueryDTO;
import com.starchef.entity.Dish;
import com.starchef.enumeration.OperationType;
import com.starchef.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 根据状态统计菜品数量
     * @param status
     * @return
     */
    @Select("select count(*) from dish where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dish(name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values(#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dto
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dto);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据主键删除菜品
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteId(Long id);

    /**
     * 根据ids删除菜品
     * @param ids
     */
    void deleteIds(List<Long> ids);

    /**
     * 修改菜品信息
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateInfo(Dish dish);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    DishVO getInfoById(Long id);

    /**
     * 根据分类id查询相关菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Dish categoryId);
}
