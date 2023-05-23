package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.utils.QuantityParseValidator;
import com.es.phoneshop.utils.ReferenceTool;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

public abstract class CartItemServlet extends HttpServlet {

    protected QuantityParseValidator quantityParseValidator;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        quantityParseValidator = QuantityParseValidator.getInstance();
    }

    protected void addProductToCart(HttpServletRequest request, long productId, int quantity, Map<Long, String> errors) throws IOException {
        Cart cart = cartService.getCart(request);
        try {
            cartService.addProductToCart(cart, productId, quantity);
        } catch (OutOfStockException e) {
            errors.put(productId, "Out of stock, available " + e.getStockAvailable());
        } catch (IllegalArgumentException e) {
            errors.put(productId, "Wrong amount of products");
        }
    }

    protected void updateProductToCart(HttpServletRequest request, long productId,
                                       int quantity, Map<Long, String> errors, ReferenceTool<Integer> sameQuantityCount) {
        Cart cart = cartService.getCart(request);
        try {
            if (!cartService.updateProductInCart(cart, productId, quantity)) {
                sameQuantityCount.set(sameQuantityCount.get() + 1);
            }
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
