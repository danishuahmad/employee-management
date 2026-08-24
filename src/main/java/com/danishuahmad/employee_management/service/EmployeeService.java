package com.danishuahmad.employee_management.service;

import java.util.List;
import java.util.Optional;

import com.danishuahmad.employee_management.model.dto.DepartmentStatsDTO;
import com.danishuahmad.employee_management.model.entity.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee createEmployee(Employee employee);
    Optional<Employee> updateEmployee(Long id, Employee employee);
    List<Employee> getEmployeesByDepartment(String department);
    List<DepartmentStatsDTO> getDepartmentStats();
    boolean deleteEmployee(Long id);
}
