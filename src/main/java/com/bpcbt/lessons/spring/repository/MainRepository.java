package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;

import java.util.List;

public interface MainRepository {
    String DEFAULT_CURRENCY = "RUB";

    Account getCustomerAccount(String name);

    Integer convertAmount(Account account, String currencyTo);

    Integer convertAmount(Integer amount, String currencyFrom, String currencyTo);

    void transfer(String customerFrom, String customerTo, Integer amount, String currency);

    void printCustomersAccounts();

    void printTable(String tableName);

    Account getAccountById(Integer id);

    Customer getCustomerByName(String name);

    Account getAccountByAccountNumber(Integer accountNumber);

    List<String> getCurrencies();

    List<Customer> getCustomers();

    List<Account> getAccounts();

    Boolean customerWithNameExists(String name);

    Boolean accountWithAccountNumberExists(Integer accountNumber);

    void insertCustomerWithAccount(String name, Integer accountNumber, String currency, Integer amount);
}
