package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long productId = Long.valueOf(request.getPathInfo().substring(1));

        Cart cart = cartService.getCart(request);
        try {
            cartService.deleteProductInCart(cart, productId);
        } catch (ProductNotFoundException e) {
            response.sendRedirect(request.getContextPath() + "/cart?errors=" + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }

}
