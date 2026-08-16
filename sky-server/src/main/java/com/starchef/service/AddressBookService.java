package com.starchef.service;

import com.starchef.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 查询地址簿
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 添加地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 返回默认地址
     * @return
     */
    AddressBook defaultAddress();

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     *
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 修改地址为默认地址
     * @param id
     * @return
     */
    void setDefault(AddressBook addressBook);
}
