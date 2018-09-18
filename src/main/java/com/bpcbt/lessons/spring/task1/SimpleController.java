package com.bpcbt.lessons.spring.task1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Component
public class SimpleController {

    private JdbcTemplate jdbcTemplate;
    private static final String DEFAULT_CURRENCY = "RUB";

    @Autowired
    public SimpleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void run() {
        printCustomersAccounts();
        transfer("Vasily", "Ilya", 100, "RUB");
        printCustomersAccounts();
    }

    Account getCustomerAccount(String name) {
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
        try {
            return list.get(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No account for user with name + " + name);
            return null;
        }
    }

    Integer convertAmount(Account account, String currencyTo) {
        return convertAmount(account.getAmount(), account.getCurrency(), currencyTo);
    }

    Integer convertAmount(Integer amount, String currencyFrom, String currencyTo) {
        String sql = "select * from currency_rates where currency1=? and currency2=?";

        List<Float> list = jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, currencyFrom);
                    preparedStatement.setString(2, currencyTo);
                },
                (rs, rowNum) -> {
                    Float multiplier = rs.getFloat("multiplier");
                    return amount * multiplier;
                });

        try {
            return Math.round(list.get(0));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Impossible to convert currency");
            return null;
        }
    }

    void transfer(String customerFrom, String customerTo, Integer amount, String currency) {
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

    void printCustomersAccounts() {
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

    void printTable(String tableName) {
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
}
