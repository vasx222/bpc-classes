package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findFirstByAccountNumber(Integer accountNumber);
    boolean existsAccountByAccountNumber(Integer accountNumber);
}
