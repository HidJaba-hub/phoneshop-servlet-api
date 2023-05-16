package com.es.phoneshop.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
public class ProductHistory {

    Deque<Product> recentlyViewedProducts;

    public ProductHistory() {
        recentlyViewedProducts = new ArrayDeque<>();
    }
}
