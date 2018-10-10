package com.bpcbt.lessons.spring.model;

public class Customer {
    private Integer id;
    private String name;
    private Integer accountId;

    public Customer(Integer id, String name, Integer accountId) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
