package com.bpcbt.lessons.spring.task1;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTestTask1 {
    private static SimpleController controller;

    @BeforeClass
    public static void initController() {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("com.bpcbt.lessons.spring");
        controller = applicationContext.getBean(SimpleController.class);
    }

    @Test
    public void checkTransfer() {
        Account accountFrom = controller.getCustomerAccount("Vasily");
        Account accountTo = controller.getCustomerAccount("Ilya");
        Assertions.assertThat(accountFrom.getAmount()).isEqualTo(200);
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100000);
        controller.transfer("Vasily", "Ilya", 300, "RUB");
        accountFrom = controller.getCustomerAccount("Vasily");
        accountTo = controller.getCustomerAccount("Ilya");
        Assertions.assertThat(accountTo.getAmount()).isEqualTo(100300);
        Assertions.assertThat(Math.round((200f * 79.46f - 300f) * 0.01f)).isEqualTo(accountFrom.getAmount());
    }

    @Test
    public void checkPrint() {
        controller.printCustomersAccounts();
    }

    @Test
    public void checkGetCustomerAccount() {
        Assertions.assertThat(controller.getCustomerAccount("Vasily"))
                .isEqualToComparingOnlyGivenFields(new Account(5, 22222, "EUR", 200),
                        "id", "accountNumber", "currency");
        Assertions.assertThatThrownBy(() -> controller.getCustomerAccount("NoSuchName"))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void checkConvertAmount() {
        Assertions.assertThat(controller.convertAmount(100, "RUB", "USD")).isEqualTo(2);
        Assertions.assertThatThrownBy(() ->controller.convertAmount(200, "A", "B"))
                .isExactlyInstanceOf(RuntimeException.class);
    }
}
