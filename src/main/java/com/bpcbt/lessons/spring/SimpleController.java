package com.bpcbt.lessons.spring;

import com.bpcbt.lessons.spring.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleController {

    private JdbcTemplate jdbcTemplate;
    private static final String DEFAULT_CURRENCY = "RUB";

    @Autowired
    public SimpleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        return list.stream().findFirst().orElseThrow(() -> new RuntimeException("Impossible to find account by name"));
    }

    public Integer convertAmount(Account account, String currencyTo) {
        return convertAmount(account.getAmount(), account.getCurrency(), currencyTo);
    }

    public Integer convertAmount(Integer amount, String currencyFrom, String currencyTo) {
        String sql = "select * from currency_rates where currency1=? and currency2=?";

        List<Float> list = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, currencyFrom);
                    preparedStatement.setString(2, currencyTo);
                },
                (rs, rowNum) -> rs.getFloat("multiplier"));

        return list.stream().findFirst()
                .map(e -> Math.round(e * amount)).orElseThrow(() -> new RuntimeException("Impossible to convert amount"));
    }

    public void transfer(String customerFrom, String customerTo, Integer amount, String currency) {
        Account accountFrom = getCustomerAccount(customerFrom);
        Account accountTo = getCustomerAccount(customerTo);
        Integer amount1 = convertAmount(accountFrom, DEFAULT_CURRENCY);
        Integer amount2 = convertAmount(accountTo, DEFAULT_CURRENCY);
        amount = convertAmount(amount, currency, DEFAULT_CURRENCY);
        if (amount1 - amount < 0) {
            System.out.println("Sender has not enough money");
            return;
        }

        amount1 = convertAmount(amount1 - amount, DEFAULT_CURRENCY, accountFrom.getCurrency());
        amount2 = convertAmount(amount2 + amount, DEFAULT_CURRENCY, accountTo.getCurrency());

        String sql = "update accounts set amount = ? where id = ?";
        jdbcTemplate.update(sql, amount1, accountFrom.getId());
        jdbcTemplate.update(sql, amount2, accountTo.getId());
    }

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

    public void insertAccount(Account account) {
        String sql = "insert into accounts values (?, ?, ?, ?)";
        int inserted = jdbcTemplate.update(sql, account.getId(), account.getAccountNumber(), account.getCurrency(), account.getAmount());
        if (inserted != 0) {
            System.out.println("Account inserted");
        } else {
            System.out.println("Account was not inserted");
        }
    }

    public Account getAccountById(Integer id) {
        String sql = "select * from accounts where id = ?";
        List<Account> list = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setInt(1, id);
                },
                (rs, rowNum) -> new Account(
                        rs.getInt("accounts.id"),
                        rs.getInt("account_number"),
                        rs.getString("currency"),
                        rs.getInt("amount")));
        return list.stream().findFirst().orElseThrow(() -> new RuntimeException("Impossible to find account by id"));
    }
}
