package com.critter.chronologer.schedule.dto;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Represents the form that schedule response data takes. Does not map
 * to the database directly.
 */
public interface ScheduleDTO {
    Long getId();

    @Value("#{target.getEmployees()?.![getId()]}")
    List<Long> getEmployeeIds();

    @Value("#{target.getPets()?.![getId()]}")
    List<Long> getPetIds();

    LocalDate getDate();

    Set<EmployeeSkill> getActivities();
}
