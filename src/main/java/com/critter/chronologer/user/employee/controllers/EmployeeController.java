package com.critter.chronologer.user.employee.controllers;

import com.critter.chronologer.user.employee.dao.entities.Employee;
import com.critter.chronologer.user.employee.dto.EmployeeCreationRequest;
import com.critter.chronologer.user.employee.dto.EmployeeDTO;
import com.critter.chronologer.user.employee.dto.FindEmployeesRequest;
import com.critter.chronologer.user.employee.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Employees.
 */
@RestController
@RequestMapping("/user/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SpelAwareProxyProjectionFactory projectionFactory;

    @PostMapping
    public EmployeeDTO saveEmployee(@RequestBody EmployeeCreationRequest request) {
        Employee employee = employeeService.save(request.name(), request.skills(), request.daysAvailable());
        return toEmployeeDto(employee);
    }

    @GetMapping("/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        return toEmployeeDto(employee);
    }

    @PutMapping("/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody FindEmployeesRequest request) {
        return employeeService.findEmployeesForService(request.skills(), request.date())
                .stream().map(this::toEmployeeDto).toList();
    }

    private EmployeeDTO toEmployeeDto(Employee employee){
        return projectionFactory.createProjection(EmployeeDTO.class, employee);
    }

}
