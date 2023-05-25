package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.utils.ReferenceTool;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {

    Cart getCart(HttpServletRequest request);

    void addCartItem(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void updateCartItem(Cart cart, Long productId, int quantity, ReferenceTool<Integer> sameQuantityCount) throws OutOfStockException;

    void deleteCartItem(Cart cart, Long productId);
}
