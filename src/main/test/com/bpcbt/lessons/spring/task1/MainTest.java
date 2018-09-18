package com.bpcbt.lessons.spring.task1;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {
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
}
