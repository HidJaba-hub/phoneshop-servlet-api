package com.es.phoneshop.service;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.exception.ProductException;
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
    private static CustomProductService customProductService;
    private final ProductDao productDao;

    private CustomProductService() {
        productDao = CustomProductDao.getInstance();
    }

    public static CustomProductService getInstance() {
        if (customProductService == null) {
            writeLock.lock();
            try {
                customProductService = new CustomProductService();
            } finally {
                writeLock.unlock();
            }
        }
        return customProductService;
    }

    @Override
    public List<Product> getProducts() {
        return productDao.findProducts();
    }

    @Override
    public void deleteProduct(Long id) {
        productDao.delete(id);
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productDao.getProductById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new NoSuchElementException("Product not found for id: " + id);
        }
    }

    @Override
    public void saveProduct(Product product) {
        try {
            productDao.save(product);
        } catch (ProductException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Product> getProductByDescription(String description) {
        return productDao.getProductByDescription(description);
    }

    @Override
    public void changeState(Product product, boolean state) {
        CustomProductDao.getInstance().changeChosenState(product, state);
    }

}
