package com.es.phoneshop.DAO.orderDao;

import com.es.phoneshop.DAO.GenericDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;

import java.util.Optional;

public class CustomOrderDao extends GenericDao<Order> implements OrderDao {

    private static CustomOrderDao customOrderDao;

    private CustomOrderDao() {
        super();
    }

    public static CustomOrderDao getInstance() {
        if (customOrderDao == null) {
            synchronized (CustomOrderDao.class) {
                customOrderDao = new CustomOrderDao();
            }
        }
        return customOrderDao;
    }

    @Override
    public Optional<Order> getOrderBySecureId(String id) throws OrderNotFoundException {
        getReadLock().lock();
        try {
            return getItems().stream()
                    .filter(order -> id.equals(order.getSecureId()))
                    .findAny();
        } finally {
            getReadLock().unlock();
        }
    }

    @Override
    public void save(Order order) throws IllegalArgumentException {
        super.save(order);
    }
}
