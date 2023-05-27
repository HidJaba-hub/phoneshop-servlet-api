package com.es.phoneshop.web;

import com.es.phoneshop.service.order.CustomOrderService;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = CustomOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!StringUtils.isEmpty(request.getPathInfo())) {
            String orderId = request.getPathInfo().substring(1);
            request.setAttribute("order", orderService.getOrderById(orderId));
            request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }
}
