package com.critter.chronologer.user.services;

import com.critter.chronologer.core.exception.BusinessException;
import com.critter.chronologer.user.dao.entities.Customer;
import com.critter.chronologer.user.dao.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerReferenceById(Long id){
        var existsById = customerRepository.existsById(id);
        if(!existsById){
            throw new BusinessException("Invalid customer id");
        }

        return customerRepository.getReferenceById(id);
    }
}
