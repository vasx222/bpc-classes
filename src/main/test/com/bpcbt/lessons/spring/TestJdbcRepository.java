package com.bpcbt.lessons.spring;

import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.JdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class TestJdbcRepository {
    private static JdbcRepository jdbcRepository;

    @BeforeClass
    public static void initRepository() {
        ApplicationContext applicationContext = SpringApplication.run(SpringPrimeFacesApplication.class);
        jdbcRepository = applicationContext.getBean(JdbcRepository.class);
    }

    @Test
    public void checkTransfer() {
        Account accountFrom = jdbcRepository.getCustomerAccount("Vasily");
        Account accountTo = jdbcRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountFrom.getAmount()).isEqualTo(200);
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100000);
        jdbcRepository.transfer("Vasily", "Ilya", 300, "RUB");
        accountFrom = jdbcRepository.getCustomerAccount("Vasily");
        accountTo = jdbcRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100300);
        Assertions.assertThat(Math.round((200f * 79.46f - 300f) * 0.01f)).isEqualTo(accountFrom.getAmount());

        Assertions.assertThatThrownBy(() -> jdbcRepository.transfer("Vasily", "Ilya", 10000000, "RUB"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkPrint() {
        jdbcRepository.printCustomersAccounts();
    }

    @Test
    public void checkGetCustomerAccount() {
        Assertions.assertThat(jdbcRepository.getCustomerAccount("Vasily"))
                .isEqualToComparingOnlyGivenFields(new Account(5, 22222, "EUR", 200),
                        "id", "accountNumber", "currency");
        Assertions.assertThatThrownBy(() -> jdbcRepository.getCustomerAccount("NoSuchName"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkConvertAmount() {
        Assertions.assertThat(jdbcRepository.convertAmount(100, "RUB", "USD")).isEqualTo(2);
        Assertions.assertThatThrownBy(() -> jdbcRepository.convertAmount(200, "A", "B"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkInsertCustomerWithAccount() {
        jdbcRepository.insertCustomerWithAccount("Bob", 12345, "RUB", 1000);
        Customer customer1 = jdbcRepository.getCustomerByName("Bob");
        Account account1 = jdbcRepository.getAccountByAccountNumber(12345);

        Assertions.assertThat(customer1.getName()).isEqualTo("Bob");
        Assertions.assertThat(account1.getAccountNumber()).isEqualTo(12345);
        Assertions.assertThat(account1.getCurrency()).isEqualTo("RUB");
        Assertions.assertThat(account1.getAmount()).isEqualTo(1000);
        Assertions.assertThat(customer1.getAccountId()).isEqualTo(account1.getId());
    }

    @Test
    public void checkExistMethods() {
        Assertions.assertThat(jdbcRepository.customerWithNameExists("Vasily")).isTrue();
        Assertions.assertThat(jdbcRepository.customerWithNameExists("NoSuchName")).isFalse();
        Assertions.assertThat(jdbcRepository.accountWithAccountNumberExists(22222)).isTrue();
        Assertions.assertThat(jdbcRepository.accountWithAccountNumberExists(-1)).isFalse();
    }
}
