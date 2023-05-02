package com.es.phoneshop.service;

import com.es.phoneshop.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);

    List<Product> getProductByDescription(String description);

    List<Product> getProducts();

    void deleteProduct(Long id);

    void changeState(Product product, boolean state);

    void saveProduct(Product product);
}
