package com.bpcbt.lessons.spring.exception;

public class AmountConversionException extends RuntimeException {
    public AmountConversionException() {
    }

    public AmountConversionException(String message) {
        super(message);
    }
}
