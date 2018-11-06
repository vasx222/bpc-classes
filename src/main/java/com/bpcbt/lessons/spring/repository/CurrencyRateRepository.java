package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {
    Optional<CurrencyRate> findCurrencyRateByCurrency1AndCurrency2(String currency1, String currency2);
}
