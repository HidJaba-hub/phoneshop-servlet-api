package com.es.phoneshop.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

@Getter
@Setter
public class ProductHistory {

    private Long id;
    private Deque<Product> recentlyViewedProducts;

    public ProductHistory() {
        this.id = UUID.randomUUID().getMostSignificantBits();
        recentlyViewedProducts = new ArrayDeque<>();
    }
}
