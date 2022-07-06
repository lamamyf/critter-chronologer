package com.critter.chronologer.schedule.dao.entities;

import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.user.employee.dao.entities.Employee;
import com.critter.chronologer.user.employee.dao.entities.enums.EmployeeSkill;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
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

    public Schedule() {
    }

    public Schedule(List<Employee> employees, List<Pet> pets, LocalDate date, Set<EmployeeSkill> activities) {
        this.employees = employees;
        this.pets = pets;
        this.date = date;
        this.activities = activities;
    }
}
