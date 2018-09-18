package com.bpcbt.lessons.spring.task1;

public class Account {
    private final Integer id;
    private final Integer accountNumber;
    private final String currency;
    private final Integer amount;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }

    Account(Integer id, Integer accountNumber, String currency, Integer amount) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getAmount() {
        return amount;
    }
}
