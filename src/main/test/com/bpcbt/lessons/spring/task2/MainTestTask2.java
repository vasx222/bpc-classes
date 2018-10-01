package com.bpcbt.lessons.spring.task2;

import com.bpcbt.lessons.spring.task1.Account;
import com.bpcbt.lessons.spring.task1.SimpleController;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTestTask2 {
    private static SimpleController controller;

    @BeforeClass
    public static void initController() {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("com.bpcbt.lessons.spring");
        controller = applicationContext.getBean(SimpleController.class);
    }

    @Test
    public void checkSendJSON() throws InterruptedException {
        Server server = new Server(3345, controller);
        server.start();
        Client client = new Client("localhost", 3345);
        Account account = new Account(20, 12345, "USD", 350000);
        client.sendMessage(Generator.getJSONFromAccount(account));
        Thread.sleep(1000);
        Account sameAccount = controller.getAccountById(20);
        Assertions.assertThat(account).isEqualToComparingFieldByField(sameAccount);
    }
}
