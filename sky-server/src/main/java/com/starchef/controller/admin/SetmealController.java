package com.starchef.controller.admin;

import com.starchef.dto.SetmealDTO;
import com.starchef.dto.SetmealPageQueryDTO;
import com.starchef.result.PageResult;
import com.starchef.result.Result;
import com.starchef.service.SetmealService;
import com.starchef.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache", key = "#a0.categoryId")
    @ApiOperation("新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询套餐（带缓存）
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache", key = "#a0 == null ? 'all' : #a0")
    public Result<List<SetmealVO>> list(Long categoryId) {
        log.info("根据分类id查询套餐: categoryId={}", categoryId);
        List<SetmealVO> list = setmealService.listByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 启用禁用套餐
     * @param status
     * @param id
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("启用禁用套餐")
    @PutMapping("/status/{status}")
    public Result changeStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用套餐: status={}, id={}", status, id);
        setmealService.changeStatus(status, id);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result getBySetmealId(@PathVariable Long id) {
        log.info("根据id查询套餐信息: id: {}", id);
        SetmealVO setmealVO = setmealService.getBySetmealId(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @PutMapping
    @ApiOperation("修改套餐信息")
    public Result updateInfo(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateInfo(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("删除套餐")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }
}
