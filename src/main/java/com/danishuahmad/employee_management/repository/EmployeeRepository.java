package com.danishuahmad.employee_management.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import com.danishuahmad.employee_management.model.entity.Employee;

public interface EmployeeRepository extends JpaRepository<@NotNull Employee, @NotNull Long> {
    
}
