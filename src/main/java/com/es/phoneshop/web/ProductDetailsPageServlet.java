package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.service.productHistory.CustomProductHistoryService;
import com.es.phoneshop.service.productHistory.ProductHistoryService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductService productService;
    private ProductHistoryService productHistoryService;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        cartService = DefaultCartService.getInstance();
        productHistoryService = CustomProductHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {
            {
                long productId = Long.valueOf(request.getPathInfo().substring(1));
                request.setAttribute("product", productService.getProductById(productId));
                request.setAttribute("cart", cartService.getCart(request));
                makeRecentlyViewedProductList(request, productId);
            }
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {

            long productId = Long.valueOf(request.getPathInfo().substring(1));
            String quantityStr = request.getParameter("quantity");

            int quantity = checkQuantity(request, response, quantityStr, productId);
            addProductToCart(request, response, productId, quantity);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }

    }

    private int checkQuantity(HttpServletRequest request, HttpServletResponse response,
                              String quantityStr, long productId) throws IOException {
        try {
            int quantity;
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityStr).intValue();
            return quantity;
        } catch (ParseException exception) {
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + "Not a number");
            return 0;
        }
    }

    private void addProductToCart(HttpServletRequest request, HttpServletResponse response,
                                  long productId, int quantity) throws IOException {
        String errorString;
        Cart cart = cartService.getCart(request);
        try {
            cartService.addProductToCart(cart, productId, Integer.valueOf(quantity));
        } catch (OutOfStockException e) {
            errorString = "Out of stock, available " + e.getStockAvailable();
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + errorString);
            return;
        } catch (IllegalArgumentException e) {
            errorString = "Wrong amount of products";
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + errorString);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private void makeRecentlyViewedProductList(HttpServletRequest request, long productId) {
        ProductHistory productHistory = productHistoryService.getProductHistory(request);
        request.setAttribute("viewedProducts", productHistoryService.getProductHistory(request));
        productHistoryService.addViewedProduct(productHistory, productService.getProductById(productId));
    }
}