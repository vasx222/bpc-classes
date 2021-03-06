package com.bpcbt.lessons.spring.repository.impl;

import com.bpcbt.lessons.spring.exception.*;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcRepositoryImpl implements MainRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getCustomerAccount(String name) {
        String sql = "select * from customers inner join " +
                "accounts on customers.account_id=accounts.id where customers.name=?";

        List<Account> list = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, name);
                },
                (rs, rowNum) -> new Account(
                        rs.getInt("accounts.id"),
                        rs.getInt("account_number"),
                        rs.getString("currency"),
                        rs.getInt("amount")));
        return list.stream().findFirst().orElseThrow(() -> new AccountNotFoundException("Impossible to find account by name"));
    }

    @Override
    public Integer convertAmount(Account account, String currencyTo) {
        return convertAmount(account.getAmount(), account.getCurrency(), currencyTo);
    }

    @Override
    public Integer convertAmount(Integer amount, String currencyFrom, String currencyTo) {
        String sql = "select * from currency_rates where currency1=? and currency2=?";

        List<Float> list = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, currencyFrom);
                    preparedStatement.setString(2, currencyTo);
                },
                (rs, rowNum) -> rs.getFloat("multiplier"));

        return list.stream().findFirst()
                .map(e -> Math.round(e * amount)).orElseThrow(() -> new AmountConversionException("Impossible to convert amount"));
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

        String sql = "update accounts set amount = ? where id = ?";
        jdbcTemplate.update(sql, amount1, accountFrom.getId());
        jdbcTemplate.update(sql, amount2, accountTo.getId());
    }

    @Override
    public void printCustomersAccounts() {
        String sql = "select * from customers inner join accounts on customers.account_id = accounts.id";
        jdbcTemplate.query(sql, rs -> {
            do {
                System.out.println(
                        "CUSTOMER_ID = " + rs.getInt("customers.id") + " " +
                                "ACCOUNT_ID = " + rs.getInt("accounts.id") + " " +
                                "NAME = " + rs.getString("name") + " " +
                                "ACCOUNT_NUMBER = " + rs.getInt("account_number") + " " +
                                "CURRENCY = " + rs.getString("currency") + " " +
                                "AMOUNT = " + rs.getInt("amount")
                );
            } while (rs.next());
        });
    }

    @Override
    public void printTable(String tableName) {
        System.out.println("tableName = " + tableName);
        jdbcTemplate.query("select * from " + tableName,
                rs -> {
                    do {
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            System.out.print(rs.getMetaData().getColumnName(i) + " = " + rs.getObject(i) + " ");
                        }
                        System.out.println();
                    } while (rs.next());
                }
        );
    }

    @Override
    public Account getAccountById(Integer id) {
        String sql = "select * from accounts where id = ?";
        List<Account> list = jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                (rs, rowNum) -> new Account(
                        rs.getInt("accounts.id"),
                        rs.getInt("account_number"),
                        rs.getString("currency"),
                        rs.getInt("amount")));
        return list.stream().findFirst().orElseThrow(() -> new AccountNotFoundException("Impossible to find account by id"));
    }

    @Override
    public Customer getCustomerByName(String name) {
        String sql = "select * from customers where customers.name = ?";
        List<Customer> list = jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setString(1, name),
                (rs, rowNum) -> new Customer(
                        rs.getInt("customers.id"),
                        rs.getString("customers.name"),
                        getAccountById(rs.getInt("customers.account_id"))));
        return list.stream().findFirst().orElseThrow(() -> new CustomerNotFoundException("Impossible to find customer by name"));
    }

    @Override
    public Account getAccountByAccountNumber(Integer accountNumber) {
        String sql = "select * from accounts where account_number = ?";
        List<Account> list = jdbcTemplate.query(sql,
                preparedStatement -> preparedStatement.setInt(1, accountNumber),
                (rs, rowNum) -> new Account(
                        rs.getInt("accounts.id"),
                        rs.getInt("account_number"),
                        rs.getString("currency"),
                        rs.getInt("amount")));
        return list.stream().findFirst().orElseThrow(() -> new AccountNotFoundException("Impossible to find account by account_number"));
    }

    @Override
    public List<String> getCurrencies() {
        String sql = "select distinct currency1 from currency_rates";
        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("currency1"));
    }

    @Override
    public List<Customer> getCustomers() {
        String sql = "select * from customers";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Customer(
                rs.getInt("customers.id"),
                rs.getString("customers.name"),
                getAccountById(rs.getInt("customers.account_id"))));
    }

    @Override
    public List<Account> getAccounts() {
        String sql = "select * from accounts";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Account(
                rs.getInt("accounts.id"),
                rs.getInt("accounts.account_number"),
                rs.getString("accounts.currency"),
                rs.getInt("accounts.amount")));
    }

    @Override
    public Boolean customerWithNameExists(String name) {
        try {
            getCustomerByName(name);
        } catch (CustomerNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean accountWithAccountNumberExists(Integer accountNumber) {
        try {
            getAccountByAccountNumber(accountNumber);
        } catch (AccountNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void insertCustomerWithAccount(String name, Integer accountNumber, String currency, Integer amount) {
        if (customerWithNameExists(name)) {
            throw new CustomerAlreadyExistsException();
        }
        if (accountWithAccountNumberExists(accountNumber)) {
            throw new AccountAlreadyExistsException();
        }
        final String sql1 = "insert into accounts (account_number, currency, amount) values(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sql1, new String[]{"id"});
                    ps.setInt(1, accountNumber);
                    ps.setString(2, currency);
                    ps.setInt(3, amount);
                    return ps;
                },
                keyHolder);
        final String sql2 = "insert into customers (name, account_id) values(?, ?)";
        jdbcTemplate.update(sql2, name, keyHolder.getKey());
    }
}
