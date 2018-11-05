package com.bpcbt.lessons.spring.repository.impl;

import com.bpcbt.lessons.spring.exception.*;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.CurrencyRate;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.AccountRepository;
import com.bpcbt.lessons.spring.repository.CurrencyRateRepository;
import com.bpcbt.lessons.spring.repository.CustomerRepository;
import com.bpcbt.lessons.spring.repository.MainRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaRepositoryImpl implements MainRepository {

    @Getter
    private AccountRepository accountRepository;
    @Getter
    private CustomerRepository customerRepository;
    @Getter
    private CurrencyRateRepository currencyRateRepository;

    @Autowired
    public JpaRepositoryImpl(AccountRepository accountRepository, CustomerRepository customerRepository, CurrencyRateRepository currencyRateRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public Account getCustomerAccount(String name) {
        return customerRepository.findCustomerByName(name).map(Customer::getAccount).orElseThrow(() -> new AccountNotFoundException());
    }

    @Override
    public Integer convertAmount(Account account, String currencyTo) {
        return convertAmount(account.getAmount(), account.getCurrency(), currencyTo);
    }

    @Override
    public Integer convertAmount(Integer amount, String currencyFrom, String currencyTo) {
        return currencyRateRepository.findCurrencyRateByCurrency1AndCurrency2(currencyFrom, currencyTo)
                .map(o -> Math.round(o.getMultiplier() * amount)).orElseThrow(() -> new AmountConversionException());
    }

    @Override
    public void transfer(String customerFrom, String customerTo, Integer amount, String currency) {
        Account accountFrom = getCustomerAccount(customerFrom);
        Account accountTo = getCustomerAccount(customerTo);
        Integer amount1 = convertAmount(accountFrom, DEFAULT_CURRENCY);
        Integer amount2 = convertAmount(accountTo, DEFAULT_CURRENCY);
        amount = convertAmount(amount, currency, DEFAULT_CURRENCY);
        if (amount1 - amount < 0) {
            throw new MoneyTransferException("Impossible to transfer money");
        }

        amount1 = convertAmount(amount1 - amount, DEFAULT_CURRENCY, accountFrom.getCurrency());
        amount2 = convertAmount(amount2 + amount, DEFAULT_CURRENCY, accountTo.getCurrency());

        accountRepository.saveAndFlush(accountFrom.withAmount(amount1));
        accountRepository.saveAndFlush(accountTo.withAmount(amount2));
    }

    @Override
    public void printCustomersAccounts() {
        // todo: printCustomersAccounts
    }

    @Override
    public void printTable(String tableName) {
        // todo: printTable
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Impossible to find account by id"));
    }

    @Override
    public Customer getCustomerByName(String name) {
        return customerRepository.findCustomerByName(name).orElseThrow(() -> new CustomerNotFoundException());
    }

    @Override
    public Account getAccountByAccountNumber(Integer accountNumber) {
        return accountRepository.findFirstByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException());
    }

    @Override
    public List<String> getCurrencies() {
        return currencyRateRepository.findAll().stream().map(CurrencyRate::getCurrency1).distinct().collect(Collectors.toList());
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
        return customerRepository.existsCustomerByName(name);
    }

    @Override
    public Boolean accountWithAccountNumberExists(Integer accountNumber) {
        return accountRepository.existsAccountByAccountNumber(accountNumber);
    }

    @Override
    public void insertCustomerWithAccount(String name, Integer accountNumber, String currency, Integer amount) {
        if (customerWithNameExists(name)) {
            throw new CustomerAlreadyExistsException();
        }
        if (accountWithAccountNumberExists(accountNumber)) {
            throw new AccountAlreadyExistsException();
        }
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAmount(amount);
        account.setCurrency(currency);
        accountRepository.saveAndFlush(account);
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAccount(account);
        customerRepository.saveAndFlush(customer);
    }
}
