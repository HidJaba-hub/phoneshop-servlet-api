package com.es.phoneshop.service.order;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;

import java.util.List;

public interface OrderService {

    Order createOrder(Cart cart);

    Order getOrderById(String id) throws OrderNotFoundException;

    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);
}
