package com.starchef.service.impl;

import com.starchef.context.BaseContext;
import com.starchef.entity.AddressBook;
import com.starchef.mapper.AddressBookMapper;
import com.starchef.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询地址簿
     * @param addressBook
     * @return
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 添加地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        addressBookMapper.add(addressBook);
    }

    /**
     * 返回默认地址
     * @return
     */
    @Override
    public AddressBook defaultAddress() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        return addressBookMapper.defaultAddress(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据Id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 修改地址为默认地址
     * @param addressBook
     * @return
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        AddressBook ab = new AddressBook();
        ab.setUserId(BaseContext.getCurrentId());
        addressBookMapper.setNotDefault(ab);
        addressBookMapper.update(addressBook);
    }
}
