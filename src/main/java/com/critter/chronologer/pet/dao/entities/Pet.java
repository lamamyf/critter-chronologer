package com.critter.chronologer.pet.dao.entities;

import com.critter.chronologer.pet.dao.entities.enms.PetType;
import com.critter.chronologer.user.customer.dao.entities.Customer;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
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

    public Pet() {
    }

    public Pet(PetType type, String name, Customer owner, LocalDate birthDate, String notes) {
        this.type = type;
        this.name = name;
        this.owner = owner;
        this.birthDate = birthDate;
        this.notes = notes;
    }
}
