package com.bpcbt.lessons.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "account_number")
    private Integer accountNumber;
    @Column(name = "currency")
    private String currency;
    @Column(name = "amount")
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
}
