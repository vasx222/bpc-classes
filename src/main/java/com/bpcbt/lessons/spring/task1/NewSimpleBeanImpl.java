package com.bpcbt.lessons.spring.task1;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("russian")
public class NewSimpleBeanImpl implements SimpleBean {
    @Override
    public void say() {
        System.out.println("Говорю только по-русски");
    }
}
