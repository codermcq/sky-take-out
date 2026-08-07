package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿相关接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询所有地址簿
     * @return
     */
    @ApiOperation("查询所有地址簿")
    @GetMapping("/list")
    public Result list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 根据Id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据Id查询地址")
    public Result getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }


    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @ApiOperation("添加地址")
    @PostMapping
    public Result add(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     * @return
     */
    @ApiOperation("默认地址")
    @GetMapping("/default")
    public Result defaultAddress() {
        AddressBook addressBook = addressBookService.defaultAddress();
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @ApiOperation("修改地址")
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 修改地址为默认地址
     * @param id
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("修改地址为默认地址")
    public Result setDefault(@RequestParam Long id) {
        AddressBook addressBook = AddressBook.builder().isDefault(1).id(id).build();
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
