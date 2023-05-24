package com.es.phoneshop.web;

import com.es.phoneshop.model.entity.RecentlyViewedProducts;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.productHistory.CustomRecentlyViewedProductsService;
import com.es.phoneshop.service.productHistory.RecentlyViewedProductsService;
import com.es.phoneshop.web.cart.CartItemServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsPageServlet extends CartItemServlet {

    private ProductService productService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        recentlyViewedProductsService = CustomRecentlyViewedProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {
            {
                long productId = Long.valueOf(request.getPathInfo().substring(1));
                request.setAttribute("product", productService.getProductById(productId));
                fillRecentlyViewedProductList(request, productId);
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
            Map<Long, String> errors = new HashMap<>();
            String quantityStr = request.getParameter("quantity");

            int quantity;
            try {
                quantityParseValidator.validate(quantityStr, errors, productId);
                if(errors.isEmpty()) {
                    quantity = parseQuantity(quantityStr, request);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products/" + productId +
                            "?error=" + errors.get(productId));
                    return;
                }
            } catch (ParseException exception) {
                response.sendRedirect(request.getContextPath() + "/products/" + productId +
                        "?error=" + "Not a number");
                return;
            }
            addProduct(request, productId, quantity, errors);
            if (errors.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/products/" + productId +
                        "?message=Product added to cart");
            } else {
                response.sendRedirect(request.getContextPath() + "/products/" + productId +
                        "?error=" + errors.get(productId) + "&errorQuantity=" + quantity);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    private void fillRecentlyViewedProductList(HttpServletRequest request, long productId) {
        RecentlyViewedProducts recentlyViewedProducts = recentlyViewedProductsService.getRecentlyViewedProducts(request);
        recentlyViewedProductsService.addRecentlyViewedProduct(recentlyViewedProducts, productService.getProductById(productId));
    }
}