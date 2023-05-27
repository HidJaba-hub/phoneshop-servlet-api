package com.es.phoneshop.model.entity.cart;

import com.es.phoneshop.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class CartItem implements Serializable, Cloneable {

    private Product product;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItem cartItem)) {
            return false;
        }
        return Objects.equals(product, cartItem.product);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.hashCode());
    }

    @Override
    public String toString() {
        return "CartItem{" + "id=" + product.getId() +
                ", product=" + product.getCode() +
                ", quantity=" + quantity +
                '}';
    }
}
