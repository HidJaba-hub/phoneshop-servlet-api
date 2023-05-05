package com.es.phoneshop.service;

import com.es.phoneshop.DAO.CustomProductDao;
import com.es.phoneshop.DAO.ProductDao;
import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.exception.ProductDefinitionException;
import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public class CustomProductService implements ProductService {
    private static CustomProductService customProductService;
    private ProductDao productDao;

    private CustomProductService() {
        productDao = CustomProductDao.getInstance();
    }

    public static CustomProductService getInstance() {
        if (customProductService == null) {
            synchronized (CustomProductService.class) {
                customProductService = new CustomProductService();
            }
        }
        return customProductService;
    }

    @Override
    public List<Product> getProducts(SortField sortField, SortOrder sortOrder, String query) {
        return productDao.findProducts(sortField, sortOrder, query);
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
            throw new ProductDefinitionException(id,"Product not found for id: " + id);
        }
    }

    @Override
    public void saveProduct(Product product) {
            productDao.save(product);
    }

    @Override
    public void changeState(Product product, boolean state) {
        productDao.changeChosenState(product, state);
    }

}
