package com.critter.chronologer.pet.dao.entities;

import com.critter.chronologer.pet.dao.entities.enms.PetType;
import com.critter.chronologer.user.entities.Customer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Pet {

    @Id
    @GeneratedValue
    private Long id;

    private PetType type;

    private String name;

    @ManyToOne
    @JoinColumn
    private Customer owner;

    private LocalDate birthDate;

    private String notes;
}
