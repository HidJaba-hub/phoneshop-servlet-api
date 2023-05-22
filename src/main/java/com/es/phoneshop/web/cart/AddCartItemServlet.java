package com.es.phoneshop.web.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddCartItemServlet extends CartItemServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        String redirectionPath = request.getParameter("path");
        Long productId = Long.valueOf(request.getPathInfo().substring(1));

        Map<Long, String> errors = new HashMap<>();

        int index = Arrays.stream(productIds)
                .map(Long::valueOf).toList()
                .indexOf(productId);

        int quantity;
        try {
            quantity = parseQuantity(quantities[index], request);
        } catch (ParseException exception) {
            response.sendRedirect(redirectionPath + "&errors=" + "Not a number" + "&id=" + productId + "&quantity=" + quantities[index]);
            return;
        }
        addProductToCart(request, productId, quantity, errors);

        if (errors.isEmpty()) {
            response.sendRedirect(redirectionPath + "&message=Cart updated successfully");
        } else {
            response.sendRedirect(redirectionPath + "&errors=" + errors.get(productId) + "&id=" + productId + "&quantity=" + quantity);
        }
    }
}
