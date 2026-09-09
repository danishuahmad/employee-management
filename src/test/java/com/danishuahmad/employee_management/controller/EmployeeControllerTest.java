package com.danishuahmad.employee_management.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.danishuahmad.employee_management.model.dto.DepartmentStatsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.danishuahmad.employee_management.model.dto.CreateEmployeeDTO;
import com.danishuahmad.employee_management.model.dto.UpdateEmployeeDTO;
import com.danishuahmad.employee_management.model.entity.Employee;
import com.danishuahmad.employee_management.service.EmployeeService;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployees_returnListWithStatus200() throws Exception {
        List<Employee> employees = List.of(
            new Employee(1L, "John", "Engineering", 20000)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees")).
            andExpect(status().isOk()).
            andExpect(jsonPath("$[0].name").value("John"));

    }

    @Test
    void getEmployeeById_returns200_whenFound() throws Exception {
        Employee employee = new Employee(1L, "John", "Sales", 2000);
    
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1")).
            andExpect(status().isOk()).
            andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getEmployeeById_returns404_whenNotFound() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/99")).
            andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_returns400WhenSalaryIsNegative() throws Exception {
        CreateEmployeeDTO request = new CreateEmployeeDTO("John Doe", "Sales", -5000);

        mockMvc.perform(post("/employees").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))

        ).andExpect(status().isBadRequest()).
        andExpect(jsonPath("$.errors.salary").value("Salary must be positive"));
    }

    @Test
    void createEmployee_returns201_withCreatedEmployee() throws Exception {
        CreateEmployeeDTO request = new CreateEmployeeDTO("Mustermann", "Sales", 1000);
        Employee saved = new Employee(1L, "Mustermann", "Sales", 1000);


        when(employeeService.createEmployee(
            org.mockito.ArgumentMatchers.any(Employee.class))
        ).thenReturn(saved);
    
        mockMvc.perform(post("/employees").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))
            ).
            andExpect(status().isCreated()).
            andExpect(jsonPath("$.name").value("Mustermann"));
    
    }

    @Test
    void updateEmployee_returns200_whenFound() throws Exception {
        UpdateEmployeeDTO request = new UpdateEmployeeDTO("Mark", "Production", 2000);
        Employee updated = new Employee(1L, "Mark Shawn", "Logistics", 2500);

        when(employeeService.updateEmployee(
            org.mockito.ArgumentMatchers.eq(1L), 
            org.mockito.ArgumentMatchers.any(Employee.class))
        ).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/employees/1").
                contentType("application/json").
                content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk()).
            andExpect(jsonPath("$.name").value("Mark Shawn")).
            andExpect(jsonPath("$.department").value("Logistics")).
            andExpect(jsonPath("$.salary").value(2500));
    }

    @Test
    void updateEmployee_returns404_whenNotFound() throws Exception {
        UpdateEmployeeDTO request = new UpdateEmployeeDTO("NoOne", "Unknown", 1);

        when(employeeService.updateEmployee(
            org.mockito.ArgumentMatchers.eq(99L),
            org.mockito.ArgumentMatchers.any(Employee.class))
        ).thenReturn(Optional.empty());

        mockMvc.perform(put("/employees/99").
            contentType("application/json").
            content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound());

    }

    @Test
    void deleteEmployee_returns204_whenDeleted() throws Exception{
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        mockMvc.perform(delete("/employees/1")).andExpect(status().isNoContent());

    }

    @Test 
    void deleteEmployee_returns404_whenNotFound() throws Exception {
        when(employeeService.deleteEmployee(99L)).thenReturn(false);
        mockMvc.perform(delete("/employees/99")).andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByDepartment_returnsListWith200() throws Exception {
        List<Employee> employees = List.of(
                new Employee(1L, "John", "Sales", 2000),
                new Employee(2L, "Doe", "HR", 1500)
        );

        when(employeeService.getEmployeesByDepartment("sales")).thenReturn(List.of(employees.getFirst()));

        mockMvc.perform(get("/employees/department/sales")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].name").value("John"));

        verify(employeeService).getEmployeesByDepartment("sales");
    }
    @Test
    void getEmployeesByDepartment_returnsEmptyListWith200() throws Exception{
        when(employeeService.getEmployeesByDepartment("sales")).thenReturn(List.of());

        mockMvc.perform(get("/employees/department/sales")).
            andExpect(status().isOk()).
            andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getDepartmentStats_returnListWith200() throws Exception {
        List<DepartmentStatsDTO> result = List.of(
            new DepartmentStatsDTO("hr", 2L, 2150.0),
            new DepartmentStatsDTO("engineering", 3L, 3600.0)
        );

        when(employeeService.getDepartmentStats()).thenReturn(result);

        mockMvc.perform(get("/employees/stats")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].department").value("hr")).
                andExpect(jsonPath("$[1].employeeCount").value(3L));

        verify(employeeService).getDepartmentStats();
    }
    @Test
    void getDepartmentStats_returnsEmptyListWith200() throws Exception {
        when(employeeService.getDepartmentStats()).thenReturn(List.of());

        mockMvc.perform(get("/employees/stats")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isEmpty());
    }

}
