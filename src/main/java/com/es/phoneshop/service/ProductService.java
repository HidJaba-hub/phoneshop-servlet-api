package com.es.phoneshop.service;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);

    List<Product> getProducts(SortField sortField, SortOrder sortOrder, String query);

    void deleteProduct(Long id);

    void changeState(Product product, boolean state);

    void saveProduct(Product product);
}
