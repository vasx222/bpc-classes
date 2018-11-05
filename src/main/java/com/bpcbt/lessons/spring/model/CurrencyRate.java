package com.bpcbt.lessons.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "currency1")
    private String currency1;
    @Column(name = "currency2")
    private String currency2;
    @Column(name = "multiplier")
    private Float multiplier;
}
