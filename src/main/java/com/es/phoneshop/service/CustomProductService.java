package com.es.phoneshop.service;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomProductService implements ProductService {
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock writeLock = lock.writeLock();
    //idk is it necessary to synchronize service, because there are read/write locks in dao and it will be locked for any operation
    private static volatile CustomProductService customProductService;
    private final ProductDao productDao;

    private CustomProductService() {
        productDao = CustomProductDao.getCustomProductDao();
    }

    public static CustomProductService getCustomProductService() {
        CustomProductService localInstance = customProductService;
        if (localInstance == null) {
            writeLock.lock();
            try {
                localInstance = customProductService;
                if (localInstance == null) {
                    customProductService = localInstance = new CustomProductService();
                }
            } finally {
                writeLock.unlock();
            }
        }
        return localInstance;
    }

    @Override
    public List<Product> getProductsNotNull() {
        List<Product> products;
        products = productDao.findProducts();
        return products;
    }

    @Override
    public void buyProduct(Long id) {
        productDao.delete(id);
    }

    @Override
    public Product getProductById(Long id) {
        Product product = null;
        try {
            Optional<Product> optionalProduct = productDao.getProductById(id);
            if (optionalProduct.isPresent()) {
                product = optionalProduct.get();
            }
        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> getProductByDescription(String description) {
        List<Product> products;

        products = productDao.getProductByDescription(description);

        return products;
    }

    @Override
    public void changeState(Product product, boolean state) {
        CustomProductDao.getCustomProductDao().changeChosenState(product, state);
    }

}
