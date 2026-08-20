package com.danishuahmad.employee_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.danishuahmad.employee_management.model.entity.Employee;
import com.danishuahmad.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
 
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    @Override
    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employee){
        return employeeRepository.findById(id).map(existing -> {
            existing.setName(employee.getName());
            existing.setDepartment(employee.getDepartment());
            existing.setSalary(employee.getSalary());
            return employeeRepository.save(existing);
        });
    }

    @Override
    public boolean deleteEmployee(Long id){
        if( employeeRepository.existsById(id) ){
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
