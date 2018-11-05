package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByName(String name);
    boolean existsCustomerByName(String name);
}
