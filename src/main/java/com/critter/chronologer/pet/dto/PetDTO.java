package com.critter.chronologer.pet.dto;

import com.critter.chronologer.pet.dao.entities.enms.PetType;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
public interface PetDTO {
    Long getId();
    PetType getType();
    String getName();
    @Value("#{target.getOwner().getId()}")
    Long getOwnerId();
    LocalDate getBirthDate();
    String getNotes();
}
