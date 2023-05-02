package com.es.phoneshop.web;

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
        request.setAttribute("products", productService.getProducts());
        productService.getProducts().forEach(product -> productService.changeState(product, false));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case ("findProduct"):
                phoneDescription = request.getParameter("phoneDescription");
                productService.getProducts().forEach(product -> productService.changeState(product, false));
                if (!StringUtils.isEmpty(phoneDescription)) {
                    productService.getProductByDescription(phoneDescription).forEach(product -> productService.changeState(product, true));
                }
                break;
        }
        request.setAttribute("products", productService.getProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
