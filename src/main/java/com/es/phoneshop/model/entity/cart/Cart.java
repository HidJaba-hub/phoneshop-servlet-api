package com.es.phoneshop.model.entity.cart;

import lombok.Getter;

import java.util.LinkedHashSet;

@Getter
public class Cart {

    LinkedHashSet<CartItem> items;
    public Cart() {
        this.items = new LinkedHashSet<>();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.toString() +
                '}';
    }
}
