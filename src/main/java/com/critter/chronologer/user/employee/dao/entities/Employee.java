package com.critter.chronologer.user.employee.dao.entities;

import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @CollectionTable
    private Set<DayOfWeek> daysAvailable;
}
