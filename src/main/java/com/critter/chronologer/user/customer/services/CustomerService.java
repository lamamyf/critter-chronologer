package com.critter.chronologer.user.customer.services;

import com.critter.chronologer.core.exception.BusinessException;
import com.critter.chronologer.user.customer.dao.entities.Customer;
import com.critter.chronologer.user.customer.dao.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer save(String name, String phoneNumber, String notes) {
        return customerRepository.save(new Customer(name, phoneNumber, notes));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByPetId(Long petId) {
        return customerRepository.findByPetsId(petId)
                .orElseThrow(() -> new BusinessException("Invalid pet id"));
    }

    @Transactional
    public Customer getCustomerReferenceById(Long id){
        var existsById = customerRepository.existsById(id);
        if(!existsById){
            throw new BusinessException("Invalid customer id");
        }

        return customerRepository.getReferenceById(id);
    }
}
