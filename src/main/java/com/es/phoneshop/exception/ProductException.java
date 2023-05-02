package com.es.phoneshop.exception;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }

    public ProductException() {
        super();
    }
}
