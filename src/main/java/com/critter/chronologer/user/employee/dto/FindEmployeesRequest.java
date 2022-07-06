package com.critter.chronologer.user.employee.dto;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a request to find available employees by skills.
 */
public record FindEmployeesRequest(Set<EmployeeSkill> skills, LocalDate date) {
}
