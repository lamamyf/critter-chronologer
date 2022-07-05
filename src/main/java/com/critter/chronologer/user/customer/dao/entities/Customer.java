package com.critter.chronologer.user.customer.dao.entities;

import com.critter.chronologer.pet.dao.entities.Pet;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String phoneNumber;

    private String notes;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pet> pets;

    public Customer() {
    }

    public Customer(String name, String phoneNumber, String notes) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }
}
