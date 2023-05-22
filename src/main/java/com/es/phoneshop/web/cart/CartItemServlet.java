package com.es.phoneshop.web.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.exception.SameArgumentException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.utils.ReferenceTool;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

public abstract class CartItemServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
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
            cartService.updateProductInCart(cart, productId, quantity);
        } catch (OutOfStockException e) {
            errors.put(productId, "Out of stock, available " + e.getStockAvailable());
        } catch (IllegalArgumentException e) {
            errors.put(productId, "Wrong amount of products");
        } catch (SameArgumentException e) {
            sameQuantityCount.set(sameQuantityCount.get() + 1);
        } catch (ProductNotFoundException e) {
            errors.put(productId, e.getMessage());
        }
    }

    protected int parseQuantity(String quantityStr, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        String stringInput = removePointsFromString(removeMinusesFromString(removeNumbersFromString(quantityStr)));
        if (StringUtils.isEmpty(stringInput)) {
            return format.parse(quantityStr).intValue();
        } else {
            throw new ParseException(quantityStr, 0);
        }
    }

    public String removeNumbersFromString(String input) {
        return input.replaceAll("\\d+", "");
    }

    public String removeMinusesFromString(String input) {
        return input.replaceAll("-", "");
    }

    public String removePointsFromString(String input) {
        return input.replaceAll("\\.", "");
    }
}
