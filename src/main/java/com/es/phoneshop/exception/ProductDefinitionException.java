package com.es.phoneshop.exception;

public class ProductDefinitionException extends RuntimeException {
    private Long id;

    public ProductDefinitionException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public ProductDefinitionException(String message) {
        super(message);
    }

    public Long getId() {
        return id;
    }
}
