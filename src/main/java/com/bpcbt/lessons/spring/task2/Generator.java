package com.bpcbt.lessons.spring.task2;

import com.alibaba.fastjson.JSON;
import com.bpcbt.lessons.spring.task1.Account;

public class Generator {
    public static String getJSONFromAccount(Account account) {
        return JSON.toJSONString(account);
    }
    public static Account getAccountFromJSON(String account) {
        return JSON.parseObject(account, Account.class);
    }
}
