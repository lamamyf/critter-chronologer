package com.critter.chronologer.pet.dao.repositories;

import com.critter.chronologer.pet.dao.entities.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long ownerId);
}
