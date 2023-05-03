package com.es.phoneshop.DAO;

import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProductById(Long id);

    List<Product> findProducts();

    void save(Product product);

    void delete(Long id);

    void changeChosenState(Product product, boolean state);
    void setProducts(List<Product> products);
    List<Product> getProducts();
}
