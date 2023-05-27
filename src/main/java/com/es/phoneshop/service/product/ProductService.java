package com.es.phoneshop.service.product;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.entity.Product;

import java.util.List;

public interface ProductService {

    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getProducts();

    List<Product> getProductsByQuery(String query);

    List<Product> getProductsWithSortingAndQuery(SortField sortField, SortOrder sortOrder, String query);

    void deleteProduct(Long id);

    void saveProduct(Product product);
}
