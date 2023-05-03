package com.es.phoneshop.exception;

public class ProductDefinitionException extends RuntimeException {
    public ProductDefinitionException(String message) {
        super(message);
    }

    public ProductDefinitionException() {
        super();
    }
}
