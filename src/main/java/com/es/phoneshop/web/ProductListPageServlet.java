package com.es.phoneshop.web;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    public ProductService productService;
    private String phoneIdToBuy;
    private String phoneDescription;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        productService = CustomProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String query = request.getParameter("query");


        if (StringUtils.isEmpty(sort) && StringUtils.isEmpty(order) && StringUtils.isEmpty(query)) {
            request.setAttribute("products", productService.getProducts());
        }
        if (StringUtils.isEmpty(sort) && StringUtils.isEmpty(order) && !StringUtils.isEmpty(query)) {
            request.setAttribute("products", productService.findProductsByQuery(query));
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            request.setAttribute("products", productService.getProductsWithSortingAndQuery(
                    SortField.valueOf(sort.toUpperCase()), SortOrder.valueOf(order.toUpperCase()), query));
        }
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

}
