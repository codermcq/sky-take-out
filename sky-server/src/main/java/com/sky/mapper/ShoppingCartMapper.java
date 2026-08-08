package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 动态查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 添加购物车对应的菜品数量
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);

    /**
     * 插入购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime}) ")
    void insert(ShoppingCart shoppingCart);

    /**
     * 数量为空则在购物车中删除
     * @param cart
     */
    void delete(ShoppingCart cart);

    /**
     * 清空购物车
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(ShoppingCart shoppingCart);

    /**
     * 批量插入购物车
     * @param carts
     */
    void insertBatch(List<ShoppingCart> carts);
}
