package com.starchef.controller.admin;

import com.starchef.dto.CategoryDTO;
import com.starchef.dto.CategoryPageQueryDTO;
import com.starchef.entity.Category;
import com.starchef.result.PageResult;
import com.starchef.result.Result;
import com.starchef.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增的分类信息: {}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分类分页查询")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用禁用分类状态")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id) {
        log.info("启用禁用分类状态: {} {}", status, id);
        categoryService.status(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     * @return
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result updateCateInfo(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改的分类信息为: {}", categoryDTO);
        categoryService.updateInfo(categoryDTO);
        return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除的分类id为: {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @return
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("根据类型查询分类: type={}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
