package com.critter.chronologer.user.customer.controllers;


import com.critter.chronologer.user.customer.dao.entities.Customer;
import com.critter.chronologer.user.customer.dto.CustomerCreationRequest;
import com.critter.chronologer.user.customer.dto.CustomerDTO;
import com.critter.chronologer.user.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Customers.
 */
@RestController
@RequestMapping("/user/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final SpelAwareProxyProjectionFactory projectionFactory;

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerCreationRequest request){
        Customer customer = customerService.save(request.name(), request.phoneNumber(), request.notes());
        return toCustomerDto(customer);
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers(){
        return customerService.findAll().stream().map(this::toCustomerDto).toList();
    }

    @GetMapping("/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = customerService.findCustomerByPetId(petId);
        return toCustomerDto(customer);
    }

    private CustomerDTO toCustomerDto(Customer customer){
        return projectionFactory.createProjection(CustomerDTO.class, customer);
    }
}
