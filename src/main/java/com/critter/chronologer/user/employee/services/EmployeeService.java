package com.critter.chronologer.user.employee.services;

import com.critter.chronologer.core.exception.BusinessException;
import com.critter.chronologer.user.employee.dao.entities.Employee;
import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import com.critter.chronologer.user.employee.dao.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee save(String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable) {
        return employeeRepository.save(new Employee(name, skills, daysAvailable));
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new BusinessException("Invalid employee id"));
    }

    @Transactional
    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = findById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date){
        return employeeRepository.findDistinctBySkillsInAndDaysAvailableIn(skills, Set.of(date.getDayOfWeek()));
    }

    @Transactional
    public Employee getEmployeeReferenceById(Long id){
        var existsById = employeeRepository.existsById(id);
        if(!existsById){
            throw new BusinessException("Invalid employee id");
        }

        return employeeRepository.getReferenceById(id);
    }
}
