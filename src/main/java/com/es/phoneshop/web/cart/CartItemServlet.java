package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.utils.ReferenceTool;
import com.es.phoneshop.validators.DefaultParseValidator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

public abstract class CartItemServlet extends HttpServlet {

    protected DefaultParseValidator defaultParseValidator;
    protected CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        defaultParseValidator = DefaultParseValidator.getInstance();
    }

    protected void addProduct(HttpServletRequest request, long productId, int quantity, Map<Long, String> errors) throws IOException {
        Cart cart = cartService.getCart(request);
        try {
            cartService.addCartItem(cart, productId, quantity);
        } catch (OutOfStockException e) {
            errors.put(productId, "Out of stock, available " + e.getStockAvailable());
        } catch (IllegalArgumentException e) {
            errors.put(productId, "Wrong amount of products");
        }
    }

    protected void updateProduct(HttpServletRequest request, long productId,
                                 int quantity, Map<Long, String> errors, ReferenceTool<Integer> sameQuantityCount) {
        Cart cart = cartService.getCart(request);
        try {
            cartService.updateCartItem(cart, productId, quantity, sameQuantityCount);
        } catch (OutOfStockException e) {
            errors.put(productId, "Out of stock, available " + e.getStockAvailable());
        } catch (IllegalArgumentException e) {
            errors.put(productId, "Wrong amount of products");
        } catch (ProductNotFoundException e) {
            errors.put(productId, e.getMessage());
        }
    }

    protected int parseQuantity(String quantityStr, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityStr).intValue();
    }
}
