package com.critter.chronologer.schedule.dto;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ScheduleCreationRequest(List<Long> employeeIds,  List<Long> petIds,
                                      LocalDate date, Set<EmployeeSkill> activities) {
}
