package com.es.phoneshop.model.entity.cart;

import com.es.phoneshop.model.entity.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashSet;

@Getter
@Setter
public class Cart extends GenericEntity implements Serializable {

    private LinkedHashSet<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalPrice;
    private Currency currency;

    public Cart() {
        super();
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
