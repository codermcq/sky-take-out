package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 查询地址簿
     * @param addressBook
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "VALUES(#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault}) ")
    void add(AddressBook addressBook);

    /**
     * 返回默认地址
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and is_default = 1 limit 1")
    AddressBook defaultAddress(AddressBook addressBook);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据Id查询地址
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 设置全部为非默认
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void setNotDefault(AddressBook addressBook);
}
