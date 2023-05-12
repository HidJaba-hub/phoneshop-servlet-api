package com.es.phoneshop.web;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSortServlet extends HttpServlet {

    private ProductService productService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productService = CustomProductService.getInstance();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String query = request.getParameter("query");
        List<Product> products = productService.getProductsWithSortingAndQuery(SortField.valueOf(sort.toUpperCase()), SortOrder.valueOf(order.toUpperCase()), query);
        String json = objectMapper.writeValueAsString(products
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList()));
        response.getWriter().write(json);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}