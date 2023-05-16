package com.es.phoneshop.web;

import com.es.phoneshop.model.entity.ProductHistory;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.productHistory.CustomProductHistoryService;
import com.es.phoneshop.service.productHistory.ProductHistoryService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;

public class ViewedProductFilter implements Filter {

    private ProductHistoryService productHistoryService;
    private ProductService productService;

    @Override
    public void init(FilterConfig config) {
        productHistoryService = CustomProductHistoryService.getInstance();
        productService = CustomProductService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (servletRequest != null && (request).getMethod().equalsIgnoreCase("GET")
                && !StringUtils.isEmpty(request.getPathInfo())) {
            ProductHistory productHistory = productHistoryService.getProductHistory(request);
            request.setAttribute("viewedProducts", productHistoryService.getProductHistory(request));

            long productId = Long.valueOf(request.getPathInfo().substring(1));
            productHistoryService.addViewedProduct(productHistory, productService.getProductById(productId));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
