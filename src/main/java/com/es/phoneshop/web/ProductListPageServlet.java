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
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductListPageServlet extends HttpServlet {
    public ProductService productService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productService = CustomProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        if (StringUtils.isEmpty(query)) {
            request.setAttribute("products", productService.getProducts());
        }
        else {
            request.setAttribute("products", productService.findProductsByQuery(query));
        }
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

}
