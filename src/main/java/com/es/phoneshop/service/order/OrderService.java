package com.es.phoneshop.service.order;

import com.es.phoneshop.PaymentMethod;
import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;

import java.util.List;

public interface OrderService {

    Order getOrder(Cart cart);

    Order getOrderById(String id);

    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);
}
