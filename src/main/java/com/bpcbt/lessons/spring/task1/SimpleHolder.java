package com.bpcbt.lessons.spring.task1;

public class SimpleHolder {

    SimpleBean simpleBean;
    public SimpleHolder(SimpleBean simpleBean) {
        this.simpleBean = simpleBean;
    }

    SimpleBean getSimpleBean() {
        return simpleBean;
    }
}
