package com.es.phoneshop.service.order;

import com.es.phoneshop.DAO.orderDao.CustomOrderDao;
import com.es.phoneshop.DAO.orderDao.OrderDao;
import com.es.phoneshop.PaymentMethod;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;
import com.es.phoneshop.model.entity.cart.Cart;
import com.es.phoneshop.model.entity.cart.CartItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomOrderService implements OrderService {

    private OrderDao orderDao;

    private CustomOrderService() {
        orderDao = CustomOrderDao.getInstance();
    }

    public static CustomOrderService getInstance() {
        return CustomOrderService.SingletonManager.INSTANCE.getSingleton();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new)));
        order.setCurrency(cart.getCurrency());
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    @Override
    public Order getOrderById(String id) throws OrderNotFoundException {
        Optional<Order> optionalOrder = orderDao.getOrderBySecureId(id);
        return optionalOrder.orElseThrow(() ->
                new OrderNotFoundException(id, "Order not found for id: " + id));
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
    }

    private enum SingletonManager {
        INSTANCE;
        private static final CustomOrderService singleton = new CustomOrderService();

        public CustomOrderService getSingleton() {
            return singleton;
        }
    }
}
