package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.cart.Cart;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {

    Cart getCart(HttpServletRequest request);

    void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
