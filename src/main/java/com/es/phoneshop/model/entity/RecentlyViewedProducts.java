package com.es.phoneshop.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

@Getter
@Setter
public class RecentlyViewedProducts implements Serializable {

    private Long id;
    private Deque<Product> recentlyViewedProducts;

    public RecentlyViewedProducts() {
        this.id = UUID.randomUUID().getMostSignificantBits();
        recentlyViewedProducts = new ArrayDeque<>();
    }
}
