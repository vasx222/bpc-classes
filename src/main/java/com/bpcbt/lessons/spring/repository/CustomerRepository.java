package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
