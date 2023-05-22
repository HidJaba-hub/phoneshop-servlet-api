package com.es.phoneshop.model.entity.cart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.UUID;

@Getter
@Setter
public class Cart implements Serializable {

    private Long id;
    private LinkedHashSet<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private Currency currency;

    public Cart() {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.items = new LinkedHashSet<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.toString() +
                '}';
    }
}
