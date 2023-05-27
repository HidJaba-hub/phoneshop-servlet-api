package com.es.phoneshop.DAO.orderDao;

import com.es.phoneshop.DAO.GenericDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.entity.Order;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomOrderDao extends GenericDao<Order> implements OrderDao {

    private static CustomOrderDao customOrderDao;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();

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
        readLock.lock();
        try {
            return getItems().stream()
                    .filter(order -> id.equals(order.getSecureId()))
                    .findAny();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(Order order) {
        super.save(order);
    }
}
