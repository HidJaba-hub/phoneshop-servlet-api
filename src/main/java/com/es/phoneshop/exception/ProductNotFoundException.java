package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException {
    private Long id;

    public ProductNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
