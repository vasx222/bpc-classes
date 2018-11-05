package com.bpcbt.lessons.spring;

import com.bpcbt.lessons.spring.exception.AccountNotFoundException;
import com.bpcbt.lessons.spring.exception.AmountConversionException;
import com.bpcbt.lessons.spring.exception.MoneyTransferException;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.MainRepository;
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

    private MainRepository mainRepository;

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
        Account accountFrom = mainRepository.getCustomerAccount("Vasily");
        Account accountTo = mainRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountFrom.getAmount()).isEqualTo(200);
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100000);
        mainRepository.transfer("Vasily", "Ilya", 300, "RUB");
        accountFrom = mainRepository.getCustomerAccount("Vasily");
        accountTo = mainRepository.getCustomerAccount("Ilya");
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100300);
        Assertions.assertThat(Math.round((200f * 79.46f - 300f) * 0.01f)).isEqualTo(accountFrom.getAmount());

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
        String customerName = RandomStringUtils.randomAlphabetic(10);
        Integer accountNumber = Integer.valueOf(RandomStringUtils.randomNumeric(6));

        mainRepository.insertCustomerWithAccount(customerName, accountNumber, "RUB", 1000);
        Customer customer1 = mainRepository.getCustomerByName(customerName);
        Account account1 = mainRepository.getAccountByAccountNumber(accountNumber);

        Assertions.assertThat(customer1.getName()).isEqualTo(customerName);
        Assertions.assertThat(account1.getAccountNumber()).isEqualTo(accountNumber);
        Assertions.assertThat(account1.getCurrency()).isEqualTo("RUB");
        Assertions.assertThat(account1.getAmount()).isEqualTo(1000);
        Assertions.assertThat(customer1.getAccount()).isEqualToComparingFieldByField(account1);
    }

    @Test
    public void checkExistMethods() {
        Assertions.assertThat(mainRepository.customerWithNameExists("Vasily")).isTrue();
        Assertions.assertThat(mainRepository.customerWithNameExists("NoSuchName")).isFalse();
        Assertions.assertThat(mainRepository.accountWithAccountNumberExists(22222)).isTrue();
        Assertions.assertThat(mainRepository.accountWithAccountNumberExists(-1)).isFalse();
    }
}
