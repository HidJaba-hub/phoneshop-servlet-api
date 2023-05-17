package com.es.phoneshop.model.entity.cart;

import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.UUID;

@Getter
public class Cart implements Serializable {

    private Long id;
    private LinkedHashSet<CartItem> items;

    public Cart() {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.items = new LinkedHashSet<>();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.toString() +
                '}';
    }
}
