package com.critter.chronologer.pet.services;

import com.critter.chronologer.core.exception.BusinessException;
import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.pet.dao.entities.enms.PetType;
import com.critter.chronologer.pet.dao.repositories.PetRepository;
import com.critter.chronologer.user.customer.dao.entities.Customer;
import com.critter.chronologer.user.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final CustomerService customerService;

    @Transactional
    public Pet save(PetType type, String name, long ownerId, LocalDate birthDate, String notes) {
        Customer owner = customerService.getCustomerReferenceById(ownerId);
        Pet pet =  petRepository.save(new Pet(type, name, owner, birthDate, notes));

        if(owner.getPets() != null){
            owner.getPets().add(pet);
        }else {
            List<Pet> pets = new ArrayList<>();
            pets.add(pet);
            owner.setPets(pets);
        }

        return pet;
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

    @Transactional
    public Pet getPetReferenceById(Long id){
        var existsById = petRepository.existsById(id);
        if(!existsById){
            throw new BusinessException("Invalid pet id");
        }

        return petRepository.getReferenceById(id);
    }
}
