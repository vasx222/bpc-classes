package com.bpcbt.lessons.spring.exception;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException() {
    }

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
