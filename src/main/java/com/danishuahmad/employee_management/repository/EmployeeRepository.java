package com.danishuahmad.employee_management.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import com.danishuahmad.employee_management.model.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<@NotNull Employee, @NotNull Long> {
    List<Employee> findByDepartmentIgnoreCase(String department);
}
