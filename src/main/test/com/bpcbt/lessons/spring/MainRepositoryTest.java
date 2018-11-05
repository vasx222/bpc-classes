package com.bpcbt.lessons.spring;

import com.bpcbt.lessons.spring.exception.*;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.MainRepository;
import com.bpcbt.lessons.spring.repository.impl.JpaRepositoryImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MainRepositoryTest {

    @Parameterized.Parameter
    public String repositoryName;

    private static MainRepository mainRepository;

    private static ApplicationContext context;

    @Parameterized.Parameters(name = "Tests for {0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]{
                "jdbcRepositoryImpl", "jpaRepositoryImpl"
        });
    }

    @BeforeClass
    public static void initContext() {
        context = SpringApplication.run(SpringPrimeFacesApplication.class);
    }

    @Before
    public void initRepository() {
        mainRepository = (MainRepository) context.getBean(repositoryName);
    }

    @Test
    public void checkTransfer() {
        JpaRepositoryImpl jpaRepository = context.getBean(JpaRepositoryImpl.class);
        long accountsNumber = jpaRepository.getAccountRepository().count();
        long customersNumber = jpaRepository.getCustomerRepository().count();

        Account accountFrom = mainRepository.getCustomerAccount("Vasily");
        Account accountTo = mainRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountFrom.getAccountNumber()).isEqualTo(22222);
        Assertions.assertThat(accountTo.getAccountNumber()).isEqualTo(11111);
        mainRepository.transfer("Vasily", "Ilya", 300, "RUB");
        Account accountFrom1 = mainRepository.getCustomerAccount("Vasily");
        Account accountTo1 = mainRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountTo1.getAmount()).isEqualTo(accountTo.getAmount() + 300);
        Assertions.assertThat(Math.round(((float)accountFrom.getAmount() * 79.46f - 300f) * 0.01f)).isEqualTo(accountFrom1.getAmount());

        Assertions.assertThat(accountsNumber).isEqualTo(jpaRepository.getAccountRepository().count());
        Assertions.assertThat(customersNumber).isEqualTo(jpaRepository.getCustomerRepository().count());

        Assertions.assertThatThrownBy(() -> mainRepository.transfer("Vasily", "Ilya", 10000000, "RUB"))
                .isExactlyInstanceOf(MoneyTransferException.class);
    }

    @Test
    public void checkPrint() {
        mainRepository.printCustomersAccounts();
    }

    @Test
    public void checkGetCustomerAccount() {
        Assertions.assertThat(mainRepository.getCustomerAccount("Vasily"))
                .isEqualToComparingOnlyGivenFields(new Account(5, 22222, "EUR", 200),
                        "id", "accountNumber", "currency");
        Assertions.assertThatThrownBy(() -> mainRepository.getCustomerAccount("NoSuchName"))
                .isExactlyInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void checkConvertAmount() {
        Assertions.assertThat(mainRepository.convertAmount(100, "RUB", "USD")).isEqualTo(2);
        Assertions.assertThatThrownBy(() -> mainRepository.convertAmount(200, "A", "B"))
                .isExactlyInstanceOf(AmountConversionException.class);
    }

    @Test
    public void checkInsertCustomerWithAccount() {
        JpaRepositoryImpl jpaRepository = context.getBean(JpaRepositoryImpl.class);
        long accountsNumber = jpaRepository.getAccountRepository().count();
        long customersNumber = jpaRepository.getCustomerRepository().count();

        String customerName = RandomStringUtils.randomAlphabetic(10);
        Integer accountNumber = Integer.valueOf(RandomStringUtils.randomNumeric(6));

        mainRepository.insertCustomerWithAccount(customerName, accountNumber, "RUB", 1000);
        Customer customer = mainRepository.getCustomerByName(customerName);
        Account account = mainRepository.getAccountByAccountNumber(accountNumber);

        Assertions.assertThat(customer.getName()).isEqualTo(customerName);
        Assertions.assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
        Assertions.assertThat(account.getCurrency()).isEqualTo("RUB");
        Assertions.assertThat(account.getAmount()).isEqualTo(1000);
        Assertions.assertThat(customer.getAccount()).isEqualToComparingFieldByField(account);

        Assertions.assertThat(accountsNumber + 1).isEqualTo(jpaRepository.getAccountRepository().count());
        Assertions.assertThat(customersNumber + 1).isEqualTo(jpaRepository.getCustomerRepository().count());

        Assertions.assertThatThrownBy(() ->
                mainRepository.insertCustomerWithAccount("Vasily", -1, "RUB", 1000))
                .isExactlyInstanceOf(CustomerAlreadyExistsException.class);
        Assertions.assertThatThrownBy(() ->
                mainRepository.insertCustomerWithAccount("Timmy", 11111, "RUB", 1000))
                .isExactlyInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    public void checkExistMethods() {
        Assertions.assertThat(mainRepository.customerWithNameExists("Vasily")).isTrue();
        Assertions.assertThat(mainRepository.customerWithNameExists("NoSuchName")).isFalse();
        Assertions.assertThat(mainRepository.accountWithAccountNumberExists(22222)).isTrue();
        Assertions.assertThat(mainRepository.accountWithAccountNumberExists(-1)).isFalse();
    }
}
