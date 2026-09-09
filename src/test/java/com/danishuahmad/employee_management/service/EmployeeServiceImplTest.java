package com.danishuahmad.employee_management.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.danishuahmad.employee_management.model.dto.DepartmentStatsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import com.danishuahmad.employee_management.model.entity.Employee;
import com.danishuahmad.employee_management.repository.EmployeeRepository;;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getEmployeeById_returnsEmployeeWhenFound() {
        Employee employee = new Employee(1L, "Danish", "Engineering", 8000);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Danish");
    }
    @Test
    void getEmployeeById_returnsEmpty_whenNotFound() {
        when(employeeRepository.findById(9L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(9L);

        assertThat(result).isEmpty();

    }

    
    // ---------- getAllEmployees ----------
    @Test
    void getAllEmployees_returnsAllEmployees() {
        List<Employee> employees = List.of(
            new Employee(1L, "John", "Sales", 2000),
            new Employee(1L, "Doe", "Marketing", 2500)
        );

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
    }

    // ---------- createEmployee ----------
    @Test
    void createEmployee_savesAndReturnsEmployee(){

        Employee newEmployee = new Employee(null, "GoodEmployee", "Engineering", 2000 );
        Employee savedEmployee = new Employee(1L, "GoodEmployee", "Engineering", 2000);

        when(employeeRepository.save(newEmployee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertThat(result.getId()).isEqualTo(savedEmployee.getId());
        assertThat(result.getName()).isEqualTo(savedEmployee.getName());
        verify(employeeRepository).save(newEmployee);
    }

    // ---------- updateEmployee ----------
    @Test
    void updateEmployee_updateAndReturnEmployee_whenExists() {

        Employee existing = new Employee(1L, "Anna", "HR", 3000);
        Employee updated = new Employee(null, "Anna Doe", "HR", 2900);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(existing)).thenReturn(existing);

        Optional<Employee> result = employeeService.updateEmployee(1L, updated);

        assertThat(employeeRepository.findById(1L)).isPresent();
        assertThat(result.get().getName()).isEqualTo("Anna Doe");
        assertThat(result.get().getDepartment()).isEqualTo("HR");
        assertThat(result.get().getSalary()).isEqualTo(2900);

    }

    // ---------- deleteEmployee ----------
    @Test
    void deleteEmployee_returnsTrue_whenEmployeeExists() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        boolean result = employeeService.deleteEmployee(1L);

        assertThat(result).isTrue();
        verify(employeeRepository).deleteById(1L);

    }
    @Test
    void deleteEmployee_returnFalse_whenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        boolean result = employeeService.deleteEmployee(1L);

        assertThat(result).isFalse();
        verify(employeeRepository, never()).deleteById(any());
    }

    // ---------- getEmployeesByDepartment ----------
    @Test
    void getEmployeesByDepartment_returnAllEmployees_whenDepartmentIsFound() {
        List<Employee> employees = List.of(
                new Employee(1L, "John", "Sales", 2000),
                new Employee(2L, "Doe", "HR", 1500)
        );

        when(employeeRepository.findByDepartmentIgnoreCase("sales")).thenReturn(List.of(employees.getFirst()));
        when(employeeRepository.findByDepartmentIgnoreCase("hr")).thenReturn(List.of(employees.get(1)));

        List<Employee> result = employeeService.getEmployeesByDepartment("sales");

        assertThat(result).hasSize(1);
        verify(employeeRepository).findByDepartmentIgnoreCase("sales");

        result = employeeService.getEmployeesByDepartment("hr");
        assertThat(result.size()).isEqualTo(1);
        verify(employeeRepository).findByDepartmentIgnoreCase("hr");
    }
    @Test
    void getEmployeesByDepartment_returnEmpty_whenDepartmentIsNotFound() {
        when(employeeRepository.findByDepartmentIgnoreCase("hr")).thenReturn(List.of());

        List<Employee> result = employeeService.getEmployeesByDepartment("hr");

        assertThat(result).isEmpty();
        verify(employeeRepository).findByDepartmentIgnoreCase("hr");
    }

    // ---------- getDepartmentStats ----------
    @Test
        void getDepartmentStats_returnAllStats() {
            List<Employee> employees = List.of(
                new Employee(1L, "John", "hr", 2000),
                new Employee(2L, "Olivia", "hr", 2300),
                new Employee(3L, "David", "sales", 3600),
                new Employee(4L, "Hassan", "engineering", 4100 ),
                new Employee(5L, "Stacy", "engineering", 3000),
                new Employee(6L, "Tom", "engineering", 3700)
            );
            List<DepartmentStatsDTO> expected = List.of(
                    new DepartmentStatsDTO("hr", 2L, 2150.0),
                    new DepartmentStatsDTO("sales", 1L, 3600.0),
                    new DepartmentStatsDTO("engineering", 3L, 3600.0)
            );

            when(employeeRepository.findAll()).thenReturn(employees);

            List<DepartmentStatsDTO> result = employeeService.getDepartmentStats();

            verify(employeeRepository).findAll();
            assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        }



}
