package com.critter.chronologer.pet.controllers;

import com.critter.chronologer.pet.dao.entities.Pet;
import com.critter.chronologer.pet.dto.PetCreationRequest;
import com.critter.chronologer.pet.dto.PetDTO;
import com.critter.chronologer.pet.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final SpelAwareProxyProjectionFactory projectionFactory;

    @PostMapping
    public PetDTO savePet(@RequestBody PetCreationRequest request) {
        Pet pet = petService.save(request.type(), request.name(), request.ownerId(), request.birthDate(), request.notes());
        return toPetDto(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findById(petId);
        return toPetDto(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.findAll().stream().map(this::toPetDto).toList();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.findByOwnerId(ownerId).stream().map(this::toPetDto).toList();
    }

    private PetDTO toPetDto(Pet pet){
        return projectionFactory.createProjection(PetDTO.class, pet);
    }
}
