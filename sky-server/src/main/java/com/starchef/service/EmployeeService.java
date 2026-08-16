package com.starchef.service;

import com.starchef.dto.EmployeeDTO;
import com.starchef.dto.EmployeeLoginDTO;
import com.starchef.dto.EmployeePageQueryDTO;
import com.starchef.entity.Employee;
import com.starchef.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态
     * @param id
     * @param status
     */
    void status(Long id, Integer status);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Integer id);

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    void updateEmpInfo(EmployeeDTO employeeDTO);

    /**
     * 根据id删除员工
     * @return
     */
    void deleteById(Integer id);
}
