package com.critter.chronologer.user.employee.dto;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents the form that employee response data takes. Does not map
 * to the database directly.
 */
public interface EmployeeDTO {
    Long getId();
    String getName();
    Set<EmployeeSkill> getSkills();
    Set<DayOfWeek> getDaysAvailable();
}
