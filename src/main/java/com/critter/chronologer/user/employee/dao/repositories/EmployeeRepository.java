package com.critter.chronologer.user.employee.dao.repositories;

import com.critter.chronologer.user.employee.dao.entities.Employee;
import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findDistinctBySkillsInAndDaysAvailableIn(Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable);
}
