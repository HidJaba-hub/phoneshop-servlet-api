package com.es.phoneshop.DAO;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProductById(Long id);

    List<Product> findProducts(SortField sortField, SortOrder sortOrder, String query);

    void save(Product product);

    void delete(Long id);

    void changeChosenState(Product product, boolean state);

    List<Product> getProducts();

    void setProducts(List<Product> products);
}
