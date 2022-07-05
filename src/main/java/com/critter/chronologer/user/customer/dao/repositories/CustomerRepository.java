package com.critter.chronologer.user.customer.dao.repositories;

import com.critter.chronologer.user.customer.dao.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPetsId(Long petId);
}
