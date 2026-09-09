package com.danishuahmad.employee_management.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Department is required")
        String department,

        @Min(value = 0, message = "Salary must be positive")
        int salary
) {}
