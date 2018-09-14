package com.bpcbt.lessons.spring.task1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("com.bpcbt.lessons.spring");
        applicationContext.getBean(SimpleController.class).run();
    }

}
