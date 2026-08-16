package com.starchef.controller.user;

import com.starchef.entity.Category;
import com.starchef.result.Result;
import com.starchef.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据类型查询分类
     * @return
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("根据类型查询分类: {}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
