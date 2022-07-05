package com.critter.chronologer.pet.services;

import com.critter.chronologer.core.exception.BusinessException;
import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.pet.dao.entities.enms.PetType;
import com.critter.chronologer.pet.dao.repositories.PetRepository;
import com.critter.chronologer.user.customer.dao.entities.Customer;
import com.critter.chronologer.user.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final CustomerService customerService;

    public Pet save(PetType type, String name, long ownerId, LocalDate birthDate, String notes) {
        Customer owner = customerService.getCustomerReferenceById(ownerId);
        Pet pet =  new Pet(type, name, owner, birthDate, notes);
        return petRepository.save(pet);
    }

    public Pet findById(Long id){
        return petRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Invalid pet id"));
    }

    public List<Pet> findAll(){
        return petRepository.findAll();
    }

    public List<Pet> findByOwnerId(Long ownerId){
        return petRepository.findByOwnerId(ownerId);
    }
}
