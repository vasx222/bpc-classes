package com.bpcbt.lessons.spring.task1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("simple")
@Scope("prototype")
public class SimpleBeanImpl implements SimpleBean {

    @Autowired
    String textGenerator;

    public SimpleBeanImpl() {
    }

    @Override
    public void say() {
        System.out.println(textGenerator);
    }
}
