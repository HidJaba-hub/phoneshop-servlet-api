package com.es.phoneshop.web.cart;

import com.es.phoneshop.utils.ReferenceTool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends CartItemServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();
        ReferenceTool<Integer> sameQuantityCount = new ReferenceTool<>(0);

        if (productIds == null || quantities == null) {
            doGet(request, response);
            return;
        }
        for (int i = 0; i < productIds.length; i++) {
            long productId = Long.parseLong(productIds[i]);

            int quantity;
            try {
                quantity = parseQuantity(quantities[i], request);
            } catch (ParseException exception) {
                errors.put(productId, "Not a number");
                continue;
            }
            updateProductToCart(request, productId, quantity, errors, sameQuantityCount);
        }
        if (errors.isEmpty() && sameQuantityCount.get() != productIds.length) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
