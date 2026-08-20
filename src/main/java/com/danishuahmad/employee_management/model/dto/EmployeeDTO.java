package com.danishuahmad.employee_management.model.dto;

import com.danishuahmad.employee_management.model.entity.Employee;

public record EmployeeDTO(Long id, String name, String department, int salary) {
    public static EmployeeDTO fromEntity(Employee employee){
        return new EmployeeDTO(
            employee.getId(),
            employee.getName(),
            employee.getDepartment(),
            employee.getSalary()
        );
    }
}
