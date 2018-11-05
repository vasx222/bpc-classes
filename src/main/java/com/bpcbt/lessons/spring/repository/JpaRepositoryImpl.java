package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaRepositoryImpl implements MainRepository {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public JpaRepositoryImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Account getCustomerAccount(String name) {
        return null;
    }

    @Override
    public Integer convertAmount(Account account, String currencyTo) {
        return null;
    }

    @Override
    public Integer convertAmount(Integer amount, String currencyFrom, String currencyTo) {
        return null;
    }

    @Override
    public void transfer(String customerFrom, String customerTo, Integer amount, String currency) {

    }

    @Override
    public void printCustomersAccounts() {

    }

    @Override
    public void printTable(String tableName) {

    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Impossible to find account by id"));
    }

    @Override
    public Customer getCustomerByName(String name) {
        return null;
    }

    @Override
    public Account getAccountByAccountNumber(Integer accountNumber) {
        return null;
    }

    @Override
    public List<String> getCurrencies() {
        return null;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Boolean customerWithNameExists(String name) {
        return null;
    }

    @Override
    public Boolean accountWithAccountNumberExists(Integer accountNumber) {
        return null;
    }

    @Override
    public void insertCustomerWithAccount(String name, Integer accountNumber, String currency, Integer amount) {

    }
}
