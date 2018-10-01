package com.bpcbt.lessons.spring.task1;

public class Account {
    private Integer id;
    private Integer accountNumber;
    private String currency;
    private Integer amount;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }

    public Account(Integer id, Integer accountNumber, String currency, Integer amount) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
