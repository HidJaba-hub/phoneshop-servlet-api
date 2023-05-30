package com.es.phoneshop.DAO.orderDao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;

import java.util.Optional;

public interface OrderDao {

    Optional<Order> getOrderBySecureId(String id) throws OrderNotFoundException;

    void save(Order order) throws IllegalArgumentException;
}
