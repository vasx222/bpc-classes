package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.exception.AccountNotFoundException;
import com.bpcbt.lessons.spring.exception.AmountConversionException;
import com.bpcbt.lessons.spring.exception.CustomerNotFoundException;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.CurrencyRate;
import com.bpcbt.lessons.spring.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaRepositoryImpl implements MainRepository {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CurrencyRateRepository currencyRateRepository;

    @Autowired
    public JpaRepositoryImpl(AccountRepository accountRepository, CustomerRepository customerRepository, CurrencyRateRepository currencyRateRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public Account getCustomerAccount(String name) {
        return null;
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

    }
}
