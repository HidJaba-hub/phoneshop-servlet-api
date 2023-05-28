package com.es.phoneshop.web;

import com.es.phoneshop.PaymentMethod;
import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.service.order.CustomOrderService;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.web.cart.CartItemServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends CartItemServlet {

    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = CustomOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("order", orderService.createOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, "firstName", errors, order::setFirstName, ValidatorMethod.NAME);
        setRequiredParameter(request, "lastName", errors, order::setLastName, ValidatorMethod.NAME);
        setRequiredParameter(request, "phone", errors, order::setPhone, ValidatorMethod.PHONE);
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress, ValidatorMethod.NONE);
        setDeliveryDate(request, errors, order);
        setPaymentMethod(request, errors, order);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.cleanCart(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("order", order);
            request.setAttribute("errors", errors);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer, ValidatorMethod validator) {
        String value = request.getParameter(parameter);
        if (StringUtils.isEmpty(value)) {
            errors.put(parameter, "Value is required");
        } else {
            validateParameter(validator, parameter, value, errors);
        }
        if (!errors.containsKey(parameter)) {
            consumer.accept(value);
        }
    }

    private void validateParameter(ValidatorMethod validator, String parameter, String value, Map<String, String> errors) {
        switch (validator) {
            case NAME -> defaultParseValidator.validateName(value, errors, parameter);
            case PHONE -> defaultParseValidator.validateNumber(value, errors, parameter);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("deliveryDate");
        if (StringUtils.isEmpty(value)) {
            errors.put("deliveryDate", "Value is required");
        } else {
            defaultParseValidator.validateDate(value, errors, "deliveryDate");
        }
        if (errors.containsKey("deliveryDate")) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate orderDeliveryDate = LocalDate.parse(value, formatter);
            if (orderDeliveryDate.isAfter(LocalDate.now())) {
                order.setDeliveryDate(orderDeliveryDate);
            } else {
                errors.put("deliveryDate", "Date can't be past");
            }
        } catch (DateTimeParseException e) {
            errors.put("deliveryDate", "Date is incorrect");
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (StringUtils.isEmpty(value)) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }

    private enum ValidatorMethod {
        NAME, PHONE, NONE
    }
}
