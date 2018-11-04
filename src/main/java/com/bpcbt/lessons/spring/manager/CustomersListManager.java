package com.bpcbt.lessons.spring.manager;

import com.bpcbt.lessons.spring.repository.JdbcRepository;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope("request")
public class CustomersListManager {

    private List<Customer> customers;
    private Map<Integer, Account> accounts;
    private JdbcRepository jdbcRepository;

    @Autowired
    public CustomersListManager(JdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
        this.customers = jdbcRepository.getCustomers();
        this.accounts = jdbcRepository.getAccounts().stream().collect(Collectors.toMap(Account::getId, Function.identity()));
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Map<Integer, Account> getAccounts() {
        return accounts;
    }
}
