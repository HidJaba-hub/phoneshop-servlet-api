package com.es.phoneshop.DAO.productDao;

import com.es.phoneshop.SortField;
import com.es.phoneshop.SortOrder;
import com.es.phoneshop.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Optional<Product> getProductById(Long id);

    List<Product> findProducts();

    List<Product> sortProductsByFieldAndQuery(SortField sortField, SortOrder sortOrder, String query);

    List<Product> findProductsByQuery(String query);

    void save(Product product);

    void delete(Long id);

    List<Product> getProducts();

}
