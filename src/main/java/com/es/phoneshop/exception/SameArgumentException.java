package com.es.phoneshop.exception;

import com.es.phoneshop.model.entity.cart.CartItem;

public class SameArgumentException extends RuntimeException {

    private CartItem cartItem;

    public SameArgumentException(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
