package com.starchef.controller.admin;

import com.starchef.constant.JwtClaimsConstant;
import com.starchef.dto.EmployeeDTO;
import com.starchef.dto.EmployeeLoginDTO;
import com.starchef.dto.EmployeePageQueryDTO;
import com.starchef.entity.Employee;
import com.starchef.properties.JwtProperties;
import com.starchef.result.PageResult;
import com.starchef.result.Result;
import com.starchef.service.EmployeeService;
import com.starchef.utils.JwtUtil;
import com.starchef.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        log.info("员工退出登录");
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工信息: {}", employeeDTO);

        employeeService.save(employeeDTO);

        return Result.success();
    }


    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工状态
     * @param id
     * @param status
     */
    @ApiOperation("启用禁用员工状态")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工状态: status={}, id={}", status, id);
        employeeService.status(id, status);

        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @ApiOperation("根据id查询员工")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("员工id: {}", id);
        Employee employee  = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @ApiOperation("编辑员工信息")
    @PutMapping
    public Result updateEmpInfo(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改的员工信息: {}", employeeDTO);
        employeeService.updateEmpInfo(employeeDTO);
        return Result.success();
    }

    /**
     * 根据id删除员工
     * @return
     */
    @ApiOperation("根据id删除员工")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        log.info("删除的员工id: {}", id);
        employeeService.deleteById(id);
        return Result.success();
    }
}
