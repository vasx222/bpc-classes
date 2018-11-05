package com.bpcbt.lessons.spring.repository;

import com.bpcbt.lessons.spring.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
