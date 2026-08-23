package com.danishuahmad.employee_management.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danishuahmad.employee_management.model.dto.CreateEmployeeDTO;
import com.danishuahmad.employee_management.model.dto.EmployeeDTO;
import com.danishuahmad.employee_management.model.dto.UpdateEmployeeDTO;
import com.danishuahmad.employee_management.model.entity.Employee;
import com.danishuahmad.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees(){
        //return List.of(new EmployeeDTO(1L, "Danish", "Sales", 2000));
        return employeeService.getAllEmployees().stream().map(EmployeeDTO::fromEntity).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<@NotNull EmployeeDTO> getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id).map(EmployeeDTO::fromEntity).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<@NotNull EmployeeDTO> createEmployee(@RequestBody CreateEmployeeDTO request){
        Employee employee = new Employee(null, request.name(), request.department(), request.salary());
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeDTO.fromEntity(createdEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NotNull EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeDTO request){
        Employee employee = new Employee(null, request.name(), request.department(), request.salary());
        return employeeService.updateEmployee(id, employee).map(EmployeeDTO::fromEntity).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NotNull Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/department/{department}")
    public List<EmployeeDTO> getEmployeesByDepartment(@PathVariable String department) {
        return employeeService.getEmployeesByDepartment(department).
                stream().
                map(EmployeeDTO::fromEntity).
                collect(Collectors.toList());
    }
    
}
