package com.critter.chronologer.pet.dto;

import com.critter.chronologer.pet.dao.entities.enms.PetType;
import java.time.LocalDate;

public record PetCreationRequest(PetType type, String name, Long ownerId,
                                 LocalDate birthDate, String notes) { }
