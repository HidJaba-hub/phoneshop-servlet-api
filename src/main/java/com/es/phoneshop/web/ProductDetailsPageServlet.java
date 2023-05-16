package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
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
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {
            {
                long productId = Long.valueOf(request.getPathInfo().substring(1));
                request.setAttribute("product", productService.getProductById(productId));
                request.setAttribute("cart", cartService.getCart(request));
            } // compressing field of view
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {
            long productId = Long.valueOf(request.getPathInfo().substring(1));
            String quantityStr = request.getParameter("quantity");
            int quantity;

            try {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                quantity = format.parse(quantityStr).intValue();
            } catch (ParseException exception) {
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + "Not a number");
                return;
            }

            Cart cart = cartService.getCart(request);
            try {
                cartService.addProductToCart(cart, productId, Integer.valueOf(quantity));
            } catch (OutOfStockException e) {
                String errorString;
                if (e.getStockAvailable() == 0) errorString = "Wrong amount of products";
                else errorString = "Out of stock, available " + e.getStockAvailable();
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + errorString);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }

    }
}