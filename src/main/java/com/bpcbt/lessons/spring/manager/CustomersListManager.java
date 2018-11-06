package com.bpcbt.lessons.spring.manager;

import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("request")
public class CustomersListManager {

    private List<Customer> customers;
    private MainRepository mainRepository;

    @Autowired
    public CustomersListManager(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
        this.customers = mainRepository.getCustomers();
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
