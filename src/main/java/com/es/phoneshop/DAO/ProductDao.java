package com.es.phoneshop.DAO;

import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProductById(Long id);

    List<Product> getProductByDescription(String criteria);

    List<Product> findProducts();

    void save(Product product);

    void delete(Long id);
}
