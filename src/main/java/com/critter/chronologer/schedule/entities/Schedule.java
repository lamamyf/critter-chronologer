package com.critter.chronologer.schedule.entities;

import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.user.entities.Employee;
import com.critter.chronologer.user.entities.enums.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(fetch =  FetchType.LAZY)
    private List<Employee> employees;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection
    @CollectionTable
    private Set<EmployeeSkill> activities;
}
