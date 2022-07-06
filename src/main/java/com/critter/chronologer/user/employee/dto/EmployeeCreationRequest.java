package com.critter.chronologer.user.employee.dto;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;

import java.time.DayOfWeek;
import java.util.Set;

public record EmployeeCreationRequest(String name, Set<EmployeeSkill>
                                      skills, Set<DayOfWeek> daysAvailable) {
}
