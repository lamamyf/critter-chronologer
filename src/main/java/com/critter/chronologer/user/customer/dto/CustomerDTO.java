package com.critter.chronologer.user.customer.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */
public interface CustomerDTO {
    Long getId();
    String getName();
    String getPhoneNumber();
    String getNotes();
    @Value("#{target.getPets()?.![getId()]}")
    List<Long> getPetIds();
}
